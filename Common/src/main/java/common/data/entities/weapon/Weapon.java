package common.data.entities.weapon;

import common.data.Entity;

public class Weapon extends Entity {
    private final String shootImplName;

    public Weapon(String shootImplName) {
        this.shootImplName = shootImplName;
    }

    public String getShootImplName() {
        return shootImplName;
    }
}
