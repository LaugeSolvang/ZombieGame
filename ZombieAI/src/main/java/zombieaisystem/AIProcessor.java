package zombieaisystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.IZombieAI;
import common.services.IPostEntityProcessingService;

public class AIProcessor implements IPostEntityProcessingService, IZombieAI {
    @Override
    public void process(GameData gameData, World world) {

    }

    @Override
    public void moveTowards(GameData gameData, World world) {

    }
}
