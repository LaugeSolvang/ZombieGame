import common.data.entities.zombie.IZombieAI;
import common.services.IPostEntityProcessingService;

module ZombieAI {
    requires Common;
    provides IPostEntityProcessingService with zombieaisystem.AIProcessor;
    provides IZombieAI with zombieaisystem.AIProcessor;
}