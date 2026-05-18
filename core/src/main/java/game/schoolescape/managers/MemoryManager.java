package game.schoolescape.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class MemoryManager {
    private static Preferences prefs = Gdx.app.getPreferences("schoolescape_prefs");

    public static void saveMusicSettings(boolean isOn) {
        prefs.putBoolean("music", isOn);
        prefs.flush();
    }

    public static boolean loadIsMusicOn() {
        return prefs.getBoolean("music", true);
    }

    public static void saveSoundSettings(boolean isOn) {
        prefs.putBoolean("sound", isOn);
        prefs.flush();
    }

    public static boolean loadIsSoundOn() {
        return prefs.getBoolean("sound", true);
    }
}
