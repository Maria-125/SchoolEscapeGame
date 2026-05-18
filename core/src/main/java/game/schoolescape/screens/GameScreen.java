package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import game.schoolescape.SchoolEscapeGame;
import game.schoolescape.managers.*;
import game.schoolescape.objects.*;
import game.schoolescape.ui.*;

public class GameScreen extends ScreenAdapter {
    SchoolEscapeGame game;
    LevelManager levelManager;
    WorldScroller world;
    Player player;
    Teacher teacher;
    Array<Book> books;
    Array<Grade> grades;
    Array<PaperShot> shots;
    GameLogic logic;
    GradeCalculator calculator;
    PauseMenu pause;
    private UIFactory.Button pauseButton;
    private OrthographicCamera uiCamera;
    float spawnTimer = 0;

    public GameScreen(SchoolEscapeGame game, LevelManager levelManager) {
        this.game = game;
        this.levelManager = levelManager;

        world = new WorldScroller();

        player = new Player();
        player.x = 250;
        player.setBaseSpeed(levelManager.getPlayerBaseSpeed());

        teacher = new Teacher();
        teacher.x = 50;
        teacher.y = 120;
        teacher.currentSpeed = levelManager.getTeacherStartSpeed();
        teacher.acceleration = levelManager.getTeacherAcceleration();
        teacher.speedIncreaseTimer = 0;

        books = new Array<>();
        grades = new Array<>();
        shots = new Array<>();

        logic = new GameLogic();
        calculator = new GradeCalculator();
        pause = new PauseMenu(game);

        spawnTimer = 0;

        levelManager.booksCollected = 0;
        levelManager.booksToCollect = levelManager.getBooksRequired();

        uiCamera = new OrthographicCamera(800, 480);
        uiCamera.position.set(400, 240, 0);
        uiCamera.update();

        pauseButton = new UIFactory.Button(720, 400, 60, 50, "||", game.font);

        System.out.println("=== НАЧАЛО УРОВНЯ " + levelManager.currentLevel + " ===");
        System.out.println("Скорость ученика: " + player.currentSpeed);
        System.out.println("Скорость учителя: " + teacher.currentSpeed);
        System.out.println("Ускорение учителя: " + teacher.acceleration);
        System.out.println("Интервал спавна: " + levelManager.getSpawnInterval());
        System.out.println("Нужно учебников: " + levelManager.booksToCollect);
    }

    @Override
    public void render(float delta) {
        handleInput();
        if (!pause.isPaused && !logic.gameOver) {
            updateGame(delta);
        }

        // Камера
        game.camera.position.x = player.x + 150;
        game.camera.position.y = 350;
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        world.draw(game.batch, game.camera.position.x);
        player.draw(game.batch);
        teacher.draw(game.batch);
        for (Book b : books) b.draw(game.batch);
        for (Grade g : grades) g.draw(game.batch);
        for (PaperShot s : shots) s.draw(game.batch);
        drawUI();
        game.batch.end();

        game.batch.setProjectionMatrix(uiCamera.combined);
        game.batch.begin();
        pauseButton.draw(game.batch);
        pause.drawPauseMenu(game.batch, game.font, game);
        game.batch.end();

        if (logic.gameOver) {
            game.setScreen(new GameOverScreen(game, levelManager));
        }
    }

    private void drawUI() {
        float x = game.camera.position.x - 380;
        float y = game.camera.position.y + 220;
        float textWidth = 280;
        float textHeight = 200;
        float offsetY = 20;

        // Подложка
        game.batch.setColor(0, 0, 0, 0.6f);
        game.batch.draw(game.whitePixel, x - 10, y - textHeight + offsetY, textWidth, textHeight);
        game.batch.setColor(1, 1, 1, 1);

        // Текст
        game.font.draw(game.batch, "Class: " + levelManager.currentLevel + "/11", x, y);
        game.font.draw(game.batch, "Lives: " + logic.lives, x, y - 30);
        game.font.draw(game.batch, "Avg Grade: " + String.format("%.2f", calculator.getAverage()), x, y - 60);
        game.font.draw(game.batch, "Player Speed: " + (int)player.currentSpeed, x, y - 90);
        game.font.draw(game.batch, "Teacher Speed: " + (int)teacher.currentSpeed, x, y - 120);
        game.font.draw(game.batch, "Books: " + levelManager.booksCollected + "/" + levelManager.booksToCollect, x, y - 150);
    }

    void updateGame(float delta) {
        player.update(delta);
        teacher.update(player.x, player.currentSpeed, delta);
        teacher.clampToCamera(game.camera.position.x);

        updateObjects(delta);
        spawnObjects(delta);
        checkCollisions();
        checkTeacherCaught();
        checkLevelCompletion();
    }

