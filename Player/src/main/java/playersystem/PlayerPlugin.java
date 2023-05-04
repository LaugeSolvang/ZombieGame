package playersystem;

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

    @Override
    public void start(GameData gameData, World world) {
        player = createPlayer(gameData);
        world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData) {
        Entity player = new Player();

        float deceleration = 2000;
        float acceleration = 4000;
        float maxSpeed = 100;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        int life = 10000;
        String path = "player.png";
        player.setPath(path);

        player.add(new MovingPart(deceleration, acceleration, maxSpeed));
        player.add(new PositionPart(x, y));
        player.add(new LifePart(life));

        return player;
    }
    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }
}
