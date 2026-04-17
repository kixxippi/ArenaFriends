package io.github.some_example_name.arena.visitor;

import io.github.some_example_name.arena.PuddleVisual;
import io.github.some_example_name.arena.WallVisual;

public class DisposeVisitor implements ArenaVisualVisitor {
    @Override
    public void visit(WallVisual wall) {
        wall.dispose();
    }

    @Override
    public void visit(PuddleVisual puddle) {
        puddle.dispose();
    }
}
