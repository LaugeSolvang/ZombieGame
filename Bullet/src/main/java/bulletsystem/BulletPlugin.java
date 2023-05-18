package bulletsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.Bullet;
import common.services.IGamePluginService;
import common.services.KeyPressListener;

import static common.data.GameKeys.EIGHT;

public class BulletPlugin implements IGamePluginService, KeyPressListener {
    boolean isActive = true;

    @Override
    public void start(GameData gameData, World world) {
        isActive = true;
        gameData.setActivePlugin(this.getClass().getName(), isActive);
    }

    @Override
    public void stop(GameData gameData, World world) {
        isActive = false;
        gameData.setActivePlugin(this.getClass().getName(), isActive);
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Bullet.class) {
                world.removeEntity(e);
            }
        }
    }

    @Override
    public void onKeyPressed(int key, GameData gameData, World world) {
        if (key == EIGHT) {
            if (isActive) {
                stop(gameData, world);
            } else {
                start(gameData, world);
            }
        }
    }
}
