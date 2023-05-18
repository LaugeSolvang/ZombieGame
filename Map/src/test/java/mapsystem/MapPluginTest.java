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
        gameData.setDisplayWidth(640);
        gameData.setDisplayHeight(480);
        world = new World();
        world.setMap(new String[gameData.getDisplayWidth() / 32][gameData.getDisplayHeight() / 32]);
    }

    @Test
    public void testStart_MapCreation() {
        mapPlugin.start(gameData, world);

        assertNotNull(world.getMap());
        assertEquals(gameData.getDisplayWidth() / 32, world.getMap().length);
        assertEquals(gameData.getDisplayHeight() / 32, world.getMap()[0].length);
    }

    @Test
    public void testStart_ObstructionSpawned() {
        mapPlugin.start(gameData, world);

        assertTrue(world.getEntities(Obstruction.class).size() > 0);
    }

    @Test
    public void testStop_ObstructionRemoved() {
        mapPlugin.start(gameData, world);

        mapPlugin.stop(gameData, world);

        assertEquals(0, world.getEntities(Obstruction.class).size());
    }
    @Test
    public void testOnKeyPressed() {
        // Initially active
        assertTrue(mapPlugin.isActive);

        // Key press should deactivate
        mapPlugin.onKeyPressed(NINE, gameData, world);
        assertEquals(0, world.getEntities(Obstruction.class).size());
        assertFalse(mapPlugin.isActive);

        // Key press again should reactivate
        mapPlugin.onKeyPressed(NINE, gameData, world);
        assertTrue(mapPlugin.isActive);
        assertTrue(world.getEntities(Obstruction.class).size() > 0);
    }
}