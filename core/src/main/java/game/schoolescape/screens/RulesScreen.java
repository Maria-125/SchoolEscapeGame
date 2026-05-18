package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.ui.UIFactory;

public class RulesScreen extends ScreenAdapter {
    private SchoolEscapeGame game;
    private UIFactory.Button backButton;

    public RulesScreen(SchoolEscapeGame game) {
        this.game = game;
        backButton = new UIFactory.Button(300, 80, 200, 50, "Back", game.font);
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

        game.font.draw(game.batch, "RULES", 370, 395);

        game.font.draw(game.batch, "SPACE - jump", 250, 370);
        game.font.draw(game.batch, "F - shoot paper", 250, 340);
        game.font.draw(game.batch, "Collect 5 to speed up", 250, 310);
        game.font.draw(game.batch, "Avoid 2 to not slow down", 250, 280);
        game.font.draw(game.batch, "Shoot books before they hit you", 250, 250);
        game.font.draw(game.batch, "Don't let teacher catch you!", 250, 220);
        game.font.draw(game.batch, "Collect books to pass the level", 250, 190);
        game.font.draw(game.batch, "Complete 11 classes to escape!", 250, 160);

        backButton.draw(game.batch);

        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.camera.unproject(touch);
            float tx = touch.x, ty = touch.y;

            if (backButton.isHit(tx, ty)) {
                game.setScreen(new MenuScreen(game));
            }
        }
    }
}
