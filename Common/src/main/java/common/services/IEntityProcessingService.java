package common.services;

import common.data.GameData;
import common.data.World;

import java.io.IOException;

public interface IEntityProcessingService {
    void process(GameData gameData, World world) throws IOException;
}
