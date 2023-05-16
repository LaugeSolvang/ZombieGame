package mapsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.bullet.Bullet;
import common.data.entityparts.PositionPart;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MapProcessorTest {

    private MapProcessor mapProcessor;
    private World world;
    private GameData gameData;

    @Before
    public void setUp() {
        mapProcessor = new MapProcessor();
        world = new World();
        gameData = new GameData();
    }

    @Test
    public void testGenerateSpawnLocation() {
        String[][] map = {
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""},
                {"", "", "", ""}
        };
        world.setMap(map);
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);

        Bullet bullet = new Bullet();
        PositionPart bulletPosPart = new PositionPart(400.0f, 300.0f);
        bullet.add(bulletPosPart);
        world.addEntity(bullet);

        int[] spawnLocation = mapProcessor.generateSpawnLocation(world, gameData);

        assertNotNull(spawnLocation);
        assertTrue(spawnLocation[0] >= 0 && spawnLocation[0] < map.length);
        assertTrue(spawnLocation[1] >= 0 && spawnLocation[1] < map[0].length);
        assertTrue(isValidLocation(map, bulletPosPart, spawnLocation[0], spawnLocation[1]));
    }

    @Test
    public void testIsValidLocation() {
        String[][] map = {
                {"", "", "", ""},
                {"", "obstruction", "", ""},
                {"", "", "", ""},
                {"", "", "", ""}
        };
        PositionPart playerPosPart = new PositionPart(400.0f, 300.0f);

        assertFalse(isValidLocation(map, playerPosPart, 1, 1));
        assertTrue(isValidLocation(map, playerPosPart, 2, 2));
    }

    private boolean isValidLocation(String[][] map, PositionPart playerPosPart, int x, int y) {
        int xStart = Math.max(x - 1, 0);
        int xEnd = Math.min(x + 1, map.length - 1);
        int yStart = Math.max(y - 1, 0);
        int yEnd = Math.min(y + 2, map[0].length - 1);

        // 3x3 grid checking for obstructions
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (map[i][j].equals("obstruction")) {
                    return false;
                }
            }
        }

        xStart = Math.max(x - 5, 0);
        xEnd = Math.min(x + 5, map.length - 1);
        yStart = Math.max(y - 5, 0);
        yEnd = Math.min(y + 6, map[0].length - 1);
        int pX = (int) (playerPosPart.getX() / GameData.TILE_SIZE);
        int pY = (int) (playerPosPart.getY() / GameData.TILE_SIZE);

        // 10x10 grid checking for player
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (pX == i &&
                        pY == j) {
                    return false;
                }
            }
        }
        return true;
    }
}