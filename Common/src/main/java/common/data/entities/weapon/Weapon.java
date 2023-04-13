package common.data.entities.weapon;

import common.data.Entity;
import common.data.GameData;
import common.data.World;

public class Weapon extends Entity {
    private final String shootImplName;

    public Weapon(String shootImplName) {
        this.shootImplName = shootImplName;
    }

    public String getShootImplName() {
        return shootImplName;
    }
}
