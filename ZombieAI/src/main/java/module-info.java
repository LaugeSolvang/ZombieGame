import common.services.IPostEntityProcessingService;

module ZombieAI {
    requires Common;
    provides IPostEntityProcessingService with zombieaisystem.AIProcessor;
}