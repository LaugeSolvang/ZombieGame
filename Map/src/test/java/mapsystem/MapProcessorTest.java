package mapsystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entityparts.PositionPart;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static common.data.GameData.TILE_SIZE;
import static org.junit.Assert.*;

public class MapProcessorTest {

    private MapProcessor mapProcessor;
    private String[][] map;
    private World world;
    private GameData gameData;
    private PositionPart playerPosPart;

    @Before
    public void setUp() {
        mapProcessor = new MapProcessor();
        world = new World();
        gameData = new GameData();
        playerPosPart = new PositionPart(400.f, 300.f);
        map = new String[][] {
                {"", "", "", ""},
                {"", "obstruction", "", ""},
                {"", "", "", ""},
                {"", "", "", ""}
        };
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
        bullet.add(playerPosPart);
        world.addEntity(bullet);

        int[] spawnLocation = mapProcessor.generateSpawnLocation(world, gameData);

        assertNotNull(spawnLocation);
        assertTrue(spawnLocation[0] >= 0 && spawnLocation[0] < map.length);
        assertTrue(spawnLocation[1] >= 0 && spawnLocation[1] < map[0].length);
        assertTrue(isValidLocation(map, playerPosPart, spawnLocation[0], spawnLocation[1]));
    }

    @Test
    public void testIsValidLocation_WhenObstruction_ReturnsFalse() {

        // if there is an obstruction, test passes
        boolean validLocation = isValidLocation(map, playerPosPart, 1, 1);

        assertFalse(validLocation);
    }

    @Test
    public void testIsValidLocation_WhenNoObstructions_ReturnsTrue() {

        // if there is no obstruction, test passes
        boolean validLocation = isValidLocation(map, playerPosPart, 1, 3);

        assertTrue(validLocation);
    }

    private boolean isValidLocation(String[][] map, PositionPart playerPosPart, int x, int y) {
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