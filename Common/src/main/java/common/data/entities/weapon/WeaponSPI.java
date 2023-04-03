package common.data.entities.weapon;

import common.data.Entity;

public interface WeaponSPI {
    Entity createWeapon(int x, int y);
}
