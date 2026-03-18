package io.github.some_example_name.arena;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class WallVisual {
    private final Rectangle rect;
    private final Texture texture;

    public WallVisual(Rectangle rect, String texturePath) {
        this.rect = rect;
        this.texture = new Texture(texturePath);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
    }

    public void dispose() {
        texture.dispose();
    }
}
