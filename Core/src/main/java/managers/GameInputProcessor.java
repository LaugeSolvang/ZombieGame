package managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import common.data.GameData;
import common.data.GameKeys;

public class GameInputProcessor extends InputAdapter {
    private final GameData gameData;

    public GameInputProcessor(GameData gameData) {
        this.gameData = gameData;
    }

    public boolean keyDown(int k) {
        if(k == Keys.UP) {
            gameData.getKeys().setKey(GameKeys.UP, true);
        }
        if(k == Keys.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, true);
        }
        if(k == Keys.DOWN) {
            gameData.getKeys().setKey(GameKeys.DOWN, true);
        }
        if(k == Keys.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, true);
        }
        if(k == Keys.ENTER) {
            gameData.getKeys().setKey(GameKeys.ENTER, true);
        }
        if(k == Keys.ESCAPE) {
            gameData.getKeys().setKey(GameKeys.ESCAPE, true);
        }
        if(k == Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, true);
        }
        if(k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, true);
        }
        if(k == Keys.NUM_1 || k == Keys.NUMPAD_1) {
            gameData.getKeys().setKey(GameKeys.ONE, true);
        }
        if(k == Keys.NUM_2 || k == Keys.NUMPAD_2) {
            gameData.getKeys().setKey(GameKeys.TWO, true);
        }
        if (k == Keys.TAB) {
            gameData.getKeys().setKey(GameKeys.TAB, true);
        }
        if (k == Keys.DEL) {
            gameData.getKeys().setKey(GameKeys.DEL, true);
        }
        if(k == Keys.NUM_8 || k == Keys.NUMPAD_8) {
            gameData.getKeys().setKey(GameKeys.EIGHT, true);
        }
        if(k == Keys.NUM_9 || k == Keys.NUMPAD_9) {
            gameData.getKeys().setKey(GameKeys.NINE, true);
        }
        return true;
    }

    public boolean keyUp(int k) {
        if(k == Keys.UP) {
            gameData.getKeys().setKey(GameKeys.UP, false);
        }
        if(k == Keys.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, false);
        }
        if(k == Keys.DOWN) {
            gameData.getKeys().setKey(GameKeys.DOWN, false);
        }
        if(k == Keys.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, false);
        }
        if(k == Keys.ENTER) {
            gameData.getKeys().setKey(GameKeys.ENTER, false);
        }
        if(k == Keys.ESCAPE) {
            gameData.getKeys().setKey(GameKeys.ESCAPE, false);
        }
        if(k == Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, false);
        }
        if(k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, false);
        }
        if(k == Keys.NUM_1 || k == Keys.NUMPAD_1) {
            gameData.getKeys().setKey(GameKeys.ONE, false);
        }
        if(k == Keys.NUM_2 || k == Keys.NUMPAD_2) {
            gameData.getKeys().setKey(GameKeys.TWO, false);
        }
        if (k == Keys.TAB) {
            gameData.getKeys().setKey(GameKeys.TAB, false);
        }
        if (k == Keys.DEL) {
            gameData.getKeys().setKey(GameKeys.DEL, false);
        }
        if(k == Keys.NUM_8 || k == Keys.NUMPAD_8) {
            gameData.getKeys().setKey(GameKeys.EIGHT, false);
        }
        if(k == Keys.NUM_9 || k == Keys.NUMPAD_9) {
            gameData.getKeys().setKey(GameKeys.NINE, false);
        }
        return true;
    }

}
