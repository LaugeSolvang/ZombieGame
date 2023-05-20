package weaponsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.InventoryPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static common.data.GameKeys.ENTER;
import static org.junit.jupiter.api.Assertions.*;

class WeaponPluginTest {
    private static final String WEAPON_ID = "weaponsystem.WeaponProcessor";
    private static final int WEAPON_DAMAGE = 50;
    private static final int FIRE_RATE = 10;

    private WeaponPlugin weaponPlugin;
    private GameData gameData;
    private World world;
    private Weapon weapon;

    @BeforeEach
    public void setUp() {
        weaponPlugin = new WeaponPlugin();
        gameData = new GameData();
        world = new World();
        weapon = new Weapon(WEAPON_ID, WEAPON_DAMAGE, FIRE_RATE);
    }

    @Test
    public void riflePlugin_startAndStop_removesWeaponFromWorld() {
        // Arrange
        world.addEntity(weapon);
        assertTrue(world.getEntities(Weapon.class).size() > 0);

        // Act
        weaponPlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Weapon.class).size());

        // Act
        weaponPlugin.start(gameData, world);

        // Assert
        assertTrue(weaponPlugin.isActive);
    }

    @Test
    public void riflePlugin_stop_removesWeaponAndDeactivates() {
        // Arrange
        world.addEntity(weapon);
        assertTrue(world.getEntities(Weapon.class).size() > 0);
        setupPlayerWithWeapon();

        // Act
        weaponPlugin.stop(gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Weapon.class).size());
        assertFalse(weaponPlugin.isActive);
        assertTrue(playerInventoryIsEmpty());
    }

    @Test
    public void riflePlugin_onKeyPress_activatesAndDeactivates() {
        // Arrange
        weaponPlugin.start(gameData, world);
        world.addEntity(weapon);
        assertTrue(world.getEntities(Weapon.class).size() > 0);
        setupPlayerWithWeapon();

        // Act
        weaponPlugin.onKeyPressed(ENTER, gameData, world);

        // Assert
        assertEquals(0, world.getEntities(Weapon.class).size());
        assertTrue(playerInventoryIsEmpty());

        // Act
        weaponPlugin.onKeyPressed(ENTER, gameData, world);

        // Assert
        assertTrue(weaponPlugin.isActive);
        assertEquals(0, world.getEntities(Weapon.class).size());
        assertTrue(playerInventoryIsEmpty());
    }

    private void setupPlayerWithWeapon() {
        Player player = new Player();
        InventoryPart inventory = new InventoryPart();
        inventory.addWeapon(world, weapon);
        player.add(inventory);
        world.addEntity(player);
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
}