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
        private float originalMinWidth; // Красивая базовая ширина, которую мы передали

        public Button(float x, float y, float width, float height, String text, BitmapFont font, Texture buttonTexture) {
            this.x = x;
            this.y = y;
            this.text = text;
            this.font = font;
            this.texture = buttonTexture;

            // ИСПРАВЛЕНО: Мы больше не берём огромные пиксели из файла картинки напрямую.
            // Мы жёстко используем те аккуратные размеры, которые ты передала на экранах (например, 200х50)!
            this.width = width;
            this.height = height;
            this.originalMinWidth = width;

            // Проверяем, помещается ли текст, и если надо — аккуратно расширяем рамку
            adjustSizeToText();
        }

        public void setText(String newText) {
            this.text = newText;
            adjustSizeToText();
        }

        private void adjustSizeToText() {
            if (text != null && !text.isEmpty() && font != null) {
                GlyphLayout layout = new GlyphLayout(font, text);

                // Добавляем небольшой аккуратный отступ (запас) по краям
                float textWidthWithMargins = layout.width + 30;

                // Если текст шире, чем базовая кнопка (200px), то кнопка плавно раздвигается ровно под длину слова
                if (textWidthWithMargins > originalMinWidth) {
                    this.width = textWidthWithMargins;
                } else {
                    this.width = originalMinWidth; // Если текст короткий ("Старт"), кнопка остаётся аккуратной и стандартной
                }
            }
        }

        public boolean isHit(float tx, float ty) {
            return tx >= x && tx <= x + width && ty >= y && ty <= y + height;
        }

        public void draw(SpriteBatch batch) {
            if (texture != null) {
                // Отрисовываем картинку, сжимая её до нужных нам аккуратных пропорций width и height
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
