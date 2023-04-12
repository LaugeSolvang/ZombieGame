package weaponsystem;

import common.data.Entity;
import common.data.GameData;
import common.data.World;
import common.data.entities.weapon.Weapon;
import common.data.entities.weapon.WeaponSPI;
import common.data.entityparts.PositionPart;
import common.data.entityparts.SpritePart;
import common.services.IGamePluginService;


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
        weapon.add(new SpritePart("Weapon/src/main/resources/weapon.png"));
        SpritePart spritePart = weapon.getPart(SpritePart.class);

        float width = spritePart.getWidth();
        float height = spritePart.getHeight();
        weapon.add(new PositionPart(x, y, width, height, 3.14f/2));

        spritePart.setPosition(x,y);

        return weapon;

    }
}
