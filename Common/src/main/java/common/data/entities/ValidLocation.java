package common.data.entities;

import common.data.GameData;
import common.data.World;

public interface ValidLocation {
    int[] spawnEntities(World world, GameData gameData);
}
