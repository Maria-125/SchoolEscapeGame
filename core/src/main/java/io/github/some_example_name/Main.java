package io.github.some_example_name;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import game.schoolescape.SchoolEscapeGame;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("School Escape");
        config.setWindowedMode(800, 480);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new SchoolEscapeGame(), config);
    }
}
