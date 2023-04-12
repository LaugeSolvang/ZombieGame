package playersystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entityparts.LifePart;
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
        Entity player = new Player();
        player.setSprite(new Sprite(new Texture(Gdx.files.internal("Player/src/main/resources/player.png"))));

        float deceleration = 300;
        float acceleration = 600;
        float maxSpeed = 100;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 0;
        float width = player.getSprite().getWidth();
        float height = player.getSprite().getHeight();

        player.add(new MovingPart(deceleration, acceleration, maxSpeed, rotationSpeed));
        player.add(new PositionPart(x, y, width, height, radians));
        player.add(new LifePart(100));

        player.getSprite().setPosition(x,y);

        return player;
    }
    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }
}
