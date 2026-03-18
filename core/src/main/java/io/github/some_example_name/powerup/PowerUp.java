package io.github.some_example_name.powerup;

import io.github.some_example_name.model.Player;

public abstract class PowerUp {
    public abstract void applyTo(Player player, long nowMs);
}
