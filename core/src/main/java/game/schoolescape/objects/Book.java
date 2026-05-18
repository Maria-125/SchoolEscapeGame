package game.schoolescape.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.schoolescape.managers.FilePaths;

public class Book {
    public float x, y = 155;
    public float width = 35, height = 45;
    public float speed = 180;
    public boolean active = true;
    private Texture texture;

    public Book(float startX) {
        this.x = startX;
        this.y = 165;
        texture = new Texture(FilePaths.BOOK);
    }

    public void update(float delta) {
        x -= speed * delta;
        if (x + width < 0) active = false;
    }

    public void draw(SpriteBatch batch) {
        if (active) batch.draw(texture, x, y, width*2, height*2);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isVisible(float cameraX) {
        float screenLeft = cameraX - 400;   // левый край экрана
        float screenRight = cameraX + 400;  // правый край экрана
        return (x + width > screenLeft && x < screenRight);
    }

    public void dispose() {
        texture.dispose();
    }
}
