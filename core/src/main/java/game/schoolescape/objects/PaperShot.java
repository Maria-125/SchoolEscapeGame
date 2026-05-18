package game.schoolescape.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.schoolescape.managers.FilePaths;

public class PaperShot {
    public float x, y;
    public float startX;
    public float width = 20, height = 20;
    public float speed = 600;
    public boolean active = true;
    private Texture texture;

    public PaperShot(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.startX = startX;
        texture = new Texture(FilePaths.PAPER);
    }

    public void update(float delta) {
        x += speed * delta;
        if (x > startX + 600) {
            active = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (active && texture != null) {
            batch.draw(texture, x, y, width*4, height*4);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }
}
