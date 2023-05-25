import common.data.entities.ValidLocation;
import common.services.IGamePluginService;
import common.services.KeyPressListener;
import mapsystem.ValidMapLocation;

module Map {
    exports mapsystem;
    requires Common;
    provides ValidLocation with ValidMapLocation;
    provides IGamePluginService with mapsystem.MapPlugin;
    provides KeyPressListener with mapsystem.MapPlugin;
}