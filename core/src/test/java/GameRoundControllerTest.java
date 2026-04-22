import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.arena.ArenaFactory;
import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.powerup.HealPowerUp;
import io.github.some_example_name.powerup.PowerUpType;
import io.github.some_example_name.powerup.WorldPowerUp;
import io.github.some_example_name.screen.GameRoundController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameRoundControllerTest {

    private static final float VW = 1408f;
    private static final float VH = 768f;

    private GameRoundController newController() {
        return new GameRoundController(VW, VH, 80, 80, VW - 140, VH - 140);
    }

    private Player newPlayer(float x, float y, int playerNum) {
        // Player(..., loadTextures=false)
        return new Player(x, y, 64, 64, null, null, new Sword(10, 500), playerNum, false);
    }

    @Test
    void clampPlayerToWorldShouldClampToBounds() {
        GameRoundController round = newController();
        Player p = newPlayer(-1000, -1000, 1);

        round.clampPlayerToWorld(p);
        assertTrue(p.getX() >= 0);
        assertTrue(p.getY() >= 0);

        p.setPosition(99999, 99999);
        round.clampPlayerToWorld(p);
        assertTrue(p.getX() <= (VW - p.getWidth()) + 0.001f);
        assertTrue(p.getY() <= (VH - p.getHeight()) + 0.001f);
    }

    @Test
    void resetForNextFightShouldHealClearBuffsAndClearPowerUps() {
        GameRoundController round = newController();
        Player p1 = newPlayer(300, 300, 1);
        Player p2 = newPlayer(400, 400, 2);

        p1.takeDamage(50);
        p2.takeDamage(70);

        p1.setSpeedMultiplier(2f);
        p1.setBonusDamage(10);
        p2.setSpeedMultiplier(3f);
        p2.setBonusDamage(5);

        Array<WorldPowerUp> pus = new Array<>();
        pus.add(new WorldPowerUp(PowerUpType.HEAL, new Rectangle(10, 10, 10, 10), new HealPowerUp(10), null));
        pus.add(new WorldPowerUp(PowerUpType.HEAL, new Rectangle(20, 20, 10, 10), new HealPowerUp(10), null));

        round.resetForNextFight(p1, p2, pus);

        assertEquals(p1.getMaxHp(), p1.getHp());
        assertEquals(p2.getMaxHp(), p2.getHp());

        assertEquals(1f, p1.getSpeedMultiplier(), 0.0001f);
        assertEquals(0, p1.getBonusDamage());
        assertEquals(1f, p2.getSpeedMultiplier(), 0.0001f);
        assertEquals(0, p2.getBonusDamage());

        assertEquals(0, pus.size);
    }

    @Test
    void onFightEndShouldIncrementRoundsAndResetIfNotMatchOver() {
        GameRoundController round = newController();
        Player p1 = newPlayer(300, 300, 1);
        Player p2 = newPlayer(400, 400, 2);
        Array<WorldPowerUp> pus = new Array<>();

        p1.takeDamage(20);
        p2.takeDamage(20);

        round.onFightEnd(1, p1, p2, pus);

        assertEquals(1, round.getP1RoundsWon());
        assertEquals(0, round.getP2RoundsWon());
        assertFalse(round.isMatchOver());

        // reset случился
        assertEquals(p1.getMaxHp(), p1.getHp());
        assertEquals(p2.getMaxHp(), p2.getHp());
    }

    @Test
    void onFightEndSecondWinShouldEndMatch() {
        GameRoundController round = newController();
        Player p1 = newPlayer(80, 80, 1);
        Player p2 = newPlayer(1000, 600, 2);
        Array<WorldPowerUp> pus = new Array<>();

        round.onFightEnd(1, p1, p2, pus);
        assertFalse(round.isMatchOver());

        round.onFightEnd(1, p1, p2, pus);
        assertTrue(round.isMatchOver());
        assertEquals(2, round.getP1RoundsWon());
    }

    @Test
    void updateGameplayShouldPickUpPowerUpForP1() {
        GameRoundController round = newController();

        Arena arena = ArenaFactory.createArena(1, VW, VH, false);
        Player p1 = newPlayer(100, 100, 1);
        Player p2 = newPlayer(1200, 600, 2);

        p1.takeDamage(30);
        int hpAfterDamage = p1.getHp();

        Array<WorldPowerUp> pus = new Array<>();
        pus.add(new WorldPowerUp(
            PowerUpType.HEAL,
            new Rectangle(p1.getX(), p1.getY(), 40, 40),
            new HealPowerUp(10),
            null
        ));

        round.updateGameplay(0.016f, arena, p1, p2, pus);

        assertEquals(0, pus.size);
        assertEquals(hpAfterDamage + 10, p1.getHp());
    }

    @Test
    void updateGameplayShouldDetectWinnerAndAdvanceRounds() {
        GameRoundController round = newController();

        Arena arena = ArenaFactory.createArena(1, VW, VH, false);
        Player p1 = newPlayer(80, 80, 1);
        Player p2 = newPlayer(1000, 600, 2);
        Array<WorldPowerUp> pus = new Array<>();

        p1.takeDamage(999);
        assertTrue(p1.isDead());
        assertFalse(p2.isDead());

        round.updateGameplay(0.016f, arena, p1, p2, pus);

        assertEquals(1, round.getP2RoundsWon());
        assertFalse(round.isMatchOver());
    }

    @Test
    void updateWinnerBannerShouldMoveAfterMatchOver() {
        GameRoundController round = newController();
        Player p1 = newPlayer(80, 80, 1);
        Player p2 = newPlayer(1000, 600, 2);
        Array<WorldPowerUp> pus = new Array<>();

        // доводим до matchOver
        round.onFightEnd(1, p1, p2, pus);
        round.onFightEnd(1, p1, p2, pus);
        assertTrue(round.isMatchOver());

        float y0 = round.getWinnerBannerY();
        round.updateWinnerBanner(1.0f);
        float y1 = round.getWinnerBannerY();

        assertNotEquals(y0, y1);
    }
}
