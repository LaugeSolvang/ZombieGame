package riflesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.bullet.BulletSPI;
import common.data.entities.player.Player;
import common.data.entities.weapon.IShoot;
import common.data.entities.weapon.Weapon;
import common.data.entityparts.*;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Objects;
import java.util.ServiceLoader;

import static common.data.GameData.TILE_SIZE;
import static java.util.stream.Collectors.toList;

public class RifleProcessor implements IEntityProcessingService, IShoot {
    String shootImplName = "riflesystem.RifleProcessor";
    private float rifleTime = 0.0f;
    private final int RIFLE_SPAWN_INTERVAL = 30;
    private Collection<? extends ValidLocation> validLocations = getValidLocation();
    private Collection<? extends BulletSPI> bulletSPIS = getBulletSPIs();

    @Override
    public void process(GameData gameData, World world) {
        if (!gameData.isActivePlugin(RiflePlugin.class.getName())) {
            rifleTime = 0;
            return;
        }

        for (Entity playerEntity : world.getEntities(Player.class)) {
            Player player = (Player)playerEntity;
            InventoryPart inventory = player.getPart(InventoryPart.class);
            Entity weaponEntity = inventory.getCurrentWeapon();
            if (weaponEntity != null) {
                updateWeaponDirection(playerEntity, weaponEntity);
                updateTimer(gameData, weaponEntity);
            }
        }
        spawnWeapons(gameData, world);
    }

    private void updateWeaponDirection(Entity playerEntity, Entity weaponEntity) {
        MovingPart movingPart = playerEntity.getPart(MovingPart.class);
        Weapon weapon = (Weapon) weaponEntity;

        if (weapon == null || !Objects.equals(weapon.getShootImplName(), shootImplName)) {
            return;
        }

        if (movingPart.getDx() < 0) {
            String path = "Rifle/src/main/resources/rifle-kopi.png";
            weapon.setPath(path);
        }
        if (movingPart.getDx() > 0) {
            String path = "Rifle/src/main/resources/rifle.png";
            weapon.setPath(path);
        }
    }
    private void spawnWeapons(GameData gameData, World world) {
        // calculate the number of weapons to spawn based on game time
        int weaponsToSpawn = (int) Math.sqrt(rifleTime / 4) + 1;

        if (rifleTime % RIFLE_SPAWN_INTERVAL <= gameData.getDelta()) {
            rifleTime += 0.1;
            for (ValidLocation validLocation : validLocations) {
                for (int i = 0; i < weaponsToSpawn; i++) {
                    int[] spawnLocation = validLocation.generateSpawnLocation(world, gameData);
                    int x = spawnLocation[0];
                    int y = spawnLocation[1];
                    world.addEntity(createEntity(x*TILE_SIZE,y*TILE_SIZE));
                }
            }
        }
    }
    private void updateTimer(GameData gameData, Entity weapon) {
        TimerPart timerPart = weapon.getPart(TimerPart.class);
        timerPart.process(gameData, weapon);
    }

    @Override
    public void useWeapon(Player player, GameData gameData, World world) {
        InventoryPart inventory = player.getPart(InventoryPart.class);
        Weapon weapon = inventory.getCurrentWeapon();
        TimerPart timerPart = weapon.getPart(TimerPart.class);
        if (timerPart.getExpiration() <= 0 && weapon.getAmmo() > 0) {
            for (BulletSPI bullet : bulletSPIS) {
                world.addEntity(bullet.createBullet(weapon, gameData));
                weapon.reduceAmmon();
                timerPart.setExpiration(weapon.getFireRate());
            }
        }
    }
    private Entity createEntity(int x, int y) {
        int ammo = 60;
        float fireRate = 0.1F;
        int damage = 25;
        Entity weapon = new Weapon(shootImplName, ammo, fireRate);

        String path = "Rifle/src/main/resources/rifle.png";
        weapon.setPath(path);

        PositionPart positionPart = new PositionPart(x, y);
        positionPart.setDimension(new int[]{32,32});
        weapon.add(positionPart);
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
    public void setValidLocations(Collection<? extends ValidLocation> validLocations) {
        this.validLocations = validLocations;
    }

    public void setBulletSPIS(Collection<? extends BulletSPI> bulletSPIS) {
        this.bulletSPIS = bulletSPIS;
    }
    public float getRifleTime() {
        return rifleTime;
    }
}
