package mapsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.obstruction.Obstruction;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;

public class MapPlugin implements IGamePluginService {
    String[][] map;
    @Override
    public void start(GameData gameData, World world)  {
        //Create a map with strings per 32x32 pixels of the whole display
        map = new String[gameData.getDisplayWidth() / 32][gameData.getDisplayHeight() / 32];

        int thirdLength = map.length / 3;
        int twoThirdHeight = map[0].length - (thirdLength + 3);
        int twoThirdLength = map.length - (thirdLength + 3);

        // Spawn obstructions around the perimeter of the game
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 0 || j == 0 || i == map.length-1 || j == map[0].length-1 ||
                        (i >= thirdLength && i < thirdLength + 3 && j >= thirdLength && j < thirdLength + 3) ||
                        (i >= twoThirdLength && i < twoThirdLength + 3 && j >= thirdLength && j < thirdLength + 3) ||
                        (i >= thirdLength && i < thirdLength + 3 && j >= twoThirdHeight && j < twoThirdHeight + 3) ||
                        (i >= twoThirdLength && i < twoThirdLength + 3 && j >= twoThirdHeight && j < twoThirdHeight + 3)) {
                    Entity obstruction = createObstruction(i * 32, j * 32);
                    world.addEntity(obstruction);
                    map[i][j] = "obstruction";
                }
            }
        }
        world.setMap(map);
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
}
