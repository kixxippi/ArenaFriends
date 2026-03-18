package io.github.some_example_name.powerup;

import io.github.some_example_name.effect.SpeedEffect;
import io.github.some_example_name.model.Player;

// C SpeedPowerUp extends PowerUp
public class SpeedPowerUp extends PowerUp {
    private final long durationMs;
    private final float multiplier;

    public SpeedPowerUp(long durationMs, float multiplier) {
        this.durationMs = durationMs;
        this.multiplier = multiplier;
    }

    @Override
    public void applyTo(Player player, long nowMs) {
        long endTime = nowMs + durationMs;
        SpeedEffect effect = new SpeedEffect(endTime, multiplier);
        effect.applyTo(player, nowMs);
    }
}
