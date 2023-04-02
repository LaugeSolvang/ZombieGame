package playersystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {
    private Entity player;

    public PlayerPlugin() {
    }
    @Override
    public void start(GameData gameData, World world) {
        player = createPlayer(gameData);
        world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData) {
        float deceleration = 50;
        float acceleration = 100;
        float maxSpeed = 150;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 3.1415f / 2;

        Entity player = new Player();
        player.add(new MovingPart(deceleration, acceleration, maxSpeed, rotationSpeed));
        player.add(new PositionPart(x, y, radians));

        player.setSprite(new Sprite(new Texture(Gdx.files.internal("Player/src/main/resources/pixel_art.png"))));
        player.getSprite().setPosition(x,y);

        return player;
    }
    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }
}
