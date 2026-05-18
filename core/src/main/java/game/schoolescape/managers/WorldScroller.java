package game.schoolescape.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldScroller {
    private Texture texture;
    private int width = 800;
    private int height = 480;

    public WorldScroller() {
        texture = new Texture("background.png");
    }

    public void draw(SpriteBatch batch, float cameraX) {
        float x = cameraX - 400;
        int offset = (int) (x / width);
        float startX = offset * width;

        for (int i = -1; i <= 2; i++) {
            batch.draw(texture, startX + i * width, 110, width, height);
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
