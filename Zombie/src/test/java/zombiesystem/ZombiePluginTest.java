package zombiesystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.Zombie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static common.data.GameKeys.ESCAPE;
import static org.junit.jupiter.api.Assertions.*;

class ZombiePluginTest {
    private ZombiePlugin zombiePlugin;
    private GameData gameData;
    private World world;
    private Zombie zombie;

    @BeforeEach
    public void setUp() {
        zombiePlugin = new ZombiePlugin();
        gameData = new GameData();
        world = new World();
        zombie = new Zombie();
    }

    @Test
    public void zombiePlugin_startAndStop_removesZombieFromWorld() {
        // Arrange
        world.addEntity(zombie);
        assertTrue(world.getEntities(Zombie.class).size() > 0);

        // Act
        zombiePlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Zombie.class).size());

        // Act
        zombiePlugin.start(gameData, world);

        // Assert
        assertTrue(zombiePlugin.isActive);
    }

    @Test
    public void zombiePlugin_stop_removesZombieAndDeactivates() {
        // Arrange
        world.addEntity(zombie);
        assertTrue(world.getEntities(Zombie.class).size() > 0);

        // Act
        zombiePlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Zombie.class).size());
        assertFalse(zombiePlugin.isActive);
    }

    @Test
    public void zombiePlugin_onKeyPress_activatesAndDeactivates() {
        // Arrange
        zombiePlugin.start(gameData, world);
        world.addEntity(zombie);
        assertTrue(world.getEntities(Zombie.class).size() > 0);

        // Act
        zombiePlugin.onKeyPressed(ESCAPE, gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Zombie.class).size());

        // Act
        zombiePlugin.onKeyPressed(ESCAPE, gameData, world);

        // Assert
        assertTrue(zombiePlugin.isActive);
        assertEquals(0, world.getEntities(Zombie.class).size());
    }
}