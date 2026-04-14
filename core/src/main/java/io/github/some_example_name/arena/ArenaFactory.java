package io.github.some_example_name.arena;

public class ArenaFactory {
    public static Arena createArena(int mapId, float worldWidth, float worldHeight) {
        switch (mapId) {
            case 1:
                return new WallsOnlyArena(worldWidth, worldHeight);
            case 2:
                return new PuddlesOnlyArena(worldWidth, worldHeight);
            case 3:
            default:
                return new MixedArena(worldWidth, worldHeight);
        }
    }

    // Abstract Factory: returns the concrete session factory for the given map
    public static GameSessionFactory createSessionFactory(int mapId) {
        switch (mapId) {
            case 1: return new Map1SessionFactory();
            case 2: return new Map2SessionFactory();
            case 3:
            default: return new Map3SessionFactory();
        }
    }
}
