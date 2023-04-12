package playersystem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import common.data.Entity;
import common.data.GameData;
import common.data.GameKeys;
import common.data.World;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.player.Player;
import common.data.entities.weapon.IShoot;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static common.data.GameKeys.*;
import static java.util.stream.Collectors.toList;


public class PlayerProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(Player.class)) {
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
            if (gameData.getKeys().isPressed(GameKeys.SPACE)) {
                for (IShoot weapon : getIShoot()) {
                    weapon.useWeapon(player, gameData, world);
                }
            }
            if (gameData.getKeys().isDown(RIGHT)){
                player.setSprite(new Sprite(new Texture("Player/src/main/resources/player-kopi.png")));
            }
            if (gameData.getKeys().isDown(LEFT)){
                player.setSprite(new Sprite(new Texture("Player/src/main/resources/player.png")));
            }
        }

    }
    private Collection<? extends IShoot> getIShoot() {
        return ServiceLoader.load(IShoot.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}
