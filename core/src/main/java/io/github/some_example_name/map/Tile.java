package io.github.some_example_name.map;

import com.badlogic.gdx.math.Rectangle;

public class Tile {
    public TileType type;
    public Rectangle bounds; // позиция и размер в мире

    public Tile(TileType type, float x, float y, float size) {
        this.type = type;
        this.bounds = new Rectangle(x, y, size, size);
    }

    // можно ли ходить по этому тайлу
    public boolean isWalkable() {
        return type == TileType.FLOOR;
    }
}
