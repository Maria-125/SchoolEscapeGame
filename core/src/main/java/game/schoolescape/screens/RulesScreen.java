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
        // ИСПРАВЛЕНО: Добавлен седьмой параметр game.buttonTexture, текст кнопки переведен на русский язык
        backButton = new UIFactory.Button(300, 80, 200, 50, "Назад", game.font, game.buttonTexture);
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

        // ИСПРАВЛЕНО: Все тексты правил переведены на русский язык и обновлены под управление двумя кнопками на телефоне
        game.font.draw(game.batch, "ПРАВИЛА ИГРЫ", 340, 390);
        game.font.draw(game.batch, "Собирай 5, чтобы ускориться", 100, 360);
        game.font.draw(game.batch, "Избегай 2, чтобы не замедляться", 100, 330);
        game.font.draw(game.batch, "Сбивай учебники бумажными самолётиками", 100, 300);
        game.font.draw(game.batch, "   пока они не попали в тебя", 100, 280);
        game.font.draw(game.batch, "Не позволяй учителю поймать тебя!", 100, 250);
        game.font.draw(game.batch, "Собери нужное количество учебников", 100, 220);
        game.font.draw(game.batch, "   чтобы пройти уровень", 100, 200);
        game.font.draw(game.batch, "Пройди все 11 классов, чтобы сбежать из школы!", 100, 170);

        backButton.draw(game.batch);

        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            game.viewport.unproject(touch);
            float tx = touch.x, ty = touch.y;

            if (backButton.isHit(tx, ty)) {
                game.setScreen(new MenuScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

}
