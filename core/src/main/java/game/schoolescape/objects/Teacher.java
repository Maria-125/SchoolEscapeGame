package game.schoolescape.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.schoolescape.managers.FilePaths;

public class Teacher {
    public float x = 50, y = 160;
    public float width = 45, height = 70;
    public float currentSpeed = 75;
    public float acceleration = 1.7f;
    public float speedIncreaseTimer = 0;
    private Texture texture;

    public Teacher() {
        texture = new Texture(FilePaths.TEACHER);
    }

    public void update(float playerX, float playerSpeed, float delta) {
        speedIncreaseTimer += delta;
        if (speedIncreaseTimer > 8f) {
            speedIncreaseTimer = 0;
            currentSpeed += acceleration;
            if (currentSpeed > 120) currentSpeed = 120;
        }

        if (currentSpeed > playerSpeed + 10) {
            currentSpeed = playerSpeed + 10;
        }

        if (x < playerX - 30) {
            x += currentSpeed * delta;
        }
    }

    // Ограничение позиции учителя относительно камеры
    public void clampToCamera(float cameraX) {
        float minX = cameraX - 400;
        float maxX = cameraX + 400 - width;

        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
    }

    public void resetSpeed() {
        currentSpeed = 60;
        speedIncreaseTimer = 0;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width*3, height*3);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void dispose() {
        texture.dispose();
    }
}
