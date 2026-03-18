package io.github.some_example_name.arena;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

//render and dispose Arena(walls/puddles)
public class ArenaRenderer {

    public void renderWalls(SpriteBatch batch, Array<WallVisual> walls) {
        for (WallVisual wall : walls) {
            wall.render(batch);
        }
    }

    public void renderPuddles(SpriteBatch batch, Array<PuddleVisual> puddles) {
        for (PuddleVisual puddle : puddles) {
            puddle.render(batch);
        }
    }

    public void disposeWalls(Array<WallVisual> walls) {
        for (WallVisual wall : walls) {
            wall.dispose();
        }
    }

    public void disposePuddles(Array<PuddleVisual> puddles) {
        for (PuddleVisual puddle : puddles) {
            puddle.dispose();
        }
    }
}
