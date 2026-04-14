package io.github.some_example_name.arena;

import io.github.some_example_name.model.Player;
import io.github.some_example_name.arena.logic.ArenaLogicStrategy;

public interface Arena {
    // handle collisions with walls and adjust player position
    void handleWalls(Player player);
    // handle puddle effects
    void handlePuddles(Player player);

    // strategy hook
    void setLogicStrategy(ArenaLogicStrategy logicStrategy);

    void applyLogic(Player player);
}
