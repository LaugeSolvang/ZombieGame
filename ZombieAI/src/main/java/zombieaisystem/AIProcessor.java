package zombieaisystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.zombie.IZombieAI;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IPostEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class AIProcessor implements IPostEntityProcessingService, IZombieAI {
    @Override
    public void process(GameData gameData, World world) {
        List<Zombie> zombies = new ArrayList<>();
        for (Entity entity : world.getEntities(Zombie.class)) {
            zombies.add((Zombie) entity);
        }
        if (zombies.isEmpty()) {
            return;
        }
        if (gameData.getGameTime() < 1) {
            return;
        }
        int startIndex = (int) (gameData.getGameTime() * 100) % (int) gameData.getGameTime(); // Calculate the start index based on the time elapsed and the time interval (200 milliseconds).
        String[][] map = world.getMap();
        Entity player = world.getEntities(Player.class).stream().findFirst().orElse(null);
        if (player == null) {
            return;
        }
        PositionPart playerPosition = player.getPart(PositionPart.class);

        for (int i = 0; i < 18; i++) {
            int indexToUpdate = (startIndex + i) % zombies.size(); // Calculate the index of the zombie to update.
            Zombie zombie = zombies.get(indexToUpdate);

            PositionPart zombiePosition = zombie.getPart(PositionPart.class);

            AStar aStar = new AStar();
            int tileSize = gameData.getTileSize();
            String INITIAL_STATE = (int)(zombiePosition.getX() / tileSize) + "," + (int)(zombiePosition.getY() / tileSize);
            String GOAL_STATE = (int)(playerPosition.getX() / tileSize) + "," + (int)(playerPosition.getY() / tileSize);
            zombie.setPathFinding(aStar.treeSearch(map, INITIAL_STATE, GOAL_STATE));
        }
    }

    @Override
    public void moveTowards(GameData gameData, World world) {
        for (Entity zombie : world.getEntities(Zombie.class)) {
            PositionPart zombiePosition = zombie.getPart(PositionPart.class);
            MovingPart zombieMovement = zombie.getPart(MovingPart.class);
            List<int[]> pathFinding = ((Zombie) zombie).getPathFinding();
            //System.out.println(Arrays.deepToString(pathFinding));
            if (pathFinding == null) {
                continue;
            }

            float currentX = (int) zombiePosition.getX();
            float currentY = (int) zombiePosition.getY();

            int targetX;
            int targetY;

            if (pathFinding.size() == 1) {
                 targetX = pathFinding.get(0)[0] * gameData.getTileSize();
                 targetY = pathFinding.get(0)[1] * gameData.getTileSize();
            } else if (pathFinding.size() == 2) {
                targetX = pathFinding.get(1)[0] * gameData.getTileSize();
                targetY = pathFinding.get(1)[1] * gameData.getTileSize();
            } else {
                targetX = pathFinding.get(2)[0] * gameData.getTileSize();
                targetY = pathFinding.get(2)[1] * gameData.getTileSize();

                if (targetX == currentX && targetY == currentY) {
                    pathFinding.remove(2);
                    if (pathFinding.size() >= 3) {
                        targetX = pathFinding.get(2)[0] * gameData.getTileSize();
                        targetY = pathFinding.get(2)[1] * gameData.getTileSize();
                    }
                }
            }

            float diffX = targetX - currentX;
            float diffY = targetY - currentY;

            //System.out.println("DiffX: "+ diffX+" CurrentX: "+currentX+" NextX: "+targetX+" Dx: "+zombieMovement.getDx());
            //System.out.println("DiffY: "+ diffY+" CurrentY: "+currentY+" NextY: "+targetY+" Dy: "+zombieMovement.getDy());


            if (Math.abs(diffX) <= Math.abs(zombieMovement.getDx()*gameData.getDelta())) {
                zombieMovement.setDx(0);
                zombiePosition.setX(currentX);
            } else {
                zombieMovement.setRight(diffX > 0);
                zombieMovement.setLeft(diffX < 0);
            }
            if (Math.abs(diffY) <= Math.abs(zombieMovement.getDy()*gameData.getDelta())) {
                zombieMovement.setDy(0);
                zombiePosition.setY(currentY);
            } else {
                zombieMovement.setUp(diffY > 0);
                zombieMovement.setDown(diffY < 0);
            }

            //System.out.println(Arrays.deepToString(pathFinding));
            zombieMovement.process(gameData, zombie);
            zombiePosition.process(gameData, zombie);

            zombieMovement.setUp(false);
            zombieMovement.setDown(false);
            zombieMovement.setRight(false);
            zombieMovement.setLeft(false);
        }
    }
}
