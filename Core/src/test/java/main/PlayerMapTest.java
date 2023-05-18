package main;

import collisionsystem.CollisionProcessor;
import common.data.Entity;
import common.data.GameData;
import common.data.GameKeys;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;
import common.services.IPostEntityProcessingService;
import common.services.KeyPressListener;
import common.utility.ImageDimension;
import mapsystem.MapPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import playersystem.PlayerPlugin;
import playersystem.PlayerProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerMapTest {
    private GameData gameData;
    private World world;
    private GameLogic gameLogic;
    private GameKeys gameKeys;
    private IGamePluginService playerPlugin;
    private IGamePluginService  mapPlugin;
    private IEntityProcessingService playerProcessor;
    private IPostEntityProcessingService collisionProcessor;
    private KeyPressListener playerkeyPressListener;
    private KeyPressListener mapKeyPressListener;
    private final int displayHeight = 736;
    private final int displayWidth = 1440;

    @BeforeEach
    void setUp() {
        // Initialize gameData and world
        gameData = new GameData();

        gameData.setDelta((float) 1 /60);
        gameData.setDisplayHeight(displayHeight);
        gameData.setDisplayWidth(displayWidth);

        world = new World();
        gameKeys = new GameKeys();

        //Create plugins
        playerPlugin = new PlayerPlugin();
        mapPlugin = new MapPlugin();
        Collection<IGamePluginService> gamePluginServices = new ArrayList<>();
        gamePluginServices.add(playerPlugin);
        gamePluginServices.add(mapPlugin);

        // Create processors
        playerProcessor = new PlayerProcessor();
        Collection<IEntityProcessingService> entityProcessingServices = new ArrayList<>();
        entityProcessingServices.add(playerProcessor);

        //Create postprocessors
        collisionProcessor = new CollisionProcessor();
        Collection<IPostEntityProcessingService> postEntityProcessingServices = new ArrayList<>();
        postEntityProcessingServices.add(collisionProcessor);

        //Create keyPressListeners
        playerkeyPressListener = new PlayerPlugin();
        mapKeyPressListener = new MapPlugin();
        Collection<KeyPressListener> keyPressListeners = new ArrayList<>();
        keyPressListeners.add(playerkeyPressListener);
        keyPressListeners.add(mapKeyPressListener);

        // Initialize GameLogic with the above services
        gameLogic = new GameLogic(
                gamePluginServices,
                entityProcessingServices,
                postEntityProcessingServices,
                keyPressListeners);
        int value[] = ImageDimension.getDimensions("Common/src/main/resources/sprites/weapon.png");
        System.out.println(value[0]);
    }

    @Test
    void testPlayerMapInteraction() {
        // Start the plugin services and perform an update
        gameLogic.startPluginServices(gameData, world);
        // Check if a player and a weapon are present in the world
        boolean playerPresent = world.getEntities(Player.class).size() > 0;
        assertTrue(playerPresent, "Player entity not found in the world");
        assertTrue(world.getMap()!= null, "Map not found in the world");

        for (Entity obstruction: world.getEntities()) {
            PositionPart positionPart = obstruction.getPart(PositionPart.class);
            positionPart.setDimension(new int[]{32,32});
        }
        List<Entity> player = world.getEntities(Player.class);
        PositionPart playerPositionPart = player.get(0).getPart(PositionPart.class);
        playerPositionPart.setDimension(new int[]{31,61});
        MovingPart playerMovingPart = player.get(0).getPart(MovingPart.class);

        assertEquals((int)playerPositionPart.getX(),displayWidth/2);
        assertEquals((int)playerPositionPart.getY(),displayHeight/2);

        do {
            gameKeys.setKey(GameKeys.RIGHT, true);
            gameKeys.setKey(GameKeys.UP, true);
            gameLogic.update(gameData, world);
        }
        while (playerMovingPart.getDx() >= 0);
        assertEquals((int)playerPositionPart.getX(),831);
        assertEquals((int)playerPositionPart.getY(),481);
        gameKeys.setKey(GameKeys.NINE, true);
        gameLogic.checkForUserInput(gameData, world);

        System.out.println(playerMovingPart.getDx());

        do {
            gameKeys.setKey(GameKeys.RIGHT, true);
            gameKeys.setKey(GameKeys.UP, true);
            gameLogic.update(gameData, world);
            System.out.println(playerMovingPart.getDx());
        }
        while (playerMovingPart.getDx() < 70F);

        assertNotEquals((int)playerPositionPart.getX(),831);
        assertNotEquals((int)playerPositionPart.getY(),481);
    }
}
