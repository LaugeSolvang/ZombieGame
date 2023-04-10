package common.data.entities.weapon;

import common.data.Entity;
import common.data.GameData;
import common.data.World;

public interface IShoot {
    void useWeapon(Entity shooter, GameData gameData, World world);
}
