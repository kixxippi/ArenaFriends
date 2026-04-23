package io.github.some_example_name.screen;

import io.github.some_example_name.powerup.PowerUpType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Observer pattern — конкретный наблюдатель: логирование событий матча.
 *
 * Реализует GameEventListener. Записывает все игровые события через SLF4J.
 * Не содержит GUI-кода — работает только с игровыми данными.
 *
 * Подключение:
 *   round.addListener(new LoggingGameEventListener());
 */
public class LoggingGameEventListener implements GameEventListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingGameEventListener.class);

    @Override
    public void onRoundEnd(int winnerPlayerNumber, int p1RoundsWon, int p2RoundsWon) {
        log.info("Round ended — winner: Player {}, score: P1={} P2={}",
            winnerPlayerNumber, p1RoundsWon, p2RoundsWon);
    }

    @Override
    public void onMatchEnd(int winnerPlayerNumber) {
        log.info("Match finished — winner: Player {}", winnerPlayerNumber);
    }

    @Override
    public void onPowerUpPickedUp(int playerNumber, PowerUpType type) {
        log.info("Player {} picked up power-up: {}", playerNumber, type);
    }
}
