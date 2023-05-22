import common.data.entities.zombie.IZombieAI;
import common.services.IPostEntityProcessingService;
import common.services.KeyPressListener;

module ZombieAI {
    requires Common;
    provides IPostEntityProcessingService with zombieaisystem.AIProcessor;
}