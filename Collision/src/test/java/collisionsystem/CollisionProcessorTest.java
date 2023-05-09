package collisionsystem;


import common.data.Entity;
import common.data.GameData;
import common.data.GameKeys;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entities.zombie.Zombie;
import common.data.entities.weapon.Weapon;
import common.data.entities.player.Player;

import common.data.entityparts.DamagePart;
import common.data.entityparts.LifePart;
import common.data.entityparts.PositionPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollisionProcessorTest {
    private CollisionProcessor processor;
    private GameData gameData;
    private World world;

    @BeforeEach
    void setUp() {
        processor = new CollisionProcessor();
        gameData = new GameData();
        world = new World();
    }

    @Test
    void testWeaponPlayerCollision() {
        Weapon weapon = (Weapon)createWeapon("TestWeaponImpl",5, 5, 10);

        System.out.println(weapon.getAmmo());
        Player player = (Player)createPlayer(5, 5);
        world.addEntity(weapon);
        world.addEntity(player);

        gameData.getKeys().setKey(GameKeys.SHIFT, true);

        processor.process(gameData, world);

        assertTrue(player.getWeapons().contains(weapon), "Player should have picked up the weapon");
    }

    @Test
    void testWeaponPlayerCollision_sameWeaponType() {
        Weapon weapon1 = (Weapon) createWeapon("TestWeaponImpl",5, 5, 10);
        Weapon weapon2 = (Weapon) createWeapon("TestWeaponImpl",5, 5, 20);
        Player player = (Player) createPlayer(5, 5);

        player.addWeaponToInventory(weapon1);
        world.addEntity(weapon2);
        world.addEntity(player);

        gameData.getKeys().setKey(GameKeys.SHIFT, true);

        processor.process(gameData, world);

        assertFalse(world.getEntities().contains(weapon2), "Weapon should be removed after collision");
        assertTrue(player.getWeapons().contains(weapon1), "Player should have the first weapon");
        assertEquals(30, weapon1.getAmmo(), "Ammo should be summed after collision with same weapon type");
    }

    @Test
    void testWeaponPlayerCollisionDifferentWeaponType() {
        Weapon weapon1 = (Weapon) createWeapon("TestWeaponImpl1",5, 5,  10);
        Weapon weapon2 = (Weapon) createWeapon("TestWeaponImpl2",5, 5,  20);
        Player player = (Player) createPlayer(5, 5);

        player.addWeaponToInventory(weapon1);
        world.addEntity(weapon2);
        world.addEntity(player);

        gameData.getKeys().setKey(GameKeys.SHIFT, true);

        processor.process(gameData, world);

        assertFalse(world.getEntities().contains(weapon2), "Weapon should be removed after collision");
        assertTrue(player.getWeapons().contains(weapon1), "Player should have the first weapon");
        assertTrue(player.getWeapons().contains(weapon2), "Player should have the second weapon");
    }

    @Test
    void testZombiePlayerCollision() {
        Entity zombie = createZombie(5, 5);
        Entity player = createPlayer(5, 5);

        world.addEntity(zombie);
        world.addEntity(player);
        LifePart playerLifePart = player.getPart(LifePart.class);
        int life = playerLifePart.getLife();
        System.out.println(life);

        processor.process(gameData, world);

        for (Entity playerEntity: world.getEntities(Player.class)) {
            LifePart playerEntityLifePart = playerEntity.getPart(LifePart.class);
            System.out.println(playerEntityLifePart.getLife());
            assertEquals(playerEntityLifePart.getLife() - life, -10);
        }
    }

    @Test
    void testBulletZombieCollision() {
        Entity bullet = createBullet(5, 5);
        Entity zombie = createZombie(5, 5);

        world.addEntity(bullet);
        world.addEntity(zombie);

        LifePart zombieLifePart = zombie.getPart(LifePart.class);

        processor.process(gameData, world);

        for (Entity playerEntity: world.getEntities(Zombie.class)) {
            LifePart zombieEntityLifePart = playerEntity.getPart(LifePart.class);
            System.out.println(zombieLifePart.getLife());


            assertEquals(zombieEntityLifePart.getLife()- zombieLifePart.getLife(), -1);
        }
        System.out.println(zombieLifePart.getLife());

        assertFalse(world.getEntities().contains(bullet), "Bullet should be removed after collision");
    }

    // Helper methods to create entities

    private Entity createBullet(float x, float y) {
        Entity bullet = new Bullet();
        bullet.add(new PositionPart(x, y, 3, 3));
        bullet.add(new DamagePart(10));
        return bullet;
    }

    private Entity createZombie(float x, float y) {
        Entity zombie = new Zombie();
        zombie.add(new PositionPart(x, y, 2, 2));
        zombie.add(new DamagePart(5));
        zombie.add(new LifePart(10));
        return zombie;
    }

    private Entity createWeapon(String shootImplName, float x, float y, int ammo) {
        Entity weapon = new Weapon(shootImplName, 10, ammo);
        weapon.add(new PositionPart(x, y, 2, 2));
        return weapon;
    }

    private Entity createPlayer(float x, float y) {
        Entity player = new Player();
        player.add(new PositionPart(x, y, 3, 3));
        player.add(new LifePart(100));
        return player;
    }
}