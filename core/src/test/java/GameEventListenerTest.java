import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.arena.ArenaFactory;
import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.powerup.HealPowerUp;
import io.github.some_example_name.powerup.PowerUpType;
import io.github.some_example_name.powerup.WorldPowerUp;
import io.github.some_example_name.screen.GameEventListener;
import io.github.some_example_name.screen.GameRoundController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameEventListenerTest {

    private static final float VW = 1408f;
    private static final float VH = 768f;

    /** Тестовый слушатель — просто записывает все вызовы в список. */
    static class RecordingListener implements GameEventListener {
        final List<String> events = new ArrayList<>();

        @Override
        public void onRoundEnd(int winner, int p1, int p2) {
            events.add("roundEnd:winner=" + winner + ",p1=" + p1 + ",p2=" + p2);
        }

        @Override
        public void onMatchEnd(int winner) {
            events.add("matchEnd:winner=" + winner);
        }

        @Override
        public void onPowerUpPickedUp(int playerNumber, PowerUpType type) {
            events.add("powerUp:player=" + playerNumber + ",type=" + type);
        }
    }

    private GameRoundController newController() {
        return new GameRoundController(VW, VH, 80, 80, VW - 140, VH - 140);
    }

    private Player newPlayer(float x, float y, int num) {
        return new Player(x, y, 64, 64, null, null, new Sword(10, 500), num, false);
    }

    @Test
    void listenerShouldReceiveRoundEndEvent() {
        GameRoundController round = newController();
        RecordingListener listener = new RecordingListener();
        round.getEventBus().addListener(listener);

        Player p1 = newPlayer(80, 80, 1);
        Player p2 = newPlayer(1000, 600, 2);

        round.onFightEnd(1, p1, p2, new Array<>());

        assertEquals(1, listener.events.size());
        assertEquals("roundEnd:winner=1,p1=1,p2=0", listener.events.get(0));
        assertFalse(round.isMatchOver());
    }

    @Test
    void listenerShouldReceiveMatchEndEvent() {
        GameRoundController round = newController();
        RecordingListener listener = new RecordingListener();
        round.getEventBus().addListener(listener);

        Player p1 = newPlayer(80, 80, 1);
        Player p2 = newPlayer(1000, 600, 2);

        round.onFightEnd(1, p1, p2, new Array<>()); // раунд 1 -> roundEnd
        round.onFightEnd(1, p1, p2, new Array<>()); // раунд 2 -> matchEnd

        assertTrue(round.isMatchOver());
        assertEquals(2, listener.events.size());
        assertEquals("roundEnd:winner=1,p1=1,p2=0", listener.events.get(0));
        assertEquals("matchEnd:winner=1",             listener.events.get(1));
    }

    @Test
    void listenerShouldReceivePowerUpPickedUpEvent() {
        GameRoundController round = newController();
        RecordingListener listener = new RecordingListener();
        round.getEventBus().addListener(listener);

        Arena arena = ArenaFactory.createArena(1, VW, VH, false);
        Player p1 = newPlayer(100, 100, 1);
        Player p2 = newPlayer(1200, 600, 2);
        p1.takeDamage(30);

        Array<WorldPowerUp> pus = new Array<>();
        pus.add(new WorldPowerUp(
            PowerUpType.HEAL,
            new Rectangle(p1.getX(), p1.getY(), 40, 40),
            new HealPowerUp(10),
            null
        ));

        round.updateGameplay(0.016f, arena, p1, p2, pus);

        assertEquals(1, listener.events.size());
        assertEquals("powerUp:player=1,type=HEAL", listener.events.get(0));
    }

    @Test
    void multipleListenersShouldAllBeNotified() {
        GameRoundController round = newController();
        RecordingListener l1 = new RecordingListener();
        RecordingListener l2 = new RecordingListener();
        round.getEventBus().addListener(l1);
        round.getEventBus().addListener(l2);

        Player p1 = newPlayer(80, 80, 1);
        Player p2 = newPlayer(1000, 600, 2);
        round.onFightEnd(2, p1, p2, new Array<>());

        assertEquals(1, l1.events.size());
        assertEquals(1, l2.events.size());
        assertEquals(l1.events.get(0), l2.events.get(0));
    }

    @Test
    void removedListenerShouldNotReceiveEvents() {
        GameRoundController round = newController();
        RecordingListener listener = new RecordingListener();
        round.getEventBus().addListener(listener);
        round.getEventBus().removeListener(listener);

        Player p1 = newPlayer(80, 80, 1);
        Player p2 = newPlayer(1000, 600, 2);
        round.onFightEnd(1, p1, p2, new Array<>());

        assertTrue(listener.events.isEmpty());
    }

    @Test
    void addSameListenerTwiceShouldNotDuplicateEvents() {
        GameRoundController round = newController();
        RecordingListener listener = new RecordingListener();
        round.getEventBus().addListener(listener);
        round.getEventBus().addListener(listener); // второй раз — игнорируется

        Player p1 = newPlayer(80, 80, 1);
        Player p2 = newPlayer(1000, 600, 2);
        round.onFightEnd(1, p1, p2, new Array<>());

        assertEquals(1, listener.events.size());
    }
}
