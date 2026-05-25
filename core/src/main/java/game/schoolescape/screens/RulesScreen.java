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

        // Кнопка отцентрирована (400 - половина ширины кнопки)
        backButton = new UIFactory.Button(300, 45, 200, 50, "Назад", game.font, game.buttonTexture);
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Применяем вьюпорт, чтобы подложка и кнопки стояли ровно на экране Honor
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        if (game.menuBackground != null) {
            game.batch.draw(game.menuBackground, 0, 0, 800, 480);
        }

        // ДОБАВЛЕНО: Рисуем черную полупрозрачную подложку под правила (прозрачность 70%)
        if (game.whitePixel != null) {
            game.batch.setColor(0, 0, 0, 0.7f); // Черный цвет, альфа = 0.7

            // Рисуем рамку: x = 80, y = 110, ширина = 640, высота = 320
            game.batch.draw(game.whitePixel, 80, 110, 640, 320);

            game.batch.setColor(1, 1, 1, 1); // Сбрасываем цвет обратно в белый
        }

        // ДОБАВЛЕНО: Так как подложка темная, делаем текст правил БЕЛЫМ для идеальной читаемости
        game.font.setColor(1, 1, 1, 1);

        // Координаты X сдвинуты со 100 на 110, чтобы текст красиво сидел внутри черной рамки
        game.font.draw(game.batch, "ПРАВИЛА ИГРЫ", 335, 420);
        game.font.draw(game.batch, "Собирай 5, чтобы ускориться", 110, 370);
        game.font.draw(game.batch, "Избегай 2, чтобы не замедляться", 110, 340);
        game.font.draw(game.batch, "Сбивай учебники бумажными самолётиками", 110, 310);
        game.font.draw(game.batch, "   пока они не попали в тебя", 110, 290);
        game.font.draw(game.batch, "Не позволяй учителю поймать тебя!", 110, 260);
        game.font.draw(game.batch, "Собери нужное количество учебников", 110, 230);
        game.font.draw(game.batch, "   чтобы пройти уровень", 110, 210);
        game.font.draw(game.batch, "Пройди все 11 классов, чтобы сбежать из школы!", 110, 150);

        // ИСПРАВЛЕНО: Перед отрисовкой кнопки возвращаем глобальный ЧЕРНЫЙ цвет шрифта,
        // чтобы текст внутри кнопки "Назад" отображался корректно
        game.font.setColor(com.badlogic.gdx.graphics.Color.BLACK);
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