    void spawnObjects(float delta) {
        spawnTimer += delta;
        float interval = levelManager.getSpawnInterval();
        if (spawnTimer > interval) {
            spawnTimer = 0;
            float spawnX = game.camera.position.x + 600;
            int type = (int)(Math.random() * 3);
            if (type == 0) {
                books.add(new Book(spawnX));
            } else if (type == 1) {
                grades.add(new Grade(spawnX, 5));
            } else {
                grades.add(new Grade(spawnX, 2));
            }
        }
    }

    void checkCollisions() {
        for (int i = 0; i < shots.size; i++) {
            PaperShot shot = shots.get(i);
            for (int j = 0; j < books.size; j++) {
                Book book = books.get(j);
                if (book.isVisible(game.camera.position.x) && shot.getBounds().overlaps(book.getBounds())) {
                    shots.removeIndex(i--);
                    books.removeIndex(j--);
                    levelManager.booksCollected++;
                    if (game.soundManager != null && game.soundManager.hit != null)
                        game.soundManager.hit.play();
                    break;
                }
            }
        }

        for (int i = 0; i < grades.size; i++) {
            Grade grade = grades.get(i);
            if (player.getBounds().overlaps(grade.getBounds())) {
                calculator.addGrade(grade.value);
                if (grade.value == 5) {
                    player.increaseSpeed();
                    if (game.soundManager != null && game.soundManager.collect5 != null)
                        game.soundManager.collect5.play();
                } else {
                    player.decreaseSpeed();
                    if (game.soundManager != null && game.soundManager.collect2 != null)
                        game.soundManager.collect2.play();
                }
                grades.removeIndex(i--);
            }
        }

        for (int i = 0; i < books.size; i++) {
            Book book = books.get(i);
            if (book.isVisible(game.camera.position.x) && player.getBounds().overlaps(book.getBounds())) {
                books.removeIndex(i--);
                logic.loseLife();
                teacher.currentSpeed += 5;
                if (game.soundManager != null && game.soundManager.hit != null)
                    game.soundManager.hit.play();
            }
        }
    }

    void checkTeacherCaught() {
        if (player.getBounds().overlaps(teacher.getBounds())) {
            logic.loseLife();
            player.x = 250;
            teacher.x = 50;
            player.resetSpeed();
            teacher.currentSpeed = levelManager.getTeacherStartSpeed();
            teacher.speedIncreaseTimer = 0;
            levelManager.resetToCheckpoint();
            if (game.soundManager != null && game.soundManager.hit != null)
                game.soundManager.hit.play();
        }
    }

    void checkLevelCompletion() {
        if (levelManager.booksCollected >= levelManager.booksToCollect) {
            levelManager.setCheckpoint();
            float avg = calculator.getAverage();

            if (levelManager.currentLevel >= 11) {
                game.setScreen(new WinScreen(game, levelManager, avg));
            } else {
                game.setScreen(new LevelCompleteScreen(game, levelManager, avg));
            }
        }
    }

    void handleInput() {
        // Клавиша SPACE для прыжка
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !pause.isPaused) {
            player.jump();
            if (game.soundManager != null && game.soundManager.jump != null)
                game.soundManager.jump.play();
        }

        // Клавиша F для стрельбы
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && !pause.isPaused) {
            float bulletX = player.x + 40;
            float bulletY = player.y + 25;
            shots.add(new PaperShot(bulletX, bulletY));
            if (game.soundManager != null && game.soundManager.shoot != null)
                game.soundManager.shoot.play();
        }

        // Клавиша P для паузы
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause.toggle();
        }

        // Обработка касаний
        if (Gdx.input.justTouched()) {
            float screenX = Gdx.input.getX();
            float screenY = Gdx.input.getY();

            Vector3 uiTouch = new Vector3(screenX, screenY, 0);
            uiCamera.unproject(uiTouch);

            if (pauseButton != null && pauseButton.isHit(uiTouch.x, uiTouch.y)) {
                pause.toggle();
                return;
            }

            if (pause.isPaused) {
                pause.handleInput(game, uiTouch, this);
                return;
            }

            // Мировые координаты
            Vector3 worldTouch = new Vector3(screenX, screenY, 0);
            game.camera.unproject(worldTouch);
            float tx = worldTouch.x, ty = worldTouch.y;

            if (!pause.isPaused && !logic.gameOver) {
                float bulletX = player.x + 40;
                float bulletY = player.y + 25;
                shots.add(new PaperShot(bulletX, bulletY));
                if (game.soundManager != null && game.soundManager.shoot != null)
                    game.soundManager.shoot.play();
            }
        }
    }

    void updateObjects(float delta) {
        for (int i = 0; i < shots.size; i++) {
            shots.get(i).update(delta);
            if (!shots.get(i).active) shots.removeIndex(i--);
        }
        for (int i = 0; i < books.size; i++) {
            books.get(i).update(delta);
            if (!books.get(i).active) books.removeIndex(i--);
        }
        for (int i = 0; i < grades.size; i++) {
            grades.get(i).update(delta);
            if (!grades.get(i).active) grades.removeIndex(i--);
        }
    }
}
