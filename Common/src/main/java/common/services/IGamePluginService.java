package common.services;

import common.data.GameData;
import common.data.World;

public interface IGamePluginService {
    void start(GameData gameData, World world);

    void stop(GameData gameData, World world);

}
