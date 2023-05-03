import common.data.entities.ValidLocation;
import common.data.entities.zombie.IZombieAI;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Zombie{
    requires Common;
    provides IGamePluginService with zombiesystem.ZombiePlugin;
    provides IEntityProcessingService with zombiesystem.ZombieProcessor;
    uses IZombieAI;
    uses ValidLocation;
}