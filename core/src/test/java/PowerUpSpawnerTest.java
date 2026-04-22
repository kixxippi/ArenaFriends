import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.arena.PuddlesOnlyArena;
import io.github.some_example_name.arena.WallsOnlyArena;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.powerup.PowerUpSpawner;
import io.github.some_example_name.powerup.WorldPowerUp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpSpawnerTest {

    private Arena arenaNoTextures() {
        return new WallsOnlyArena(1408, 768, false);
    }

    private Player p1() {
        return new Player(80, 80, 64, 64, null, null, null, 1, false);
    }

    private Player p2() {
        return new Player(1200, 600, 64, 64, null, null, null, 2, false);
    }

    @Test
    void trySpawnShouldReturnNullIfTooEarly() {
        PowerUpSpawner spawner = new PowerUpSpawner(1408, 768, arenaNoTextures(), 15000, false);

        Array<WorldPowerUp> existing = new Array<>();
        WorldPowerUp first = spawner.trySpawn(1000, p1(), p2(), existing);
        assertNotNull(first);

        // раньше, чем 1000+15000
        WorldPowerUp second = spawner.trySpawn(2000, p1(), p2(), existing);
        assertNull(second);

        // dispose не должен падать даже без texture
        assertDoesNotThrow(first::dispose);
    }

    @Test
    void trySpawnShouldSpawnAndNotOverlapPlayersOrExisting() {
        PowerUpSpawner spawner = new PowerUpSpawner(1408, 768, arenaNoTextures(), 0, false);

        Array<WorldPowerUp> existing = new Array<>();
        Player p1 = p1();
        Player p2 = p2();

        WorldPowerUp spawned = spawner.trySpawn(1, p1, p2, existing);
        assertNotNull(spawned);

        assertFalse(spawned.getRect().overlaps(p1.getRect()));
        assertFalse(spawned.getRect().overlaps(p2.getRect()));

        // если добавить как existing — следующий не должен пересекаться с ним
        existing.add(spawned);

        WorldPowerUp spawned2 = spawner.trySpawn(2, p1, p2, existing);
        assertNotNull(spawned2);
        assertFalse(spawned2.getRect().overlaps(spawned.getRect()));

        spawned.dispose();
        spawned2.dispose();
    }

    @Test
    void trySpawnShouldNotOverlapPuddles() {
        var arena = new PuddlesOnlyArena(1408, 768, false);
        PowerUpSpawner spawner = new PowerUpSpawner(1408, 768, arena, 0, false);

        Array<WorldPowerUp> existing = new Array<>();

        // за несколько попыток почти гарантированно получим спавн
        WorldPowerUp spawned = spawner.trySpawn(1, p1(), p2(), existing);
        assertNotNull(spawned);

        // не должен пересекаться с любой лужей
        for (var puddle : arena.getPuddles()) {
            assertFalse(puddle.overlaps(spawned.getRect()), "Spawned overlaps puddle: " + puddle);
        }

        spawned.dispose();
    }
}
