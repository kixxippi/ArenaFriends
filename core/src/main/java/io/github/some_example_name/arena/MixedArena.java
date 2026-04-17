package io.github.some_example_name.arena;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.arena.visitor.VisitableVisual;

public class MixedArena extends BaseRectangleArena {

    private final Array<WallVisual> wallVisuals = new Array<>();
    private final Array<PuddleVisual> puddleVisuals = new Array<>();

    // for testing

    public MixedArena(float worldWidth, float worldHeight) {
        this(worldWidth, worldHeight, true);
    }

    public MixedArena(float worldWidth, float worldHeight, boolean loadTextures) {
        float thickness = 20f;

        Rectangle wall1 = new Rectangle(worldWidth * 0.55f, worldHeight * 0.35f, 427 / 4f, 280 / 4f);
        walls.add(wall1);
        wallVisuals.add(new WallVisual(wall1, loadTextures ? "walls/wall_horizontal.png" : null));

        Rectangle wall2 = new Rectangle(worldWidth * 0.35f, worldHeight * 0.65f, 427 / 4f, 280 / 4f);
        walls.add(wall2);
        wallVisuals.add(new WallVisual(wall2, loadTextures ? "walls/wall_horizontal.png" : null));

        Rectangle wall3 = new Rectangle(worldWidth * 0.15f, worldHeight * 0.15f, 257, 84);
        walls.add(wall3);
        wallVisuals.add(new WallVisual(wall3, loadTextures ? "walls/wall_horizontal2.png" : null));

        Rectangle wall4 = new Rectangle(worldWidth * 0.25f, worldHeight * 0.46f, 125 / 4f, 428 / 4f);
        walls.add(wall4);
        wallVisuals.add(new WallVisual(wall4, loadTextures ? "walls/wall_to_corner.png" : null));

        Rectangle wall5 = new Rectangle(worldWidth * 0.15f, worldHeight * 0.62f, 50, 212);
        walls.add(wall5);
        wallVisuals.add(new WallVisual(wall5, loadTextures ? "walls/wall_vertical.png" : null));

        Rectangle wall6 = new Rectangle(worldWidth * 0.8f, worldHeight * 0.1f, 50, 212);
        walls.add(wall6);
        wallVisuals.add(new WallVisual(wall6, loadTextures ? "walls/wall_vertical.png" : null));

        Rectangle wall7 = new Rectangle(worldWidth * 0.6f, worldHeight * 0.8f, 257, 84);
        walls.add(wall7);
        wallVisuals.add(new WallVisual(wall7, loadTextures ? "walls/wall_horizontal2.png" : null));

        Rectangle puddle1 = new Rectangle(worldWidth * 0.45f, worldHeight * 0.5f, 100, 60);
        puddles.add(puddle1);
        puddleVisuals.add(new PuddleVisual(puddle1, loadTextures ? "puddles/puddle2.png" : null));

        Rectangle puddle2 = new Rectangle(worldWidth * 0.03f, worldHeight * 0.35f, 100, 60);
        puddles.add(puddle2);
        puddleVisuals.add(new PuddleVisual(puddle2, loadTextures ? "puddles/puddle1.png" : null));

        Rectangle puddle3 = new Rectangle(worldWidth * 0.45f, worldHeight * 0.10f, 150, 80);
        puddles.add(puddle3);
        puddleVisuals.add(new PuddleVisual(puddle3, loadTextures ? "puddles/puddle3.png" : null));

        Rectangle puddle4 = new Rectangle(worldWidth * 0.32f, worldHeight * 0.85f, 100, 60);
        puddles.add(puddle4);
        puddleVisuals.add(new PuddleVisual(puddle4, loadTextures ? "puddles/puddle1.png" : null));

        Rectangle puddle5 = new Rectangle(worldWidth * 0.9f, worldHeight * 0.15f, 100, 60);
        puddles.add(puddle5);
        puddleVisuals.add(new PuddleVisual(puddle5, loadTextures ? "puddles/puddle2.png" : null));

        Rectangle puddle6 = new Rectangle(worldWidth * 0.75f, worldHeight * 0.55f, 150, 80);
        puddles.add(puddle6);
        puddleVisuals.add(new PuddleVisual(puddle6, loadTextures ? "puddles/puddle3.png" : null));
    }

    @Override
    public Array<VisitableVisual> getVisuals() {
        Array<VisitableVisual> out = new Array<>();
        out.addAll(getWallVisuals());
        out.addAll(getPuddleVisuals());
        return out;
    }


    public Array<WallVisual> getWallVisuals() {
        return wallVisuals;
    }

    public Array<PuddleVisual> getPuddleVisuals() {
        return puddleVisuals;
    }
}
