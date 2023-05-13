package common.services;

import common.data.GameData;
import common.data.World;

public interface KeyPressListener {
    void onKeyPressed(int key, GameData gameData, World world);
}

