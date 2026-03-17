package io.github.some_example_name.effect;

import io.github.some_example_name.model.Player;

// C DamageEffect extends Effect
public class DamageEffect extends Effect {
    private final int extraDamage;

    public DamageEffect(long endTimeMs, int extraDamage) {
        super(endTimeMs);
        this.extraDamage = extraDamage;
    }

    @Override
    public void applyTo(Player player, long nowMs) {
        // здесь пока просто идея:
        // в будущем можно хранить "базовый" урон меча и добавлять extraDamage,
        // сейчас оставим заготовку — чтобы был overriding
    }
}
