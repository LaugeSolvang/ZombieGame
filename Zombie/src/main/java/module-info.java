import common.data.entities.ValidLocation;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;
import common.services.KeyPressListener;

module Zombie{
    requires Common;
    provides IGamePluginService with zombiesystem.ZombiePlugin;
    provides IEntityProcessingService with zombiesystem.ZombieProcessor;
    provides KeyPressListener with zombiesystem.ZombiePlugin;
    uses ValidLocation;
}