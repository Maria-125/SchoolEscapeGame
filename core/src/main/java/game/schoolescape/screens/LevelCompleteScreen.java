package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.LevelManager;
import game.schoolescape.ui.UIFactory;

public class LevelCompleteScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private LevelManager levelManager;
    private float averageGrade;

    private UIFactory.Button nextButton;
    private UIFactory.Button restartButton;
    private UIFactory.Button settingsButton;

    public LevelCompleteScreen(SchoolEscapeGame game, LevelManager levelManager, float averageGrade) {
        this.game = game;
        this.levelManager = levelManager;
        this.averageGrade = averageGrade;

        nextButton = new UIFactory.Button(300, 240, 200, 50, "Next Class", game.font);
        restartButton = new UIFactory.Button(300, 170, 200, 50, "Start Over", game.font);
        settingsButton = new UIFactory.Button(300, 100, 200, 50, "Settings", game.font);
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

        game.font.draw(game.batch, "Class " + levelManager.currentLevel + " Complete!", 250, 380);
        game.font.draw(game.batch, "Average Grade: " + String.format("%.2f", averageGrade), 250, 350);

        nextButton.draw(game.batch);
        restartButton.draw(game.batch);
        settingsButton.draw(game.batch);

        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touch);
            float tx = touch.x, ty = touch.y;

            if (nextButton.isHit(tx, ty)) {
                if (levelManager.currentLevel >= 11) {
                    game.setScreen(new WinScreen(game, levelManager, averageGrade));
                } else {
                    levelManager.nextLevel();
                    game.setScreen(new GameScreen(game, levelManager));
                }
            }
            else if (restartButton.isHit(tx, ty)) {
                // Возврат в главное меню
                game.setScreen(new MenuScreen(game));
            }
            else if (settingsButton.isHit(tx, ty)) {
                game.setScreen(new SettingsScreen(game, this));
            }
        }
    }
}
