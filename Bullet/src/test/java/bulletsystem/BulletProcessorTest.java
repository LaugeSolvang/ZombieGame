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
    private GameData gameData;
    private World world;
    private Entity weapon;

    @BeforeEach
    public void setUp() {
        bulletProcessor = new BulletProcessor();
        gameData = new GameData();
        world = new World();
        weapon = new Entity();
        weapon.add(new PositionPart(5, 5, 0));
        weapon.add(new DamagePart(10));
        weapon.add(new LifePart(1));
        weapon.add(new MovingPart(100, 200, 100));
    }

    @Test
    public void testCreateBullet() {
        Entity bullet = bulletProcessor.createBullet(weapon, gameData);

        assertNotNull(bullet);
        assertTrue(bullet instanceof Bullet);

        PositionPart positionPart = bullet.getPart(PositionPart.class);
        assertNotNull(positionPart);
        assertEquals(7.5f, positionPart.getX());
        assertEquals(7.5f, positionPart.getY());

        LifePart lifePart = bullet.getPart(LifePart.class);
        assertNotNull(lifePart);
        assertEquals(1, lifePart.getLife());

        MovingPart movingPart = bullet.getPart(MovingPart.class);
        assertNotNull(movingPart);
        //assertTrue(movingPart.isRight());

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
        world.addEntity(bullet);

        bulletProcessor.process(gameData, world);

        assertFalse(world.getEntities().isEmpty());
        assertTrue(world.getEntities().contains(bullet));

        TimerPart timerPart = bullet.getPart(TimerPart.class);
        timerPart.setExpiration(-1);
        bulletProcessor.process(gameData, world);

        assertFalse(world.getEntities().contains(bullet));
    }
}