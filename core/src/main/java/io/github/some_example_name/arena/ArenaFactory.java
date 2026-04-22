package io.github.some_example_name.arena;

import io.github.some_example_name.Holder;
import io.github.some_example_name.arena.exceptions.UnknownMapIdException;
import io.github.some_example_name.arena.logic.ArenaLogicStrategy;
import io.github.some_example_name.arena.logic.PuddlesLogicStrategy;
import io.github.some_example_name.arena.logic.WallsLogicStrategy;

public class ArenaFactory {

    public static Arena createArena(int mapId, float worldWidth, float worldHeight) {
        return createArena(mapId, worldWidth, worldHeight, true);
    }

    public static Arena createArena(int mapId, float worldWidth, float worldHeight, boolean loadTextures) {
        Arena arena;

        switch (mapId) {
            case 1:
                arena = new WallsOnlyArena(worldWidth, worldHeight, loadTextures);
                arena.setLogicStrategy(new WallsLogicStrategy(arena));
                return arena;

            case 2:
                arena = new PuddlesOnlyArena(worldWidth, worldHeight, loadTextures);
                arena.setLogicStrategy(new PuddlesLogicStrategy(arena));
                return arena;

            case 3:
                arena = new MixedArena(worldWidth, worldHeight, loadTextures);

                Holder<ArenaLogicStrategy> strategyHolder = new Holder<>(player -> {
                    arena.handleWalls(player);
                    arena.handlePuddles(player);
                });

                arena.setLogicStrategy(strategyHolder.get());
                return arena;

            default:
                throw new UnknownMapIdException(mapId);
        }
    }
}
