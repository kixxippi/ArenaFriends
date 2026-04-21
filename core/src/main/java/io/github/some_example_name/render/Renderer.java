package io.github.some_example_name.render;

import com.badlogic.gdx.graphics.Texture;

public interface Renderer {
    void draw(Texture texture, float x, float y, float width, float height);
}
