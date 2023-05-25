package mapsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.player.Player;
import common.data.entityparts.PositionPart;

import java.util.Objects;
import java.util.Random;

import static common.data.GameData.TILE_SIZE;

public class ValidMapLocation implements ValidLocation {
    @Override
    public int[] generateSpawnLocation(World world, GameData gameData) {
        String[][] map = world.getMap();
        Random rand = new Random();
        PositionPart playerPosPart = new PositionPart(gameData.getDisplayWidth()/2, gameData.getDisplayHeight()/2);
        for (Entity player : world.getEntities(Player.class)) {
            playerPosPart = player.getPart(PositionPart.class);
        }

        int x;
        int y;
        int attempts = 0;
        int maxAttempts = 20;

        do {
            x = rand.nextInt(map.length - 1);
            y = rand.nextInt(map[0].length - 1);
            attempts++;
            if (attempts >= maxAttempts) {
                return null;
            }
        } while (!isValidLocation(map, playerPosPart, x, y));
        return new int[]{x, y};
    }
     boolean isValidLocation(String[][] map, PositionPart playerPosPart, int x, int y) {
        int xStart = Math.max(x - 1, 0);
        int xEnd = Math.min(x + 1, map.length - 1);
        int yStart = Math.max(y - 1, 0);
        int yEnd = Math.min(y + 2, map[0].length - 1);

        //3x3 grid checking for obstructions
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (Objects.equals(map[i][j], "obstruction")) {
                    return false;
                }
            }
        }
        xStart = Math.max(x - 5, 0);
        xEnd = Math.min(x + 5, map.length - 1);
        yStart = Math.max(y - 5, 0);
        yEnd = Math.min(y + 6, map[0].length - 1);
        int pX = (int)(playerPosPart.getX()/TILE_SIZE);
        int py = (int)(playerPosPart.getY()/TILE_SIZE);

        //10x10 grid checking for player
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (pX == i && py == j) {
                    return false;
                }
            }
        }
        return true;
    }
}
