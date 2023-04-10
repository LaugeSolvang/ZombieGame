import common.data.entities.weapon.WeaponSPI;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Map {
    exports mapsystem;
    requires Common;
    requires com.badlogic.gdx;
    provides IEntityProcessingService with mapsystem.MapProcessor;
    provides IGamePluginService with mapsystem.MapPlugin;
    uses WeaponSPI;
}