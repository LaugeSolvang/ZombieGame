package common.data.entities.weapon;

import common.data.Entity;

public class Weapon extends Entity {
    private final String shootImplName;
    private int ammo;
    private float fireRate;
    private boolean equipped = false;

    public Weapon(String shootImplName, int ammo, float fireRate) {
        this.shootImplName = shootImplName;
        this.ammo = ammo;
        this.fireRate = fireRate;
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

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public float getFireRate() {
        return fireRate;
    }
    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }
}
