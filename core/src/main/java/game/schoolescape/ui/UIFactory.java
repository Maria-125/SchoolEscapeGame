package game.schoolescape.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UIFactory {

    public static class Button {
        public float x, y, width, height;
        private String text;
        private BitmapFont font;
        private Texture texture;
        private float originalMinWidth;

        public Button(float x, float y, float width, float height, String text, BitmapFont font, Texture buttonTexture) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.font = font;
            this.texture = buttonTexture;

            this.width = width;
            this.height = height;
            this.originalMinWidth = width;

            adjustSizeToText();
        }

        public void setText(String newText) {
            this.text = newText;
            adjustSizeToText();
        }

        private void adjustSizeToText() {
            if (text != null && !text.isEmpty() && font != null) {
                GlyphLayout layout = new GlyphLayout(font, text);

                float textWidthWithMargins = layout.width + 30;

                if (textWidthWithMargins > originalMinWidth) {
                    this.width = textWidthWithMargins;
                } else {
                    this.width = originalMinWidth;
                }
            }
        }

        public boolean isHit(float tx, float ty) {
            return tx >= x && tx <= x + width && ty >= y && ty <= y + height;
        }

        public void draw(SpriteBatch batch) {
            if (texture != null) {
                batch.draw(texture, x, y, width, height);
            }

            if (text != null && !text.isEmpty()) {
                GlyphLayout layout = new GlyphLayout(font, text);
                float textX = x + (width - layout.width) / 2;
                float textY = y + (height + layout.height) / 2;
                font.draw(batch, text, textX, textY);
            }
        }
    }
}
