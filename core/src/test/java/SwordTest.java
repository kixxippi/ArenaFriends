import io.github.some_example_name.combat.Sword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwordTest {

    @Test
    void canAttackInitiallyShouldBeTrue() {
        Sword s = new Sword(10, 500);
        assertTrue(s.canAttack(500));
    }

    @Test
    void recordAttackShouldEnforceCooldown() {
        Sword s = new Sword(10, 500);

        long t0 = 1000;
        assertTrue(s.canAttack(t0));

        s.recordAttack(t0);

        assertFalse(s.canAttack(t0));          // immediately after
        assertFalse(s.canAttack(t0 + 499));    // still in cooldown
        assertTrue(s.canAttack(t0 + 500));     // cooldown passed
    }

    @Test
    void getDamageShouldReturnConstructorValue() {
        Sword s = new Sword(42, 500);
        assertEquals(42, s.getDamage());
    }
}
