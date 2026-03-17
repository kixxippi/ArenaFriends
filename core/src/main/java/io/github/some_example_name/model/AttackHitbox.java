package io.github.some_example_name.model;

import com.badlogic.gdx.math.Rectangle;

public class AttackHitbox {
    int damage;
    private final Rectangle rect;

    public AttackHitbox(Rectangle rect, int damage) {
        this.rect = rect;
        this.damage = damage;
    }

    public boolean hits(Player target) {
        return rect.overlaps(target.getRect());
    }

    public int getDamage() {
        return damage;
    }

    public Rectangle getRect() {
        return rect;
    }
}
