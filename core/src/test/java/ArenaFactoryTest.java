import io.github.some_example_name.arena.*;
import io.github.some_example_name.arena.exceptions.UnknownMapIdException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArenaFactoryTest {

    @Test
    void createArenaWallsOnlyWithoutTextures() {
        Arena a = ArenaFactory.createArena(1, 1408, 768, false);
        assertNotNull(a);
        assertEquals(WallsOnlyArena.class, a.getClass());
    }

    @Test
    void createArenaPuddlesOnlyWithoutTextures() {
        Arena a = ArenaFactory.createArena(2, 1408, 768, false);
        assertNotNull(a);
        assertEquals(PuddlesOnlyArena.class, a.getClass());
    }

    @Test
    void createArenaMixedWithoutTextures() {
        Arena a = ArenaFactory.createArena(3, 1408, 768, false);
        assertNotNull(a);
        assertEquals(MixedArena.class, a.getClass());
    }

    @Test
    void createArenaUnknownMapShouldThrow() {
        assertThrows(UnknownMapIdException.class, () -> ArenaFactory.createArena(999, 1408, 768, false));
    }
}
