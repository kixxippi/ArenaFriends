package io.github.some_example_name.arena;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PuddlesOnlyArena extends BaseRectangleArena {

    private final Array<PuddleVisual> puddleVisuals = new Array<>();

    public PuddlesOnlyArena(float worldWidth, float worldHeight) {

        Rectangle puddle1 = new Rectangle(worldWidth * 0.25f, worldHeight * 0.3f, 120, 80);
        puddles.add(puddle1);
        puddleVisuals.add(new PuddleVisual(puddle1, "puddles/puddle1.png"));

        Rectangle puddle2 = new Rectangle(worldWidth * 0.6f, worldHeight * 0.6f, 150, 100);
        puddles.add(puddle2);
        puddleVisuals.add(new PuddleVisual(puddle2, "puddles/puddle2.png"));

        Rectangle puddle3 = new Rectangle(worldWidth * 0.2f, worldHeight * 0.8f, 150, 80);
        puddles.add(puddle3);
        puddleVisuals.add(new PuddleVisual(puddle3, "puddles/puddle3.png"));

        Rectangle puddle4 = new Rectangle(worldWidth * 0.06f, worldHeight * 0.45f, 150, 100);
        puddles.add(puddle4);
        puddleVisuals.add(new PuddleVisual(puddle4, "puddles/puddle2.png"));

        Rectangle puddle5 = new Rectangle(worldWidth * 0.85f, worldHeight * 0.38f, 120, 80);
        puddles.add(puddle5);
        puddleVisuals.add(new PuddleVisual(puddle5, "puddles/puddle1.png"));

        Rectangle puddle6 = new Rectangle(worldWidth * 0.55f, worldHeight * 0.15f, 150, 80);
        puddles.add(puddle6);
        puddleVisuals.add(new PuddleVisual(puddle6, "puddles/puddle3.png"));
    }

    public Array<PuddleVisual> getPuddleVisuals() {
        return puddleVisuals;
    }
}
