package zombieaisystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static common.data.GameData.TILE_SIZE;
import static org.junit.jupiter.api.Assertions.*;

class AIProcessorTest {
    private AIProcessor aiProcessor;
    private World world;
    private GameData gameData;
    private Player player;
    private Zombie zombie;
    private PositionPart playerPositionPart;
    private PositionPart zombiePositionPart;
    private MovingPart zombieMovingPart;
    private String[][] map;
    private final float delta = 1/60F;

    @BeforeEach
    public void setUp() {
        world = new World();
        gameData = new GameData();
        gameData.setDelta(delta);
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(800);
        player = new Player();
        zombie = new Zombie();
        playerPositionPart = new PositionPart(10,10,31,63);
        player.add(playerPositionPart);
        zombiePositionPart = new PositionPart(400,400,31,63);
        zombieMovingPart = new MovingPart(10,100,100);
        zombie.add(zombiePositionPart);
        zombie.add(zombieMovingPart);
        aiProcessor = new AIProcessor();

        map = new String[20][20];
        for (int i = 3; i < 8; i++) {
            map[4][i] = "obstruction";
        }
        world.setMap(map);

        world.addEntity(player);
        world.addEntity(zombie);
    }

    @Test
    public void testProcessWithObstructions() {
        aiProcessor.process(gameData, world);

        List<int[]> expectedPath = Arrays.asList(
                new int[]{12, 12},
                new int[]{11, 12},
                new int[]{11, 11},
                new int[]{11, 10},
                new int[]{10, 10},
                new int[]{9, 10},
                new int[]{9, 9},
                new int[]{8, 9},
                new int[]{8, 8},
                new int[]{7, 8},
                new int[]{6, 8},
                new int[]{5, 8},
                new int[]{4, 8},
                new int[]{3, 8},
                new int[]{3, 7},
                new int[]{3, 6},
                new int[]{3, 5},
                new int[]{3, 4},
                new int[]{3, 3},
                new int[]{2, 3},
                new int[]{2, 2},
                new int[]{2, 1},
                new int[]{1, 1},
                new int[]{0, 1},
                new int[]{0, 0}
        );

        // Assuming that Zombie's getPathFinding() method returns List<int[]>
        List<int[]> obstructionPath = zombie.getPathFinding();

        assertEquals(expectedPath.size(), obstructionPath.size());
        for (int i = 0; i < expectedPath.size(); i++) {
            assertArrayEquals(expectedPath.get(i), obstructionPath.get(i));
        }
    }
    @Test
    public void testProcessWithoutObstruction() {
        map = new String[20][20];
        world.setMap(map);
        aiProcessor.process(gameData, world);

        List<int[]> expectedPath = Arrays.asList(
                new int[]{12, 12},
                new int[]{11, 12},
                new int[]{11, 11},
                new int[]{11, 10},
                new int[]{10, 10},
                new int[]{9, 10},
                new int[]{9, 9},
                new int[]{8, 9},
                new int[]{8, 8},
                new int[]{7, 8},
                new int[]{7, 7},
                new int[]{7, 6},
                new int[]{6, 6},
                new int[]{6, 5},
                new int[]{5, 5},
                new int[]{4, 5},
                new int[]{4, 4},
                new int[]{3, 4},
                new int[]{3, 3},
                new int[]{2, 3},
                new int[]{2, 2},
                new int[]{1, 2},
                new int[]{1, 1},
                new int[]{0, 1},
                new int[]{0, 0}
        );

        // Assuming that Zombie's getPathFinding() method returns List<int[]>
        List<int[]> actualPath = zombie.getPathFinding();

        assertEquals(expectedPath.size(), actualPath.size());
        for (int i = 0; i < expectedPath.size(); i++) {
            assertArrayEquals(expectedPath.get(i), actualPath.get(i));
        }
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
        aiProcessor.moveTowards(gameData, zombie);

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
        zombie.setPathFinding(pathFinding);
        zombiePositionPart.setPosition(12*TILE_SIZE, 12*TILE_SIZE);

        // Process move towards
        aiProcessor.moveTowards(gameData, zombie);

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
        aiProcessor.setAITime(2);
        aiProcessor.moveTowards(gameData, zombie);

        // Check if the targetX and targetY are correctly assigned
        assertEquals(12*TILE_SIZE + zombieMovingPart.getDx()*delta, zombiePositionPart.getX(),0.003);
        assertEquals(12*TILE_SIZE + zombieMovingPart.getDy()*delta, zombiePositionPart.getY(),0.003);
    }
    @Test
    public void testMoveTowardsWithNoPath() {
        // No path set
        aiProcessor.moveTowards(gameData, zombie);

        // We expect the zombie not to move
        assertEquals(400, zombiePositionPart.getX());
        assertEquals(400, zombiePositionPart.getY());
    }
    @Test
    public void testProcessWithoutPlayer() {
        // Remove player from the world
        world.removeEntity(player);
        aiProcessor.process(gameData, world);

        // We do not expect a path to be calculated
        assertNull(zombie.getPathFinding());
    }
    @Test
    public void testProcessWithMultipleZombies() {
        // Add additional zombies
        Zombie zombie2 = new Zombie();
        PositionPart zombie2PositionPart = new PositionPart(500,500,31,63);
        MovingPart zombie2MovingPart = new MovingPart(100,100,100);
        zombie2.add(zombie2PositionPart);
        zombie2.add(zombie2MovingPart);
        world.addEntity(zombie2);

        Zombie zombie3 = new Zombie();
        PositionPart zombie3PositionPart = new PositionPart(600,600,31,63);
        MovingPart zombie3MovingPart = new MovingPart(100,100,100);
        zombie3.add(zombie3PositionPart);
        zombie3.add(zombie3MovingPart);
        world.addEntity(zombie3);

        aiProcessor.process(gameData, world);

        // We expect a path to be calculated for each zombie
        assertNotNull(zombie.getPathFinding());
        assertNotNull(zombie2.getPathFinding());
        assertNotNull(zombie3.getPathFinding());
    }
}