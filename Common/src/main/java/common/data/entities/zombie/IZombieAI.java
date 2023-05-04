package common.data.entities.zombie;

import common.data.Entity;
import common.data.GameData;

public interface IZombieAI {
    void moveTowards(GameData gameData, Entity zombie);
}
