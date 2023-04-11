package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;

import java.util.Random;

public class ZombieProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Zombie.class)) {
            PositionPart positionPart = enemy.getPart(PositionPart.class);
            MovingPart movingPart = enemy.getPart(MovingPart.class);
            LifePart lifePart = enemy.getPart(LifePart.class);

            Random rand = new Random();

            int direction = rand.nextInt(4) - 1; // -1 for left, 0 for up/down, 1 for right

            if (direction == -1) {
                movingPart.setLeft(true);
            } else if (direction == 0) {
                movingPart.setUp(true);
            } else if (direction == 1){
                movingPart.setRight(true);
            } else {
                movingPart.setDown(true);
            }

            movingPart.process(gameData, enemy);
            positionPart.process(gameData, enemy);
            lifePart.process(gameData, enemy);

            movingPart.setRight(false);
            movingPart.setLeft(false);
            movingPart.setUp(false);
            movingPart.setDown(false);

        }
    }
}
