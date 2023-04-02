import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Player {
    requires Common;
    requires com.badlogic.gdx;
    provides IEntityProcessingService with playersystem.PlayerProcessor;
    provides IGamePluginService with playersystem.PlayerPlugin;
}