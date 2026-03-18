package io.github.some_example_name.arena;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MixedArena extends BaseRectangleArena {

    private final Array<WallVisual> wallVisuals = new Array<>();
    private final Array<PuddleVisual> puddleVisuals = new Array<>();

    public MixedArena(float worldWidth, float worldHeight) {
        float thickness = 20f;

        // === Стена 1 (каменная) ===
        Rectangle wall1 = new Rectangle(worldWidth / 3f, 0, thickness, worldHeight * 0.7f);
        walls.add(wall1);
        wallVisuals.add(new WallVisual(wall1, "wall_stone.png"));

        // === Стена 2 (деревянная) ===
        Rectangle wall2 = new Rectangle(worldWidth * 0.7f, worldHeight * 0.3f, thickness, worldHeight * 0.7f);
        walls.add(wall2);
        wallVisuals.add(new WallVisual(wall2, "wall_wood.png"));

        // === Лужа 1 (обычная вода) ===
        Rectangle puddle1 = new Rectangle(worldWidth * 0.2f, worldHeight * 0.2f, 100, 60);
        puddles.add(puddle1);
        puddleVisuals.add(new PuddleVisual(puddle1, "puddle_water.png"));

        // === Лужа 2 (ядовитая) ===
        Rectangle puddle2 = new Rectangle(worldWidth * 0.55f, worldHeight * 0.55f, 140, 90);
        puddles.add(puddle2);
        puddleVisuals.add(new PuddleVisual(puddle2, "puddle_poison.png"));
    }

    public Array<WallVisual> getWallVisuals() {
        return wallVisuals;
    }

    public Array<PuddleVisual> getPuddleVisuals() {
        return puddleVisuals;
    }
}
