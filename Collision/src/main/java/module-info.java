import common.services.IPostEntityProcessingService;

module Collision {
    requires Common;
    exports collisionsystem;
    provides IPostEntityProcessingService with collisionsystem.CollisionProcessor;
}