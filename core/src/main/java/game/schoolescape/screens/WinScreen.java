package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.LevelManager;
import game.schoolescape.ui.UIFactory;

public class WinScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private LevelManager levelManager;
    private float averageGrade;
    private OrthographicCamera uiCamera;

    private UIFactory.Button menuButton;

    public WinScreen(SchoolEscapeGame game, LevelManager levelManager, float averageGrade) {
        this.game = game;
        this.levelManager = levelManager;
        this.averageGrade = averageGrade;

        uiCamera = new OrthographicCamera(800, 480);
        uiCamera.position.set(400, 240, 0);
        uiCamera.update();

        menuButton = new UIFactory.Button(300, 150, 200, 50, "Начать сначала", game.font, game.buttonTexture);
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

        if (game.winBackground != null) {
            game.batch.draw(game.winBackground, 0, 0, 800, 480);
        }

        // красный
        game.font.setColor(1, 0, 0, 1);
        game.font.draw(game.batch, "ПОЗДРАВЛЯЕМ!", 320, 400);

        // синий
        game.font.setColor(0, 0, 1, 1);
        game.font.draw(game.batch, "Вы сбежали из школы!", 285, 350);

        // розовый
        game.font.setColor(0.8f, 0, 0.8f, 1);
        game.font.draw(game.batch, "Средний балл: " + String.format("%.2f", averageGrade), 310, 300);

        // фиолетовый
        game.font.setColor(0.4f, 0, 0.6f, 1);
        game.font.draw(game.batch, "Вы прошли все 11 классов!", 270, 250);

        game.font.setColor(1, 1, 1, 1);
        menuButton.draw(game.batch);

        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            game.viewport.unproject(touch);

            if (menuButton.isHit(touch.x, touch.y)) {
                com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("schoolescape_prefs");
                prefs.putInteger("saved_level", 1);
                prefs.flush();

                game.setScreen(new MenuScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }
}
