package common.data.entityparts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import common.data.Entity;
import common.data.GameData;

public class SpritePart implements EntityPart{
    Sprite sprite;
    float width;
    float height;

    public SpritePart(String path) {
        this.sprite = new Sprite(new Texture(Gdx.files.internal(path)));
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x,y);
    }

    @Override
    public void process(GameData gameData, Entity entity) {

    }
}
