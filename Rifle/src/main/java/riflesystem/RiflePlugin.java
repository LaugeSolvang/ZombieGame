package riflesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.weapon.Weapon;
import common.services.KeyPressListener;
import common.services.IGamePluginService;

import static common.data.GameKeys.ENTER;

public class RiflePlugin implements IGamePluginService, KeyPressListener {
    boolean isActive = true;

    @Override
    public void start(GameData gameData, World world) {
        gameData.setActivePlugin(this.getClass().getName(), isActive);
    }

    @Override
    public void stop(GameData gameData, World world) {
        gameData.setActivePlugin(this.getClass().getName(), isActive);
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Weapon.class) {
                world.removeEntity(e);
            }
        }
    }

    @Override
    public void onKeyPressed(int key, GameData gameData, World world) {
        if (key == ENTER) {
            if (isActive) {
                isActive = false;
                stop(gameData, world);
            } else {
                isActive = true;
                start(gameData, world);
            }
        }
    }
}
