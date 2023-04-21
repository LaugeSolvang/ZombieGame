package mapsystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.weapon.WeaponSPI;
import common.data.entities.zombie.ZombieSPI;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class MapProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        /*
        if ((gameData.getGameTime() % 5 <= gameData.getDelta()) && (gameData.getGameTime() > 1)) {
            // Spawn a zombie
            for (ZombieSPI zombie : getZombieSPI()) {
                world.addEntity(zombie.createZombie(13 * 32, 13 * 32));
            }
            //Spawn a weapon
            for (WeaponSPI weapon : getWeaponSPI()) {
                world.addEntity(weapon.createWeapon(gameData.getDisplayWidth()/2+64, gameData.getDisplayHeight()/2+64));
            }
        }

         */
    }

    protected Collection<? extends WeaponSPI> getWeaponSPI() {
        return ServiceLoader.load(WeaponSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    protected Collection<? extends ZombieSPI> getZombieSPI() {
        return ServiceLoader.load(ZombieSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
