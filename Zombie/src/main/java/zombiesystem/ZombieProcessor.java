package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.ValidLocation;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.DamagePart;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;

import static common.data.GameData.TILE_SIZE;
import static java.util.stream.Collectors.toList;


public class ZombieProcessor implements IEntityProcessingService {
    private float zombieTime = 0.0f;
    private final int ZOMBIE_SPAWN_INTERVAL = 15;
    private Collection<? extends ValidLocation> validLocations = getValidLocations();

    @Override
    public void process(GameData gameData, World world) {
        if (!gameData.isActivePlugin(ZombiePlugin.class.getName())) {
            zombieTime = 0F;
            return;
        }
        zombieTime += gameData.getDelta();

        moveZombies(gameData, world);
        spawnZombies(gameData, world);
    }

    private void spawnZombies(GameData gameData, World world) {
        // calculate the number of zombies to spawn based on game time
        int zombiesToSpawn = (int) Math.sqrt(zombieTime) + 3;

        if ((zombieTime % ZOMBIE_SPAWN_INTERVAL <= (gameData.getDelta()))) {
            zombieTime += 0.1;
            for (int i = 0; i < zombiesToSpawn; i++) {
                for (ValidLocation validLocation : validLocations) {
                    int[] spawnLocation = validLocation.generateSpawnLocation(world, gameData);
                    int x = spawnLocation[0] * TILE_SIZE;
                    int y = spawnLocation[1] * TILE_SIZE;
                    world.addEntity(createEntity(x,y));
                }
            }
        }
    }
    private void moveZombies(GameData gameData, World world) {
        for (Entity zombie : world.getEntities(Zombie.class)) {
            PositionPart zombiePosition = zombie.getPart(PositionPart.class);
            MovingPart zombieMovement = zombie.getPart(MovingPart.class);

            List<int[]> pathFinding = ((Zombie) zombie).getPathFinding();
            if (pathFinding == null) {
                return;
            }

            float currentX = (int) zombiePosition.getX();
            float currentY = (int) zombiePosition.getY();

            int targetX;
            int targetY;

            if (pathFinding.size() <= 2) {
                targetX = pathFinding.get(pathFinding.size() - 1)[0] * TILE_SIZE;
                targetY = pathFinding.get(pathFinding.size() - 1)[1] * TILE_SIZE;
            } else {
                targetX = pathFinding.get(2)[0] * TILE_SIZE;
                targetY = pathFinding.get(2)[1] * TILE_SIZE;

                if (targetX == currentX && targetY == currentY) {
                    pathFinding.remove(2);
                    if (pathFinding.size() >= 3) {
                        targetX = pathFinding.get(2)[0] * TILE_SIZE;
                        targetY = pathFinding.get(2)[1] * TILE_SIZE;
                    }
                }
            }

            float diffX = targetX - currentX;
            float diffY = targetY - currentY;
            float delta = gameData.getDelta();

            if (Math.abs(diffX) <= Math.abs(zombieMovement.getDx() * delta)) {
                zombieMovement.setDx(0);
                zombiePosition.setX(currentX);
            } else {
                zombieMovement.setRight(diffX > 0);
                zombieMovement.setLeft(diffX < 0);
            }
            if (Math.abs(diffY) <= Math.abs(zombieMovement.getDy() * delta)) {
                zombieMovement.setDy(0);
                zombiePosition.setY(currentY);
            } else {
                zombieMovement.setUp(diffY > 0);
                zombieMovement.setDown(diffY < 0);
            }

            zombieMovement.process(gameData, zombie);
            zombiePosition.process(gameData, zombie);

            zombieMovement.setUp(false);
            zombieMovement.setDown(false);
            zombieMovement.setRight(false);
            zombieMovement.setLeft(false);

            MovingPart movingPart = zombie.getPart(MovingPart.class);
            //makes the zombie face the direction they are going
            if (movingPart.getDx() < 0){
                String path = "Zombie/src/main/resources/zombie.png";
                zombie.setPath(path);
            }
            if (movingPart.getDx() > 0){
                String path = "Zombie/src/main/resources/zombie-kopi.png";
                zombie.setPath(path);
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
        String path = "Zombie/src/main/resources/zombie.png";
        zombie.setPath(path);

        PositionPart positionPart = new PositionPart(x, y);
        positionPart.setDimension(new int[]{31,61});
        zombie.add(positionPart);

        zombie.add(new MovingPart(deceleration, acceleration, maxSpeed));
        zombie.add(new LifePart(life));
        zombie.add(new DamagePart(damage));

        return zombie;
    }
    private Collection<? extends ValidLocation> getValidLocations() {
        return ServiceLoader.load(ValidLocation.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    public void setValidLocations(Collection<? extends ValidLocation> validLocations) {
        this.validLocations = validLocations;
    }

    public void setZombieTime(float zombieTime) {
        this.zombieTime = zombieTime;
    }
}
