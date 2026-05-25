package game.schoolescape.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.schoolescape.managers.FilePaths;

public class Teacher {
    public float x = 50, y = 210;
    public float width = 45, height = 70;
    public float currentSpeed = 75;
    public float acceleration = 1.7f;
    public float speedIncreaseTimer = 0;

    private Texture texture1;
    private Texture texture2;
    private float animTimer = 0;
    private boolean useSecondTexture = false;

    private Texture textureHit1;
    private Texture textureHit2;
    public boolean isHitting = false;
    private float hitAnimTimer = 0;
    private boolean useSecondHitTexture = false;

    public Teacher() {
        texture1 = new Texture(FilePaths.TEACHER);
        texture2 = new Texture(FilePaths.TEACHER2);

        textureHit1 = new Texture(FilePaths.TEACHER_HIT1);
        textureHit2 = new Texture(FilePaths.TEACHER_HIT2);
    }

    public void update(float playerX, float playerSpeed, float delta) {
        if (isHitting) {
            hitAnimTimer += delta;
            if (hitAnimTimer > 0.15f) {
                hitAnimTimer = 0;
                useSecondHitTexture = !useSecondHitTexture;
            }
            return;
        }

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

            animTimer += delta;
            if (animTimer > 0.15f) {
                animTimer = 0;
                useSecondTexture = !useSecondTexture;
            }
        } else {
            useSecondTexture = false;
        }
    }

    public void clampToCamera(float cameraX) {
        float minX = cameraX - 400;
        float maxX = cameraX + 400 - width;

        if (x < minX) x = minX;
        if (x > maxX) x = maxX;
    }

    public void resetSpeed() {
        currentSpeed = 60;
        speedIncreaseTimer = 0;
        x = 50;
        y = 210;
        isHitting = false;
        hitAnimTimer = 0;
    }

    public void draw(SpriteBatch batch) {
        Texture currentTexture;

        if (isHitting) {
            currentTexture = useSecondHitTexture ? textureHit2 : textureHit1;
        } else {
            currentTexture = useSecondTexture ? texture2 : texture1;
        }

        batch.draw(currentTexture, x, y, width * 3, height * 3);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void dispose() {
        if (texture1 != null) texture1.dispose();
        if (texture2 != null) texture2.dispose();
        if (textureHit1 != null) textureHit1.dispose();
        if (textureHit2 != null) textureHit2.dispose();
    }
}
