import common.data.entities.zombie.IZombieAI;
import common.data.entities.zombie.ZombieSPI;
import common.services.IEntityProcessingService;
import common.services.IGamePluginService;

module Zombie{
    requires Common;
    provides IGamePluginService with zombiesystem.ZombiePlugin;
    provides IEntityProcessingService with zombiesystem.ZombieProcessor;
    provides ZombieSPI with zombiesystem.ZombiePlugin;
    uses IZombieAI;
}