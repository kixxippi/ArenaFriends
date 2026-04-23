package io.github.some_example_name.screen;

import io.github.some_example_name.powerup.PowerUpType;

/**
 * Observer pattern — интерфейс наблюдателя за игровыми событиями.
 *
 * GameRoundController является Subject (издателем): он хранит список
 * слушателей и вызывает их при наступлении игровых событий.
 *
 * Реализации (конкретные наблюдатели) подписываются через
 * GameRoundController.addListener(). Паттерн работает полностью
 * на уровне игровой логики — без GUI-кода.
 */
public interface GameEventListener {

    /**
     * Вызывается когда раунд завершён, но матч ещё продолжается.
     *
     * @param winnerPlayerNumber номер победителя раунда (1 или 2)
     * @param p1RoundsWon        счёт игрока 1 после этого раунда
     * @param p2RoundsWon        счёт игрока 2 после этого раунда
     */
    void onRoundEnd(int winnerPlayerNumber, int p1RoundsWon, int p2RoundsWon);

    /**
     * Вызывается когда матч полностью завершён (кто-то выиграл 2 раунда).
     *
     * @param winnerPlayerNumber номер победителя матча (1 или 2)
     */
    void onMatchEnd(int winnerPlayerNumber);

    /**
     * Вызывается когда игрок подбирает усиление.
     *
     * @param playerNumber номер игрока (1 или 2)
     * @param type         тип подобранного усиления
     */
    void onPowerUpPickedUp(int playerNumber, PowerUpType type);
}
