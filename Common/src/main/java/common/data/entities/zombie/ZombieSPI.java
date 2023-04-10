package common.data.entities.zombie;

import common.data.Entity;

public interface ZombieSPI {
    Entity createZombie(int x, int y);

}
