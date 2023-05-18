package riflesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.bullet.Bullet;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.player.Player;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.InventoryPart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.data.entityparts.TimerPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static common.data.GameData.TILE_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RifleProcessorTest {
    private static final String WEAPON_ID = "riflesystem.RifleProcessor";
    private static final int WEAPON_DAMAGE = 50;
    private static final float WEAPON_FIRE_RATE = 0.1F;
    private static final int EXPECTED_X = 23;
    private static final int EXPECTED_Y = 11;


    private RifleProcessor rifleProcessor;
    private GameData gameData;
    private World world;
    private Player player;
    private BulletSPI bulletSPI;
    private ValidLocation validLocation;
    private Weapon weapon;

    @BeforeEach
    public void setUp() {
        rifleProcessor = new RifleProcessor();
        gameData = new GameData();
        gameData.setDelta(1/60F);
        world = new World();
        player = new Player();
        MovingPart movingPart = new MovingPart(0, 0, 0);
        PositionPart positionPart = new PositionPart(10, 10);

        movingPart.setDx(-10);
        player.add(movingPart);
        player.add(positionPart);

        bulletSPI = mock(BulletSPI.class);
        when(bulletSPI.createBullet(any(Entity.class), any(GameData.class))).thenReturn(new Bullet());

        validLocation = mock(ValidLocation.class);
        when(validLocation.generateSpawnLocation(any(World.class), any(GameData.class))).thenReturn(new int[]{EXPECTED_X, EXPECTED_Y});

        weapon = new Weapon(WEAPON_ID, WEAPON_DAMAGE, WEAPON_FIRE_RATE);
        weapon.add(new MovingPart(0, 0, 0));
        weapon.add(new PositionPart(0, 0));
        weapon.add(new TimerPart(1));

        rifleProcessor.setBulletSPIS(Collections.singleton(bulletSPI));
        rifleProcessor.setValidLocations(Collections.singleton(validLocation));
    }

    @Test
    public void process_withInactivePlugin_resetsRifleTime() {
        // Arrange
        gameData.setActivePlugin(RiflePlugin.class.getName(), false);

        // Act
        rifleProcessor.process(gameData, world);

        // Assert
        assertEquals(0, rifleProcessor.getRifleTime());
    }

    @Test
    public void process_withActivePluginAndWeapon_updatesWeaponDirectionAndTimer() {
        //Arrange
        gameData.setActivePlugin(RiflePlugin.class.getName(), true);
        setupPlayerWithWeapon();

        //Act
        rifleProcessor.process(gameData, world);

        //Assert
        TimerPart timerPart = weapon.getPart(TimerPart.class);
        assertAll("weapon",
                () -> assertNotEquals(1, timerPart.getExpiration()),
                () -> assertEquals("Rifle/src/main/resources/rifle-kopi.png", weapon.getPath())
        );
        //Arrange
        MovingPart movingPart = player.getPart(MovingPart.class);
        assertNotNull(movingPart, "MovingPart should not be null");
        movingPart.setDx(10);

        //Act
        rifleProcessor.process(gameData, world);

        //Assert
        assertEquals("Rifle/src/main/resources/rifle.png", weapon.getPath(), "Weapon path should be changed based on moving part direction");
    }


    @Test
    public void spawnWeapons_spawnsWeaponsAtValidLocations() {
        //Arrange
        gameData.setActivePlugin(RiflePlugin.class.getName(), true);

        //Act
        rifleProcessor.process(gameData, world);

        //Assert
        for (Entity weapon: world.getEntities(Weapon.class)) {
            PositionPart positionPart = weapon.getPart(PositionPart.class);
            assertEquals(EXPECTED_X*TILE_SIZE, positionPart.getX());
            assertEquals(EXPECTED_Y*TILE_SIZE, positionPart.getY());
        }
    }
    @Test
    public void useWeapon_ShouldNotShootBullet_WhenTImeIsNotAvailable() {
        // Arrange
        setupPlayerWithWeapon();
        world.addEntity(weapon);

        // Act
        rifleProcessor.useWeapon(player, gameData, world);

        // Assert
        assertTrue(world.getEntities(Bullet.class).isEmpty(), "Bullet should not be present in the world before weapon use");
    }
    @Test
    public void useWeapon_ShouldNotShootBullet_WhenAmmoIsNotAvailable() {
        // Arrange
        setupPlayerWithWeapon();
        weapon.setAmmo(0);
        world.addEntity(weapon);
        TimerPart timerPart = weapon.getPart(TimerPart.class);
        timerPart.setExpiration(0);

        // Act
        rifleProcessor.useWeapon(player, gameData, world);

        // Assert
        assertTrue(world.getEntities(Bullet.class).isEmpty(), "Bullet should not be present in the world before weapon use");
    }

    @Test
    public void useWeapon_shootsBulletIfAmmoAndTimeIsAvailable() {
        //Arrange
        gameData.setActivePlugin(RiflePlugin.class.getName(), true);
        setupPlayerWithWeapon();

        TimerPart timerPart = weapon.getPart(TimerPart.class);
        assertNotNull(timerPart, "TimerPart should not be null");
        timerPart.setExpiration(0);

        //Act
        rifleProcessor.useWeapon(player, gameData, world);

        //Assert
        for (Entity weapon: world.getEntities(Weapon.class)) {
            TimerPart timerPart2 = weapon.getPart(TimerPart.class);
            assertEquals(timerPart2.getExpiration(), 0.1F, "TimerPart expiration should be updated after weapon use");
            Weapon weapon1 = (Weapon) weapon;
            assertEquals(49, weapon1.getAmmo(), "Weapon ammo should decrease by 1 after use");
        }
    }
    private void setupPlayerWithWeapon() {
        InventoryPart inventory = new InventoryPart();
        inventory.addWeapon(world, weapon);
        player.add(inventory);
        world.addEntity(player);
    }
}
