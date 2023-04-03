package common.data.entities.zombie;

import common.data.GameData;
import common.data.World;

public interface IZombieAI {
    void moveTowards(GameData gameData, World world);
}
