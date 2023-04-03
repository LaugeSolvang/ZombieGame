package bulletsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.BulletSPI;
import common.services.IEntityProcessingService;

public class BulletProcessor implements IEntityProcessingService, BulletSPI {
    @Override
    public void process(GameData gameData, World world) {

    }

    @Override
    public Entity createBullet(Entity weapon, GameData gameData) {
        return null;
    }
}
