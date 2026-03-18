package io.github.some_example_name.powerup;

import io.github.some_example_name.effect.DamageEffect;
import io.github.some_example_name.model.Player;

// C DamagePowerUp extends PowerUp
public class DamagePowerUp extends PowerUp {
    private final long durationMs;
    private final int extraDamage;

    public DamagePowerUp(long durationMs, int extraDamage) {
        this.durationMs = durationMs;
        this.extraDamage = extraDamage;
    }

    @Override
    public void applyTo(Player player, long nowMs) {
        long endTime = nowMs + durationMs;
        DamageEffect effect = new DamageEffect(endTime, extraDamage);
        effect.applyTo(player, nowMs);
    }
}
