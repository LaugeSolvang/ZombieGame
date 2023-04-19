package playersystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;

import java.util.ServiceLoader;

import static common.data.GameKeys.*;


public class PlayerProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity playerEntity : world.getEntities(Player.class)) {
            Player player = (Player) playerEntity;

            processMovement(gameData, player);
            processWeapon(gameData, world, player);
            processWeaponSwitching(gameData, player);
        }
    }

    private void processMovement(GameData gameData, Player player) {
        MovingPart movingPart = player.getPart(MovingPart.class);
        movingPart.setLeft(gameData.getKeys().isDown(LEFT));
        movingPart.setRight(gameData.getKeys().isDown(RIGHT));
        movingPart.setUp(gameData.getKeys().isDown(UP));
        movingPart.setDown(gameData.getKeys().isDown(DOWN));
        movingPart.process(gameData, player);

        PositionPart positionPart = player.getPart(PositionPart.class);
        positionPart.process(gameData, player);
    }
    

    private void processWeapon(GameData gameData, World world, Player player) {
        if (!player.getWeapons().isEmpty()) {
            Weapon weapon = player.getCurrentWeapon();
            PositionPart playerPositionPart = player.getPart(PositionPart.class);
            PositionPart weaponPositionPart = weapon.getPart(PositionPart.class);

            weaponPositionPart.setPosition(playerPositionPart.getX(), playerPositionPart.getY());

            if (gameData.getKeys().isPressed(SPACE) && weapon.getAmmo() > 0) {
                IShoot shootImpl = getShootImpl(weapon);
                shootImpl.useWeapon(player, gameData, world);
            } else if (weapon.getAmmo() == 0) {
                world.removeEntity(weapon);
            }
        }
    }

    private void processWeaponSwitching(GameData gameData, Player player) {
        if (gameData.getKeys().isPressed(ONE)) {
            player.cycleWeapon(-1);
        } else if (gameData.getKeys().isPressed(TWO)) {
            player.cycleWeapon(1);
        }
    }
    private IShoot getShootImpl(Weapon weapon) {
        return ServiceLoader.load(IShoot.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(s -> s.getClass().getName().equals(weapon.getShootImplName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find shoot implementation: " + weapon.getShootImplName()));
    }
}
