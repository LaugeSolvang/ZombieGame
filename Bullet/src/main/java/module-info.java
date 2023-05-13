import common.data.entities.bullet.BulletSPI;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;
import common.services.KeyPressListener;

module Bullet {
    requires Common;
    provides IEntityProcessingService with bulletsystem.BulletProcessor;
    provides IGamePluginService with bulletsystem.BulletPlugin;
    provides KeyPressListener with bulletsystem.BulletPlugin;
    provides BulletSPI with bulletsystem.BulletProcessor;
}