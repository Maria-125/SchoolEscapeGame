package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.LevelManager;
import game.schoolescape.ui.UIFactory;

public class MenuScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private UIFactory.Button startButton;
    private UIFactory.Button settingsButton;
    private UIFactory.Button rulesButton;
    private OrthographicCamera uiCamera;

    public MenuScreen(SchoolEscapeGame game) {
        this.game = game;

        uiCamera = new OrthographicCamera(800, 480);
        uiCamera.position.set(400, 240, 0);
        uiCamera.update();

        startButton = new UIFactory.Button(300, 250, 200, 50, "Старт", game.font, game.buttonTexture);
        settingsButton = new UIFactory.Button(300, 180, 200, 50, "Настройки", game.font, game.buttonTexture);
        rulesButton = new UIFactory.Button(300, 110, 200, 50, "Правила", game.font, game.buttonTexture);
    }

    @Override
    public void render(float delta) {
        game.font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(uiCamera.combined);

        game.batch.begin();
        if (game.menuBackground != null) {
            game.batch.draw(game.menuBackground, 0, 0, 800, 480);
        }

        game.font.draw(game.batch, "SCHOOL ESCAPE", 310, 360);

        startButton.draw(game.batch);
        settingsButton.draw(game.batch);
        rulesButton.draw(game.batch);
        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touch);

            if (startButton.isHit(touch.x, touch.y)) {
                LevelManager levelManager = new LevelManager();

                levelManager.currentLevel = 1;
                levelManager.booksCollected = 0;
                levelManager.booksToCollect = levelManager.getBooksRequired();
                levelManager.setCheckpoint();

                com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("schoolescape_prefs");
                prefs.putInteger("saved_level", 1);
                prefs.flush();

                game.setScreen(new GameScreen(game, levelManager));
            }
            else if (settingsButton.isHit(touch.x, touch.y)) {
                game.setScreen(new SettingsScreen(game, this));
            }
            else if (rulesButton.isHit(touch.x, touch.y)) {
                game.setScreen(new RulesScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

}
