package main;

import collisionsystem.CollisionProcessor;
import common.data.Entity;
import common.data.GameData;
import common.data.GameKeys;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.player.Player;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.InventoryPart;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;
import common.services.IPostEntityProcessingService;
import common.services.KeyPressListener;
import mapsystem.MapPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import playersystem.PlayerPlugin;
import playersystem.PlayerProcessor;
import weaponsystem.WeaponPlugin;
import weaponsystem.WeaponProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PlayerWeaponTest {
    private GameData gameData;
    private World world;
    private GameLogic gameLogic;
    private GameKeys gameKeys;
    private IGamePluginService playerPlugin;
    private IGamePluginService  weaponPlugin;
    private IEntityProcessingService playerProcessor;
    private IEntityProcessingService weaponProcessor;
    private IPostEntityProcessingService collisionProcessor;
    private KeyPressListener playerkeyPressListener;
    private KeyPressListener mapKeyPressListener;
    private final int displayHeight = 736;
    private final int displayWidth = 1440;
    private List<ValidLocation> validLocations;



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
        weaponPlugin = new WeaponPlugin();
        Collection<IGamePluginService> gamePluginServices = new ArrayList<>();
        gamePluginServices.add(playerPlugin);
        gamePluginServices.add(weaponPlugin);

        // Create processors
        playerProcessor = new PlayerProcessor();
        weaponProcessor = new WeaponProcessor();

        Collection<IEntityProcessingService> entityProcessingServices = new ArrayList<>();
        entityProcessingServices.add(weaponProcessor);
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
        WeaponProcessor weapon = (WeaponProcessor)weaponProcessor;
        ValidLocation validLocation = Mockito.mock(ValidLocation.class);
        when(validLocation.generateSpawnLocation(any(World.class), any(GameData.class))).thenReturn(new int[]{23, 11});
        validLocations = new ArrayList<>();
        validLocations.add(validLocation);
        weapon.setValidLocations(validLocations);
    }
    @Test
    void testPlayerWeaponInteraction() {
        gameLogic.startPluginServices(gameData, world);
        boolean playerPresent = world.getEntities(Player.class).size() > 0;
        assertTrue(playerPresent, "Player entity not found in the world");
        boolean weaponPresent = world.getEntities(Weapon.class).size() > 0;
        assertFalse(weaponPresent, "Map not found in the world");

        gameLogic.update(gameData,world);
        weaponPresent = world.getEntities(Weapon.class).size() > 0;
        assertTrue(weaponPresent, "Map not found in the world");
        gameKeys.setKey(GameKeys.SHIFT, true);
        gameLogic.update(gameData,world);
        for (Entity entity: world.getEntities(Player.class)) {
            Player player = (Player) entity;
            InventoryPart inventory = player.getPart(InventoryPart.class);
            assertNotNull(inventory.getWeapons());
        }
    }
}
