package io.github.some_example_name.powerup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class WorldPowerUp {
    private final PowerUpType type;
    private final Rectangle rect;
    private final PowerUp powerUp;
    private final Texture texture;

    public WorldPowerUp(PowerUpType type, Rectangle rect, PowerUp powerUp, Texture texture) {
        this.type = type;
        this.rect = rect;
        this.powerUp = powerUp;
        this.texture = texture;
    }

    public PowerUpType getType() { return type; }
    public Rectangle getRect() { return rect; }
    public PowerUp getPowerUp() { return powerUp; }

    public void render(SpriteBatch batch) {
        if (texture == null) return;
        batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }
}
