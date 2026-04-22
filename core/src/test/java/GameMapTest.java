import io.github.some_example_name.map.GameMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMapTest {

    @Test
    void constructorTestModeDoesNotLoadTexture() {
        GameMap m = new GameMap(1, false);
        assertEquals(1, m.getMapId());
        // главное: не падает
        m.dispose();
    }

    @Test
    void renderWithNoTextureShouldNotThrow() {
        GameMap m = new GameMap(123, false);
        // batch == null, но render() должен сначала проверить mapTexture==null и выйти
        assertDoesNotThrow(() -> m.render(null, 1408, 768));
    }
}
