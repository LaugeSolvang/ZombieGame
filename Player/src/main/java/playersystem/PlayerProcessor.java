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

import java.util.List;
import java.util.ServiceLoader;

import static common.data.GameKeys.*;


public class PlayerProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity playerEntity : world.getEntities(Player.class)) {
            Player player = (Player) playerEntity;

            processMovement(gameData, player);
            processWeapon(gameData, world, player);
            processWeaponSwitching(gameData, world, player);

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

        //makes the player face the direction they are going
        if (movingPart.getDx() < 0){
            String path = "player.png";
            player.setPath(path);
        }
        if (movingPart.getDx() > 0){
            String path = "player-kopi.png";
            player.setPath(path);
        }
    }
    

    private void processWeapon(GameData gameData, World world, Player player) {
        List<Weapon> weapons = player.getWeapons();
        if (!weapons.isEmpty()) {
            PositionPart playerPosPart = player.getPart(PositionPart.class);
            for (Weapon weapon: weapons) {
                PositionPart weaponPosPart = weapon.getPart(PositionPart.class);
                weaponPosPart.setPosition(playerPosPart.getX(), playerPosPart.getY(), playerPosPart.getRadians());
            }

            Weapon weapon = player.getCurrentWeapon();
            if (gameData.getKeys().isPressed(SPACE)) {
                if (weapon.getAmmo() == 0) {
                    weapons.remove(weapon);
                    world.removeEntity(weapon);
                    player.cycleWeapon(1);
                    world.addEntity(player.getCurrentWeapon());
                } else {
                    IShoot shootImpl = getShootImpl(player.getCurrentWeapon());
                    shootImpl.useWeapon(player, gameData, world);
                }
            }
        }
    }

    private void processWeaponSwitching(GameData gameData, World world, Player player) {
        if (!(gameData.getKeys().isPressed(ONE) || gameData.getKeys().isPressed(TWO))) {
            return;
        }
        world.removeEntity(player.getCurrentWeapon().getID());
        if (gameData.getKeys().isPressed(ONE)) {
            player.cycleWeapon(-1);
        } else if (gameData.getKeys().isPressed(TWO)) {
            player.cycleWeapon(1);
        }
        world.addEntity(player.getCurrentWeapon());
    }
    private IShoot getShootImpl(Weapon weapon) {
        return ServiceLoader.load(IShoot.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(s -> s.getClass().getName().equals(weapon.getShootImplName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find shoot implementation: " + weapon.getShootImplName()));
    }
}
