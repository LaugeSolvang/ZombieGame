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

import static common.data.GameData.TILE_SIZE;

public class AIProcessor implements IPostEntityProcessingService, IZombieAI {
    private AStar aStar = new AStar();
    private float AITime = 0.0F;
    @Override
    public void process(GameData gameData, World world) {
        AITime += gameData.getDelta();
        List<Zombie> zombies = new ArrayList<>();
        for (Entity entity : world.getEntities(Zombie.class)) {
            zombies.add((Zombie) entity);
        }
        Entity player = world.getEntities(Player.class).stream().findFirst().orElse(null);

        if (player == null || zombies.isEmpty()) {return;}

        int startIndex;
        if (AITime >= 1) {
            startIndex = (int) (AITime * 100) % (int) AITime;
        } else {
            startIndex = 0; // or any other value that you want to use when gameTime is under 1
        }
        String[][] map = world.getMap();
        PositionPart playerPosition = player.getPart(PositionPart.class);


        for (int i = 0; i < 4; i++) {
            int indexToUpdate = (startIndex+i) % zombies.size(); // Calculate the index of the zombie to update.
            Zombie zombie = zombies.get(indexToUpdate);

            PositionPart zombiePosition = zombie.getPart(PositionPart.class);

            String INITIAL_STATE = (int)(zombiePosition.getX() / TILE_SIZE) + "," + (int)(zombiePosition.getY() / TILE_SIZE);
            String GOAL_STATE = (int)(playerPosition.getX() / TILE_SIZE) + "," + (int)(playerPosition.getY() / TILE_SIZE);
            zombie.setPathFinding(aStar.treeSearch(map, INITIAL_STATE, GOAL_STATE));
        }
    }
    @Override
    public void moveTowards(GameData gameData, Entity zombie) {
        PositionPart zombiePosition = zombie.getPart(PositionPart.class);
        MovingPart zombieMovement = zombie.getPart(MovingPart.class);

        List<int[]> pathFinding = ((Zombie) zombie).getPathFinding();
        if (pathFinding == null) {
            return;
        }

        float currentX = (int) zombiePosition.getX();
        float currentY = (int) zombiePosition.getY();

        int targetX;
        int targetY;

        if (pathFinding.size() <= 2) {
            targetX = pathFinding.get(pathFinding.size() - 1)[0] * TILE_SIZE;
            targetY = pathFinding.get(pathFinding.size() - 1)[1] * TILE_SIZE;
        } else {
            targetX = pathFinding.get(2)[0] * TILE_SIZE;
            targetY = pathFinding.get(2)[1] * TILE_SIZE;

            if (targetX == currentX && targetY == currentY) {
                pathFinding.remove(2);
                if (pathFinding.size() >= 3) {
                    targetX = pathFinding.get(2)[0] * TILE_SIZE;
                    targetY = pathFinding.get(2)[1] * TILE_SIZE;
                }
            }
        }

        float diffX = targetX - currentX;
        float diffY = targetY - currentY;
        float delta = gameData.getDelta();

        if (Math.abs(diffX) <= Math.abs(zombieMovement.getDx() * delta)) {
            zombieMovement.setDx(0);
            zombiePosition.setX(currentX);
        } else {
            zombieMovement.setRight(diffX > 0);
            zombieMovement.setLeft(diffX < 0);
        }
        if (Math.abs(diffY) <= Math.abs(zombieMovement.getDy() * delta)) {
            zombieMovement.setDy(0);
            zombiePosition.setY(currentY);
        } else {
            zombieMovement.setUp(diffY > 0);
            zombieMovement.setDown(diffY < 0);
        }

        zombieMovement.process(gameData, zombie);
        zombiePosition.process(gameData, zombie);

        zombieMovement.setUp(false);
        zombieMovement.setDown(false);
        zombieMovement.setRight(false);
        zombieMovement.setLeft(false);
    }

    public void setAITime(float AITime) {
        this.AITime = AITime;
    }
}
