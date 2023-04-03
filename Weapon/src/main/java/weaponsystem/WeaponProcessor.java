package weaponsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.WeaponSPI;
import common.services.IEntityProcessingService;

public class WeaponProcessor implements IEntityProcessingService, WeaponSPI, IShoot {
    @Override
    public void process(GameData gameData, World world) {

    }

    @Override
    public Entity createWeapon(int x, int y) {
        return null;
    }

    @Override
    public Entity useWeapon(Entity shooter, GameData gameData) {
        return null;
    }
}
