package common.data;


import java.util.HashMap;
import java.util.Map;

public class GameData {
    private float delta;
    private float gameTime;
    private int displayWidth;
    private int displayHeight;
    public static final int TILE_SIZE = 32;
    private final GameKeys keys = new GameKeys();
    private final Map<String, Boolean> activePlugins = new HashMap<>();

    public GameKeys getKeys() {
        return keys;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public float getGameTime() {
        return gameTime;
    }

    public void setGameTime(float gameTime) {
        this.gameTime = gameTime;
    }

    public void setActivePlugin(String pluginKey, boolean isActive) {
        activePlugins.put(pluginKey, isActive);
    }

    public boolean isActivePlugin(String pluginKey) {
        return activePlugins.getOrDefault(pluginKey, false);
    }
}
