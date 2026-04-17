package io.github.some_example_name.arena;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.arena.visitor.VisitableVisual;

public class WallsOnlyArena extends BaseRectangleArena {

    private final Array<WallVisual> wallVisuals = new Array<>();

    // for testing

    public WallsOnlyArena(float worldWidth, float worldHeight) {
        this(worldWidth, worldHeight, true);
    }

    public WallsOnlyArena(float worldWidth, float worldHeight, boolean loadTextures) {
        float thickness = 38f;

        // bottom
        Rectangle bottom = new Rectangle(0, 0, worldWidth, thickness);
        walls.add(bottom);
        wallVisuals.add(new WallVisual(bottom, loadTextures ? "walls/wall_bottom.png" : null));

        // top
        Rectangle top = new Rectangle(0, worldHeight - thickness, worldWidth, thickness);
        walls.add(top);
        wallVisuals.add(new WallVisual(top, loadTextures ? "walls/wall_top.png" : null));

        // left
        Rectangle left = new Rectangle(0, 0, thickness, worldHeight);
        walls.add(left);
        wallVisuals.add(new WallVisual(left, loadTextures ? "walls/wall_left.png" : null));

        // right
        Rectangle right = new Rectangle(worldWidth - thickness, 0, thickness, worldHeight);
        walls.add(right);
        wallVisuals.add(new WallVisual(right, loadTextures ? "walls/wall_right.png" : null));

        Rectangle wall1 = new Rectangle(200, 200, 427 / 4f, 280 / 4f);
        walls.add(wall1);
        wallVisuals.add(new WallVisual(wall1, loadTextures ? "walls/wall_horizontal.png" : null));

        Rectangle wall2 = new Rectangle(200 + 427 / 4f - 30, 200 + 280 / 4f - 10, 125 / 4f, 428 / 4f);
        walls.add(wall2);
        wallVisuals.add(new WallVisual(wall2, loadTextures ? "walls/wall_to_corner.png" : null));

        Rectangle wall3 = new Rectangle(worldWidth * 0.55f, worldHeight * 0.35f, 257, 84);
        walls.add(wall3);
        wallVisuals.add(new WallVisual(wall3, loadTextures ? "walls/wall_horizontal2.png" : null));

        Rectangle wall4 = new Rectangle(worldWidth * 0.35f, worldHeight * 0.65f, 257, 84);
        walls.add(wall4);
        wallVisuals.add(new WallVisual(wall4, loadTextures ? "walls/wall_horizontal2.png" : null));
    }

    @Override
    public Array<VisitableVisual> getVisuals() {
        Array<VisitableVisual> out = new Array<>();
        out.addAll(getWallVisuals());
        return out;
    }


    public Array<WallVisual> getWallVisuals() {
        return wallVisuals;
    }
}
