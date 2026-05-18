package game.schoolescape.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.schoolescape.managers.FilePaths;

public class Player {
    public float x = 250, y = 90;
    public float width = 40, height = 50;
    public float velocityY = 0;
    public boolean isGrounded = true;
    public float currentSpeed = 55;
    public void setBaseSpeed(float speed) {
        currentSpeed = speed;
    };
    private float minSpeed = 40;
    private float maxSpeed = 120;
    private Texture texture;

    public Player() {
        texture = new Texture(FilePaths.PLAYER);
    }

    public void update(float delta) {
        x += currentSpeed * delta;

        // гравитация
        velocityY += -800 * delta;
        y += velocityY * delta;
        if (y <= 150) {
            y = 150;
            velocityY = 0;
            isGrounded = true;
        } else {
            isGrounded = false;
        }
    }

    public void increaseSpeed() {
        currentSpeed += 5;
        if (currentSpeed > maxSpeed) currentSpeed = maxSpeed;
        System.out.println("Скорость ученика: " + currentSpeed);
    }

    public void decreaseSpeed() {
        currentSpeed -= 5;
        if (currentSpeed < minSpeed) currentSpeed = minSpeed;
        System.out.println("Скорость ученика: " + currentSpeed);
    }

    public void resetSpeed() {
        currentSpeed = 60;
    }

    public void jump() {
        if (isGrounded) {
            velocityY = 400;
            isGrounded = false;
        }
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
