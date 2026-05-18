package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.MemoryManager;
import game.schoolescape.ui.UIFactory;

public class SettingsScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private UIFactory.Button musicButton;
    private UIFactory.Button soundButton;
    private UIFactory.Button backButton;
    private ScreenAdapter previousScreen;

    public SettingsScreen(SchoolEscapeGame game, ScreenAdapter previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        String musicText = MemoryManager.loadIsMusicOn() ? "Music: ON" : "Music: OFF";
        String soundText = MemoryManager.loadIsSoundOn() ? "Sound: ON" : "Sound: OFF";

        musicButton = new UIFactory.Button(300, 300, 200, 50, musicText, game.font);
        soundButton = new UIFactory.Button(300, 230, 200, 50, soundText, game.font);
        backButton = new UIFactory.Button(300, 160, 200, 50, "Back", game.font);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.position.set(400, 240, 0);
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();
        if (game.menuBackground != null) {
            game.batch.draw(game.menuBackground, 0, 0, 800, 480);
        }
        game.font.draw(game.batch, "Settings", 350, 380);
        musicButton.draw(game.batch);
        soundButton.draw(game.batch);
        backButton.draw(game.batch);
        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touch);
            float tx = touch.x, ty = touch.y;

            if (musicButton.isHit(tx, ty)) {
                boolean newState = !MemoryManager.loadIsMusicOn();
                MemoryManager.saveMusicSettings(newState);
                game.soundManager.isMusicOn = newState;
                if (newState) game.soundManager.playMusic();
                else game.soundManager.stopMusic();
                musicButton.setText(newState ? "Music: ON" : "Music: OFF");
            }
            else if (soundButton.isHit(tx, ty)) {
                boolean newState = !MemoryManager.loadIsSoundOn();
                MemoryManager.saveSoundSettings(newState);
                game.soundManager.isSoundOn = newState;
                soundButton.setText(newState ? "Sound: ON" : "Sound: OFF");
            }
            else if (backButton.isHit(tx, ty)) {
                if (previousScreen != null) {
                    game.setScreen(previousScreen);
                } else {
                    game.setScreen(new MenuScreen(game));
                }
            }
        }
    }
}
