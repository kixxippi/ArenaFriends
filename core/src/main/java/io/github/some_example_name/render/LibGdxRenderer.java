package io.github.some_example_name.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LibGdxRenderer implements Renderer {
    private final SpriteBatch batch;

    public LibGdxRenderer(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void draw(Texture texture, float x, float y, float width, float height) {
        batch.draw(texture, x, y, width, height);
    }
}
