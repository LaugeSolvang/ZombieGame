package common.data.entities.weapon;

import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;

public interface IShoot {
    void useWeapon(Player shooter, GameData gameData, World world);
}
