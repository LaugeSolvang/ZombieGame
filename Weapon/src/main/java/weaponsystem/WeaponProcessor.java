package weaponsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.player.Player;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.Weapon;
import common.data.entities.weapon.WeaponSPI;
import common.data.entityparts.PositionPart;
import common.data.entityparts.SpritePart;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class WeaponProcessor implements IEntityProcessingService, IShoot, WeaponSPI {
    @Override
    public void process(GameData gameData, World world) {
    }

    @Override
    public void useWeapon(Player player, GameData gameData, World world) {
        for (BulletSPI bullet : getBulletSPIs()) {
            world.addEntity(bullet.createBullet(player, gameData));
            player.getCurrentWeapon().reduceAmmon();
        }
    }

    @Override
    public Entity createWeapon(int x, int y) {
        Entity weapon = new Weapon("weaponsystem.WeaponProcessor", 10);
        weapon.add(new SpritePart("Weapon/src/main/resources/weapon.png"));
        SpritePart spritePart = weapon.getPart(SpritePart.class);
        spritePart.setPosition(x,y);

        float width = spritePart.getWidth();
        float height = spritePart.getHeight();
        weapon.add(new PositionPart(x, y, width, height, 3.14f/2));

        return weapon;
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
