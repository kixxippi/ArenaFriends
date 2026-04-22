import io.github.some_example_name.arena.BaseRectangleArena;
import io.github.some_example_name.arena.PuddlesOnlyArena;
import io.github.some_example_name.arena.WallsOnlyArena;
import io.github.some_example_name.arena.exceptions.ArenaLogicNotConfiguredException;
import io.github.some_example_name.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseRectangleArenaTest {

    @Test
    void applyLogicWithoutStrategyShouldThrow() {
        BaseRectangleArena arena = new WallsOnlyArena(1408, 768, false);
        Player p = new Player(0, 0, 10, 10, null, null, null, 1, false);

        assertThrows(ArenaLogicNotConfiguredException.class, () -> arena.applyLogic(p));
    }

    @Test
    void handlePuddlesShouldResetSpeedWhenNotInPuddle() {
        BaseRectangleArena arena = new PuddlesOnlyArena(1408, 768, false);
        Player p = new Player(0, 0, 10, 10, null, null, null, 1, false);

        // игрок точно не в луже (лужи далеко по координатам)
        arena.handlePuddles(p);
        assertEquals(1.0f, p.getSpeedMultiplier(), 0.0001f);
    }
}
