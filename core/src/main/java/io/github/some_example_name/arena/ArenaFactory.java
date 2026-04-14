package io.github.some_example_name.arena;

import io.github.some_example_name.arena.logic.PuddlesLogicStrategy;
import io.github.some_example_name.arena.logic.WallsLogicStrategy;

public class ArenaFactory {
    public static Arena createArena(int mapId, float worldWidth, float worldHeight) {
        Arena arena;

        switch (mapId) {
            case 1:
                arena = new WallsOnlyArena(worldWidth, worldHeight);
                arena.setLogicStrategy(new WallsLogicStrategy(arena));
                return arena;

            case 2:
                arena = new PuddlesOnlyArena(worldWidth, worldHeight);
                arena.setLogicStrategy(new PuddlesLogicStrategy(arena));
                return arena;

            case 3:
            default:
                arena = new MixedArena(worldWidth, worldHeight);

                arena.setLogicStrategy(player -> {
                    arena.handleWalls(player);
                    arena.handlePuddles(player);
                });

                return arena;
        }
    }
}
