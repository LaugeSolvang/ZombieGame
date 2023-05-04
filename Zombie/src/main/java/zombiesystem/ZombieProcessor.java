package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.zombie.IZombieAI;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.DamagePart;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;


public class ZombieProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        spawnZombies(gameData, world);
        moveZombies(gameData, world);
    }

    private void spawnZombies(GameData gameData, World world) {
        int tileSize = gameData.getTileSize();

        // calculate the number of zombies to spawn based on game time
        int zombiesToSpawn = (int) Math.sqrt(gameData.getGameTime() / 10000) + 3;

        int zombieSpawnInterval = 15;
        if ((gameData.getGameTime() % zombieSpawnInterval <= gameData.getDelta())) {
            for (int i = 0; i < zombiesToSpawn; i++) {
                for (ValidLocation validLocation : getValidLocations()) {
                    int[] spawnLocation = validLocation.generateSpawnLocation(world, gameData);
                    int x = spawnLocation[0];
                    int y = spawnLocation[1];
                    world.addEntity(createEntity(x*tileSize,y*tileSize));
                }
            }
        }
    }
    private void moveZombies(GameData gameData, World world) {
        for (Entity zombie : world.getEntities(Zombie.class)) {
            for (IZombieAI AI : getIZombieAIs()) {
                AI.moveTowards(gameData, zombie);
            }
        }
    }
    private Entity createEntity(int x, int y) {
        Entity zombie = new Zombie();

        float deceleration = 200;
        float acceleration = 400;
        float maxSpeed = 80;
        int life = 50;
        int damage = 1;
        String path = "zombie.png";
        zombie.setPath(path);

        zombie.add(new MovingPart(deceleration, acceleration, maxSpeed));
        zombie.add(new PositionPart(x, y));
        zombie.add(new LifePart(life));
        zombie.add(new DamagePart(damage));

        return zombie;
    }
    private Collection<? extends IZombieAI> getIZombieAIs() {
        return ServiceLoader.load(IZombieAI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
    private Collection<? extends ValidLocation> getValidLocations() {
        return ServiceLoader.load(ValidLocation.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
