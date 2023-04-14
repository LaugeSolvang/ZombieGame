package common.data.entities.weapon;

import common.data.Entity;

public class Weapon extends Entity {
    private final String shootImplName;
    private int ammo;
    private boolean equipped = false;


    public Weapon(String shootImplName, int ammo) {
        this.shootImplName = shootImplName;
        this.ammo = ammo;
    }

    public String getShootImplName() {
        return shootImplName;
    }

    public void reduceAmmon() {
        ammo--;
    }

    public int getAmmo() {
        return ammo;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }
}
