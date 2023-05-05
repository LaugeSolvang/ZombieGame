package common.data.entities.weapon;

import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;

/**
 The IShoot interface defines the contract for classes implementing
 shooting behavior for weapon entities in the game world.
 */
public interface IShoot {
    /**
     Uses a weapon to shoot bullets in the game world.
     @param shooter The player entity holding the weapon.
     @param gameData The game data containing information about the game state.
     @param world The game world containing all entities.
     */
    void useWeapon(Player shooter, GameData gameData, World world);
}
