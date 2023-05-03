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
    int zombieSpawnInterval = 15;
    @Override
    public void process(GameData gameData, World world) {
        int tileSize = gameData.getTileSize();

        // calculate the number of zombies to spawn based on game time
        int zombiesToSpawn = (int) Math.sqrt(gameData.getGameTime() / 10000) + 3;

        if ((gameData.getGameTime() % zombieSpawnInterval <= gameData.getDelta())) {
            for (int i = 0; i < zombiesToSpawn; i++) {
                for (ValidLocation validLocation : getValidLocation()) {
                    int[] spawnLocation = validLocation.spawnEntities(world, gameData);
                    int x = spawnLocation[0];
                    int y = spawnLocation[1];
                    world.addEntity(createEntity(x*tileSize,y*tileSize));
                }
            }
        }

        for (IZombieAI AI : getIZombieAIs()) {
            AI.moveTowards(gameData, world);
        }
    }
    private Entity createEntity(int x, int y) {
        Entity zombie = new Zombie();

        float deacceleration = 200;
        float acceleration = 400;
        float maxSpeed = 80;
        float rotationSpeed = 5;
        float radians = 0;
        String path = "zombie.png";
        zombie.setPath(path);

        zombie.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        zombie.add(new PositionPart(x, y, 0));
        zombie.add(new LifePart(1));
        zombie.add(new DamagePart(1));

        return zombie;

    }
    private Collection<? extends IZombieAI> getIZombieAIs() {
        return ServiceLoader.load(IZombieAI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
    protected Collection<? extends ValidLocation> getValidLocation() {
        return ServiceLoader.load(ValidLocation.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
