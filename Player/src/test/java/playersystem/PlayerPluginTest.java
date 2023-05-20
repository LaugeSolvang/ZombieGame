package playersystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.InventoryPart;
import common.data.entityparts.PositionPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static common.data.GameKeys.DEL;
import static org.junit.jupiter.api.Assertions.*;

class PlayerPluginTest {
    private PlayerPlugin playerPlugin;
    private GameData gameData;
    private World world;
    private Weapon weapon;

    @BeforeEach
    public void setUp() {
        playerPlugin = new PlayerPlugin();
        gameData = new GameData();
        world = new World();
        weapon = new Weapon("playersystem.PlayerWeapon", 25, 5);
        weapon.add(new PositionPart(10,10));
    }

    @Test
    public void playerPlugin_startAndStop_removesPlayerFromWorld() {
        // Arrange
        playerPlugin.start(gameData, world);
        assertTrue(world.getEntities(Player.class).size() > 0);

        // Act
        playerPlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Player.class).size());

        // Act
        playerPlugin.start(gameData, world);

        // Assert
        assertTrue(playerPlugin.isActive);
    }
    @Test
    public void playerPlugin_stop_removesPlayerAndDeactivates() {
        // Arrange
        playerPlugin.start(gameData, world);
        assertTrue(world.getEntities(Player.class).size() > 0);
        setupPlayerWithWeapon();

        // Act
        playerPlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Player.class).size());
        assertFalse(playerPlugin.isActive);
        assertTrue(playerInventoryIsEmpty());
    }

    @Test
    public void playerPlugin_onKeyPress_activatesAndDeactivates() {
        // Arrange
        playerPlugin.start(gameData, world);
        assertTrue(world.getEntities(Player.class).size() > 0);
        setupPlayerWithWeapon();

        // Act
        playerPlugin.onKeyPressed(DEL, gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Player.class).size());
        assertTrue(playerInventoryIsEmpty());

        // Act
        playerPlugin.onKeyPressed(DEL, gameData, world);

        // Assert
        assertTrue(playerPlugin.isActive);
        assertEquals(1, world.getEntities(Player.class).size());
        assertFalse(playerHasWeapon());
    }

    private void setupPlayerWithWeapon() {
        Player player = (Player) world.getEntities(Player.class).get(0);
        InventoryPart inventory = player.getPart(InventoryPart.class);
        inventory.addWeapon(world, weapon);
    }

    private boolean playerInventoryIsEmpty() {
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Player.class) {
                InventoryPart inventory = e.getPart(InventoryPart.class);
                return inventory.getWeapons().isEmpty();
            }
        }
        return true;
    }

    private boolean playerHasWeapon() {
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Player.class) {
                InventoryPart inventory = e.getPart(InventoryPart.class);
                return !inventory.getWeapons().isEmpty();
            }
        }
        return false;
    }

}