package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

// Класс игрока: храним позицию, скорость, здоровье и даём методы для логики
public class Player {
    private final Rectangle rect; // прямоугольник хитбокса
    private float speed; // скорость движения

    private final int maxHp;
    private int hp;

    private Direction facing; // направление, куда смотрит игрок

    private Texture textureRight;
    private Texture textureLeft;

    public Player(float x, float y, float w, float h,
                  String textureRightPath, String textureLeftPath) {
        this.rect = new Rectangle(x, y, w, h);
        this.speed = 200f;

        this.maxHp = 100;
        this.hp = maxHp;

        this.facing = Direction.DOWN; // по умолчанию смотрим вниз

        // загружаем текстуры из assets
        this.textureRight = new Texture(Gdx.files.internal(textureRightPath));
        this.textureLeft = new Texture(Gdx.files.internal(textureLeftPath));
    }

    public void move(float dx, float dy, float dt) {
        // нормализуем вектор, чтобы по диагонали не было быстрее
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len > 0) {
            dx /= len;
            dy /= len;

            // обновляем направление, куда смотрит игрок
            if (Math.abs(dx) >= Math.abs(dy)) {
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
        int healAmount = 20;
        hp += healAmount;
        if (hp > maxHp) hp = maxHp;
    }

    // Полное восстановление до maxHp (вызиваем при новом раунде)
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

    // Атакуем другого игрока: если попали хитбоксом, наносим урон
    public void attack(Player target) {
        Rectangle hitbox = createAttackHitbox();
        if (hitbox.overlaps(target.getRect())) {
            target.takeDamage(10);
        }
    }

    // Рисуем спрайт игрока
    public void render(SpriteBatch batch, int p) {
        Texture current;

        // выбираем текстуру в зависимости от направления
        if (facing == Direction.LEFT) {
            current = textureLeft;
        } else if (facing == Direction.RIGHT) {
            current = textureRight;
        } else {
            if (p == 1)  current = textureRight;
            else current = textureLeft;
        }

        batch.draw(current, rect.x, rect.y, rect.width, rect.height);
    }

    // Освобождаем ресурсы игрока.
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
