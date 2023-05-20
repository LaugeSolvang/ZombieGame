package playersystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.player.Player;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.InventoryPart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;

import java.util.List;
import java.util.ServiceLoader;

import static common.data.GameKeys.*;


public class PlayerProcessor implements IEntityProcessingService {
    private IShoot shootImpl = null;
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
        float value = movingPart.getDx();
        if (movingPart.getDx() < 0){
            String path = "Player/src/main/resources/player.png";
            player.setPath(path);
        }
        if (movingPart.getDx() > 0){
            String path = "Player/src/main/resources/player-kopi.png";
            player.setPath(path);
        }
    }


    private void processWeapon(GameData gameData, World world, Player player) {
        InventoryPart inventory = player.getPart(InventoryPart.class);
        List<Weapon> weapons = inventory.getWeapons();
        if (!weapons.isEmpty()) {
            PositionPart playerPosPart = player.getPart(PositionPart.class);
            for (Weapon weapon: weapons) {
                PositionPart weaponPosPart = weapon.getPart(PositionPart.class);
                weaponPosPart.setPosition(playerPosPart.getX(), playerPosPart.getY(), playerPosPart.getRadians());
            }

            Weapon weapon = inventory.getCurrentWeapon();
            if (gameData.getKeys().isPressed(SPACE)) {
                if (weapon.getAmmo() == 0) {
                    inventory.cycleWeapon(world, 1);
                    inventory.removeWeapon(weapon);
                } else {
                    IShoot newShootImpl =  getShootImpl(inventory.getCurrentWeapon());
                    if (newShootImpl != null) {shootImpl = newShootImpl;}
                    shootImpl.useWeapon(player, gameData, world);
                }
            }
        }
    }

    private void processWeaponSwitching(GameData gameData, World world, Player player) {
        InventoryPart inventory = player.getPart(InventoryPart.class);
        if (inventory.getCurrentWeapon() == null) {
            return;
        }

        // Switch to the next or previous weapon
        if (gameData.getKeys().isPressed(ONE)) {
            inventory.cycleWeapon(world, -1);
        } else if (gameData.getKeys().isPressed(TWO)) {
            inventory.cycleWeapon(world, 1);
        }
    }
    private IShoot getShootImpl(Weapon weapon) {
        return ServiceLoader.load(IShoot.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(s -> s.getClass().getName().equals(weapon.getShootImplName()))
                .findFirst()
                .orElse(null);
    }

    public void setShootImpl(IShoot shootImpl) {
        this.shootImpl = shootImpl;
    }
}
