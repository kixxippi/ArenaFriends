import io.github.some_example_name.arena.PuddlesOnlyArena;
import io.github.some_example_name.arena.WallsOnlyArena;
import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArenaAndPlayerTest {

    @Test
    void playerTakeDamage_shouldNotGoBelowZero() {
        Player p = new Player(0, 0, 64, 64, null,
            null, new Sword(10, 500),
            1, false);

        p.takeDamage(999);

        assertEquals(0, p.getHp());
        assertTrue(p.isDead());
    }

    @Test
    void playerHeal_shouldNotExceedMaxHp() {
        Player p = new Player(0, 0, 64, 64, null,
            null, new Sword(10, 500),
            1, false);

        p.takeDamage(60);
        p.heal();
        p.heal();
        p.heal();
        p.heal();
        p.heal();

        assertEquals(p.getMaxHp(), p.getHp());
    }

    @Test
    void arenaHandlePuddles_inPuddleShouldSlowDown() {
        PuddlesOnlyArena arena = new PuddlesOnlyArena(1408, 768, false);

        Player p = new Player(360, 240, 10, 10, null, null, null, 1, false);

        arena.handlePuddles(p);

        assertEquals(0.5f, p.getSpeedMultiplier(), 0.0001f);
    }

    @Test
    void arenaHandleWalls_overlappingWallShouldPushOut() {
        WallsOnlyArena arena = new WallsOnlyArena(1408, 768, false);

        Player p = new Player(10, 10, 50, 50, null, null, null, 1, false);

        arena.handleWalls(p);
        assertFalse(p.getRect().overlaps(arena.getWalls().get(0)));
    }
}
