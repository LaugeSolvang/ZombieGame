import common.data.entities.ValidLocation;
import common.services.IGamePluginService;
import common.services.KeyPressListener;

module Map {
    exports mapsystem;
    requires Common;
    provides ValidLocation with mapsystem.MapProcessor;
    provides IGamePluginService with mapsystem.MapPlugin;
    provides KeyPressListener with mapsystem.MapPlugin;
}