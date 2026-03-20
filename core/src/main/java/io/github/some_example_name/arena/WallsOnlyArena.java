package io.github.some_example_name.arena;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class WallsOnlyArena extends BaseRectangleArena {

    private final Array<WallVisual> wallVisuals = new Array<>();

    public WallsOnlyArena(float worldWidth, float worldHeight) {
        float thickness = 38f;

        // bottom
        Rectangle bottom = new Rectangle(0, 0, worldWidth, thickness);
        walls.add(bottom);
        wallVisuals.add(new WallVisual(bottom, "walls/wall_bottom.png"));

        // top
        Rectangle top = new Rectangle(0,worldHeight - thickness, worldWidth, thickness);
        walls.add(top);
        wallVisuals.add(new WallVisual(top, "walls/wall_top.png"));

        // left
        Rectangle left = new Rectangle(0, 0, thickness, worldHeight);
        walls.add(left);
        wallVisuals.add(new WallVisual(left, "walls/wall_left.png"));

        //right
        Rectangle right = new Rectangle(worldWidth - thickness, 0, thickness, worldHeight);
        walls.add(right);
        wallVisuals.add(new WallVisual(right, "walls/wall_right.png"));


        Rectangle wall1 = new Rectangle(200, 200, 427/4, 280/4);
        walls.add(wall1);
        wallVisuals.add(new WallVisual(wall1, "walls/wall_horizontal.png"));

        Rectangle wall2 = new Rectangle(200 + 427/4 - 30, 200 + 280/4 - 10, 125/4, 428/4);
        walls.add(wall2);
        wallVisuals.add(new WallVisual(wall2, "walls/wall_to_corner.png"));

        Rectangle wall3 = new Rectangle(worldWidth * 0.55f, worldHeight * 0.35f, 257, 84);
        walls.add(wall3);
        wallVisuals.add(new WallVisual(wall3, "walls/wall_horizontal2.png"));

        Rectangle wall4 = new Rectangle(worldWidth * 0.35f, worldHeight * 0.65f, 257, 84);
        walls.add(wall4);
        wallVisuals.add(new WallVisual(wall4, "walls/wall_horizontal2.png"));
    }

    public Array<WallVisual> getWallVisuals() {
        return wallVisuals;
    }
}
