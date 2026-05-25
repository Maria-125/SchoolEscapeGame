package game.schoolescape.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.schoolescape.managers.FilePaths;

public class Grade {
    public float x, y = 210;
    public float width = 30, height = 40;
    public float speed = 150;
    public boolean active = true;
    public int value;
    private Texture texture;

    public Grade(float startX, int value) {
        this.x = startX;
        this.y = 215;
        this.value = value;
        if (value == 5) texture = new Texture(FilePaths.GRADE_5);
        else texture = new Texture(FilePaths.GRADE_2);
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
        float screenLeft = cameraX - 400;
        float screenRight = cameraX + 400;
        return (x + width > screenLeft && x < screenRight);
    }

    public void dispose() {
        texture.dispose();
    }
}
