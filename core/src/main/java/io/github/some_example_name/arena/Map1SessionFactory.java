package io.github.some_example_name.arena;

import io.github.some_example_name.map.GameMap;
import io.github.some_example_name.powerup.PowerUpSpawner;

// Concrete factory for map 1 (walls only)
public class Map1SessionFactory implements GameSessionFactory {

    @Override
    public GameMap createMap() {
        return new GameMap(1);
    }

    @Override
    public Arena createArena(float worldWidth, float worldHeight) {
        return new WallsOnlyArena(worldWidth, worldHeight);
    }

    @Override
    public ArenaRenderer createArenaRenderer() {
        return new ArenaRenderer();
    }

    @Override
    public PowerUpSpawner createPowerUpSpawner(float worldWidth, float worldHeight, Arena arena) {
        return new PowerUpSpawner(worldWidth, worldHeight, arena, POWER_UP_SPAWN_INTERVAL_MS);
    }
}
