package zombiesystem;

import common.data.Entity;
import common.data.entities.obstruction.Obstruction;
import org.junit.Test;
import org.junit.Before;
import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.Zombie;

import static common.data.GameKeys.ESCAPE;
import static org.junit.Assert.*;

public class ZombiePluginTest {

    private ZombiePlugin zombiePlugin;
    private GameData gameData;
    private World world;

    @Before
    public void setUp() {
        zombiePlugin = new ZombiePlugin();
        gameData = new GameData();
        world = new World();
    }

    @Test
    public void testStartSetsPluginActiveInGameData() {
        zombiePlugin.start(gameData, world);

        assertTrue(gameData.isActivePlugin(zombiePlugin.getClass().getName()));
    }

    @Test
    public void testStopSetsPluginInactiveInGameData() {
        zombiePlugin.stop(gameData, world);

        assertFalse(gameData.isActivePlugin(zombiePlugin.getClass().getName()));
    }

    @Test
    public void testStopRemovesZombiesFromWorld() {
        Entity zombieEntity = new Zombie();
        world.addEntity(zombieEntity);

        zombiePlugin.stop(gameData, world);

        assertFalse(world.getEntities().contains(zombieEntity));
    }

    @Test
    public void testOnKeyPressed() {
        assertTrue(zombiePlugin.isActive);

        zombiePlugin.onKeyPressed(ESCAPE, gameData, world);
        assertFalse(zombiePlugin.isActive);

        zombiePlugin.onKeyPressed(ESCAPE, gameData, world);
        assertTrue(zombiePlugin.isActive);
    }
}