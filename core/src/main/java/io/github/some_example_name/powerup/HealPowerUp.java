package io.github.some_example_name.powerup;

import io.github.some_example_name.model.Player;

// C HealPowerUp extends PowerUp
public class HealPowerUp extends PowerUp {
    private final int healAmount;

    public HealPowerUp(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void applyTo(Player player, long nowMs) {
        // простая логика: вылечить игрока
        player.heal(); // внутри heal ограничивает до maxHp
        // если хочешь ровно healAmount — можно добавить метод heal(int)
    }
}
