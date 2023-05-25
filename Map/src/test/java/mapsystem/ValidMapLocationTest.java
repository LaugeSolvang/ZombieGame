package mapsystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entityparts.PositionPart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static common.data.GameData.TILE_SIZE;
import static org.junit.jupiter.api.Assertions.*;

public class ValidMapLocationTest {
    private ValidMapLocation validMapLocation;
    private GameData gameData;
    private World world;
    private Player player;
    private String[][] emptyMap;
    private String[][] obstructionMap;
    private PositionPart positionPart;
    private final int playerX = 15;
    private final int playerY = 15;

    @BeforeEach
    public void setUp() {
        validMapLocation = new ValidMapLocation();
        gameData = new GameData();
        world = new World();
        player = new Player();
        positionPart = new PositionPart(TILE_SIZE*playerX, TILE_SIZE*playerY);
        player.add(positionPart);
        world.addEntity(player);
        emptyMap = new String[20][20];
        obstructionMap = new String[20][20];
        for (int i = 0; i < emptyMap.length; i++) {
            for (int j = 0; j < emptyMap[i].length; j++) {
                if (i == 3 | i == 4 | i == 5 | i == 6 |
                    j == 3 | j == 4 | j == 5 | j == 6) {
                    emptyMap[i][j] = "obstruction";
                }
            }
        }

    }

    @Test
    public void generateSpawnLocation_withValidPlayerLocation_returnsValidSpawnLocation() {
        //Arrange
        world.setMap(obstructionMap);

        // Act
        int[] spawnLocation = validMapLocation.generateSpawnLocation(world, gameData);

        // Assert
        assertNotNull(spawnLocation);
        assertEquals(2, spawnLocation.length);
        assertTrue(validMapLocation.isValidLocation(obstructionMap, player.getPart(PositionPart.class), spawnLocation[0], spawnLocation[1]));
    }
    @Test
    public void generateSpawnLocation_withInvalidPlayerLocation_returnsNull() {
        // Arrange
        world.setMap(emptyMap);
        for (String[] strings : emptyMap) {
            Arrays.fill(strings, "obstruction");
        }

        // Act
        int[] spawnLocation = validMapLocation.generateSpawnLocation(world, gameData);

        // Assert
        assertNull(spawnLocation);
    }
    @Test
    public void isValidLocation_withValidLocation_returnsTrue() {
        //Arrange
        world.setMap(obstructionMap);
        // Act
        boolean isValid = validMapLocation.isValidLocation(obstructionMap, positionPart, 8, 8);

        // Assert
        assertTrue(isValid);
    }
    @Test
    public void isValidLocation_withValidLocation_returnsFalse_player() {
        //Arrange
        world.setMap(obstructionMap);
        // Act
        boolean isValid = validMapLocation.isValidLocation(obstructionMap, positionPart, playerX-5, playerY-5);

        // Assert
        assertFalse(isValid);
    }
    @Test
    public void isValidLocation_withInvalidLocation_returnsFalse_obstruction() {
        // Arrange
        world.setMap(obstructionMap);

        // Act
        boolean isValid = validMapLocation.isValidLocation(emptyMap, positionPart, 3, 3);

        // Assert
        assertFalse(isValid);
    }
}