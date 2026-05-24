package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.LevelManager;
import game.schoolescape.ui.UIFactory;

public class GameOverScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private LevelManager levelManager;
    private OrthographicCamera uiCamera;

    private UIFactory.Button restartLevelButton;
    private UIFactory.Button startOverButton;
    private UIFactory.Button settingsButton;

    public GameOverScreen(SchoolEscapeGame game, LevelManager levelManager) {
        this.game = game;
        this.levelManager = levelManager;

        uiCamera = new OrthographicCamera(800, 480);
        uiCamera.position.set(400, 240, 0);
        uiCamera.update();

        restartLevelButton = new UIFactory.Button(300, 250, 200, 50, "Заново уровень", game.font, game.buttonTexture);
        startOverButton = new UIFactory.Button(300, 180, 200, 50, "Начать сначала", game.font, game.buttonTexture);
        settingsButton = new UIFactory.Button(300, 110, 200, 50, "Настройки", game.font, game.buttonTexture);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(uiCamera.combined);

        game.batch.begin();
        if (game.menuBackground != null) {
            game.batch.draw(game.menuBackground, 0, 0, 800, 480);
        }

        game.font.draw(game.batch, "ИГРА ОКОНЧЕНА", 320, 380);
        game.font.draw(game.batch, "Тебя поймали!", 330, 350);

        restartLevelButton.draw(game.batch);
        startOverButton.draw(game.batch);
        settingsButton.draw(game.batch);
        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touch);
            float tx = touch.x, ty = touch.y;

            if (restartLevelButton.isHit(tx, ty)) {
                game.setScreen(new GameScreen(game, levelManager));
            }
            else if (startOverButton.isHit(tx, ty)) {
                // ИСПРАВЛЕНО: Сброс переданного levelManager до 1 класса, чтобы скорость учителя обнулилась
                levelManager.currentLevel = 1;
                levelManager.booksCollected = 0;
                levelManager.booksToCollect = levelManager.getBooksRequired();
                levelManager.setCheckpoint();

                com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("schoolescape_prefs");
                prefs.putInteger("saved_level", 1);
                prefs.flush();

                game.setScreen(new MenuScreen(game));
            }
            else if (settingsButton.isHit(tx, ty)) {
                game.setScreen(new SettingsScreen(game, this));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

}
