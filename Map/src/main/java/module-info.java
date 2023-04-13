import common.data.entities.weapon.WeaponSPI;
import common.data.entities.zombie.ZombieSPI;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Map {
    exports mapsystem;
    requires Common;
    provides IEntityProcessingService with mapsystem.MapProcessor;
    provides IGamePluginService with mapsystem.MapPlugin;
    uses WeaponSPI;
    uses ZombieSPI;
}