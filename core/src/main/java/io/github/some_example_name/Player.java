package io.github.some_example_name;

import com.badlogic.gdx.math.Rectangle;

// Класс игрока: хранит позицию, скорость, здоровье и даёт методы для логики.
public class Player {
    private final Rectangle rect; // прямоугольник хитбокса / положения
    private float speed;          // скорость движения (px/s)

    private final int maxHp;      // максимальное здоровье
    private int hp;               // текущее здоровье

    private Direction facing;     // направление, куда смотрит игрок

    public Player(float x, float y, float w, float h) {
        this.rect = new Rectangle(x, y, w, h);
        this.speed = 200f;

        this.maxHp = 100; // максимум HP
        this.hp = maxHp;  // стартуем с фулл хп

        this.facing = Direction.DOWN; // по умолчанию смотрим вниз
    }

    public void move(float dx, float dy, float dt) {
        // нормализуем вектор, чтобы по диагонали не было быстрее
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len > 0) {
            dx /= len;
            dy /= len;

            // обновляем направление, куда смотрит игрок
            if (Math.abs(dx) > Math.abs(dy)) {
                facing = dx > 0 ? Direction.RIGHT : Direction.LEFT;
            } else {
                facing = dy > 0 ? Direction.UP : Direction.DOWN;
            }

            rect.x += dx * speed * dt;
            rect.y += dy * speed * dt;
        }
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    public void heal() {
        int healAmount = 20; // сколько хилит одна "аптечка"
        hp += healAmount;
        if (hp > maxHp) hp = maxHp; // не вылезаем выше максимума
    }

    // Полное восстановление до maxHp (вызиваемпри новом раунде).
    public void fullHeal() {
        hp = maxHp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    // Создаём хитбокс удара перед игроком.
    public Rectangle createAttackHitbox() {
        float attackRange = 10f; // насколько далеко вперёд от игрока
        float ax = rect.x;
        float ay = rect.y;
        float aw = rect.width;
        float ah = rect.height;

        switch (facing) {
            case UP:
                ax = rect.x;
                ay = rect.y + rect.height;
                aw = rect.width;
                ah = attackRange;
                break;
            case DOWN:
                ax = rect.x;
                ay = rect.y - attackRange;
                aw = rect.width;
                ah = attackRange;
                break;
            case LEFT:
                ax = rect.x - attackRange;
                ay = rect.y;
                aw = attackRange;
                ah = rect.height;
                break;
            case RIGHT:
                ax = rect.x + rect.width;
                ay = rect.y;
                aw = attackRange;
                ah = rect.height;
                break;
        }

        return new Rectangle(ax, ay, aw, ah);
    }

    // Атакуем другого игрока: если попали хитбоксом, наносим урон.
    public void attack(Player target) {
        Rectangle hitbox = createAttackHitbox();
        if (hitbox.overlaps(target.getRect())) {
            target.takeDamage(10); // урон за один удар
        }
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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
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
}
