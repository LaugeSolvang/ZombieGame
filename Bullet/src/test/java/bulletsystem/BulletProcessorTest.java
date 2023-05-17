package bulletsystem;


import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entityparts.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletProcessorTest {

    private BulletProcessor bulletProcessor;
    private BulletPlugin bulletPlugin;
    private GameData gameData;
    private World world;
    private Entity weapon;

    @BeforeEach
    public void setUp() {
        bulletProcessor = new BulletProcessor();
        bulletPlugin = new BulletPlugin();
        gameData = new GameData();
        world = new World();
        weapon = new Entity();
        weapon.add(new PositionPart(5, 5, 0));
        weapon.add(new DamagePart(10));
    }

    @Test
    public void testCreateBullet() {
        Entity bullet = bulletProcessor.createBullet(weapon, gameData);

        assertNotNull(bullet);
        assertTrue(bullet instanceof Bullet);

        PositionPart positionPart = bullet.getPart(PositionPart.class);
        assertNotNull(positionPart);
        assertEquals(5f, positionPart.getX());
        assertEquals(5f, positionPart.getY());

        LifePart lifePart = bullet.getPart(LifePart.class);
        assertNotNull(lifePart);
        assertEquals(1, lifePart.getLife());

        MovingPart movingPart = bullet.getPart(MovingPart.class);
        assertNotNull(movingPart);
        assertEquals(2000F, movingPart.getAcceleration());
        assertEquals(0F, movingPart.getDeceleration());
        assertEquals(500F, movingPart.getMaxSpeed());

        TimerPart timerPart = bullet.getPart(TimerPart.class);
        assertNotNull(timerPart);
        assertEquals(1, timerPart.getExpiration());

        DamagePart damagePart = bullet.getPart(DamagePart.class);
        assertNotNull(damagePart);
        assertEquals(10, damagePart.getDamage());
    }

    @Test
    public void testProcess() {
        Entity bullet = bulletProcessor.createBullet(weapon, gameData);
        assertTrue(world.getEntities().isEmpty());

        world.addEntity(bullet);
        bulletProcessor.process(gameData, world);
        assertFalse(world.getEntities().contains(bullet));

        world.addEntity(bullet);
        bulletPlugin.start(gameData, world);
        bulletProcessor.process(gameData, world);
        assertTrue(world.getEntities().contains(bullet));

        TimerPart timerPart = bullet.getPart(TimerPart.class);
        timerPart.setExpiration(-1);
        bulletProcessor.process(gameData, world);
        System.out.println(world.getEntities().contains(bullet));

        assertFalse(world.getEntities().contains(bullet));
    }
    @Test
    void setBulletDirectionTest() {
        // Test right direction
        testBulletDirection(0, true, false, false, false);

        // Test up-right direction
        testBulletDirection((float) (Math.PI / 4), true, true, false, false);

        // Test up direction
        testBulletDirection((float) (Math.PI / 2), false, true, false, false);

        // Test up-left direction
        testBulletDirection((float) (3 * Math.PI / 4), false, true, true, false);

        // Test left direction
        testBulletDirection((float) Math.PI, false, false, true, false);

        // Test down-left direction
        testBulletDirection((float) (-3 * Math.PI / 4), false, false, true, true);

        // Test down direction
        testBulletDirection((float) (-Math.PI / 2), false, false, false, true);

        // Test down-right direction
        testBulletDirection((float) (-Math.PI / 4), true, false, false, true);
    }

    private void testBulletDirection(float radians, boolean right, boolean up, boolean left, boolean down) {
        PositionPart weaponPos = weapon.getPart(PositionPart.class);
        weaponPos.setRadians(radians);
        Entity bullet = bulletProcessor.createBullet(weapon, gameData);

        MovingPart movingPart = bullet.getPart(MovingPart.class);
        assertNotNull(movingPart);
        assertEquals(right, movingPart.isRight());
        assertEquals(up, movingPart.isUp());
        assertEquals(left, movingPart.isLeft());
        assertEquals(down, movingPart.isDown());
    }
}