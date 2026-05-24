package game.schoolescape.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class EffectManager {
    public static class Particle {
        float x, y, vx, vy, life;
        Color color;
    }

    private Array<Particle> particles = new Array<>();

    // Спавн облака цветных пикселей-осколков
    public void spawnExplosion(float x, float y, Color color, int count) {
        for (int i = 0; i < count; i++) {
            Particle p = new Particle();
            p.x = x;
            p.y = y;
            p.vx = MathUtils.random(-150f, 150f);
            p.vy = MathUtils.random(-50f, 250f); // летят преимущественно вверх
            p.life = MathUtils.random(0.4f, 0.8f);
            p.color = color;
            particles.add(p);
        }
    }

    public void updateAndDraw(SpriteBatch batch, Texture pixelTexture, float delta) {
        for (int i = particles.size - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.life -= delta;
            if (p.life <= 0) {
                particles.removeIndex(i);
                continue;
            }
            // физика частиц без Box2D
            p.vy -= 400 * delta; // гравитация для частиц
            p.x += p.vx * delta;
            p.y += p.vy * delta;

            batch.setColor(p.color.r, p.color.g, p.color.b, p.life); // угасание прозрачности
            batch.draw(pixelTexture, p.x, p.y, 6, 6);
        }
        batch.setColor(Color.WHITE); // сброс маски цвета
    }
}
