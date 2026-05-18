package game.schoolescape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

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

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(800, 480);
        camera.position.set(400, 240, 0);
        camera.update();

        // Загрузка шрифта через FreeType
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FilePaths.FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();
        font = generator.generateFont(parameter);
        generator.dispose();

        // Загрузка фонов
        menuBackground = new Texture(FilePaths.MENU_BACKGROUND);
        winBackground = new Texture(FilePaths.WIN_BACKGROUND);

        // Загрузка звуков
        soundManager = new SoundManager();

        // Запуск меню
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (menuBackground != null) menuBackground.dispose();
        if (winBackground != null) winBackground.dispose();
        soundManager.dispose();
        whitePixel.dispose();
    }
}
