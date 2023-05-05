package riflesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.player.Player;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.DamagePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.data.entityparts.TimerPart;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Objects;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class RifleProcessor implements IEntityProcessingService, IShoot {
    String shootImplName = "riflesystem.RifleProcessor";
    @Override
    public void process(GameData gameData, World world) {
        updateWeaponDirection(world);
        spawnWeapons(gameData, world);
        updateTimer(gameData, world);
    }

    private void updateWeaponDirection(World world) {
        for (Entity playerEntity : world.getEntities(Player.class)) {
            Player player = (Player) playerEntity;
            MovingPart movingPart = player.getPart(MovingPart.class);

            Weapon currentWeapon = player.getCurrentWeapon();

            if (currentWeapon == null || !Objects.equals(currentWeapon.getShootImplName(), shootImplName)) {
                return;
            }
            String path = "rifle.png";
            currentWeapon.setPath(path);

        }
    }
    private void spawnWeapons(GameData gameData, World world) {
        int tileSize = gameData.getTileSize();
        // calculate the number of weapons to spawn based on game time
        int weaponsToSpawn = (int) Math.sqrt(gameData.getGameTime() / 10000) + 2;

        int weaponSpawnInterval = 30;
        if ((gameData.getGameTime() % weaponSpawnInterval <= gameData.getDelta())) {
            for (ValidLocation validLocation : getValidLocation()) {
                for (int i = 0; i < weaponsToSpawn; i++) {
                    int[] spawnLocation = validLocation.generateSpawnLocation(world, gameData);
                    int x = spawnLocation[0];
                    int y = spawnLocation[1];
                    world.addEntity(createEntity(x*tileSize,y*tileSize));
                }
            }
        }
    }
    private void updateTimer(GameData gameData, World world) {
        for (Entity weaponEntity : world.getEntities(Weapon.class)) {
            Weapon weapon = (Weapon) weaponEntity;
            TimerPart timerPart = weapon.getPart(TimerPart.class);
            timerPart.process(gameData, weaponEntity);
        }
    }

    @Override
    public void useWeapon(Player player, GameData gameData, World world) {
        TimerPart timerPart = player.getCurrentWeapon().getPart(TimerPart.class);
        Weapon weapon = player.getCurrentWeapon();
        if (timerPart.getExpiration() <= 0 && weapon.getAmmo() > 0) {
            for (BulletSPI bullet : getBulletSPIs()) {
                world.addEntity(bullet.createBullet(weapon, gameData));
                weapon.reduceAmmon();
                timerPart.setExpiration(weapon.getFireRate());
            }
        }
    }
    private Entity createEntity(int x, int y) {
        int ammo = 200;
        float fireRate = 0.1F;
        int damage = 25;
        Entity weapon = new Weapon(shootImplName, ammo, fireRate);

        String path = "rifle.png";
        weapon.setPath(path);

        weapon.add(new PositionPart(x, y));
        weapon.add(new TimerPart(0));
        weapon.add(new DamagePart(damage));

        return weapon;
    }
    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
    private Collection<? extends ValidLocation> getValidLocation() {
        return ServiceLoader.load(ValidLocation.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
