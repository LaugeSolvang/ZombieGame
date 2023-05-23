package zombieaisystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.PositionPart;
import common.services.IPostEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

import static common.data.GameData.TILE_SIZE;

public class AIProcessor implements IPostEntityProcessingService {
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

            AStar.State INITIAL_STATE = new AStar.State((int)(zombiePosition.getX() / TILE_SIZE), (int)(zombiePosition.getY() / TILE_SIZE));
            AStar.State GOAL_STATE = new AStar.State((int)(playerPosition.getX() / TILE_SIZE), (int)(playerPosition.getY() / TILE_SIZE));
            zombie.setPathFinding(aStar.treeSearch(map, INITIAL_STATE, GOAL_STATE));

        }
    }
}
