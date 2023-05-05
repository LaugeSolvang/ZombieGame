package common.data.entityparts;

import common.data.Entity;
import common.data.GameData;

/**
 The EntityPart interface defines the contract for classes that represent
 specific components or parts of entities in the game world.
 Each implementation of this interface should process the behavior
 related to its corresponding part for a given entity.
 */
public interface EntityPart {
    /**
     Processes the behavior related to the specific part of the entity.
     @param gameData The game data containing information about the game state.
     @param entity The entity containing this specific part.
     */
    void process(GameData gameData, Entity entity);
}
