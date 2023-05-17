package bulletsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entities.zombie.Zombie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletPluginTest {
    private GameData gameData;
    private World world;
    private BulletPlugin plugin;

    @BeforeEach
    public void setUp() {
        gameData = new GameData();
        world = new World();
        plugin = new BulletPlugin();
    }

    @Test
    public void stopRemovesAllBulletsFromWorld() {
        // Given
        Entity bullet1 = new Bullet();
        Entity bullet2 = new Bullet();
        Entity zombie = new Zombie();

        world.addEntity(bullet1);
        world.addEntity(zombie);
        world.addEntity(bullet2);

        // When
        plugin.stop(gameData, world);

        // Then
        assertEquals(1, world.getEntities().size());
        assertEquals(zombie, world.getEntities().stream().findFirst().orElse(null));
    }

    @Test
    public void stop_does_not_remove_non_bullets_from_world() {
        // Given
        Entity zombie1 = new Zombie();
        Entity zombie2 = new Zombie();

        world.addEntity(zombie1);
        world.addEntity(zombie2);

        // When
        plugin.stop(gameData, world);

        // Then
        assertEquals(2, world.getEntities().size());
        assertTrue(world.getEntities().contains(zombie1));
        assertTrue(world.getEntities().contains(zombie2));
    }

    @Test
    public void start_does_not_throw_exception() {
        // Given

        // When
        plugin.start(gameData, world);

        // Then (no exception should be thrown)
    }
}