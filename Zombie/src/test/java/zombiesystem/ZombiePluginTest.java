package zombiesystem;

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
    public void start_ActivatesPluginAndSetsIsActiveToTrue() {
        zombiePlugin.start(gameData, world);

        assertTrue(zombiePlugin.isActive);
        assertTrue(gameData.isActivePlugin(zombiePlugin.getClass().getName()));
    }

    @Test
    public void stop_DeactivatesPluginAndSetsIsActiveToFalse() {
        zombiePlugin.stop(gameData, world);

        assertFalse(zombiePlugin.isActive);
        assertFalse(gameData.isActivePlugin(zombiePlugin.getClass().getName()));
    }

    @Test
    public void stop_RemovesAllZombiesFromTheWorld() {
        Zombie zombie1 = new Zombie();
        Zombie zombie2 = new Zombie();
        world.addEntity(zombie1);
        world.addEntity(zombie2);

        zombiePlugin.stop(gameData, world);

        assertFalse(world.getEntities().contains(zombie1));
        assertFalse(world.getEntities().contains(zombie2));
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