package io.github.some_example_name.effect;

import io.github.some_example_name.model.Player;

public abstract class Effect {
    protected final long endTimeMs;

    public Effect(long endTimeMs) {
        this.endTimeMs = endTimeMs;
    }

    public long getRemainingMs(long nowMs) {
        return Math.max(0, endTimeMs - nowMs);
    }

    public boolean isActive(long nowMs) {
        return getRemainingMs(nowMs) > 0;
    }

    public abstract void applyTo(Player player, long nowMs);
}
