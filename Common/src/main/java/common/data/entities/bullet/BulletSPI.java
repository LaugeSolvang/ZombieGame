package common.data.entities.bullet;

import common.data.Entity;
import common.data.GameData;

/**
 The BulletSPI interface defines the contract for classes creating
 bullet entities for weapon entities in the game world.
 */
public interface BulletSPI {
    /**
     Creates a bullet entity associated with a weapon entity.
     @param weapon The weapon entity that fires the bullet.
     @param gameData The game data containing information about the game state.
     @return A new bullet entity.
     */
    Entity createBullet(Entity weapon, GameData gameData);
}
