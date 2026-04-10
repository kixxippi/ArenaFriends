package io.github.some_example_name.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.effect.Effect;

// player class: position, movement, health and basic logic
public class Player {
    private final Rectangle rect;
    private float baseSpeed;
    private float speedMultiplier = 1f;
    private int bonusDamage = 0;

    // one active buff at a time
    private Effect activeBuff = null;
    private long activeBuffDurationMs = 0; // for UI bar

    private final int maxHp;
    int hp;

    private Direction facing; // where the player is looking

    private Texture textureRight;// texture when facing right
    private Texture textureLeft;// texture when facing left

    private Sword sword;

    // for testing
    public Player(float x, float y, float w, float h,
                  String textureRightPath, String textureLeftPath,
                  Sword sword, int numberPlayer) {
        this(x, y, w, h, textureRightPath, textureLeftPath, sword, numberPlayer, true);
    }

    public Player(float x, float y, float w, float h,
                  String textureRightPath, String textureLeftPath,
                  Sword sword, int numberPlayer, boolean loadTextures) {
        this.rect = new Rectangle(x, y, w, h);
        this.baseSpeed = 350f;

        this.maxHp = 100;
        this.hp = maxHp;

        if (numberPlayer == 1) {
            this.facing = Direction.RIGHT;
        } else if (numberPlayer == 2) {
            this.facing = Direction.LEFT;
        }

        if (loadTextures) {
            this.textureRight = new Texture(Gdx.files.internal(textureRightPath));
            this.textureLeft = new Texture(Gdx.files.internal(textureLeftPath));
        } else {
            this.textureRight = null;
            this.textureLeft = null;
        }

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

    public void clearActiveBuff() {
        activeBuff = null;
        activeBuffDurationMs = 0;

        // normalize stats
        setSpeedMultiplier(1f);
        setBonusDamage(0);
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    public void heal() {
        heal(20);
    }

    public void heal(int amount) {
        hp += amount;
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
        attack(target, bonusDamage);
    }

    // overloaded attack with bonus damage
    public void attack(Player target, int bonusDamage) {
        long nowMs = System.currentTimeMillis();
        AttackHitbox hitbox = attack(nowMs);
        if (hitbox != null && hitbox.hits(target)) {
            target.takeDamage(hitbox.getDamage() + bonusDamage);
        }
    }

    // called when player picks SPEED or DAMAGE
    public void setActiveBuff(Effect buff, long durationMs) {
        // remove old buff effects immediately
        clearBuffState();

        this.activeBuff = buff;
        this.activeBuffDurationMs = durationMs;
    }

    // call every frame
    public void updateBuff(long nowMs) {
        if (activeBuff == null) return;

        // apply effect (SpeedEffect/DamageEffect already know how to reset when inactive)
        activeBuff.applyTo(this, nowMs);

        // if ended -> clear and normalize
        if (!activeBuff.isActive(nowMs)) {
            activeBuff = null;
            activeBuffDurationMs = 0;
            clearBuffState();
        }
    }

    private void clearBuffState() {
        // normalize player stats (important when switching buff)
        setSpeedMultiplier(1f);
        setBonusDamage(0);
    }

    public Effect getActiveBuff() { return activeBuff; }
    public long getActiveBuffDurationMs() { return activeBuffDurationMs; }

    public void render(SpriteBatch batch) {
        if (textureRight == null || textureLeft == null) return;

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

    public Rectangle getRect() { return rect; }
    public float getX() { return rect.x; }
    public float getY() { return rect.y; }

    public void setPosition(float x, float y) {
        rect.x = x;
        rect.y = y;
    }

    public float getWidth() { return rect.width; }
    public float getHeight() { return rect.height; }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }

    public void setSpeedMultiplier(float multiplier) { this.speedMultiplier = multiplier; }
    public float getSpeedMultiplier() { return speedMultiplier; }

    public int getBonusDamage() { return bonusDamage; }
    public void setBonusDamage(int bonusDamage) { this.bonusDamage = bonusDamage; }

    public float getBaseSpeed() { return baseSpeed; }
    public void setBaseSpeed(float baseSpeed) { this.baseSpeed = baseSpeed; }

    public Direction getFacing() { return facing; }
    public void setFacing(Direction facing) { this.facing = facing; }

    public Sword getSword() { return sword; }
    public void setSword(Sword sword) { this.sword = sword; }
}
