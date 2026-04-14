package io.github.some_example_name.arena;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.model.Player;

public interface Arena {
    // handle collisions with walls and adjust player position
    void handleWalls(Player player);
    // handle puddle effects
    void handlePuddles(Player player);
    // Bridge: render visuals via the given renderer
    void render(SpriteBatch batch, ArenaRenderer renderer);
    // Bridge: dispose visual assets via the given renderer
    void disposeVisuals(ArenaRenderer renderer);
}
