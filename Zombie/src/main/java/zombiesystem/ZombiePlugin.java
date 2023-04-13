package zombiesystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.Zombie;
import common.data.entities.zombie.ZombieSPI;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.data.entityparts.SpritePart;
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
    public Entity createZombie(int x, int y) {
        Entity zombie = new Zombie();

        zombie.add(new SpritePart("Zombie/src/main/resources/zombie.png"));
        SpritePart spritePart = zombie.getPart(SpritePart.class);
        spritePart.setPosition(x,y);

        float deacceleration = 10;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float radians = 0;

        float width = spritePart.getWidth();
        float height = spritePart.getHeight();

        zombie.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        zombie.add(new PositionPart(x, y, width, height, radians));
        zombie.add(new LifePart(1));

        return zombie;
    }
}
