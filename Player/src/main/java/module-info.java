import common.data.entities.bullet.BulletSPI;
import common.data.entities.weapon.IShoot;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Player {
    exports playersystem;
    requires Common;
    requires com.badlogic.gdx;
    provides IEntityProcessingService with playersystem.PlayerProcessor;
    provides IGamePluginService with playersystem.PlayerPlugin;
    uses IShoot;
    uses BulletSPI;
}