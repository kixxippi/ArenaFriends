package io.github.some_example_name.arena;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.model.Player;

public abstract class BaseRectangleArena implements Arena {

    // collision rectangles for walls
    protected final Array<Rectangle> walls = new Array<>();
    // collision rectangles for puddles
    protected final Array<Rectangle> puddles = new Array<>();

    @Override
    public void handleWalls(Player player) {
        for (Rectangle wall : walls) {
            if (player.getRect().overlaps(wall)) {
                resolveWallCollision(player, wall);
            }
        }
    }

    @Override
    public void handlePuddles(Player player) {
        boolean inPuddle = false;
        for (Rectangle puddle : puddles) {
            if (player.getRect().overlaps(puddle)) {
                inPuddle = true;
                break;
            }
        }

        // if player is in any puddle, apply slowdown
        if (inPuddle) {
            player.setSpeedMultiplier(0.5f);
        } else {
            //reset speed
            player.setSpeedMultiplier(1f);
        }
    }

    // push player out of wall
    private void resolveWallCollision(Player player, Rectangle wall) {
        Rectangle r = player.getRect();

        float leftOverlap   = r.x + r.width  - wall.x;
        float rightOverlap  = wall.x + wall.width - r.x;
        float bottomOverlap = r.y + r.height - wall.y;
        float topOverlap    = wall.y + wall.height - r.y;

        float minOverlap = Math.min(Math.min(leftOverlap, rightOverlap),
            Math.min(bottomOverlap, topOverlap));

        if (minOverlap == leftOverlap) {
            r.x = wall.x - r.width;
        } else if (minOverlap == rightOverlap) {
            r.x = wall.x + wall.width;
        } else if (minOverlap == bottomOverlap) {
            r.y = wall.y - r.height;
        } else {
            r.y = wall.y + wall.height;
        }

        // sync back to player
        player.setPosition(r.x, r.y);
    }
}
