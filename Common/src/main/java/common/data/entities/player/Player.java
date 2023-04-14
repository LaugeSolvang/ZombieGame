package common.data.entities.player;


import common.data.Entity;
import common.data.entities.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private final List<Weapon> weapons = new ArrayList<>();
    private int currentWeaponIndex = 0;

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeaponToInventory(Weapon weapon) {
        weapons.add(weapon);
    }

    public Weapon getCurrentWeapon() {
        return weapons.get(currentWeaponIndex);
    }

    public void cycleWeapon(int direction) {
        currentWeaponIndex = (currentWeaponIndex + direction + weapons.size()) % weapons.size();
    }

}

