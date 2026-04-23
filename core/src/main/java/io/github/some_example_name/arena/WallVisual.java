package io.github.some_example_name.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.arena.visitor.Visitor;
import io.github.some_example_name.arena.visitor.Shape;

/**
 * Конкретный элемент (Visitor pattern).
 *
 * Реализует accept() так, что вызывает visitor.visitWall(this) —
 * тем самым сообщает посетителю точный тип элемента.
 */
public class WallVisual implements Shape {

    private final Rectangle rect;
    private final Texture texture;

    public WallVisual(Rectangle rect, String texturePath) {
        this.rect = rect;
        this.texture = (texturePath != null)
            ? new Texture(Gdx.files.internal(texturePath))
            : null;
    }

    /**
     * Visitor pattern — элемент вызывает метод посетителя,
     * соответствующий своему классу (visitWall).
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visitWall(this);
    }

    public Rectangle getRect()    { return rect; }
    public Texture   getTexture() { return texture; }

    public void render(SpriteBatch batch) {
        if (texture == null) return;
        batch.draw(texture, rect.x, rect.y, rect.width, rect.height);
    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }
}
