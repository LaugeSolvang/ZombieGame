package playersystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.InventoryPart;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;
import common.services.KeyPressListener;

import java.util.List;

import static common.data.GameKeys.DEL;

public class PlayerPlugin implements IGamePluginService, KeyPressListener {
    boolean isActive = true;

    @Override
    public void start(GameData gameData, World world) {
        isActive = true;
        Entity player = createPlayer(gameData);
        world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData) {
        Entity player = new Player();

        float deceleration = 2000;
        float acceleration = 4000;
        float maxSpeed = 100;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        int life = 10000;
        String path = "Player/src/main/resources/player.png";
        player.setPath(path);
        PositionPart positionPart = new PositionPart(x, y);
        positionPart.setDimension(new int[]{31,61});
        player.add(positionPart);
        player.add(new MovingPart(deceleration, acceleration, maxSpeed));
        player.add(new LifePart(life));
        player.add(new InventoryPart());

        return player;
    }
    @Override
    public void stop(GameData gameData, World world) {
        isActive = false;
        for (Entity playerEntity: world.getEntities(Player.class)) {
            Player player = (Player) playerEntity;
            InventoryPart inventory = player.getPart(InventoryPart.class);
            List<Weapon> weapons = inventory.getWeapons();

            // Drop all weapons in the world
            for (Weapon weapon: weapons) {
                // Set the position of the weapon to the player's position
                PositionPart playerPos = player.getPart(PositionPart.class);
                PositionPart weaponPos = weapon.getPart(PositionPart.class);
                weaponPos.setPosition(playerPos.getX(), playerPos.getY(), playerPos.getRadians());

                // Add the weapon to the world
                world.addEntity(weapon);
            }

            // Clear the player's inventory
            weapons.clear();

            // Remove the player from the world
            world.removeEntity(player);
        }
    }

    @Override
    public void onKeyPressed(int key, GameData gameData, World world) {
        if (key == DEL) {
            if (isActive) {
                stop(gameData, world);
            } else {
                start(gameData, world);
            }
        }
    }

}
