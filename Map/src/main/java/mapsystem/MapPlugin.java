package mapsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.obstruction.Obstruction;
import common.data.entities.zombie.ZombieSPI;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class MapPlugin implements IGamePluginService {
    int[][] map;
    @Override
    public void start(GameData gameData, World world)  {
        //Create a map with strings per 32x32 pixels of the whole display
        map = new int[gameData.getDisplayWidth()/32][gameData.getDisplayHeight()/32];

        //Spawn obstructions around the perimeter of the game
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 0 || j == 0 || i == map.length-1 || j == map[0].length-1 || (i == 15 || i == 16) && (j == 7 || j == 8)) {
                    Entity obstruction = createObstruction(i * 32, j * 32);
                    world.addEntity(obstruction);
                    map[i][j] = 1;
                } else {
                    map[i][j] = 0;
                }
            }
        }
        world.setMap(map);

        for (ZombieSPI zombie : getZombieSPI()) {
            world.addEntity(zombie.createZombie(13 * 32, 13 * 32));
        }

    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    private Entity createObstruction(int x, int y) {
        Entity obstruction = new Obstruction();

        String path = "obstruction.png";
        obstruction.setPath(path);
        obstruction.add(new PositionPart(x, y, 0));

        return obstruction;
    }
    protected Collection<? extends ZombieSPI> getZombieSPI() {
        return ServiceLoader.load(ZombieSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}
