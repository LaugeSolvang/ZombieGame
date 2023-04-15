package mapsystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.ZombieSPI;
import common.services.IEntityProcessingService;

public class MapProcessor implements IEntityProcessingService {
    MapPlugin mapPlugin = new MapPlugin();

    @Override
    public void process(GameData gameData, World world) {
        if ((gameData.getGameTime() % 15 <= gameData.getDelta()) && (gameData.getGameTime() > 1)) {
            // Spawn a zombie
            for (ZombieSPI zombie : mapPlugin.getZombieSPI()) {
                world.addEntity(zombie.createZombie(12 * 32, 12 * 32));
            }
        }
    }
}
