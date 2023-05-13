package bulletsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.data.entities.bullet.BulletSPI;
import common.data.entityparts.*;
import common.services.IEntityProcessingService;


public class BulletProcessor implements IEntityProcessingService, BulletSPI {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {
            if (!gameData.isActivePlugin(BulletPlugin.class.getName())) {
                world.removeEntity(bullet);
                return;
            }

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
        setBulletProperties(bullet, weapon);
        setBulletDirection(weaponPos, bullet);
        return bullet;
    }

    private void setBulletPosition(PositionPart weaponPos, Entity bullet) {
        float x = weaponPos.getX() + weaponPos.getHeight() / 2;
        float y = weaponPos.getY() + weaponPos.getWidth() / 2;
        bullet.add(new PositionPart(x, y));
    }

    private void setBulletProperties(Entity bullet, Entity weapon) {
        String path = "bullet.png";
        float deceleration = 0;
        float acceleration = 2000;
        float speed = 500;
        int life = 1;
        int timer = 1;

        bullet.setPath(path);
        bullet.setRadius(2);
        bullet.add(new LifePart(life));
        bullet.add(new MovingPart(deceleration, acceleration, speed));
        bullet.add(new TimerPart(timer));
        bullet.add(weapon.getPart(DamagePart.class));
    }

    private void setBulletDirection(PositionPart posPart, Entity bullet) {
        MovingPart movingPart = bullet.getPart(MovingPart.class);
        float radians = posPart.getRadians();
        float piEighth = (float) Math.PI / 8;

        if (radians >= -piEighth && radians <= piEighth) {
            movingPart.setRight(true);
        } else if (radians > piEighth && radians < 3*piEighth) {
            movingPart.setUp(true);
            movingPart.setRight(true);
        } else if (radians >= 3*piEighth && radians <= 5*piEighth) {
            movingPart.setUp(true);
        } else if (radians > 5*piEighth && radians < 7*piEighth) {
            movingPart.setUp(true);
            movingPart.setLeft(true);
        } else if (radians >= 7*piEighth || radians <= -7*piEighth) {
            movingPart.setLeft(true);
        } else if (radians < -5*piEighth) {
            movingPart.setDown(true);
            movingPart.setLeft(true);
        } else if (radians <= -3*piEighth) {
            movingPart.setDown(true);
        } else if (radians < -piEighth) {
            movingPart.setDown(true);
            movingPart.setRight(true);
        }
    }
}
