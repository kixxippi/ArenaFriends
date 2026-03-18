package io.github.some_example_name.effect;

import io.github.some_example_name.model.Player;

public class SpeedEffect extends Effect {
    private final float speedMultiplier;

    public SpeedEffect(long endTimeMs, float speedMultiplier) {
        super(endTimeMs);
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void applyTo(Player player, long nowMs) {
        if (isActive(nowMs)) {
            player.setSpeedMultiplier(speedMultiplier);
        } else {
            player.setSpeedMultiplier(1f);
        }
    }
}
