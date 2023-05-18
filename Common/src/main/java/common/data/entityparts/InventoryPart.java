
package common.data.entityparts;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class InventoryPart implements EntityPart{
    private final List<Weapon> weapons = new ArrayList<>();
    private int currentWeaponIndex = 0;

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(World world, Weapon weapon) {
        weapons.add(weapon);
        if (weapon != weapons.get(currentWeaponIndex)) {
            world.removeEntity(weapon);
        }
    }

    public void removeWeapon(Weapon weapon) {
        weapons.remove(weapon);
    }

    public Weapon getCurrentWeapon() {
        if (weapons.isEmpty()) {
            return null;
        }
        return weapons.get(currentWeaponIndex);
    }

    public Weapon getWeapon(int index) {
        if (weapons.isEmpty()) {
            return null;
        }
        return weapons.get(index);
    }

    public void cycleWeapon(World world, int direction) {
        int oldWeaponIndex = currentWeaponIndex;
        if (weapons.size() == 0) {
            currentWeaponIndex = 0;
            return;
        } else {
            currentWeaponIndex = (currentWeaponIndex + direction + weapons.size()) % weapons.size();
        }
        world.removeEntity(getWeapon(oldWeaponIndex));
        if (getCurrentWeapon().getAmmo() != 0) {
            world.addEntity(getCurrentWeapon());
        } else {
            weapons.remove(getWeapon(oldWeaponIndex));
        }
    }

    @Override
    public void process(GameData gameData, Entity entity) {

    }
}