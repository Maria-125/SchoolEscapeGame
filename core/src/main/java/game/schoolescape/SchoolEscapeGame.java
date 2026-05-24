package game.schoolescape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import game.schoolescape.managers.FilePaths;
import game.schoolescape.managers.SoundManager;
import game.schoolescape.screens.MenuScreen;

public class SchoolEscapeGame extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public BitmapFont font;
    public SoundManager soundManager;
    public Texture menuBackground;
    public Texture winBackground;
    public Texture whitePixel;
    public Texture buttonTexture;

    // Глобальный вьюпорт для фиксации пропорций игры под размер телефона
    public Viewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Базовые виртуальные размеры игрового поля
        float gameWidth = 800;
        float gameHeight = 480;

        camera = new OrthographicCamera();

        // FitViewport держит пропорции 800х480 и добавляет черные полосы на длинных экранах
        viewport = new FitViewport(gameWidth, gameHeight, camera);
        viewport.apply();

        camera.position.set(gameWidth / 2f, gameHeight / 2f, 0);
        camera.update();

        // Загрузка шрифта через FreeType
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FilePaths.FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;

        // Явно передаем генератору русские и английские буквы для корректного рендеринга на Android
        parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
            + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789][_!$%#@|\\/?-+=()[]:;,.´`\"'<>";

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();

        font = generator.generateFont(parameter);
        generator.dispose();

        // Загрузка фонов и кнопок
        menuBackground = new Texture(FilePaths.MENU_BACKGROUND);
        winBackground = new Texture(FilePaths.WIN_BACKGROUND);
        buttonTexture = new Texture(FilePaths.BUTTON);

        // Загрузка звуков
        soundManager = new SoundManager();

        // Запуск главного меню
        setScreen(new MenuScreen(this));
    }

    // Автоматическое масштабирование игрового поля под физическое разрешение смартфона
    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
        if (menuBackground != null) menuBackground.dispose();
        if (winBackground != null) winBackground.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
        if (soundManager != null) soundManager.dispose();
        if (whitePixel != null) whitePixel.dispose();
    }
}
