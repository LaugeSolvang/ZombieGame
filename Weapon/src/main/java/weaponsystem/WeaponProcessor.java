package weaponsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.player.Player;
import common.data.entities.ValidLocation;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class WeaponProcessor implements IEntityProcessingService, IShoot {
    int weaponSpawnInterval = 30;
    @Override
    public void process(GameData gameData, World world) {
        int tileSize = gameData.getTileSize();

        // calculate the number of zombies to spawn based on game time
        int weaponsToSpawn = (int) Math.sqrt(gameData.getGameTime() / 10000) + 2;

        if ((gameData.getGameTime() % weaponSpawnInterval <= gameData.getDelta())) {
            for (int i = 0; i < weaponsToSpawn; i++) {
                for (ValidLocation validLocation : getValidLocation()) {
                    int[] spawnLocation = validLocation.spawnEntities(world, gameData);
                    int x = spawnLocation[0];
                    int y = spawnLocation[1];
                    world.addEntity(createEntity(x*tileSize,y*tileSize));
                }
            }
        }
    }

    @Override
    public void useWeapon(Player player, GameData gameData, World world) {
        for (BulletSPI bullet : getBulletSPIs()) {
            world.addEntity(bullet.createBullet(player, gameData));
            player.getCurrentWeapon().reduceAmmon();
        }
    }
    private Entity createEntity(int x, int y) {
        Entity weapon = new Weapon("weaponsystem.WeaponProcessor", 20);

        String path = "weapon.png";
        weapon.setPath(path);

        weapon.add(new PositionPart(x, y, 3.14f/2));

        return weapon;    
    }
    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
    protected Collection<? extends ValidLocation> getValidLocation() {
        return ServiceLoader.load(ValidLocation.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
