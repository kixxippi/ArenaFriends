import io.github.some_example_name.effect.Effect;
import io.github.some_example_name.effect.SpeedEffect;
import io.github.some_example_name.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    @Test
    void getRemainingMsShouldNeverBeNegative() {
        Effect e = new SpeedEffect(1000, 2f);
        assertEquals(0, e.getRemainingMs(2000));
        assertFalse(e.isActive(2000));
    }

    @Test
    void isActiveShouldBeTrueBeforeEndTime() {
        Effect e = new SpeedEffect(1000, 2f);
        assertTrue(e.isActive(999));
        assertFalse(e.isActive(1000)); // at end: remaining=0 -> inactive
    }

    @Test
    void speedEffectShouldApplyMultiplierWhenActiveAndResetWhenExpired() {
        Player p = new Player(0, 0, 10, 10, null, null, null, 1, false);

        SpeedEffect e = new SpeedEffect(1000, 1.5f);

        e.applyTo(p, 999);
        assertEquals(1.5f, p.getSpeedMultiplier(), 0.0001);

        e.applyTo(p, 1000);
        assertEquals(1.0f, p.getSpeedMultiplier(), 0.0001);
    }
}
