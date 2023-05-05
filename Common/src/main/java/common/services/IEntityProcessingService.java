package common.services;

import common.data.GameData;
import common.data.World;

/**
 The IEntityProcessingService interface defines the contract for all entity processing services.
 Classes implementing this interface are responsible for processing the behaviour of specific entities in the game world.
 */
public interface IEntityProcessingService {
    /**
     The main processing method that iterates through all the entities in the world
     and processes post-entity tasks such as collision detection and AI pathfinding.
     @param gameData The game data containing information about the game state.
     @param world The game world containing all entities.
     */
    void process(GameData gameData, World world);
}
