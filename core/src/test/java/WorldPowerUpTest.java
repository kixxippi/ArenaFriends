import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.powerup.HealPowerUp;
import io.github.some_example_name.powerup.PowerUpType;
import io.github.some_example_name.powerup.WorldPowerUp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldPowerUpTest {

    @Test
    void renderAndDisposeWithNullTextureShouldNotThrow() {
        WorldPowerUp w = new WorldPowerUp(PowerUpType.HEAL, new Rectangle(1, 2, 3, 4), new HealPowerUp(10), null);

        assertDoesNotThrow(() -> w.render(null));
        assertDoesNotThrow(w::dispose);
    }
}
