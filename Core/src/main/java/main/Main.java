package main;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Zombie game");
        int width = 1500;
        int height = 800;
        cfg.setWindowSizeLimits(width, height, width, height);
        cfg.setWindowedMode(width, height);
        cfg.setResizable(false);

        new Lwjgl3Application(new Game(), cfg);
    }
}
