package io.github.some_example_name.arena;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class PuddleVisual {
    private final Rectangle rect;
    private final Texture texture;

    public PuddleVisual(Rectangle rect, String texturePath) {
        this.rect = rect;
        this.texture = (texturePath == null) ? null : new Texture(texturePath);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void render(SpriteBatch batch) {
        if (texture == null) return;
        batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }
}
