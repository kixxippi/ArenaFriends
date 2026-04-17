package io.github.some_example_name.arena.visitor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.arena.PuddleVisual;
import io.github.some_example_name.arena.WallVisual;

public class RenderVisitor implements ArenaVisualVisitor {
    private final SpriteBatch batch;

    public RenderVisitor(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void visit(WallVisual wall) {
        wall.render(batch);
    }

    @Override
    public void visit(PuddleVisual puddle) {
        puddle.render(batch);
    }
}
