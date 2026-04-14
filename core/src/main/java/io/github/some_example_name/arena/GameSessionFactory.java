package io.github.some_example_name.arena;

import io.github.some_example_name.map.GameMap;
import io.github.some_example_name.powerup.PowerUpSpawner;

// Abstract Factory: builds all components for one game session (map + arena + renderer + spawner)
public interface GameSessionFactory {
    long POWER_UP_SPAWN_INTERVAL_MS = 15000L;

    GameMap createMap();
    Arena createArena(float worldWidth, float worldHeight);
    ArenaRenderer createArenaRenderer();
    PowerUpSpawner createPowerUpSpawner(float worldWidth, float worldHeight, Arena arena);
}
