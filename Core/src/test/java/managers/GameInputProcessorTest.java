package managers;

import com.badlogic.gdx.Input;
import common.data.GameData;
import common.data.GameKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInputProcessorTest {
    private GameData gameData;
    private GameInputProcessor gameInputProcessor;

    @BeforeEach
    public void setUp() {
        gameData = new GameData();
        gameInputProcessor = new GameInputProcessor(gameData);
    }


    @Test
    public void testKeyDown_UP() {
        gameInputProcessor.keyDown(Input.Keys.UP);
        assertTrue(gameData.getKeys().isDown(GameKeys.UP));
    }
    @Test
    public void testKeyDown_LEFT() {
        gameInputProcessor.keyDown(Input.Keys.LEFT);
        assertTrue(gameData.getKeys().isDown(GameKeys.LEFT));
    }

    @Test
    public void testKeyDown_DOWN() {
        gameInputProcessor.keyDown(Input.Keys.DOWN);
        assertTrue(gameData.getKeys().isDown(GameKeys.DOWN));
    }

    @Test
    public void testKeyDown_RIGHT() {
        gameInputProcessor.keyDown(Input.Keys.RIGHT);
        assertTrue(gameData.getKeys().isDown(GameKeys.RIGHT));
    }

    @Test
    public void testKeyDown_ENTER() {
        gameInputProcessor.keyDown(Input.Keys.ENTER);
        assertTrue(gameData.getKeys().isDown(GameKeys.ENTER));
    }

    @Test
    public void testKeyDown_ESCAPE() {
        gameInputProcessor.keyDown(Input.Keys.ESCAPE);
        assertTrue(gameData.getKeys().isDown(GameKeys.ESCAPE));
    }

    @Test
    public void testKeyDown_SPACE() {
        gameInputProcessor.keyDown(Input.Keys.SPACE);
        assertTrue(gameData.getKeys().isDown(GameKeys.SPACE));
    }

    @Test
    public void testKeyDown_SHIFT() {
        gameInputProcessor.keyDown(Input.Keys.SHIFT_LEFT);
        assertTrue(gameData.getKeys().isDown(GameKeys.SHIFT));
        gameInputProcessor.keyDown(Input.Keys.SHIFT_RIGHT);
        assertTrue(gameData.getKeys().isDown(GameKeys.SHIFT));
    }

    @Test
    public void testKeyDown_ONE() {
        gameInputProcessor.keyDown(Input.Keys.NUM_1);
        assertTrue(gameData.getKeys().isDown(GameKeys.ONE));
        gameInputProcessor.keyDown(Input.Keys.NUMPAD_1);
        assertTrue(gameData.getKeys().isDown(GameKeys.ONE));
    }

    @Test
    public void testKeyDown_TWO() {
        gameInputProcessor.keyDown(Input.Keys.NUM_2);
        assertTrue(gameData.getKeys().isDown(GameKeys.TWO));
        gameInputProcessor.keyDown(Input.Keys.NUMPAD_2);
        assertTrue(gameData.getKeys().isDown(GameKeys.TWO));
    }

    @Test
    public void testKeyDown_TAB() {
        gameInputProcessor.keyDown(Input.Keys.TAB);
        assertTrue(gameData.getKeys().isDown(GameKeys.TAB));
    }

    @Test
    public void testKeyDown_DEL() {
        gameInputProcessor.keyDown(Input.Keys.DEL);
        assertTrue(gameData.getKeys().isDown(GameKeys.DEL));
    }

    @Test
    public void testKeyDown_EIGHT() {
        gameInputProcessor.keyDown(Input.Keys.NUM_8);
        assertTrue(gameData.getKeys().isDown(GameKeys.EIGHT));
        gameInputProcessor.keyDown(Input.Keys.NUMPAD_8);
        assertTrue(gameData.getKeys().isDown(GameKeys.EIGHT));
    }

    @Test
    public void testKeyDown_NINE() {
        gameInputProcessor.keyDown(Input.Keys.NUM_9);
        assertTrue(gameData.getKeys().isDown(GameKeys.NINE));
        gameInputProcessor.keyDown(Input.Keys.NUMPAD_9);
        assertTrue(gameData.getKeys().isDown(GameKeys.NINE));
    }
    @Test
    public void testKeyUp_UP() {
        gameInputProcessor.keyDown(Input.Keys.UP);
        gameInputProcessor.keyUp(Input.Keys.UP);
        assertFalse(gameData.getKeys().isDown(GameKeys.UP));
    }

    @Test
    public void testKeyUp_LEFT() {
        gameInputProcessor.keyDown(Input.Keys.LEFT);
        gameInputProcessor.keyUp(Input.Keys.LEFT);
        assertFalse(gameData.getKeys().isDown(GameKeys.LEFT));
    }

    @Test
    public void testKeyUp_DOWN() {
        gameInputProcessor.keyDown(Input.Keys.DOWN);
        gameInputProcessor.keyUp(Input.Keys.DOWN);
        assertFalse(gameData.getKeys().isDown(GameKeys.DOWN));
    }

    @Test
    public void testKeyUp_RIGHT() {
        gameInputProcessor.keyDown(Input.Keys.RIGHT);
        gameInputProcessor.keyUp(Input.Keys.RIGHT);
        assertFalse(gameData.getKeys().isDown(GameKeys.RIGHT));
    }

    @Test
    public void testKeyUp_ENTER() {
        gameInputProcessor.keyDown(Input.Keys.ENTER);
        gameInputProcessor.keyUp(Input.Keys.ENTER);
        assertFalse(gameData.getKeys().isDown(GameKeys.ENTER));
    }

    @Test
    public void testKeyUp_ESCAPE() {
        gameInputProcessor.keyDown(Input.Keys.ESCAPE);
        gameInputProcessor.keyUp(Input.Keys.ESCAPE);
        assertFalse(gameData.getKeys().isDown(GameKeys.ESCAPE));
    }

    @Test
    public void testKeyUp_SPACE() {
        gameInputProcessor.keyDown(Input.Keys.SPACE);
        gameInputProcessor.keyUp(Input.Keys.SPACE);
        assertFalse(gameData.getKeys().isDown(GameKeys.SPACE));
    }

    @Test
    public void testKeyUp_SHIFT() {
        gameInputProcessor.keyDown(Input.Keys.SHIFT_LEFT);
        gameInputProcessor.keyUp(Input.Keys.SHIFT_LEFT);
        assertFalse(gameData.getKeys().isDown(GameKeys.SHIFT));
        gameInputProcessor.keyDown(Input.Keys.SHIFT_RIGHT);
        gameInputProcessor.keyUp(Input.Keys.SHIFT_RIGHT);
        assertFalse(gameData.getKeys().isDown(GameKeys.SHIFT));
    }

    @Test
    public void testKeyUp_ONE() {
        gameInputProcessor.keyDown(Input.Keys.NUM_1);
        gameInputProcessor.keyUp(Input.Keys.NUM_1);
        assertFalse(gameData.getKeys().isDown(GameKeys.ONE));
        gameInputProcessor.keyDown(Input.Keys.NUMPAD_1);
        gameInputProcessor.keyUp(Input.Keys.NUMPAD_1);
        assertFalse(gameData.getKeys().isDown(GameKeys.ONE));
    }

    @Test
    public void testKeyUp_TWO() {
        gameInputProcessor.keyDown(Input.Keys.NUM_2);
        gameInputProcessor.keyUp(Input.Keys.NUM_2);
        assertFalse(gameData.getKeys().isDown(GameKeys.TWO));
        gameInputProcessor.keyDown(Input.Keys.NUMPAD_2);
        gameInputProcessor.keyUp(Input.Keys.NUMPAD_2);
        assertFalse(gameData.getKeys().isDown(GameKeys.TWO));
    }

    @Test
    public void testKeyUp_TAB() {
        gameInputProcessor.keyDown(Input.Keys.TAB);
        gameInputProcessor.keyUp(Input.Keys.TAB);
        assertFalse(gameData.getKeys().isDown(GameKeys.TAB));
    }

    @Test
    public void testKeyUp_DEL() {
        gameInputProcessor.keyDown(Input.Keys.DEL);
        gameInputProcessor.keyUp(Input.Keys.DEL);
        assertFalse(gameData.getKeys().isDown(GameKeys.DEL));
    }

    @Test
    public void testKeyUp_EIGHT() {
        gameInputProcessor.keyDown(Input.Keys.NUM_8);
        gameInputProcessor.keyUp(Input.Keys.NUM_8);
        assertFalse(gameData.getKeys().isDown(GameKeys.EIGHT));
        gameInputProcessor.keyDown(Input.Keys.NUMPAD_8);
        gameInputProcessor.keyUp(Input.Keys.NUMPAD_8);
        assertFalse(gameData.getKeys().isDown(GameKeys.EIGHT));
    }

    @Test
    public void testKeyUp_NINE() {
        gameInputProcessor.keyDown(Input.Keys.NUM_9);
        gameInputProcessor.keyUp(Input.Keys.NUM_9);
        assertFalse(gameData.getKeys().isDown(GameKeys.NINE));
        gameInputProcessor.keyDown(Input.Keys.NUMPAD_9);
        gameInputProcessor.keyUp(Input.Keys.NUMPAD_9);
        assertFalse(gameData.getKeys().isDown(GameKeys.NINE));
    }
}