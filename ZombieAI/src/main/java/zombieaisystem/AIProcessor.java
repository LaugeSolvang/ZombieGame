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
import java.util.Arrays;
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
        int startIndex = (int) (gameData.getGameTime() / 0.5) % zombies.size(); // Calculate the start index based on the time elapsed and the time interval (200 milliseconds).
        String[][] map = world.getMap();
        Entity player = world.getEntities(Player.class).stream().findFirst().orElse(null);
        if (player == null) {
            return;
        }
        PositionPart playerPosition = player.getPart(PositionPart.class);

        for (int i = 0; i < 5; i++) {
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
            int[][] pathFinding = ((Zombie) zombie).getPathFinding();
            //System.out.println(Arrays.deepToString(pathFinding));
            if (pathFinding == null) {
                continue;
            }

            float currentX = (int) zombiePosition.getX();
            float currentY = (int) zombiePosition.getY();
            int nextX = pathFinding[1][0] * gameData.getTileSize();
            int nextY = pathFinding[1][1] * gameData.getTileSize();
            float diffX = nextX - currentX;
            float diffY = nextY - currentY;

            //System.out.println("DiffX: "+ diffX+" CurrentX: "+currentX+" NextX: "+nextX);
            //System.out.println("DiffY: "+ diffY+" CurrentY: "+currentY+" NextY: "+nextY);


            float threshold = 0;

            if (Math.abs(diffX) > threshold) {
                zombieMovement.setRight(diffX > 0);
                zombieMovement.setLeft(diffX < 0);
            } else {
                zombieMovement.setRight(false);
                zombieMovement.setLeft(false);
                zombieMovement.setDx(0);
            }

            if (Math.abs(diffY) > threshold) {
                zombieMovement.setUp(diffY > 0);
                zombieMovement.setDown(diffY < 0);
            } else {
                zombieMovement.setDown(false);
                zombieMovement.setUp(false);
                zombieMovement.setDy(0);
            }

            zombieMovement.process(gameData, zombie);
            zombiePosition.process(gameData, zombie);
        }
    }
}
