package common.data.entities.bullet;

import common.data.Entity;
import common.data.GameData;

public interface BulletSPI {
    Entity createBullet(Entity weapon, GameData gameData);
}
