package common.data.entityparts;

import common.data.Entity;
import common.data.GameData;

public class DamagePart implements EntityPart {
    private int damage;
    public DamagePart(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void process(GameData gameData, Entity entity) {

    }
}
