package io.github.some_example_name.arena;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PuddlesOnlyArena extends BaseRectangleArena {

    private final Array<PuddleVisual> puddleVisuals = new Array<>();

    public PuddlesOnlyArena(float worldWidth, float worldHeight) {

        // Лужа 1
        Rectangle puddle1 = new Rectangle(worldWidth * 0.25f, worldHeight * 0.3f, 120, 80);
        puddles.add(puddle1);
        puddleVisuals.add(new PuddleVisual(puddle1, "puddle_water.png"));

        // Лужа 2
        Rectangle puddle2 = new Rectangle(worldWidth * 0.6f, worldHeight * 0.6f, 150, 100);
        puddles.add(puddle2);
        puddleVisuals.add(new PuddleVisual(puddle2, "puddle_slime.png"));
    }

    public Array<PuddleVisual> getPuddleVisuals() {
        return puddleVisuals;
    }
}
