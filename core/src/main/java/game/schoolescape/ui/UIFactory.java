package game.schoolescape.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.schoolescape.managers.FilePaths;

public class UIFactory {

    public static class Button {
        public float x, y, width, height;
        private String text;
        private BitmapFont font;
        private Texture texture;

        public Button(float x, float y, float width, float height, String text, BitmapFont font) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.text = text;
            this.font = font;
            this.texture = new Texture(FilePaths.BUTTON);
        }

        public void setText(String newText) {
            this.text = newText;
        }

        public boolean isHit(float tx, float ty) {
            return tx >= x && tx <= x + width && ty >= y && ty <= y + height;
        }

        public void draw(SpriteBatch batch) {
            if (texture != null) {
                batch.draw(texture, x, y, width, height);
            }
            GlyphLayout layout = new GlyphLayout(font, text);
            float textX = x + (width - layout.width) / 2;
            float textY = y + (height + layout.height) / 2;
            font.draw(batch, text, textX, textY);
        }

        public void dispose() {
            if (texture != null) texture.dispose();
        }
    }
}
