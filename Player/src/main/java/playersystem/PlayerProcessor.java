package playersystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;

import static common.data.GameKeys.LEFT;
import static common.data.GameKeys.RIGHT;
import static common.data.GameKeys.UP;


public class PlayerProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(Player.class)) {
            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);

            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(UP));
            movingPart.setDown(gameData.getKeys().isDown(UP));

            movingPart.process(gameData, player);
            positionPart.process(gameData, player);
        }
    }
}
