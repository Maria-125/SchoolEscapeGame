package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.LevelManager;
import game.schoolescape.ui.UIFactory;

public class GameOverScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private LevelManager levelManager;

    private UIFactory.Button restartLevelButton;
    private UIFactory.Button startOverButton;
    private UIFactory.Button settingsButton;

    public GameOverScreen(SchoolEscapeGame game, LevelManager levelManager) {
        this.game = game;
        this.levelManager = levelManager;

        restartLevelButton = new UIFactory.Button(300, 250, 200, 50, "Restart Level", game.font);
        startOverButton = new UIFactory.Button(300, 180, 200, 50, "Start Over", game.font);
        settingsButton = new UIFactory.Button(300, 110, 200, 50, "Settings", game.font);
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

        game.font.draw(game.batch, "GAME OVER", 330, 380);
        game.font.draw(game.batch, "You were caught!", 300, 350);

        restartLevelButton.draw(game.batch);
        startOverButton.draw(game.batch);
        settingsButton.draw(game.batch);

        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touch);
            float tx = touch.x, ty = touch.y;

            if (restartLevelButton.isHit(tx, ty)) {
                game.setScreen(new GameScreen(game, levelManager));
            }
            else if (startOverButton.isHit(tx, ty)) {
                game.setScreen(new MenuScreen(game));
            }
            else if (settingsButton.isHit(tx, ty)) {
                game.setScreen(new SettingsScreen(game, this));
            }
        }
    }
}
