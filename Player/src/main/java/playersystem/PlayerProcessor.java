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
            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);

            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(UP));
            movingPart.setDown(gameData.getKeys().isDown(DOWN));


            movingPart.process(gameData, player);
            positionPart.process(gameData, player);

            /*
            isPressed shoots everytime the button is pressed
            isDown shoots when the button is being held down (i.e. machinegun)
             */
            //Checks if the player has any weapons
            if (player.getWeapons().size() != 0) {
                //Sets the PositionPart of the weapon to the players so the weapon follows the player
                Weapon weapon = player.getCurrentWeapon();
                PositionPart positionPartWeapon = weapon.getPart(PositionPart.class);
                positionPartWeapon.setPosition(positionPart.getX(), positionPart.getY());

                //Gets the appropriate IShoot implementation if conditions are true
                if (gameData.getKeys().isPressed(SPACE) && weapon.getAmmo() > 0) {
                    IShoot shootImpl = getShootImpl(weapon);
                    shootImpl.useWeapon(player, gameData, world);
                } else if (weapon.getAmmo() == 0) {
                    world.removeEntity(weapon);
                }
            }
            //Checks if the one or two key is pressed and cycles the player's weapons accordingly
            if (gameData.getKeys().isPressed(ONE) || gameData.getKeys().isPressed(TWO)) {
                player.cycleWeapon(gameData.getKeys().isPressed(ONE) ? -1 : 1);
            }
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
