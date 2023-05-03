import common.data.entities.ValidLocation;
import common.services.IGamePluginService;

module Map {
    exports mapsystem;
    requires Common;
    provides ValidLocation with mapsystem.MapProcessor;
    provides IGamePluginService with mapsystem.MapPlugin;
}