import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.effect.Effect;
import io.github.some_example_name.model.Direction;
import io.github.some_example_name.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player newP1WithSword(int dmg, long cdMs) {
        return new Player(
            100, 100, 50, 40,
            null, null,
            new Sword(dmg, cdMs),
            1,
            false
        );
    }

    private Player newP2NoSword() {
        return new Player(
            200, 200, 50, 40,
            null, null,
            null,
            2,
            false
        );
    }

    @Test
    void moveShouldUpdatePositionAndFacingRight() {
        Player p = newP1WithSword(10, 0);

        float x0 = p.getX();
        p.move(1, 0, 1f); // вправо 1 секунду

        assertTrue(p.getX() > x0);
        assertEquals(Direction.RIGHT, p.getFacing());
    }

    @Test
    void moveShouldUpdateFacingUp() {
        Player p = newP1WithSword(10, 0);

        p.move(0, 10, 0.1f);
        assertEquals(Direction.UP, p.getFacing());
    }

    @Test
    void createAttackRectDependsOnFacing() {
        Player p = newP1WithSword(10, 0);

        p.setFacing(Direction.RIGHT);
        var rRight = p.createAttackRect();
        assertTrue(rRight.x >= p.getX() + p.getWidth());

        p.setFacing(Direction.LEFT);
        var rLeft = p.createAttackRect();
        assertTrue(rLeft.x + rLeft.width <= p.getX() + 0.0001f);

        p.setFacing(Direction.UP);
        var rUp = p.createAttackRect();
        assertTrue(rUp.y >= p.getY() + p.getHeight());

        p.setFacing(Direction.DOWN);
        var rDown = p.createAttackRect();
        assertTrue(rDown.y + rDown.height <= p.getY() + 0.0001f);
    }

    @Test
    void takeDamageShouldNotGoBelowZero() {
        Player p = newP1WithSword(10, 0);

        p.takeDamage(999);
        assertEquals(0, p.getHp());
        assertTrue(p.isDead());
    }

    @Test
    void healShouldNotExceedMaxHp() {
        Player p = newP1WithSword(10, 0);

        p.takeDamage(10);
        assertEquals(90, p.getHp());

        p.heal(999);
        assertEquals(p.getMaxHp(), p.getHp());
    }

    @Test
    void fullHealRestoresMaxHp() {
        Player p = newP1WithSword(10, 0);
        p.takeDamage(50);
        p.fullHeal();
        assertEquals(p.getMaxHp(), p.getHp());
    }

    @Test
    void attackReturnsNullWhenNoSword() {
        Player p = newP2NoSword();
        assertNull(p.attack(0));
    }

    @Test
    void attackRespectsCooldown() {
        Player p = newP1WithSword(10, 500); // 500ms cooldown

        assertNotNull(p.attack(1000)); // первый удар ok
        assertNull(p.attack(1200));    // прошло 200ms -> рано
        assertNotNull(p.attack(1600)); // прошло 600ms -> можно
    }

    @Test
    void attackPlayerShouldDealDamageWhenHitboxOverlaps() {
        // ставим игроков так, чтобы P1 смотрел вправо и доставал P2
        Player p1 = new Player(0, 0, 50, 40, null, null, new Sword(10, 0), 1, false);
        Player p2 = new Player(55, 0, 50, 40, null, null, new Sword(10, 0), 2, false);

        int hp2 = p2.getHp();
        p1.setFacing(Direction.RIGHT);
        p1.attack(p2);

        assertTrue(p2.getHp() < hp2);
    }

    @Test
    void bonusDamageShouldBeAppliedInAttackOverload() {
        Player p1 = new Player(0, 0, 50, 40, null, null, new Sword(10, 0), 1, false);
        Player p2 = new Player(55, 0, 50, 40, null, null, new Sword(10, 0), 2, false);

        p1.setFacing(Direction.RIGHT);
        int hp2 = p2.getHp();

        p1.attack(p2, 7); // 10 + 7
        assertEquals(hp2 - 17, p2.getHp());
    }

    @Test
    void clearActiveBuffResetsStats() {
        Player p = newP1WithSword(10, 0);

        p.setSpeedMultiplier(2f);
        p.setBonusDamage(5);

        p.clearActiveBuff();

        assertEquals(1f, p.getSpeedMultiplier(), 0.0001f);
        assertEquals(0, p.getBonusDamage());
        assertNull(p.getActiveBuff());
        assertEquals(0, p.getActiveBuffDurationMs());
    }

    @Test
    void updateBuffClearsWhenInactive() {
        Player p = newP1WithSword(10, 0);

        // эффект, который никогда не активен
        Effect inactive = new Effect(0L) {
            @Override public void applyTo(Player player, long nowMs) {
                player.setSpeedMultiplier(3f);
                player.setBonusDamage(9);
            }

            @Override public boolean isActive(long nowMs) { return false; }

            @Override public long getRemainingMs(long nowMs) { return 0; }
        };

        p.setActiveBuff(inactive, 1000);
        p.updateBuff(123);

        // должен сбросить бафф и нормализовать статы
        assertNull(p.getActiveBuff());
        assertEquals(0, p.getActiveBuffDurationMs());
        assertEquals(1f, p.getSpeedMultiplier(), 0.0001f);
        assertEquals(0, p.getBonusDamage());
    }

    @Test
    void renderAndDisposeWithNoTexturesShouldNotThrow() {
        Player p = newP1WithSword(10, 0);
        assertDoesNotThrow(() -> p.render(null));
        assertDoesNotThrow(p::dispose);
    }
}
