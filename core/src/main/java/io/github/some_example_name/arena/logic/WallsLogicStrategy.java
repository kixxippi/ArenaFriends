package io.github.some_example_name.arena.logic;

import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.model.Player;

public class WallsLogicStrategy implements ArenaLogicStrategy {
    private final Arena arena;

    public WallsLogicStrategy(Arena arena) {
        this.arena = arena;
    }

    @Override
    public void apply(Player player) {
        arena.handleWalls(player);
    }
}