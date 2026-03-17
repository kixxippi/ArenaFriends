package io.github.some_example_name.effect;

import io.github.some_example_name.model.Player;

// C SpeedEffect extends Effect
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
            // эффект закончился — вернём множитель к 1
            player.setSpeedMultiplier(1f);
        }
    }
}
