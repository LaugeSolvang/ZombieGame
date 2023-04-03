import common.services.IPostEntityProcessingService;

module Collision {
    requires Common;
    requires Player;
    requires Map;
    provides IPostEntityProcessingService with collisionsystem.CollisionProcessor;
}