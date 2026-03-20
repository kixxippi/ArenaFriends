package io.github.some_example_name.arena;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class MixedArena extends BaseRectangleArena {

    private final Array<WallVisual> wallVisuals = new Array<>();
    private final Array<PuddleVisual> puddleVisuals = new Array<>();

    public MixedArena(float worldWidth, float worldHeight) {
        float thickness = 20f;

        Rectangle wall1 = new Rectangle(worldWidth * 0.55f, worldHeight * 0.35f, 427/4, 280/4);
        walls.add(wall1);
        wallVisuals.add(new WallVisual(wall1, "walls/wall_horizontal.png"));

        Rectangle wall2 = new Rectangle(worldWidth * 0.35f, worldHeight * 0.65f, 427/4, 280/4);
        walls.add(wall2);
        wallVisuals.add(new WallVisual(wall2, "walls/wall_horizontal.png"));

        Rectangle wall3 = new Rectangle(worldWidth * 0.15f, worldHeight * 0.15f, 257, 84);
        walls.add(wall3);
        wallVisuals.add(new WallVisual(wall3, "walls/wall_horizontal2.png"));

        Rectangle wall4 = new Rectangle(worldWidth * 0.25f, worldHeight * 0.46f, 125/4, 428/4);
        walls.add(wall4);
        wallVisuals.add(new WallVisual(wall4, "walls/wall_to_corner.png"));

        Rectangle wall5 = new Rectangle(worldWidth * 0.15f, worldHeight * 0.65f, 50, 212);
        walls.add(wall5);
        wallVisuals.add(new WallVisual(wall5, "walls/wall_vertical.png"));

        Rectangle wall6 = new Rectangle(worldWidth * 0.8f, worldHeight * 0.1f, 50, 212);
        walls.add(wall6);
        wallVisuals.add(new WallVisual(wall6, "walls/wall_vertical.png"));

        Rectangle wall7 = new Rectangle(worldWidth * 0.6f, worldHeight * 0.8f, 257, 84);
        walls.add(wall7);
        wallVisuals.add(new WallVisual(wall7, "walls/wall_horizontal2.png"));

        Rectangle puddle1 = new Rectangle(worldWidth * 0.45f, worldHeight * 0.5f, 100, 60);
        puddles.add(puddle1);
        puddleVisuals.add(new PuddleVisual(puddle1, "puddles/puddle2.png"));

        Rectangle puddle2 = new Rectangle(worldWidth * 0.03f, worldHeight * 0.35f, 100, 60);
        puddles.add(puddle2);
        puddleVisuals.add(new PuddleVisual(puddle2, "puddles/puddle1.png"));

        Rectangle puddle3 = new Rectangle(worldWidth * 0.45f, worldHeight * 0.10f, 150, 80);
        puddles.add(puddle3);
        puddleVisuals.add(new PuddleVisual(puddle3, "puddles/puddle3.png"));

        Rectangle puddle4 = new Rectangle(worldWidth * 0.32f, worldHeight * 0.85f, 100, 60);
        puddles.add(puddle4);
        puddleVisuals.add(new PuddleVisual(puddle4, "puddles/puddle1.png"));

        Rectangle puddle5 = new Rectangle(worldWidth * 0.9f, worldHeight * 0.15f, 100, 60);
        puddles.add(puddle5);
        puddleVisuals.add(new PuddleVisual(puddle5, "puddles/puddle2.png"));

        Rectangle puddle6 = new Rectangle(worldWidth * 0.75f, worldHeight * 0.55f, 150, 80);
        puddles.add(puddle6);
        puddleVisuals.add(new PuddleVisual(puddle6, "puddles/puddle3.png"));
    }

    public Array<WallVisual> getWallVisuals() {
        return wallVisuals;
    }

    public Array<PuddleVisual> getPuddleVisuals() {
        return puddleVisuals;
    }
}
