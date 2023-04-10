package zombiesystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.zombie.Zombie;
import common.data.entityparts.LifePart;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;

public class ZombiePlugin implements IGamePluginService {
    private Entity zombie;
    @Override
    public void start(GameData gameData, World world) {

        zombie = createZombie(gameData);
        world.addEntity(zombie);

    }

    private Entity createZombie(GameData gameData) {
        Entity zombie = new Zombie();
        zombie.setSprite(new Sprite(new Texture(Gdx.files.internal("Zombie/src/main/resources/zombie.png"))));

        float deacceleration = 10;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2 + 128;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 3.1415f / 2;

        float width = zombie.getSprite().getWidth();
        float height = zombie.getSprite().getHeight();


        zombie.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        zombie.add(new PositionPart(x, y, width, height, radians));
        zombie.add(new LifePart(1));


        return zombie;
    }

    @Override
    public void stop(GameData gameData, World world) {

        world.removeEntity(zombie);
    }
}
