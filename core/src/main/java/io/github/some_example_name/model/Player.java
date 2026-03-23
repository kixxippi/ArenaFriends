package io.github.some_example_name.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.combat.Sword;

// player class: position, movement, health and basic logic
public class Player {
    private final Rectangle rect;
    private float baseSpeed;
    private float speedMultiplier = 1f;

    private final int maxHp;
    int hp;

    private Direction facing; // where the player is looking

    private Texture textureRight;// texture when facing right
    private Texture textureLeft;// texture when facing left

    private Sword sword;

    public Player(float x, float y, float w, float h,
                  String textureRightPath, String textureLeftPath,
                  Sword sword, int numberPlayer) {
        this.rect = new Rectangle(x, y, w, h);
        this.baseSpeed = 350f;

        this.maxHp = 100;
        this.hp = maxHp;

        if(numberPlayer == 1) { this.facing = Direction.RIGHT;}
        else if(numberPlayer == 2) { this.facing = Direction.LEFT;}

        this.textureRight = new Texture(Gdx.files.internal(textureRightPath));
        this.textureLeft = new Texture(Gdx.files.internal(textureLeftPath));

        this.sword = sword;
    }

    public void move(float dx, float dy, float dt) {
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len > 0) {
            // normalize direction vector
            dx /= len;
            dy /= len;

            // update facing direction
            if (Math.abs(dx) >= Math.abs(dy)) {
                facing = dx > 0 ? Direction.RIGHT : Direction.LEFT;
            } else {
                facing = dy > 0 ? Direction.UP : Direction.DOWN;
            }

            float speed = baseSpeed * speedMultiplier;

            rect.x += dx * speed * dt;
            rect.y += dy * speed * dt;
        }
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    public void heal() {
        int healAmount = 20;
        hp += healAmount;
        if (hp > maxHp) hp = maxHp;
    }

    public void fullHeal() {
        hp = maxHp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public Rectangle createAttackRect() {
        float attackRange = 30f; // how far forward we hit
        float extraSize = 16f; // how much bigger than player rectangle

        float ax = rect.x;
        float ay = rect.y;
        float aw = rect.width;
        float ah = rect.height;

        switch (facing) {
            case UP:
                ax = rect.x - extraSize / 2f;
                ay = rect.y + rect.height;
                aw = rect.width + extraSize;
                ah = attackRange;
                break;
            case DOWN:
                ax = rect.x - extraSize / 2f;
                ay = rect.y - attackRange;
                aw = rect.width + extraSize;
                ah = attackRange;
                break;
            case LEFT:
                ax = rect.x - attackRange;
                ay = rect.y - extraSize / 2f;
                aw = attackRange;
                ah = rect.height + extraSize;
                break;
            case RIGHT:
                ax = rect.x + rect.width;
                ay = rect.y - extraSize / 2f;
                aw = attackRange;
                ah = rect.height + extraSize;
                break;
        }

        return new Rectangle(ax, ay, aw, ah);
    }

    //create AttackHitbox or null if on cooldown
    public AttackHitbox attack(long nowMs) {
        if (sword == null) return null;
        if (!sword.canAttack(nowMs)) return null;

        sword.recordAttack(nowMs);
        Rectangle hitRect = createAttackRect();
        return new AttackHitbox(hitRect, sword.getDamage());
    }

    // basic attack without bonus damage
    public void attack(Player target) {
        attack(target, 0);
    }

    // overloaded attack with bonus damage
    public void attack(Player target, int bonusDamage) {
        long nowMs = System.currentTimeMillis();
        AttackHitbox hitbox = attack(nowMs);
        if (hitbox != null && hitbox.hits(target)) {
            target.takeDamage(hitbox.getDamage() + bonusDamage);
        }
    }

    public void setSpeedMultiplier(float multiplier) {
        this.speedMultiplier = multiplier;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public void render(SpriteBatch batch) {
        Texture current;

        if (facing == Direction.LEFT) {
            current = textureLeft;
        } else if (facing == Direction.RIGHT) {
            current = textureRight;
        } else {
            current = textureRight;
        }

        batch.draw(current, rect.x, rect.y, rect.width, rect.height);
    }

    public void dispose() {
        if (textureRight != null) textureRight.dispose();
        if (textureLeft != null) textureLeft.dispose();
    }

    // Getter/Setter

    public Rectangle getRect() {
        return rect;
    }

    public float getX() {
        return rect.x;
    }

    public float getY() {
        return rect.y;
    }

    public void setPosition(float x, float y) {
        rect.x = x;
        rect.y = y;
    }

    public float getWidth() {
        return rect.width;
    }

    public float getHeight() {
        return rect.height;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }
}
