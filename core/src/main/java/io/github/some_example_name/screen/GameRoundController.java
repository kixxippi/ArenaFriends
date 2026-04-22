package io.github.some_example_name.screen;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.powerup.WorldPowerUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameRoundController {
    private static final Logger log = LoggerFactory.getLogger(GameRoundController.class);

    private final float virtualWidth;
    private final float virtualHeight;

    private final float p1SpawnX;
    private final float p1SpawnY;
    private final float p2SpawnX;
    private final float p2SpawnY;

    // best of 3 state
    private int p1RoundsWon = 0;
    private int p2RoundsWon = 0;
    private boolean matchOver = false;

    // animated winner banner
    private float winnerBannerY = 0f;
    private float winnerBannerVel = 80f;
    private float winnerBannerMinY = 0f;
    private float winnerBannerMaxY = 0f;

    public GameRoundController(float virtualWidth, float virtualHeight,
                               float p1SpawnX, float p1SpawnY,
                               float p2SpawnX, float p2SpawnY) {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;
        this.p1SpawnX = p1SpawnX;
        this.p1SpawnY = p1SpawnY;
        this.p2SpawnX = p2SpawnX;
        this.p2SpawnY = p2SpawnY;
    }

    public void clampPlayerToWorld(Player p) {
        float x = MathUtils.clamp(p.getX(), 0, virtualWidth - p.getWidth());
        float y = MathUtils.clamp(p.getY(), 0, virtualHeight - p.getHeight());
        p.setPosition(x, y);
    }

    public void resetForNextFight(Player p1, Player p2, Array<WorldPowerUp> worldPowerUps) {
        log.info("Resetting for next fight. Current score: p1RoundsWon={}, p2RoundsWon={}", p1RoundsWon, p2RoundsWon);

        p1.setPosition(p1SpawnX, p1SpawnY);
        p2.setPosition(p2SpawnX, p2SpawnY);

        p1.fullHeal();
        p2.fullHeal();

        p1.clearActiveBuff();
        p2.clearActiveBuff();

        for (WorldPowerUp pu : worldPowerUps) pu.dispose();
        worldPowerUps.clear();
    }

    public void onFightEnd(int winnerPlayerNumber, Player p1, Player p2, Array<WorldPowerUp> worldPowerUps) {
        if (winnerPlayerNumber == 1) p1RoundsWon++;
        else p2RoundsWon++;

        if (p1RoundsWon >= 2 || p2RoundsWon >= 2) {
            matchOver = true;

            log.info("Match finished. Winner: Player {}", winnerPlayerNumber);
            winnerBannerMinY = virtualHeight * 0.35f;
            winnerBannerMaxY = virtualHeight * 0.55f;
            winnerBannerY = (winnerBannerMinY + winnerBannerMaxY) * 0.5f;
            return;
        }

        resetForNextFight(p1, p2, worldPowerUps);
    }

    public void updateGameplay(float dt,
                               Arena arena,
                               Player p1,
                               Player p2,
                               Array<WorldPowerUp> worldPowerUps) {
        if (matchOver) return;

        clampPlayerToWorld(p1);
        clampPlayerToWorld(p2);

        if (arena != null) {
            arena.applyLogic(p1);
            arena.applyLogic(p2);
        } else {
            log.error("Arena is null in updateGameplay()");
        }

        long nowMs = System.currentTimeMillis();

        p1.updateBuff(nowMs);
        p2.updateBuff(nowMs);

        // pickup logic
        for (int i = worldPowerUps.size - 1; i >= 0; i--) {
            WorldPowerUp pu = worldPowerUps.get(i);

            if (pu.getRect().overlaps(p1.getRect())) {
                pu.getPowerUp().applyTo(p1, nowMs);
                pu.dispose();
                worldPowerUps.removeIndex(i);
                continue;
            }

            if (pu.getRect().overlaps(p2.getRect())) {
                pu.getPowerUp().applyTo(p2, nowMs);
                pu.dispose();
                worldPowerUps.removeIndex(i);
            }
        }

        // detect fight end
        if (p1.isDead() && !p2.isDead()) {
            onFightEnd(2, p1, p2, worldPowerUps);
        } else if (p2.isDead() && !p1.isDead()) {
            onFightEnd(1, p1, p2, worldPowerUps);
        } else if (p1.isDead() && p2.isDead()) {
            log.error("Fight ended with both players dead (draw).");
        }
    }

    public void updateWinnerBanner(float delta) {
        if (!matchOver) return;

        winnerBannerY += winnerBannerVel * delta;
        if (winnerBannerY > winnerBannerMaxY) {
            winnerBannerY = winnerBannerMaxY;
            winnerBannerVel = -winnerBannerVel;
        } else if (winnerBannerY < winnerBannerMinY) {
            winnerBannerY = winnerBannerMinY;
            winnerBannerVel = -winnerBannerVel;
        }
    }

    // getters (для UI и тестов)
    public int getP1RoundsWon() { return p1RoundsWon; }
    public int getP2RoundsWon() { return p2RoundsWon; }
    public boolean isMatchOver() { return matchOver; }

    public float getWinnerBannerY() { return winnerBannerY; }
    public float getWinnerBannerVel() { return winnerBannerVel; }

    // чисто для тестов (если надо)
    public void _testSetMatchOver(boolean v) { this.matchOver = v; }
}
