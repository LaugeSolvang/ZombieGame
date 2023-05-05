package mapsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.bullet.Bullet;
import common.data.entityparts.PositionPart;

import java.util.Objects;
import java.util.Random;

import static common.data.GameData.TILE_SIZE;

public class MapProcessor implements ValidLocation {
    @Override
    public int[] generateSpawnLocation(World world, GameData gameData) {
        String[][] map = world.getMap();
        Random rand = new Random();
        PositionPart playerPosPart = new PositionPart(gameData.getDisplayWidth()/2, gameData.getDisplayHeight()/2);
        for (Entity player : world.getEntities(Bullet.class)) {
            playerPosPart = player.getPart(PositionPart.class);
        }

        int x;
        int y;

        do {
            x = rand.nextInt(map.length - 1);
            y = rand.nextInt(map[0].length - 1);
        } while (!isValidLocation(map, playerPosPart, x, y));
        return new int[]{x, y};
    }
    private boolean isValidLocation(String[][] map, PositionPart positionPart, int x, int y) {
        int xStart = Math.max(x - 1, 0);
        int xEnd = Math.min(x + 1, map.length - 1);
        int yStart = Math.max(y - 1, 0);
        int yEnd = Math.min(y + 2, map[0].length - 1);
        int pX = (int)(positionPart.getX()/TILE_SIZE);
        int py = (int)(positionPart.getY()/TILE_SIZE);

        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (Objects.equals(map[i][j], "obstruction")) {
                    return false;
                } else if (pX == i && py == j) {
                    System.out.println("spawned on player");
                    return false;
                }
            }
        }
        return true;
    }
}
