import io.github.some_example_name.effect.DamageEffect;
import io.github.some_example_name.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DamageEffectTest {
    @Test
    void damageEffectShouldSetBonusDamageWhenActiveAndResetWhenExpired() {
        Player p = new Player(0, 0, 10, 10, null, null, null, 1, false);

        DamageEffect e = new DamageEffect(1000, 10);

        e.applyTo(p, 999);
        assertEquals(10, p.getBonusDamage());

        e.applyTo(p, 1000);
        assertEquals(0, p.getBonusDamage());
    }
}
