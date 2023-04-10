package weaponsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.weapon.Weapon;
import common.data.entities.weapon.WeaponSPI;
import common.data.entityparts.PositionPart;
import common.services.IGamePluginService;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class WeaponPlugin implements IGamePluginService, WeaponSPI {
    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    @Override
    public Entity createWeapon(int x, int y) {
        Entity weapon = new Weapon();
        weapon.setSprite(new Sprite(new Texture(Gdx.files.internal("Bullet/src/main/resources/bullet.png"))));

        float width = weapon.getSprite().getWidth();
        float height = weapon.getSprite().getHeight();

        weapon.add(new PositionPart(x, y, width, height, 0));
        weapon.getSprite().setPosition(x,y);

        return weapon;

    }
}
