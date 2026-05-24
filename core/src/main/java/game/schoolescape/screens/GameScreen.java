package game.schoolescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

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
    private FitViewport uiViewport;
    float spawnTimer = 0;

    private UIFactory.Button mobileJumpButton;
    private UIFactory.Button mobileShootButton;
    private Texture jumpBtnTexture;
    private Texture shootBtnTexture;

    // Эффекты
    private ShapeRenderer shapeRenderer;
    private static class Particle {
        float x, y, vx, vy, life;
        com.badlogic.gdx.graphics.Color color;
    }
    private Array<Particle> particles;

    public GameScreen(SchoolEscapeGame game, LevelManager levelManager) {
        this.game = game;
        this.levelManager = levelManager;

        world = new WorldScroller();

        player = new Player();
        teacher = new Teacher();

        game.camera.position.x = 400;
        game.camera.position.y = 350;
        game.camera.update();

        resetPositions();

        books = new Array<>();
        grades = new Array<>();
        shots = new Array<>();

        logic = new GameLogic();
        calculator = new GradeCalculator();
        pause = new PauseMenu(game);

        spawnTimer = 0;

        levelManager.booksCollected = 0;
        levelManager.booksToCollect = levelManager.getBooksRequired();

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(800, 480, uiCamera);
        uiViewport.apply();
        uiCamera.position.set(400, 240, 0);
        uiCamera.update();

        pauseButton = new UIFactory.Button(720, 410, 60, 50, "||", game.font, game.buttonTexture);

        jumpBtnTexture = new Texture(Gdx.files.internal(FilePaths.JUMP_BUTTON));
        mobileJumpButton = new UIFactory.Button(20, 20, 90, 90, "", game.font, jumpBtnTexture);

        shootBtnTexture = new Texture(Gdx.files.internal(FilePaths.SHOOT_BUTTON));
        mobileShootButton = new UIFactory.Button(690, 20, 90, 90, "", game.font, shootBtnTexture);

        shapeRenderer = new ShapeRenderer();
        particles = new Array<>();

        System.out.println("=== НАЧАЛО УРОВНЯ " + levelManager.currentLevel + " ===");
    }

    public void resetPositions() {
        if (player != null) {
            player.x = 250;
            player.y = 150;
            player.velocityY = 0;
            player.isGrounded = true;
            player.resetSpeed();
            player.setBaseSpeed(levelManager.getPlayerBaseSpeed());
        }
        if (teacher != null) {
            teacher.x = 50;
            teacher.y = 120;
            teacher.resetSpeed();
            teacher.currentSpeed = levelManager.getTeacherStartSpeed();
            teacher.acceleration = levelManager.getTeacherAcceleration();
            teacher.speedIncreaseTimer = 0;
        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        if (!pause.isPaused && !logic.gameOver) {
            updateGame(delta);
        }

        game.camera.position.x = player.x + 150;
        game.camera.position.y = 350;
        game.camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        world.draw(game.batch, game.camera.position.x);
        player.draw(game.batch);
        teacher.draw(game.batch);
        for (Book b : books) b.draw(game.batch);
        for (Grade g : grades) g.draw(game.batch);
        for (PaperShot s : shots) s.draw(game.batch);
        drawUI();
        game.batch.end();

        if (particles.size > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(game.camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (Particle p : particles) {
                shapeRenderer.setColor(p.color.r, p.color.g, p.color.b, p.life * 2);
                shapeRenderer.rect(p.x, p.y, 8, 8);
            }
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        game.batch.setProjectionMatrix(uiCamera.combined);
        game.batch.begin();
        pauseButton.draw(game.batch);

        if (!pause.isPaused && !logic.gameOver) {
            mobileJumpButton.draw(game.batch);
            mobileShootButton.draw(game.batch);
        }

        pause.drawPauseMenu(game.batch, game.font, game);
        game.batch.end();

        if (logic.gameOver) {
            game.setScreen(new GameOverScreen(game, levelManager));
        }
    }

    private void drawUI() {
        float x = game.camera.position.x - 380;
        float y = game.camera.position.y + 220;
        float textWidth = 310;
        float textHeight = 200;
        float offsetY = 20;

        if (game.whitePixel != null) {
            game.batch.setColor(0, 0, 0, 0.6f);
            game.batch.draw(game.whitePixel, x - 10, y - textHeight + offsetY, textWidth, textHeight);
            game.batch.setColor(1, 1, 1, 1);
        }

        game.font.draw(game.batch, "Класс: " + levelManager.currentLevel + "/11", x, y);
        game.font.draw(game.batch, "Жизни: " + logic.lives, x, y - 30);
        game.font.draw(game.batch, "Ср. балл: " + String.format("%.2f", calculator.getAverage()), x, y - 60);
        game.font.draw(game.batch, "Скорость ученика: " + (int)player.currentSpeed, x, y - 90);
        game.font.draw(game.batch, "Скорость учителя: " + (int)teacher.currentSpeed, x, y - 120);
        game.font.draw(game.batch, "Учебники: " + levelManager.booksCollected + "/" + levelManager.booksToCollect, x, y - 150);
    }

    void updateGame(float delta) {
        teacher.update(player.x, player.currentSpeed, delta);

        if (!teacher.isHitting) {
            player.update(delta);
            teacher.clampToCamera(game.camera.position.x);
            updateObjects(delta);
            spawnObjects(delta);
        }

        checkCollisions();
        checkTeacherCaught();
        checkLevelCompletion();

        for (int i = particles.size - 1; i >= 0; i--) {
            Particle p = particles.size > i ? particles.get(i) : null;
            if (p != null) {
                p.life -= delta;
                p.x += p.vx * delta;
                p.y += p.vy * delta;
                p.vy -= 400 * delta;
                if (p.life <= 0) particles.removeIndex(i);
            }
        }
    }


    void updateObjects(float delta) {
        for (int i = 0; i < books.size; i++) {
            Book b = books.get(i);
            b.update(delta);
            if (!b.active) {
                b.dispose();
                books.removeIndex(i--);
            }
        }
        for (int i = 0; i < grades.size; i++) {
            Grade g = grades.get(i);
            g.update(delta);
            if (!g.active) {
                g.dispose();
                grades.removeIndex(i--);
            }
        }
        for (int i = 0; i < shots.size; i++) {
            PaperShot s = shots.get(i);
            s.update(delta);
            if (!s.active) {
                s.dispose();
                shots.removeIndex(i--);
            }
        }
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
        boolean soundOn = game.soundManager != null && game.soundManager.isSoundOn && MemoryManager.loadIsSoundOn();

        for (int i = 0; i < shots.size; ) {
            PaperShot shot = shots.get(i);
            boolean shotDestroyed = false;

            for (int j = 0; j < books.size; j++) {
                Book book = books.get(j);
                if (book.isVisible(game.camera.position.x) && shot.getBounds().overlaps(book.getBounds())) {

                    spawnParticles(book.x + book.width, book.y + book.height / 2, com.badlogic.gdx.graphics.Color.CYAN, 15);

                    shots.removeIndex(i);
                    books.removeIndex(j);
                    levelManager.booksCollected++;

                    if (soundOn && game.soundManager.hit != null)
                        game.soundManager.hit.play();

                    shotDestroyed = true;
                    break;
                }
            }

            if (!shotDestroyed) {
                i++;
            }
        }

        for (int i = 0; i < grades.size; i++) {
            Grade grade = grades.get(i);
            if (player.getBounds().overlaps(grade.getBounds())) {
                calculator.addGrade(grade.value);
                if (grade.value == 5) {
                    player.increaseSpeed();

                    spawnParticles(player.x + player.width, player.y + player.height, com.badlogic.gdx.graphics.Color.GREEN, 15);

                    if (soundOn && game.soundManager.collect5 != null)
                        game.soundManager.collect5.play();
                } else {
                    player.decreaseSpeed();

                    spawnParticles(player.x + player.width, player.y + player.height, com.badlogic.gdx.graphics.Color.RED, 15);

                    if (soundOn && game.soundManager.collect2 != null)
                        game.soundManager.collect2.play();
                }
                grades.removeIndex(i--);
            }
        }


        for (int i = 0; i < books.size; i++) {
            Book book = books.get(i);
            if (book.isVisible(game.camera.position.x) && player.getBounds().overlaps(book.getBounds())) {

                spawnParticles(player.x + player.width, player.y + player.height, com.badlogic.gdx.graphics.Color.ORANGE, 15);

                books.removeIndex(i--);
                logic.loseLife();
                teacher.currentSpeed += 5;
                if (soundOn && game.soundManager.hit != null)
                    game.soundManager.hit.play();
            }
        }
    }

    void checkTeacherCaught() {
        boolean soundOn = game.soundManager != null && game.soundManager.isSoundOn && MemoryManager.loadIsSoundOn();

        if (player.getBounds().overlaps(teacher.getBounds()) && !teacher.isHitting) {
            teacher.isHitting = true;
            hitFreezeTimer = 1.0f;

            spawnParticles(player.x + player.width, player.y + player.height, com.badlogic.gdx.graphics.Color.PURPLE, 25);

            if (soundOn && game.soundManager.hit != null) {
                game.soundManager.hit.play();
            }
        }

        if (teacher.isHitting) {
            hitFreezeTimer -= Gdx.graphics.getDeltaTime();

            if (Math.random() > 0.7) {
                spawnParticles(player.x + player.width, player.y + player.height, com.badlogic.gdx.graphics.Color.PURPLE, 3);
            }

            if (hitFreezeTimer <= 0) {
                teacher.isHitting = false;
                logic.loseLife();

                if (logic.lives > 0) {
                    game.camera.position.x = 400;
                    game.camera.update();
                    resetPositions();
                    levelManager.resetToCheckpoint();
                } else {
                    logic.gameOver = true;
                }
            }
        }
    }

    private float hitFreezeTimer = 0;

    void checkLevelCompletion() {
        if (levelManager.booksCollected >= levelManager.booksToCollect) {
            levelManager.setCheckpoint();
            float avg = calculator.getAverage();

            com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("schoolescape_prefs");
            prefs.putInteger("saved_level", levelManager.currentLevel + 1);
            prefs.flush();

            if (levelManager.currentLevel >= 11) {
                game.setScreen(new WinScreen(game, levelManager, avg));
            } else {
                game.setScreen(new LevelCompleteScreen(game, levelManager, avg));
            }
        }
    }

    private void spawnParticles(float startX, float startY, com.badlogic.gdx.graphics.Color color, int count) {
        for (int i = 0; i < count; i++) {
            Particle p = new Particle();
            p.x = startX;
            p.y = startY;
            p.vx = (float) (Math.random() * 250 - 125);
            p.vy = (float) (Math.random() * 350 - 50);
            p.color = color;
            p.life = 0.5f;
            particles.add(p);
        }
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            uiViewport.unproject(touch);

            if (pause.isPaused) {
                pause.handleInput(game, touch, this);
                return;
            }

            if (pauseButton.isHit(touch.x, touch.y)) {
                pause.toggle();
                return;
            }
        }

        if (!pause.isPaused && !logic.gameOver && !teacher.isHitting) {
            for (int i = 0; i < 2; i++) {
                if (Gdx.input.isTouched(i)) {
                    Vector3 touch = new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                    uiViewport.unproject(touch);

                    if (mobileJumpButton.isHit(touch.x, touch.y) && Gdx.input.justTouched()) {
                        player.jump();
                        if (game.soundManager != null && game.soundManager.isSoundOn && MemoryManager.loadIsSoundOn() && game.soundManager.jump != null) {
                            game.soundManager.jump.play();
                        }
                    }

                    if (mobileShootButton.isHit(touch.x, touch.y) && Gdx.input.justTouched()) {
                        shots.add(new PaperShot(player.x + 40, player.y + 20));
                        if (game.soundManager != null && game.soundManager.isSoundOn && MemoryManager.loadIsSoundOn() && game.soundManager.shoot != null) {
                            game.soundManager.shoot.play();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, false);
        uiViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        world.dispose();
        player.dispose();
        teacher.dispose();
        for (Book b : books) b.dispose();
        for (Grade g : grades) g.dispose();
        for (PaperShot s : shots) s.dispose();
        if (jumpBtnTexture != null) jumpBtnTexture.dispose();
        if (shootBtnTexture != null) shootBtnTexture.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
