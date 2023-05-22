package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static common.data.GameData.TILE_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ZombieProcessorTest {
    private ZombieProcessor zombieProcessor;
    private GameData gameData;
    private World world;
    private ZombiePlugin zombiePlugin;
    private Zombie zombie;
    private PositionPart zombiePositionPart;
    private MovingPart zombieMovingPart;
    private static final int EXPECTED_X = 23;
    private static final int EXPECTED_Y = 11;
    private static final float delta = 1/60F;


    @BeforeEach
    public void setUp() {
        zombieProcessor = new ZombieProcessor();
        zombiePlugin = new ZombiePlugin();
        gameData = new GameData();
        gameData.setDelta(delta);
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(800);
        world = new World();
        zombiePlugin = new ZombiePlugin();
        zombiePlugin.start(gameData, world);
        ValidLocation validLocation = mock(ValidLocation.class);
        zombieProcessor.setValidLocations(Collections.singleton(validLocation));
        when(validLocation.generateSpawnLocation(any(World.class), any(GameData.class))).thenReturn(new int[]{EXPECTED_X, EXPECTED_Y});
        zombie = new Zombie();
        zombiePositionPart = new PositionPart(400,400,31,63);
        zombieMovingPart = new MovingPart(10,100,100);
        zombie.add(zombiePositionPart);
        zombie.add(zombieMovingPart);
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
            assertEquals(zombie.getPath(), "Zombie/src/main/resources/zombie.png");
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

    @Test
    public void testMoveTowardsWithPathSizeLessThanTwo() {
        // Create a path with size less than or equal to 2
        List<int[]> pathFinding = new ArrayList<>();
        pathFinding.add(new int[]{11, 12});
        pathFinding.add(new int[]{12, 12});

        // Assuming that Zombie's setPathFinding() method sets the path
        zombie.setPathFinding(pathFinding);

        // Process move towards
        zombieProcessor.process(gameData,world);
        // Check if the targetX and targetY are correctly assigned
        assertEquals(400 + zombieMovingPart.getDx()*delta, zombiePositionPart.getX(),0.003);
        assertEquals(400 + zombieMovingPart.getDy()*delta, zombiePositionPart.getY(),0.003);
    }
    @Test
    public void testMoveTowardsWithPathSizeLessThan() {
        // Create a path with size less than or equal to 2
        List<int[]> pathFinding = new ArrayList<>();
        pathFinding.add(new int[]{11, 11});
        pathFinding.add(new int[]{11, 12});
        pathFinding.add(new int[]{12, 12});
        pathFinding.add(new int[]{12, 13});

        // Assuming that Zombie's setPathFinding() method sets the path
        world.addEntity(zombie);
        zombie.setPathFinding(pathFinding);
        zombiePositionPart.setPosition(12*TILE_SIZE, 12*TILE_SIZE);


        // Process move towards
        zombieProcessor.process(gameData, world);

        // Check if the targetX and targetY are correctly assigned
        assertEquals(12*TILE_SIZE + zombieMovingPart.getDx()*delta, zombiePositionPart.getX(),0.003);
        assertEquals(12*TILE_SIZE + zombieMovingPart.getDy()*delta, zombiePositionPart.getY(),0.003);
    }
    @Test
    public void testMoveTowardsWithPathSizeLess() {
        // Create a path with size less than or equal to 2
        List<int[]> pathFinding = new ArrayList<>();
        pathFinding.add(new int[]{11, 11});
        pathFinding.add(new int[]{12, 11});
        pathFinding.add(new int[]{12, 12});

        // Assuming that Zombie's setPathFinding() method sets the path
        zombie.setPathFinding(pathFinding);
        zombiePositionPart.setPosition(12*TILE_SIZE, 12*TILE_SIZE);

        // Process move towards
        zombieProcessor.setZombieTime(2);
        zombieProcessor.process(gameData, world);

        // Check if the targetX and targetY are correctly assigned
        assertEquals(12*TILE_SIZE + zombieMovingPart.getDx()*delta, zombiePositionPart.getX(),0.003);
        assertEquals(12*TILE_SIZE + zombieMovingPart.getDy()*delta, zombiePositionPart.getY(),0.003);
    }
    @Test
    public void testMoveTowardsWithNoPath() {
        // No path set
        zombieProcessor.process(gameData, world);

        // We expect the zombie not to move
        assertEquals(400, zombiePositionPart.getX());
        assertEquals(400, zombiePositionPart.getY());
    }


}