package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.LevelManager;
import game.schoolescape.ui.UIFactory;

public class LevelCompleteScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private LevelManager levelManager;
    private float averageGrade;
    private OrthographicCamera uiCamera;

    private UIFactory.Button nextButton;
    private UIFactory.Button restartButton;
    private UIFactory.Button settingsButton;

    public LevelCompleteScreen(SchoolEscapeGame game, LevelManager levelManager, float averageGrade) {
        this.game = game;
        this.levelManager = levelManager;
        this.averageGrade = averageGrade;

        uiCamera = new OrthographicCamera(800, 480);
        uiCamera.position.set(400, 240, 0);
        uiCamera.update();

        nextButton = new UIFactory.Button(300, 240, 200, 50, "Следующий класс", game.font, game.buttonTexture);
        restartButton = new UIFactory.Button(300, 170, 200, 50, "Начать сначала", game.font, game.buttonTexture);
        settingsButton = new UIFactory.Button(300, 100, 200, 50, "Настройки", game.font, game.buttonTexture);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.camera.position.set(400, 240, 0);
        game.camera.update();

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        if (game.menuBackground != null) {
            game.batch.draw(game.menuBackground, 0, 0, 800, 480);
        }

        game.font.draw(game.batch, "Класс " + levelManager.currentLevel + " пройден!", 300, 380);
        game.font.draw(game.batch, "Средний балл: " + String.format("%.2f", averageGrade), 300, 350);

        nextButton.draw(game.batch);
        restartButton.draw(game.batch);
        settingsButton.draw(game.batch);
        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            game.viewport.unproject(touch);

            if (nextButton.isHit(touch.x, touch.y)) {
                if (levelManager.currentLevel >= 11) {
                    game.setScreen(new WinScreen(game, levelManager, averageGrade));
                } else {
                    levelManager.nextLevel();
                    game.setScreen(new GameScreen(game, levelManager));
                }
            }
            else if (restartButton.isHit(touch.x, touch.y)) {
                levelManager.currentLevel = 1;
                levelManager.booksCollected = 0;
                levelManager.booksToCollect = levelManager.getBooksRequired();
                levelManager.setCheckpoint();

                com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("schoolescape_prefs");
                prefs.putInteger("saved_level", 1);
                prefs.flush();

                game.setScreen(new MenuScreen(game));
            }
            else if (settingsButton.isHit(touch.x, touch.y)) {
                game.setScreen(new SettingsScreen(game, this));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }
}
