package common.data.entities;

import common.data.Entity;

public interface SpawnSPI {
    Entity createEntity(int x, int y);
}
