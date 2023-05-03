package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.Zombie;
import common.services.IGamePluginService;

public class ZombiePlugin implements IGamePluginService {
    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Zombie.class) {
                world.removeEntity(e);
            }
        }
    }
}
