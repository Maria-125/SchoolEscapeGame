package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.LevelManager;
import game.schoolescape.ui.UIFactory;

public class WinScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private LevelManager levelManager;
    private float averageGrade;

    private UIFactory.Button menuButton;

    public WinScreen(SchoolEscapeGame game, LevelManager levelManager, float averageGrade) {
        this.game = game;
        this.levelManager = levelManager;
        this.averageGrade = averageGrade;

        menuButton = new UIFactory.Button(300, 150, 200, 50, "Main Menu", game.font);
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

        // Фон для победы
        if (game.winBackground != null) {
            game.batch.draw(game.winBackground, 0, 0, 800, 480);
        }

        // красный
        game.font.setColor(1, 0, 0, 1);
        game.font.draw(game.batch, "CONGRATULATIONS!", 270, 400);

        // синий
        game.font.setColor(0, 0, 1, 1);
        game.font.draw(game.batch, "You escaped from school!", 270, 350);

        // розовый
        game.font.setColor(0.8f, 0, 0.8f, 1);
        game.font.draw(game.batch, "Average Grade: " + String.format("%.2f", averageGrade), 270, 300);

        // фиолетовый
        game.font.setColor(0.4f, 0, 0.6f, 1);
        game.font.draw(game.batch, "You completed all 11 classes!", 270, 250);

        // Кнопка с белым текстом
        game.font.setColor(1, 1, 1, 1);
        menuButton.draw(game.batch);

        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touch);
            float tx = touch.x, ty = touch.y;

            if (menuButton.isHit(tx, ty)) {
                game.setScreen(new MenuScreen(game));
            }
        }
    }
}
