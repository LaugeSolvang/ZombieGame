package playersystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entities.player.Player;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.InventoryPart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static common.data.GameKeys.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

class PlayerProcessorTest {
    private static final String WEAPON_ID = "playersystem.PlayerProcessor";
    private static final int WEAPON_DAMAGE = 50;
    private static final float WEAPON_FIRE_RATE = 0.1F;

    private PlayerProcessor playerProcessor;
    private GameData gameData;
    private World world;
    private Player player;
    private Weapon weapon;
    private Weapon secondWeapon;

    @BeforeEach
    public void setUp() {
        playerProcessor = new PlayerProcessor();
        gameData = new GameData();
        world = new World();
        player = new Player();
        MovingPart movingPart = new MovingPart(100, 100, 100);
        PositionPart positionPart = new PositionPart(10, 10);

        movingPart.setDx(-10);
        player.add(movingPart);
        player.add(positionPart);

        weapon = new Weapon(WEAPON_ID, WEAPON_DAMAGE, WEAPON_FIRE_RATE);
        weapon.setAmmo(50);
        weapon.add(new MovingPart(0, 0, 0));
        weapon.add(new PositionPart(0, 0));
        InventoryPart inventoryPart = new InventoryPart();
        inventoryPart.addWeapon(world, weapon);
        secondWeapon = new Weapon("SecondWeapon", 30, 0.2F);
        secondWeapon.add(new MovingPart(0, 0, 0));
        secondWeapon.add(new PositionPart(0, 0));
        inventoryPart.addWeapon(world, secondWeapon);

        player.add(inventoryPart);


        IShoot iShoot = Mockito.mock(IShoot.class);
        doAnswer(invocation -> {
            Player player = invocation.getArgument(0);
            World world = invocation.getArgument(2);
            InventoryPart inventoryPart2 = player.getPart(InventoryPart.class);
            inventoryPart2.getCurrentWeapon().reduceAmmon();
            world.addEntity(new Bullet());
            return null;  // return is needed even though this is for a void method
        }).when(iShoot).useWeapon(any(Player.class), any(GameData.class), any(World.class));
        playerProcessor.setShootImpl(iShoot);
    }

    @Test
    public void process_withActivePlayer_updatesPlayerDirectionAndWeapon() {
        // Arrange
        gameData.getKeys().setKey(LEFT, true);
        gameData.getKeys().setKey(RIGHT, false);
        gameData.getKeys().setKey(UP, true);
        gameData.getKeys().setKey(DOWN, false);
        gameData.getKeys().setKey(SPACE, false);
        world.addEntity(player);
        MovingPart movingPart = null;

        // Act
        playerProcessor.process(gameData, world);

        // Assert
        for (Entity player: world.getEntities(Player.class)) {
            movingPart = player.getPart(MovingPart.class);

        }
        MovingPart finalMovingPart = movingPart;
        assertAll("Player",
                () -> assertTrue(finalMovingPart.isLeft()),
                () -> assertFalse(finalMovingPart.isRight()),
                () -> assertTrue(finalMovingPart.isUp()),
                () -> assertFalse(finalMovingPart.isDown()),
                () -> assertEquals("Player/src/main/resources/player.png", player.getPath())
        );
    }
    @Test
    public void processWeapon_withWeaponInInventory_shootsWeaponIfAmmoIsAvailable() {
        // Arrange
        gameData.getKeys().setKey(SPACE, true);
        world.addEntity(player);

        // Act
        playerProcessor.process(gameData, world);

        // Assert
        assertEquals(49, weapon.getAmmo(), "Weapon ammo should decrease by 1 after use");
        assertTrue(world.getEntities(Bullet.class).size() > 0);
    }
    @Test
    public void processWeaponSwitching_withMultipleWeaponsInInventory_cyclesWeapons() {
        // Arrange
        InventoryPart inventoryPart = player.getPart(InventoryPart.class);
        world.addEntity(player);

        // Act
        gameData.getKeys().setKey(ONE, true); // Switch to the previous weapon
        playerProcessor.process(gameData, world);

        // Assert
        assertSame(secondWeapon, inventoryPart.getCurrentWeapon());

        // Act
        gameData.getKeys().setKey(TWO, true); // Switch to the next weapon
        playerProcessor.process(gameData, world);

        // Assert
        assertSame(weapon, inventoryPart.getCurrentWeapon());
        assertEquals(1, world.getEntities(Weapon.class).size());
    }
    @Test
    public void processWeapon_withWeaponInInventory_doesNotShootIfNoAmmo() {
        // Arrange
        gameData.getKeys().setKey(SPACE, true);
        world.addEntity(player);
        weapon.setAmmo(0); // setting ammo to 0

        // Act
        playerProcessor.process(gameData, world);

        // Assert
        InventoryPart inventoryPart = player.getPart(InventoryPart.class);
        assertNotSame(weapon, inventoryPart.getCurrentWeapon(), "Weapon should be switched after ammo is finished");
        assertSame(secondWeapon, inventoryPart.getCurrentWeapon(), "Weapon should be switched after ammo is finished");
        assertFalse(inventoryPart.getWeapons().contains(weapon), "Weapon with no ammo should be removed");
    }
}