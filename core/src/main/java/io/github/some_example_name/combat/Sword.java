package io.github.some_example_name.combat;

public class Sword {
    int damage;
    long cooldownMs;

    private long lastAttackMs = 0;

    public Sword(int damage, long cooldownMs) {
        this.damage = damage;
        this.cooldownMs = cooldownMs;
    }

    public boolean canAttack(long nowMs) {
        return nowMs - lastAttackMs >= cooldownMs;
    }

    public void recordAttack(long nowMs) {
        this.lastAttackMs = nowMs;
    }

    public int getDamage() {
        return damage;
    }
}
