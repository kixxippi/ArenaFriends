package io.github.some_example_name.powerup;

import io.github.some_example_name.model.Player;

public class HealPowerUp extends PowerUp {
    private final int healAmount;

    public HealPowerUp(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void applyTo(Player player, long nowMs) {
        player.heal(healAmount);
    }
}
