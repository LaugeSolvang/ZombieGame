package bulletsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entities.bullet.BulletSPI;
import common.data.entityparts.*;
import common.services.IEntityProcessingService;

import static common.data.GameKeys.*;


public class BulletProcessor implements IEntityProcessingService, BulletSPI {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {
            PositionPart positionPart = bullet.getPart(PositionPart.class);
            MovingPart movingPart = bullet.getPart(MovingPart.class);
            TimerPart timerPart = bullet.getPart(TimerPart.class);

            if (timerPart.getExpiration() < 0) {
                world.removeEntity(bullet);
            }


            movingPart.process(gameData, bullet);
            positionPart.process(gameData, bullet);
            timerPart.process(gameData, bullet);
        }
    }

    @Override
    public Entity createBullet(Entity weapon, GameData gameData) {
        PositionPart weaponPos = weapon.getPart(PositionPart.class);
        Entity bullet = new Bullet();
        setBulletPosition(weaponPos, bullet);
        setBulletProperties(bullet);
        setBulletDirection(gameData, bullet);
        return bullet;
    }

    private void setBulletPosition(PositionPart weaponPos, Entity bullet) {
        float x = weaponPos.getX() + weaponPos.getHeight() / 2;
        float y = weaponPos.getY() + weaponPos.getWidth() / 2;
        bullet.add(new PositionPart(x, y));
    }

    private void setBulletProperties(Entity bullet) {
        String path = "bullet.png";
        float deceleration = 0;
        float acceleration = 2000;
        float speed = 500;
        int life = 1;
        int timer = 1;
        int damage = 100;

        bullet.setPath(path);
        bullet.setRadius(2);
        bullet.add(new LifePart(life));
        bullet.add(new MovingPart(deceleration, acceleration, speed));
        bullet.add(new TimerPart(timer));
        bullet.add(new DamagePart(damage));
    }

    private void setBulletDirection(GameData gameData, Entity bullet) {
        MovingPart movingPart = bullet.getPart(MovingPart.class);

        if (gameData.getKeys().isDown(UP) && gameData.getKeys().isDown(RIGHT)) {
            movingPart.setUp(true);
            movingPart.setRight(true);
        } else if (gameData.getKeys().isDown(UP) && gameData.getKeys().isDown(LEFT)) {
            movingPart.setUp(true);
            movingPart.setLeft(true);
        } else if (gameData.getKeys().isDown(LEFT) && gameData.getKeys().isDown(DOWN)) {
            movingPart.setDown(true);
            movingPart.setLeft(true);
        } else if (gameData.getKeys().isDown(DOWN) && gameData.getKeys().isDown(RIGHT)) {
            movingPart.setDown(true);
            movingPart.setRight(true);
        } else if (gameData.getKeys().isDown(UP)) {
            movingPart.setUp(true);
        } else if (gameData.getKeys().isDown(RIGHT)) {
            movingPart.setRight(true);
        } else if (gameData.getKeys().isDown(LEFT)) {
            movingPart.setLeft(true);
        } else if (gameData.getKeys().isDown(DOWN)) {
            movingPart.setDown(true);
        }
    }
}
