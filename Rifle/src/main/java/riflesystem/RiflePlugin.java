package riflesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.InventoryPart;
import common.services.KeyPressListener;
import common.services.IGamePluginService;

import static common.data.GameKeys.ENTER;

public class RiflePlugin implements IGamePluginService, KeyPressListener {
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
            if (e.getClass() == Weapon.class) {
                world.removeEntity(e);
            }
        }
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Player.class) {
                InventoryPart inventory = e.getPart(InventoryPart.class);
                inventory.getWeapons().clear();
            }
        }
    }

    @Override
    public void onKeyPressed(int key, GameData gameData, World world) {
        if (key == ENTER) {
            if (isActive) {
                stop(gameData, world);
            } else {
                start(gameData, world);
            }
        }
    }
}
