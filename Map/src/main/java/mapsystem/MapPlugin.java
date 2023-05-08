package mapsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.obstruction.Obstruction;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;

public class MapPlugin implements IGamePluginService {
    private String[][] map;

    @Override
    public void start(GameData gameData, World world)  {
        //Create a map with strings per 32x32 pixels of the whole display
        int mapWidth = gameData.getDisplayWidth() / 32;
        int mapHeight = gameData.getDisplayHeight() / 32;
        map = new String[mapWidth][mapHeight];
        // Spawn obstructions around the perimeter of the game
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (isObstructionPosition(i, j)) {
                    Entity obstruction = createObstruction(i * 32, j * 32);
                    world.addEntity(obstruction);
                    map[i][j] = "obstruction";
                }
            }
        }
        world.setMap(map);
    }

    private boolean isObstructionPosition(int i, int j) {
        int thirdLength = map.length / 3;
        int twoThirdHeight = map[0].length - (thirdLength + 3);
        int twoThirdLength = map.length - (thirdLength + 3);

        return i == 0 || j == 0 || i == map.length - 1 || j == map[0].length - 1
                || isInnerObstruction(i, j, thirdLength, thirdLength)
                || isInnerObstruction(i, j, twoThirdLength, thirdLength)
                || isInnerObstruction(i, j, thirdLength, twoThirdHeight)
                || isInnerObstruction(i, j, twoThirdLength, twoThirdHeight);
    }

    private boolean isInnerObstruction(int i, int j, int offsetX, int offsetY) {
        int innerObstructionSize = 3;
        return i >= offsetX && i < offsetX + innerObstructionSize
                && j >= offsetY && j < offsetY + innerObstructionSize;
    }
    private Entity createObstruction(int x, int y) {
        Entity obstruction = new Obstruction();

        String path = "obstruction.png";
        obstruction.setPath(path);
        obstruction.add(new PositionPart(x, y));

        return obstruction;
    }
    @Override
    public void stop(GameData gameData, World world) {
        for (Entity obstruction : world.getEntities(Obstruction.class)) {
            world.removeEntity(obstruction);
        }
        int mapWidth = gameData.getDisplayWidth() / 32;
        int mapHeight = gameData.getDisplayHeight() / 32;
        map = new String[mapWidth][mapHeight];
        world.setMap(map);
    }
}
