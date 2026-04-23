import io.github.some_example_name.arena.MixedArena;
import io.github.some_example_name.arena.PuddlesOnlyArena;
import io.github.some_example_name.arena.WallsOnlyArena;
import io.github.some_example_name.arena.visitor.StatsCollectorVisitor;
import io.github.some_example_name.arena.visitor.Shape;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsCollectorVisitorTest {

    // --- паттерн из лекции: создаём visitor, пускаем по элементам через accept ---

    @Test
    void wallsOnlyArenaShouldCountOnlyWalls() {
        WallsOnlyArena arena = new WallsOnlyArena(1408, 768, false);

        StatsCollectorVisitor stats = new StatsCollectorVisitor();
        for (Shape v : arena.getVisuals()) {
            v.accept(stats);   // WallVisual.accept -> stats.visitWall(this)
        }

        assertTrue(stats.getWallCount() > 0);
        assertEquals(0, stats.getPuddleCount());
        assertTrue(stats.getTotalWallArea() > 0f);
        assertEquals(0f, stats.getTotalPuddleArea(), 0.0001f);
    }

    @Test
    void puddlesOnlyArenaShouldCountOnlyPuddles() {
        PuddlesOnlyArena arena = new PuddlesOnlyArena(1408, 768, false);

        StatsCollectorVisitor stats = new StatsCollectorVisitor();
        for (Shape v : arena.getVisuals()) {
            v.accept(stats);   // PuddleVisual.accept -> stats.visitPuddle(this)
        }

        assertEquals(0, stats.getWallCount());
        assertTrue(stats.getPuddleCount() > 0);
        assertEquals(0f, stats.getTotalWallArea(), 0.0001f);
        assertTrue(stats.getTotalPuddleArea() > 0f);
    }

    @Test
    void mixedArenaShouldCountBothWallsAndPuddles() {
        MixedArena arena = new MixedArena(1408, 768, false);

        StatsCollectorVisitor stats = new StatsCollectorVisitor();
        for (Shape v : arena.getVisuals()) {
            v.accept(stats);
        }

        assertTrue(stats.getWallCount() > 0);
        assertTrue(stats.getPuddleCount() > 0);
        assertTrue(stats.getTotalWallArea() > 0f);
        assertTrue(stats.getTotalPuddleArea() > 0f);
    }

    @Test
    void toStringShouldContainAllFields() {
        MixedArena arena = new MixedArena(1408, 768, false);
        StatsCollectorVisitor stats = new StatsCollectorVisitor();
        for (Shape v : arena.getVisuals()) {
            v.accept(stats);
        }

        String s = stats.toString();
        assertTrue(s.contains("walls="));
        assertTrue(s.contains("puddles="));
        assertTrue(s.contains("wallArea="));
        assertTrue(s.contains("puddleArea="));
    }
}
