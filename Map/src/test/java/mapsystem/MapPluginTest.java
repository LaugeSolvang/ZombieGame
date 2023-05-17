package mapsystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.obstruction.Obstruction;
import org.junit.Before;
import org.junit.Test;

import static common.data.GameKeys.NINE;
import static org.junit.Assert.*;

public class MapPluginTest {

    private MapPlugin mapPlugin;
    private GameData gameData;
    private World world;

    @Before
    public void setUp() {
        mapPlugin = new MapPlugin();
        gameData = new GameData();
        gameData.setDisplayWidth(640);
        gameData.setDisplayHeight(480);
        world = new World();
        world.setMap(new String[gameData.getDisplayWidth() / 32][gameData.getDisplayHeight() / 32]);
    }

    @Test
    public void testStart_MapCreation() {
        // Act
        mapPlugin.start(gameData, world);

        // Assert
        assertNotNull(world.getMap());
        assertEquals(gameData.getDisplayWidth() / 32, world.getMap().length);
        assertEquals(gameData.getDisplayHeight() / 32, world.getMap()[0].length);
    }

    @Test
    public void testStart_ObstructionSpawned() {
        // Act
        mapPlugin.start(gameData, world);

        // Assert
        assertTrue(world.getEntities(Obstruction.class).size() > 0);
    }

    @Test
    public void testStop_ObstructionRemoved() {
        // Arrange
        mapPlugin.start(gameData, world);

        // Act
        mapPlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Obstruction.class).size());
    }
}