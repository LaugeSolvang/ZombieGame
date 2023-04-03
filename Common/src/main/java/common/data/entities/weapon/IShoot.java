package common.data.entities.weapon;

import common.data.Entity;
import common.data.GameData;

public interface IShoot {
    Entity useWeapon(Entity shooter, GameData gameData);
}
