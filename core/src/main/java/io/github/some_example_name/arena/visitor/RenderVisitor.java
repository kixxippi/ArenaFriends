package io.github.some_example_name.arena.visitor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.arena.PuddleVisual;
import io.github.some_example_name.arena.WallVisual;
import io.github.some_example_name.render.Renderer;

public class RenderVisitor implements ArenaVisualVisitor {
    private final Renderer renderer;

    public RenderVisitor(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void visit(WallVisual wall) {
        renderer.draw(
            wall.getTexture(),
            wall.getRect().x,
            wall.getRect().y,
            wall.getRect().width,
            wall.getRect().height
        );
    }

    @Override
    public void visit(PuddleVisual puddle) {
        renderer.draw(
            puddle.getTexture(),
            puddle.getRect().x,
            puddle.getRect().y,
            puddle.getRect().width,
            puddle.getRect().height
        );
    }
}
