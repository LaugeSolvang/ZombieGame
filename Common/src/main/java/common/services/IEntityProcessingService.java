package common.services;

import common.data.GameData;
import common.data.World;


public interface IEntityProcessingService {
    void process(GameData gameData, World world);
}
