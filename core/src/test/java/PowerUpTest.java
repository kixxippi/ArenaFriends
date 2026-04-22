import io.github.some_example_name.model.Player;
import io.github.some_example_name.powerup.DamagePowerUp;
import io.github.some_example_name.powerup.HealPowerUp;
import io.github.some_example_name.powerup.SpeedPowerUp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpTest {

    @Test
    void healPowerUpShouldIncreaseHpButNotExceedMax() {
        Player p = new Player(0, 0, 10, 10, null, null, null, 1, false);

        p.takeDamage(50);
        int before = p.getHp();

        new HealPowerUp(20).applyTo(p, System.currentTimeMillis());

        assertEquals(before + 20, p.getHp());
        // apply many times - must cap at max
        new HealPowerUp(999).applyTo(p, System.currentTimeMillis());
        assertEquals(p.getMaxHp(), p.getHp());
    }

    @Test
    void speedPowerUpShouldSetActiveBuffAndAffectSpeedWhileActive() {
        Player p = new Player(0, 0, 10, 10, null, null, null, 1, false);

        long now = 1000;
        new SpeedPowerUp(5000, 1.5f).applyTo(p, now);

        assertNotNull(p.getActiveBuff());
        assertEquals(5000, p.getActiveBuffDurationMs());

        // simulate "frame update"
        p.updateBuff(now + 1);
        assertEquals(1.5f, p.getSpeedMultiplier(), 0.0001);

        // after expiration it should reset and clear buff
        p.updateBuff(now + 5000);
        assertNull(p.getActiveBuff());
        assertEquals(1.0f, p.getSpeedMultiplier(), 0.0001);
    }

    @Test
    void damagePowerUpShouldSetActiveBuffAndAffectBonusDamageWhileActive() {
        Player p = new Player(0, 0, 10, 10, null, null, null, 1, false);

        long now = 1000;
        new DamagePowerUp(5000, 10).applyTo(p, now);

        assertNotNull(p.getActiveBuff());
        assertEquals(5000, p.getActiveBuffDurationMs());

        p.updateBuff(now + 1);
        assertEquals(10, p.getBonusDamage());

        p.updateBuff(now + 5000);
        assertNull(p.getActiveBuff());
        assertEquals(0, p.getBonusDamage());
    }
}
