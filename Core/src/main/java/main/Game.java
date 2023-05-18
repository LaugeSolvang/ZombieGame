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
import common.data.entities.player.Player;
import common.data.entityparts.LifePart;
import common.data.entityparts.PositionPart;
import common.data.Score;
import managers.ServiceLoaderUtils;
import managers.GameInputProcessor;
import managers.SpriteCache;

public class Game implements ApplicationListener {
    private SpriteBatch sb;
    private BitmapFont font;
    private final GameData gameData = new GameData();
    private final World world = new World();
    private GameLogic gameLogic;


    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        OrthographicCamera cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate((float) gameData.getDisplayWidth() / 2, (float) gameData.getDisplayHeight() / 2);
        cam.update();

        font = new BitmapFont();
        sb = new SpriteBatch();

        gameLogic = new GameLogic(
                ServiceLoaderUtils.getPluginServices(),
                ServiceLoaderUtils.getEntityProcessingServices(),
                ServiceLoaderUtils.getPostEntityProcessingServices(),
                ServiceLoaderUtils.getKeyPressListeners()
        );

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));


        gameLogic.startPluginServices(gameData, world);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(160/255f, 160/255f, 160/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        gameLogic.checkForUserInput(gameData, world);

        gameLogic.update(gameData, world);

        draw();

        gameData.getKeys().update();
    }

    private void draw() {
        int life = 0;
        for (Entity playerentity: world.getEntities(Player.class)) {
            LifePart lifePart = playerentity.getPart(LifePart.class);
            life = lifePart.getLife();
        }
        //no idea how this works in detail, but it creates something that allows you to display text in-game
        CharSequence scoreStr = "Score: " + Score.getScore();
        CharSequence lifeStr = "Life: " + life;

        sb.begin();
        //this actually displays said text
        font.draw(sb, scoreStr,40,700);
        font.draw(sb, lifeStr, 1330, 700);

        //Draw all sprites, update the sprites position beforehand
        for (Entity entity : world.getEntities()) {
            Sprite sprite = SpriteCache.getSprite(entity.getPath());
            PositionPart positionPart = entity.getPart(PositionPart.class);
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
}
