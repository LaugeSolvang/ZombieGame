package mapsystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.obstruction.Obstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static common.data.GameKeys.NINE;
import static org.junit.jupiter.api.Assertions.*;


public class MapPluginTest {
    private MapPlugin mapPlugin;
    private GameData gameData;
    private World world;

    @BeforeEach
    public void setUp() {
        mapPlugin = new MapPlugin();
        gameData = new GameData();
        gameData.setDisplayHeight(736);
        gameData.setDisplayWidth(1440);
        world = new World();
    }

    @Test
    public void mapPlugin_startAndStop_createsAndRemovesObstructions() {
        // Arrange
        mapPlugin.start(gameData, world);
        assertTrue(world.getEntities(Obstruction.class).size() > 0);

        // Act
        mapPlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Obstruction.class).size());

        // Act
        mapPlugin.start(gameData, world);

        // Assert
        assertTrue(mapPlugin.isActive);
    }

    @Test
    public void mapPlugin_stop_removesObstructionsAndDeactivates() {
        // Arrange
        mapPlugin.start(gameData, world);
        assertTrue(world.getEntities(Obstruction.class).size() > 0);

        // Act
        mapPlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Obstruction.class).size());
        assertFalse(mapPlugin.isActive);
    }

    @Test
    public void mapPlugin_onKeyPress_activatesAndDeactivates() {
        // Arrange
        mapPlugin.start(gameData, world);
        assertTrue(world.getEntities(Obstruction.class).size() > 0);

        // Act
        mapPlugin.onKeyPressed(NINE, gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Obstruction.class).size());

        // Act
        mapPlugin.onKeyPressed(NINE, gameData, world);

        // Assert
        assertTrue(mapPlugin.isActive);
        assertEquals(168, world.getEntities(Obstruction.class).size());
    }
}