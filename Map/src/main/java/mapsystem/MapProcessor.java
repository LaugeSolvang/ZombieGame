package mapsystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;

import java.util.Objects;
import java.util.Random;

public class MapProcessor implements ValidLocation {
    @Override
    public int[] generateSpawnLocation(World world, GameData gameData) {
        String[][] map = world.getMap();
        Random rand = new Random();

        int x;
        int y;

        do {
            x = rand.nextInt(map.length - 1);
            y = rand.nextInt(map[0].length - 1);
        } while (!isValidLocation(map, x, y));
        return new int[]{x, y};
    }
    private boolean isValidLocation(String[][] map, int x, int y) {
        int xStart = Math.max(x - 1, 0);
        int xEnd = Math.min(x + 1, map.length - 1);
        int yStart = Math.max(y - 1, 0);
        int yEnd = Math.min(y + 2, map[0].length - 1);

        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (Objects.equals(map[i][j], "obstruction")) {
                    return false;
                }
            }
        }
        return true;
    }
}
