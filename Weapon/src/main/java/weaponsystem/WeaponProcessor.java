package weaponsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.GameKeys;
import common.data.World;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.WeaponSPI;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class WeaponProcessor implements IEntityProcessingService, IShoot {
    @Override
    public void process(GameData gameData, World world) {

    }

    @Override
    public void useWeapon(Entity shooter, GameData gameData, World world) {
        if (gameData.getKeys().isDown(GameKeys.SPACE)) {
            for (BulletSPI bullet : getBulletSPIs()) {
                world.addEntity(bullet.createBullet(shooter, gameData));
            }
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
