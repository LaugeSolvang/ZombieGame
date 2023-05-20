package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.zombie.IZombieAI;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ZombieProcessorTest {
    private ZombieProcessor zombieProcessor;
    private GameData gameData;
    private World world;
    private ZombiePlugin zombiePlugin;
    private ValidLocation validLocation;
    private IZombieAI iZombieAI;
    private static final int EXPECTED_X = 23;
    private static final int EXPECTED_Y = 11;


    @BeforeEach
    public void setUp() {
        zombieProcessor = new ZombieProcessor();
        zombiePlugin = new ZombiePlugin();
        gameData = new GameData();
        gameData.setDelta(1/60F);
        world = new World();
        zombiePlugin = new ZombiePlugin();
        zombiePlugin.start(gameData, world);
        validLocation = mock(ValidLocation.class);
        iZombieAI = mock(IZombieAI.class);

        zombieProcessor.setValidLocations(Collections.singleton(validLocation));
        zombieProcessor.setiZombieAIS(Collections.singleton(iZombieAI));
        when(validLocation.generateSpawnLocation(any(World.class), any(GameData.class))).thenReturn(new int[]{EXPECTED_X, EXPECTED_Y});
        doAnswer(invocation -> {
            Entity zombie = invocation.getArgument(1);
            MovingPart movingPart = zombie.getPart(MovingPart.class);
            movingPart.setDx(2);
            return null;  // return is needed even though this is for a void method
        }).when(iZombieAI).moveTowards(any(GameData.class),any(Entity.class));

    }

    @Test
    public void zombieProcessor_spawnZombies() {
        // Arrange
        int initialZombieCount = world.getEntities(Zombie.class).size();

        // Act
        zombieProcessor.process(gameData, world);
        int postProcessZombieCount = world.getEntities(Zombie.class).size();

        // Assert
        assertTrue(postProcessZombieCount > initialZombieCount);
        for (Entity zombie: world.getEntities(Zombie.class)) {
            LifePart lifePart = zombie.getPart(LifePart.class);
            assertNotNull(lifePart);
            MovingPart movingPart = zombie.getPart(MovingPart.class);
            assertNotNull(movingPart);
            DamagePart damagePart = zombie.getPart(DamagePart.class);
            assertNotNull(damagePart);
            PositionPart positionPart = zombie.getPart(PositionPart.class);
            assertNotNull(positionPart);
            assertEquals(zombie.getPath(), "Zombie/src/main/resources/zombie-kopi.png");
        }
    }
    @Test
    public void zombieProcessor_spawnNoZombies() {
        // Arrange
        int initialZombieCount = world.getEntities(Zombie.class).size();
        zombiePlugin.stop(gameData,world);

        // Act
        zombieProcessor.process(gameData, world);
        int postProcessZombieCount = world.getEntities(Zombie.class).size();

        // Assert
        assertEquals(postProcessZombieCount, initialZombieCount);
    }


}