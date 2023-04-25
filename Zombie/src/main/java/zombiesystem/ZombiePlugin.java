package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.Zombie;
import common.data.entities.zombie.ZombieSPI;
import common.data.entityparts.DamagePart;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;

public class ZombiePlugin implements IGamePluginService, ZombieSPI {
    @Override
    public void start(GameData gameData, World world) {
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : world.getEntities()) {
            if (e.getClass() == Zombie.class) {
                world.removeEntity(e);
            }
        }    }

    @Override
    public Entity createEntity(int x, int y) {
        Entity zombie = new Zombie();

        float deacceleration = 500;
        float acceleration = 1000;
        float maxSpeed = 100;
        float rotationSpeed = 5;
        float radians = 0;
        String path = "zombie.png";
        zombie.setPath(path);

        zombie.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        zombie.add(new PositionPart(x, y, 0));
        zombie.add(new LifePart(1));
        zombie.add(new DamagePart(1));

        return zombie;    }
}
