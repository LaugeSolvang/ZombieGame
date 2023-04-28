package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;


public class ZombieProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        Entity player = world.getEntities(Player.class).stream().findFirst().orElse(null);
        if (player == null) {
            return;
        }

        for (Entity zombie : world.getEntities(Zombie.class)) {
            PositionPart zombiePosition = zombie.getPart(PositionPart.class);
            MovingPart zombieMovement = zombie.getPart(MovingPart.class);
            LifePart zombieLife = zombie.getPart(LifePart.class);

            PositionPart playerPosition = player.getPart(PositionPart.class);

            float deltaX = playerPosition.getX() - zombiePosition.getX();
            float deltaY = playerPosition.getY() - zombiePosition.getY();

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    zombieMovement.setRight(true);
                } else {
                    zombieMovement.setLeft(true);
                }
            } else {
                if (deltaY > 0) {
                    zombieMovement.setUp(true);
                } else {
                    zombieMovement.setDown(true);
                }
            }

            zombieMovement.process(gameData, zombie);
            zombiePosition.process(gameData, zombie);
            zombieLife.process(gameData, zombie);

            zombieMovement.setRight(false);
            zombieMovement.setLeft(false);
            zombieMovement.setUp(false);
            zombieMovement.setDown(false);

            //makes the zombie sprite face the direction it is walking
            if (zombieMovement.getDx() < 0){
                String path = "zombie.png";
                zombie.setPath(path);
            }
            if (zombieMovement.getDx() > 0){
                String path = "zombie-kopi.png";
                zombie.setPath(path);
            }
        }
    }
}
