import common.data.entities.ValidLocation;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.weapon.IShoot;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Weapon {
    requires Common;
    provides IEntityProcessingService with weaponsystem.WeaponProcessor;
    provides IGamePluginService with weaponsystem.WeaponPlugin;
    provides IShoot with weaponsystem.WeaponProcessor;
    uses BulletSPI;
    uses ValidLocation;
}