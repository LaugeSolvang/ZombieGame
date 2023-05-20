package common.data.entities.bullet;

import common.data.Entity;

public class Bullet extends Entity {
    private boolean hasHit;

    public boolean hasHit() {
        return hasHit;
    }

    public void setHasHit(boolean hasHit) {
        this.hasHit = hasHit;
    }
}
