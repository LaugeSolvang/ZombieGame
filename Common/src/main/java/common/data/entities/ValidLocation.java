package common.data.entities;

import common.data.GameData;
import common.data.World;

public interface ValidLocation {
    int[] generateSpawnLocation(World world, GameData gameData);
}
