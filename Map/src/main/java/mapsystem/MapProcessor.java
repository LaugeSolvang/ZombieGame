package mapsystem;

import common.data.GameData;
import common.data.World;
import common.data.entities.weapon.WeaponSPI;
import common.data.entities.zombie.ZombieSPI;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class MapProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        int numZombies = 3;
        int numWeapons = 1;
        int zombieSpawnInterval = 5;
        int weaponSpawnInterval = 10;
        if ((gameData.getGameTime() % zombieSpawnInterval <= gameData.getDelta()) && (gameData.getGameTime() > 1)) {
            spawnEntities(numZombies, "zombie", world, gameData);
        }
        if ((gameData.getGameTime() % weaponSpawnInterval <= gameData.getDelta()) && (gameData.getGameTime() > 1)) {
            spawnEntities(numWeapons, "weapon", world, gameData);
        }
    }

    private void spawnEntities(int numEntities, String entity, World world, GameData gameData) {
        String[][] map = world.getMap();
        //change so that tile size is stored in gameData
        int tileSize = 32;
        Random rand = new Random();

        boolean isValidLocation = false;
        int x = 0;
        int y = 0;
        while (!isValidLocation) {
            x = rand.nextInt(map.length - 1);
            y = rand.nextInt(map[0].length - 1);
            isValidLocation = isValidLocation(map, x, y);
        }

        for (int i = 0; i < numEntities; i++) {
            int xOffset = rand.nextInt(2 * tileSize) - tileSize;
            int yOffset = rand.nextInt(2 * tileSize) - tileSize;
            int entityX = (x * tileSize) + xOffset;
            int entityY = (y * tileSize) + yOffset;

            if (Objects.equals(entity, "weapon")) {
                for (WeaponSPI weapon : getWeaponSPI()) {
                    world.addEntity(weapon.createWeapon(entityX, entityY));
                }
            } else if (Objects.equals(entity, "zombie")) {
                for (ZombieSPI zombie : getZombieSPI()) {
                    world.addEntity(zombie.createZombie(entityX, entityY));
                }
            }
        }
    }
    private boolean isValidLocation(String[][] map, int x, int y) {
        int xStart = Math.max(x - 1, 0);
        int xEnd = Math.min(x + 1, map.length - 1);
        int yStart = Math.max(y - 1, 0);
        int yEnd = Math.min(y + 2, map[0].length - 1);

        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (Objects.equals(map[i][j], "obstruction")) {
                    return false;
                }
            }
        }
        return true;
    }

    protected Collection<? extends WeaponSPI> getWeaponSPI() {
        return ServiceLoader.load(WeaponSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    protected Collection<? extends ZombieSPI> getZombieSPI() {
        return ServiceLoader.load(ZombieSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
