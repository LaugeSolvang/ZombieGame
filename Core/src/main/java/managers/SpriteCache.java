package managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

public class SpriteCache {
    private static final Map<String, Sprite> spriteMap = new HashMap<>();
    public static Sprite getSprite(String path) {
        if (!spriteMap.containsKey(path)) {
            Texture texture = new Texture(Gdx.files.internal(path));
            Sprite sprite = new Sprite(texture);
            spriteMap.put(path, sprite);
        }
        return spriteMap.get(path);
    }
}

