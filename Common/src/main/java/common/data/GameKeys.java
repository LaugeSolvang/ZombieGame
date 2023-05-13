package common.data;

public class GameKeys {
    private static boolean[] keys;
    private static boolean[] pkeys;

    private static final int NUM_KEYS = 10;
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int ENTER = 4;
    public static final int ESCAPE = 5;
    public static final int SPACE = 6;
    public static final int SHIFT = 7;
    public static final int ONE = 8;
    public static final int TWO = 9;
    public GameKeys() {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];

    }
    public void update() {
        System.arraycopy(keys, 0, pkeys, 0, NUM_KEYS);
    }
    public void setKey(int k, boolean b) {
        keys[k] = b;
    }
    public boolean isDown(int k) {
        return keys[k];
    }
    public boolean isPressed(int k) {
        return keys[k] && !pkeys[k];
    }

}
