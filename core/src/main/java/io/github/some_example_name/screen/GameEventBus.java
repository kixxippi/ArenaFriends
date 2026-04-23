package io.github.some_example_name.screen;

import io.github.some_example_name.powerup.PowerUpType;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer pattern — Subject (издатель).
 *
 * Хранит список подписчиков GameEventListener и отвечает за:
 *   - регистрацию / удаление наблюдателей
 *   - рассылку уведомлений всем подписчикам
 *
 * GameRoundController владеет экземпляром GameEventBus и вызывает
 * notify-методы в нужные моменты игровой логики.
 * Таким образом обязанности разделены:
 *   GameRoundController  — игровая логика (раунды, победы, подборы)
 *   GameEventBus         — управление подписчиками и рассылка событий
 *
 * Использование:
 *   eventBus.addListener(new LoggingGameEventListener());
 *   eventBus.notifyRoundEnd(1, 1, 0);
 */
public class GameEventBus {

    private final List<GameEventListener> listeners = new ArrayList<>();

    // ---- Управление подписчиками ----

    /** Добавить наблюдателя. Один и тот же слушатель не добавляется дважды. */
    public void addListener(GameEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /** Удалить наблюдателя. */
    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    // ---- Уведомление подписчиков ----

    public void notifyRoundEnd(int winnerPlayerNumber, int p1RoundsWon, int p2RoundsWon) {
        for (GameEventListener l : listeners) {
            l.onRoundEnd(winnerPlayerNumber, p1RoundsWon, p2RoundsWon);
        }
    }

    public void notifyMatchEnd(int winnerPlayerNumber) {
        for (GameEventListener l : listeners) {
            l.onMatchEnd(winnerPlayerNumber);
        }
    }

    public void notifyPowerUpPickedUp(int playerNumber, PowerUpType type) {
        for (GameEventListener l : listeners) {
            l.onPowerUpPickedUp(playerNumber, type);
        }
    }
}
