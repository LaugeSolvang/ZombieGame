package collisionsystem;


import common.data.Entity;
import common.data.GameData;
import common.data.GameKeys;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entities.obstruction.Obstruction;
import common.data.entities.zombie.Zombie;
import common.data.entities.weapon.Weapon;
import common.data.entities.player.Player;

import common.data.entityparts.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollisionProcessorTest {
    private CollisionProcessor processor;
    private GameData gameData;
    private World world;
    private final float delta = 1/60F;

    @BeforeEach
    void setUp() {
        processor = new CollisionProcessor();
        gameData = new GameData();
        gameData.setDelta(delta);
        world = new World();
    }
    @Test
    void noCollision() {
        Entity bullet = createBullet(100, 100, 100);
        Entity bullet2 = createBullet(400, 100, 100);
        Entity bullet3 = createBullet(200, 100, 100);
        Entity bullet4 = createBullet(700, 100, 100);
        Entity bullet5 = createBullet(0, 0, 100);

        Entity obstruction = createObstruction(36, 225);
        Entity obstruction2 = createObstruction(95, 423);
        Entity obstruction3 = createObstruction(566, 789);
        Entity obstruction4 = createObstruction(543, 27);
        Entity obstruction5 = createObstruction(876, 55);
        setupEntities(
                bullet, bullet2, bullet3, bullet4, bullet5,
                obstruction, obstruction2, obstruction3, obstruction4, obstruction5
        );
        processor.process(gameData, world);

        assertEquals(10, world.getEntities().size(), "Bullet should not be removed after collision");
    }
    @Test
    void testBulletObstructionCollision() {
        Entity bullet = createBullet(5, 5, 100);
        Entity obstruction = createObstruction(5, 5);

        world.addEntity(bullet);
        world.addEntity(obstruction);

        processor.process(gameData, world);

        assertFalse(world.getEntities().contains(bullet), "Bullet should be removed after collision");
        assertTrue(world.getEntities().contains(obstruction), "Obstruction should not be removed after collision");
    }
    @Test
    void testPlayerObstructionCollision() {
        float speed = 1;
        Player player = (Player) createPlayer(5, 5);
        Entity obstruction = createObstruction(5, 5);

        PositionPart playerPosition = player.getPart(PositionPart.class);
        float oldX = playerPosition.getX();
        float oldY = playerPosition.getY();
        MovingPart playerMovement = player.getPart(MovingPart.class);
        playerMovement.setDx(speed);
        playerMovement.setDy(speed);

        world.addEntity(player);
        world.addEntity(obstruction);


        processor.process(gameData, world);

        assertEquals(oldX - delta * speed, playerPosition.getX(), 0.004, "Player's X position should change after collision");
        assertEquals(oldY - delta * speed, playerPosition.getY(), 0.004,"Player's Y position should change after collision");
        assertEquals(speed*delta*speed*delta, playerMovement.getDx(), 0.004,"Player's horizontal velocity should be set to 0 after collision");
        assertEquals(speed*delta*speed*delta, playerMovement.getDy(), 0.004,"Player's vertical velocity should be set to 0 after collision");
    }

    @Test
    void testWeaponPlayerCollision() {
        Weapon weapon = (Weapon)createWeapon("TestWeaponImpl",5, 5, 10);
        Player player = (Player)createPlayer(5, 5);
        InventoryPart inventory = player.getPart(InventoryPart.class);
        world.addEntity(weapon);
        world.addEntity(player);

        gameData.getKeys().setKey(GameKeys.SHIFT, true);

        processor.process(gameData, world);

        assertTrue(inventory.getWeapons().contains(weapon), "Player should have picked up the weapon");
    }

    @Test
    void testWeaponPlayerCollisionSameWeaponType() {
        Weapon weapon1 = (Weapon) createWeapon("TestWeaponImpl",5, 5, 10);
        Weapon weapon2 = (Weapon) createWeapon("TestWeaponImpl",5, 5, 20);
        Player player = (Player) createPlayer(5, 5);

        InventoryPart inventory = player.getPart(InventoryPart.class);
        inventory.addWeapon(world, weapon1);
        world.addEntity(weapon2);
        world.addEntity(player);

        gameData.getKeys().setKey(GameKeys.SHIFT, true);

        processor.process(gameData, world);

        assertFalse(world.getEntities().contains(weapon2), "Weapon should be removed after collision");
        assertTrue(inventory.getWeapons().contains(weapon1), "Player should have the first weapon");
        assertEquals(30, weapon1.getAmmo(), "Ammo should be summed after collision with same weapon type");
    }

    @Test
    void testWeaponPlayerCollisionDifferentWeaponType() {
        Weapon weapon1 = (Weapon) createWeapon("TestWeaponImpl1",5, 5,  10);
        Weapon weapon2 = (Weapon) createWeapon("TestWeaponImpl2",5, 5,  20);
        Player player = (Player) createPlayer(5, 5);

        InventoryPart inventory = player.getPart(InventoryPart.class);
        inventory.addWeapon(world, weapon1);
        world.addEntity(weapon2);
        world.addEntity(player);

        gameData.getKeys().setKey(GameKeys.SHIFT, true);

        processor.process(gameData, world);

        assertFalse(world.getEntities().contains(weapon2), "Weapon should be removed after collision");
        assertTrue(inventory.getWeapons().contains(weapon1), "Player should have the first weapon");
        assertTrue(inventory.getWeapons().contains(weapon2), "Player should have the second weapon");
    }

    @Test
    void testZombiePlayerCollision() {
        Entity player = createPlayer(5, 5);
        Entity zombie = createZombie(5, 5);

        world.addEntity(player);
        world.addEntity(zombie);

        processor.process(gameData, world);

        LifePart lifePart = player.getPart(LifePart.class);
        assertEquals(92, lifePart.getLife());
    }

    @Test
    void testBulletZombieCollision_NoDeath() {
        Entity bullet = createBullet(5, 5, 50);
        Entity zombie = createZombie(5, 5);

        world.addEntity(bullet);
        world.addEntity(zombie);

        processor.process(gameData, world);

        LifePart zombieEntityLifePart = zombie.getPart(LifePart.class);
        assertEquals(zombieEntityLifePart.getLife(), 50);
        assertTrue(world.getEntities().contains(zombie), "Zombie should not be removed after collision");
        assertFalse(world.getEntities().contains(bullet), "Bullet should be removed after collision");
    }
    @Test
    void testBulletZombieCollision_Death() {
        Entity bullet = createBullet(5, 5, 100);
        Entity zombie = createZombie(5, 5);

        world.addEntity(bullet);
        world.addEntity(zombie);

        processor.process(gameData, world);

        LifePart zombieEntityLifePart = zombie.getPart(LifePart.class);
        assertEquals(zombieEntityLifePart.getLife(), 0);
        assertFalse(world.getEntities().contains(zombie), "Zombie should be removed after collision");
        assertFalse(world.getEntities().contains(bullet), "Bullet should be removed after collision");
    }
    @Test
    void testBulletZombieCollision_Multiple() {
        Entity bullet = createBullet(5, 5, 100);
        Entity zombie = createZombie(5, 5);
        Entity zombie2 = createZombie(5, 5);

        world.addEntity(bullet);
        world.addEntity(zombie);
        world.addEntity(zombie2);

        assertEquals(3, world.getEntities().size(), "Zombie and bullet should not be removed before collision");

        processor.process(gameData, world);

        assertEquals(1, world.getEntities().size(), "Zombie and bullet should be removed after collision");
    }

    // Helper methods to create entities

    private void setupEntities(Entity... entities) {
        for (Entity entity : entities) {
            world.addEntity(entity);
        }
    }
    private Entity createBullet(float x, float y, int damage) {
        Entity bullet = new Bullet();
        bullet.add(new PositionPart(x, y, 3, 3));
        bullet.add(new DamagePart(damage));
        return bullet;
    }

    private Entity createZombie(float x, float y) {
        Entity zombie = new Zombie();
        zombie.add(new PositionPart(x, y, 2, 2));
        zombie.add(new DamagePart(4));
        zombie.add(new LifePart(100));
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
        player.add(new MovingPart(100,100,100));
        player.add(new LifePart(100));
        player.add(new InventoryPart());
        return player;
    }
    private Entity createObstruction(float x, float y) {
        Entity obstruction = new Obstruction();
        obstruction.add(new PositionPart(x, y, 2, 2));
        return obstruction;
    }
}