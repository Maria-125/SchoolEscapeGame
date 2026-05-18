package game.schoolescape.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.screens.GameScreen;
import game.schoolescape.screens.SettingsScreen;

public class PauseMenu {
    public boolean isPaused = false;

    private UIFactory.Button resumeButton;
    private UIFactory.Button settingsButton;

    public PauseMenu(SchoolEscapeGame game) {
        resumeButton = new UIFactory.Button(300, 280, 200, 50, "Resume", game.font);
        settingsButton = new UIFactory.Button(300, 210, 200, 50, "Settings", game.font);
    }

    public void toggle() {
        isPaused = !isPaused;
    }

    public void handleInput(SchoolEscapeGame game, Vector3 touch, GameScreen gameScreen) {
        if (!isPaused) return;

        float tx = touch.x, ty = touch.y;

        if (resumeButton.isHit(tx, ty)) {
            isPaused = false;
        }
        else if (settingsButton.isHit(tx, ty)) {
            game.setScreen(new SettingsScreen(game, gameScreen));
        }
    }

    public void drawPauseMenu(SpriteBatch batch, BitmapFont font, SchoolEscapeGame game) {
        if (!isPaused) return;

        batch.setColor(0, 0, 0, 0.7f);
        batch.draw(game.whitePixel, 0, 0, 800, 480);
        batch.setColor(1, 1, 1, 1);

        font.draw(batch, "PAUSED", 360, 380);

        resumeButton.draw(batch);
        settingsButton.draw(batch);
    }
}
