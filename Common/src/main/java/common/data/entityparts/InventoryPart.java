
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
        Weapon currentWeapon = getCurrentWeapon();
        if (currentWeapon.getAmmo() == 0) {
            world.removeEntity(currentWeapon);
            weapons.remove(currentWeapon);
        }
        if (weapons.size() == 0) {
            currentWeaponIndex = 0;
            return;
        }
        world.removeEntity(currentWeapon);
        currentWeaponIndex = (currentWeaponIndex + direction + weapons.size()) % weapons.size();
        currentWeapon = getCurrentWeapon();
        world.addEntity(currentWeapon);
    }

    @Override
    public void process(GameData gameData, Entity entity) {

    }
}