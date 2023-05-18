package main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entityparts.HealthPart;
import common.data.entityparts.LifePart;
import common.data.entityparts.PositionPart;
import common.data.entityparts.ScorePart;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;
import common.services.IPostEntityProcessingService;
import common.services.KeyPressListener;
import managers.GameInputProcessor;
import managers.SpriteCache;

import java.util.Collection;
import java.util.ServiceLoader;

import static common.data.GameKeys.*;
import static java.util.stream.Collectors.toList;

public class Game implements ApplicationListener {
    private SpriteBatch sb;
    private BitmapFont font;
    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Collection<? extends IEntityProcessingService> entityProcessingServices = getEntityProcessingServices();
    private final Collection<? extends IPostEntityProcessingService> postEntityProcessingServices = getPostEntityProcessingServices();
    private final Collection<? extends IGamePluginService> gamePluginServices = getPluginServices();
    private final Collection<? extends KeyPressListener> keyPressListeners = getKeyPressListeners();

    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        OrthographicCamera cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate((float) gameData.getDisplayWidth() / 2, (float) gameData.getDisplayHeight() / 2);
        cam.update();

        font = new BitmapFont();
        sb = new SpriteBatch();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

        for (IGamePluginService iGamePlugin : gamePluginServices) {
            iGamePlugin.start(gameData, world);
            System.out.println(iGamePlugin.getClass());
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(160/255f, 160/255f, 160/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        checkForUserInput();

        update();

        draw();

        gameData.getKeys().update();
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : entityProcessingServices) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : postEntityProcessingServices) {
            postEntityProcessorService.process(gameData, world);
        }

        gameData.setGameTime(Math.max(gameData.getGameTime() + gameData.getDelta(), 0.18F));
    }

    private void checkForUserInput() {
        if (gameData.getKeys().isPressed(ESCAPE)) {
            emitKeyPressEvent(ESCAPE);
        }
        if (gameData.getKeys().isPressed(ENTER)) {
            emitKeyPressEvent(ENTER);
        }
        if (gameData.getKeys().isPressed(TAB)) {
            emitKeyPressEvent(TAB);
        }
        if (gameData.getKeys().isPressed(DEL)) {
            emitKeyPressEvent(DEL);
        }
        if (gameData.getKeys().isPressed(EIGHT)) {
            emitKeyPressEvent(EIGHT);
        }
        if (gameData.getKeys().isPressed(NINE)) {
            emitKeyPressEvent(NINE);
        }
    }

    private void draw() {
        //no idea how this works in detail, but it creates something that allows you to display text in-game
        CharSequence scoreStr = "Score: " + ScorePart.getScore();
        CharSequence lifeStr = "Life: " + HealthPart.getHealth();

        sb.begin();
        //this actually displays said text
        font.draw(sb, scoreStr,40,700);
        font.draw(sb, lifeStr, 1330, 700);
        //Draw all sprites, update the sprites position beforehand
        for (Entity entity : world.getEntities()) {
            Sprite sprite = SpriteCache.getSprite(entity.getPath());
            PositionPart positionPart = entity.getPart(PositionPart.class);
            positionPart.setDimension(sprite.getWidth(), sprite.getHeight());
            sprite.setPosition(positionPart.getX(), positionPart.getY());

            sprite.draw(sb);
        }
        sb.end();
    }
    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private void emitKeyPressEvent(int key) {
        for (KeyPressListener listener : keyPressListeners) {
            listener.onKeyPressed(key, gameData, world);
        }
    }
    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends KeyPressListener> getKeyPressListeners() {
        return ServiceLoader.load(KeyPressListener.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
