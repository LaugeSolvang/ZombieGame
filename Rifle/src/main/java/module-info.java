import common.data.entities.ValidLocation;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.weapon.IShoot;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;
import common.services.KeyPressListener;

module Rifle {
    requires Common;
    provides IEntityProcessingService with riflesystem.RifleProcessor;
    provides IGamePluginService with riflesystem.RiflePlugin;
    provides IShoot with riflesystem.RifleProcessor;
    provides KeyPressListener with riflesystem.RiflePlugin;
    uses BulletSPI;
    uses ValidLocation;
}