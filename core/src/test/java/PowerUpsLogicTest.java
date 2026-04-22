import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.powerup.DamagePowerUp;
import io.github.some_example_name.powerup.HealPowerUp;
import io.github.some_example_name.powerup.SpeedPowerUp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpsLogicTest {

    private Player player() {
        return new Player(0, 0, 50, 40, null, null, new Sword(10, 0), 1, false);
    }

    @Test
    void healPowerUpHeals() {
        Player p = player();
        p.takeDamage(30);
        assertEquals(70, p.getHp());

        new HealPowerUp(10).applyTo(p, 1000);
        assertEquals(80, p.getHp());
    }

    @Test
    void speedPowerUpSetsActiveBuff() {
        Player p = player();
        assertNull(p.getActiveBuff());

        new SpeedPowerUp(5000, 1.5f).applyTo(p, 1000);

        assertNotNull(p.getActiveBuff());
        assertEquals(5000, p.getActiveBuffDurationMs());
    }

    @Test
    void damagePowerUpSetsActiveBuff() {
        Player p = player();
        assertNull(p.getActiveBuff());

        new DamagePowerUp(5000, 10).applyTo(p, 1000);

        assertNotNull(p.getActiveBuff());
        assertEquals(5000, p.getActiveBuffDurationMs());
    }
}
