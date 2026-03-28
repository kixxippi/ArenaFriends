package io.github.some_example_name.effect;

import io.github.some_example_name.model.Player;

public class DamageEffect extends Effect {
    private final int extraDamage;

    public DamageEffect(long endTimeMs, int extraDamage) {
        super(endTimeMs);
        this.extraDamage = extraDamage;
    }

    @Override
    public void applyTo(Player player, long nowMs) {
        if (isActive(nowMs)) {
            player.setBonusDamage(extraDamage);
        } else {
            player.setBonusDamage(0);
        }
    }
}
