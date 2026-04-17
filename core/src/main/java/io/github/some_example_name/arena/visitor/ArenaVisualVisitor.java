package io.github.some_example_name.arena.visitor;

import io.github.some_example_name.arena.WallVisual;
import io.github.some_example_name.arena.PuddleVisual;

public interface ArenaVisualVisitor {
    void visit(WallVisual wall);
    void visit(PuddleVisual puddle);
}
