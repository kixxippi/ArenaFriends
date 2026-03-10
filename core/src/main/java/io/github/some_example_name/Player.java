package io.github.some_example_name;

import com.badlogic.gdx.math.Rectangle;

public class Player {
    public Rectangle rect;
    public int speed;
    public int hp;

    public Player(float x, float y, float w, float h) {
        rect = new Rectangle(x, y, w, h);
        speed = 100;
        hp = 100;
    }
}
