import common.data.entities.weapon.IShoot;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Player {
    exports playersystem;
    requires Common;
    provides IEntityProcessingService with playersystem.PlayerProcessor;
    provides IGamePluginService with playersystem.PlayerPlugin;
    uses IShoot;
}