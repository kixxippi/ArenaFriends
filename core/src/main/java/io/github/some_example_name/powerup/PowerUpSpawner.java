package io.github.some_example_name.powerup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.arena.BaseRectangleArena;
import io.github.some_example_name.model.Player;

public class PowerUpSpawner {

    private final float worldWidth;
    private final float worldHeight;
    private final BaseRectangleArena arena;

    private final long spawnEveryMs;
    private long nextSpawnMs = 0;

    private float powerUpWidth = 40f;
    private float powerUpHeight = 40f;
    private final float margin = 50f;
    private final int maxAttempts = 50;

    public PowerUpSpawner(float worldWidth, float worldHeight, Arena arena, long spawnEveryMs) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.spawnEveryMs = spawnEveryMs;
        this.arena = (BaseRectangleArena) arena;
    }

    public WorldPowerUp trySpawn(long nowMs, Player p1, Player p2, Array<WorldPowerUp> existing) {
        if (nowMs < nextSpawnMs) return null;

        nextSpawnMs = nowMs + spawnEveryMs;

        PowerUpType type = randomType();

        if (type == PowerUpType.DAMAGE) {
            powerUpWidth = 30f;
            powerUpHeight = 60f;
        }else{
            powerUpWidth = 40f;
            powerUpHeight = 40f;
        }

        for (int i = 0; i < maxAttempts; i++) {
            float x = MathUtils.random(margin, worldWidth - margin - powerUpWidth);
            float y = MathUtils.random(margin, worldHeight - margin - powerUpHeight);

            Rectangle rect = new Rectangle(x, y, powerUpWidth, powerUpHeight);

            if (!isValidPosition(rect, p1, p2, existing)) continue;

            PowerUp puLogic = createLogic(type);
            Texture tex = new Texture(Gdx.files.internal(texturePath(type)));

            return new WorldPowerUp(type, rect, puLogic, tex);
        }

        return null;
    }

    private boolean isValidPosition(Rectangle r, Player p1, Player p2, Array<WorldPowerUp> existing) {
        if (p1 != null && r.overlaps(p1.getRect())) return false;
        if (p2 != null && r.overlaps(p2.getRect())) return false;

        if (existing != null) {
            for (WorldPowerUp pu : existing) {
                if (pu.getRect().overlaps(r)) return false;
            }
        }

        for (Rectangle w : arena.getWalls()) {
            if (w.overlaps(r)) return false;
        }

        for (Rectangle p : arena.getPuddles()) {
            if (p.overlaps(r)) return false;
        }

        return true;
    }

    private PowerUpType randomType() {
        int v = MathUtils.random(0, 2);
        if (v == 0) return PowerUpType.HEAL;
        if (v == 1) return PowerUpType.SPEED;
        return PowerUpType.DAMAGE;
    }

    private PowerUp createLogic(PowerUpType type) {
        long duration = 5000;

        switch (type) {
            case HEAL:
                return new HealPowerUp(10);
            case SPEED:
                return new SpeedPowerUp(duration, 1.5f);
            case DAMAGE:
            default:
                return new DamagePowerUp(duration, 10);
        }
    }

    private String texturePath(PowerUpType type) {
        switch (type) {
            case HEAL: return "powerups/heal.png";
            case SPEED: return "powerups/speed.png";
            case DAMAGE:
            default: return "powerups/damage.png";
        }
    }
}
