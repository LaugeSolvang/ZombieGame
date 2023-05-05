package common.data.entities;

import common.data.GameData;
import common.data.World;

/**
 The ValidLocation interface defines the contract for classes that generate
 valid spawn locations for entities in the game world.
 */
public interface ValidLocation {
    /**
     Generates a valid spawn location in the game world.
     @param world The game world containing all entities.
     @param gameData The game data containing information about the game state.
     @return An array of integers representing the valid spawn location (x, y).
     */
    int[] generateSpawnLocation(World world, GameData gameData);
}
