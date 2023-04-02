package mapsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entityparts.MovingPart;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;

public class MapPlugin implements IGamePluginService {
    String[][] map;
    private Entity obstruction;
    @Override
    public void start(GameData gameData, World world) {
        map = new String[gameData.getDisplayHeight()/32][gameData.getDisplayWidth()/32];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                obstruction = createObstruction(i*32,j*32);
                world.addEntity(obstruction);
                map[i][j] = "obstruction";
            }
        }
    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    private Entity createObstruction(int x, int y) {
        Entity obstruction = new Obstruction();
        obstruction.add(new PositionPart(x, y,0));

        obstruction.setSprite(new Sprite(new Texture(Gdx.files.internal("Player/src/main/resources/pixel_art.png"))));
        obstruction.getSprite().setPosition(x,y);

        return obstruction;

    }
}
