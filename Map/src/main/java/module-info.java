import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Map {
    requires Common;
    requires com.badlogic.gdx;
    provides IEntityProcessingService with mapsystem.MapProcessor;
    provides IGamePluginService with mapsystem.MapPlugin;
}