package common.data.entities.zombie;

import common.data.Entity;
import common.data.GameData;

/**
 The IZombieAI interface defines the contract for classes implementing
 AI behavior for zombie entities in the game world.
 */
public interface IZombieAI {
    /**
     Moves a zombie entity towards the target.
     @param gameData The game data containing information about the game state.
     @param zombie The zombie entity to move.
     */
    void moveTowards(GameData gameData, Entity zombie);
}
