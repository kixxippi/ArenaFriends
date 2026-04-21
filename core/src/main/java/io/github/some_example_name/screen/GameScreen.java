package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.some_example_name.DebugReflector;
import io.github.some_example_name.arena.visitor.DisposeVisitor;
import io.github.some_example_name.arena.visitor.RenderVisitor;
import io.github.some_example_name.arena.visitor.VisitableVisual;
import io.github.some_example_name.map.GameMap;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.Starter;
import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.arena.ArenaFactory;
import io.github.some_example_name.powerup.PowerUpSpawner;
import io.github.some_example_name.powerup.WorldPowerUp;
import io.github.some_example_name.effect.Effect;
import io.github.some_example_name.render.LibGdxRenderer;
import io.github.some_example_name.render.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// background map + two players
public class GameScreen extends ScreenAdapter {
    private static final Logger log = LoggerFactory.getLogger(GameScreen.class);


    private static final float virtualWidth = 1408f;
    private static final float virtualHeight = 768f;

    private final Starter game;
    private final int mapId;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Player p1;
    private Player p2;

    private GameMap map;

    private Arena arena;

    private Texture player1Label;
    private Texture player2Label;

    private Texture heartFull;
    private Texture heartEmpty;

    private Array<WorldPowerUp> worldPowerUps = new Array<>();
    private PowerUpSpawner powerUpSpawner;

    private Texture whitePixel;

    // best of 3 state
    private int p1RoundsWon = 0;
    private int p2RoundsWon = 0;
    private boolean matchOver = false;

    // animated winner banner
    private float winnerBannerY = 0f;
    private float winnerBannerVel = 80f;
    private float winnerBannerMinY;
    private float winnerBannerMaxY;

    // winner images
    private Texture matchWinnerP1;
    private Texture matchWinnerP2;

    private Texture rounds0;
    private Texture rounds1;
    private Texture rounds2;

    // spawn positions
    private final float p1SpawnX = 80;
    private final float p1SpawnY = 80;
    private final float p2SpawnX = virtualWidth - 140;
    private final float p2SpawnY = virtualHeight - 140;

    public GameScreen(Starter game, int mapId) {
        this.game = game;
        this.mapId = mapId;
    }

    private void drawHearts(SpriteBatch batch, int hp, int maxHp, float startX, float startY) {
        float heartW = 24f;
        float heartH = 24f;

        int fullHearts = hp / 10;

        for (int i = 0; i < 10; i++) {
            Texture t = (i < fullHearts) ? heartFull : heartEmpty;
            batch.draw(t, startX + i * heartW, startY, heartW, heartH);
        }
    }

    private void drawBuffBar(SpriteBatch batch, Player player, float x, float y) {
        Effect buff = player.getActiveBuff();
        if (buff == null) return;

        long nowMs = System.currentTimeMillis();
        if (!buff.isActive(nowMs)) return;

        long duration = player.getActiveBuffDurationMs();
        if (duration <= 0) return;

        float pct = (float) buff.getRemainingMs(nowMs) / (float) duration;
        pct = MathUtils.clamp(pct, 0f, 1f);

        float barW = 160f;
        float barH = 10f;

        // background
        batch.setColor(0f, 0f, 0f, 0.6f);
        batch.draw(whitePixel, x, y, barW, barH);

        // fill
        batch.setColor(0.2f, 1f, 0.2f, 0.9f);
        batch.draw(whitePixel, x, y, barW * pct, barH);

        batch.setColor(1f, 1f, 1f, 1f);
    }

    private Texture getRoundsTexture(int roundsWon) {
        if (rounds0 == null || rounds1 == null || rounds2 == null) return null;
        if (roundsWon <= 0) return rounds0;
        if (roundsWon == 1) return rounds1;
        return rounds2;
    }

    private void resetForNextFight() {
        log.info("Resetting for next fight. Current score: p1RoundsWon={}, p2RoundsWon={}", p1RoundsWon, p2RoundsWon);

        // reset players
        p1.setPosition(p1SpawnX, p1SpawnY);
        p2.setPosition(p2SpawnX, p2SpawnY);

        p1.fullHeal();
        p2.fullHeal();

        // remove buffs immediately
        p1.clearActiveBuff();
        p2.clearActiveBuff();

        // clear powerups on map
        for (WorldPowerUp pu : worldPowerUps) pu.dispose();
        worldPowerUps.clear();
    }

    private void onFightEnd(int winnerPlayerNumber) {
        if (winnerPlayerNumber == 1) p1RoundsWon++;
        else p2RoundsWon++;

        // check match end
        if (p1RoundsWon >= 2 || p2RoundsWon >= 2) {
            matchOver = true;

            log.info("Match finished. Winner: Player {}", winnerPlayerNumber);
            winnerBannerMinY = virtualHeight * 0.35f;
            winnerBannerMaxY = virtualHeight * 0.55f;
            winnerBannerY = (winnerBannerMinY + winnerBannerMaxY) * 0.5f;
            return;
        }

        resetForNextFight();
    }

    @Override
    public void show() {
        log.info("Round init: mapId={}, virtualSize={}x{}", mapId, virtualWidth, virtualHeight);
        // create camera and viewport with fixed virtual size
        camera = new OrthographicCamera();
        viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();
        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        camera.update();

        // create map by id
        map = new GameMap(mapId);

        // create swords for players
        Sword sword1 = new Sword(10, 500);
        Sword sword2 = new Sword(10, 500);

        // spawn players, pass textures and sword
        p1 = new Player(p1SpawnX, p1SpawnY, 64, 64,
                "players/player1_right.png",
                "players/player1_left.png",
                sword1, 1);

        p2 = new Player(p2SpawnX, p2SpawnY, 64, 64,
                "players/player2_right.png",
                "players/player2_left.png",
                sword2, 2);
        log.debug("Players spawned: p1 at ({},{}), p2 at ({},{})", p1SpawnX, p1SpawnY, p2SpawnX, p2SpawnY);

        // create arena and renderer
        try {
            arena = ArenaFactory.createArena(mapId, virtualWidth, virtualHeight);
            log.info("Arena created: {}", arena.getClass().getSimpleName());
        } catch (RuntimeException ex) {
            log.error("Arena creation failed for mapId={}", mapId, ex);
            throw ex;
        }

        log.debug("Arena dump:\n{}", DebugReflector.dump(arena));
        log.debug("P1 dump:\n{}", DebugReflector.dump(p1));
        log.debug("P2 dump:\n{}", DebugReflector.dump(p2));

        player1Label = new Texture(Gdx.files.internal("ui/player1.png"));
        player2Label = new Texture(Gdx.files.internal("ui/player2.png"));

        heartFull = new Texture(Gdx.files.internal("ui/heart_full.png"));
        heartEmpty = new Texture(Gdx.files.internal("ui/heart_empty.png"));

        powerUpSpawner = new PowerUpSpawner(virtualWidth, virtualHeight, arena, 15000);

        // 1x1 white pixel texture for drawing bars + dark overlay
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1, 1, 1, 1);
        pm.fill();
        whitePixel = new Texture(pm);
        pm.dispose();

        // winner images
        matchWinnerP1 = new Texture(Gdx.files.internal("ui/match_winner_p1.png"));
        matchWinnerP2 = new Texture(Gdx.files.internal("ui/match_winner_p2.png"));

        // optional rounds won icons
        rounds0 = new Texture(Gdx.files.internal("ui/rounds_0.png"));
        rounds1 = new Texture(Gdx.files.internal("ui/rounds_1.png"));
        rounds2 = new Texture(Gdx.files.internal("ui/rounds_2.png"));
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        updateGameplay(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        // clear screen
        Gdx.gl.glClearColor(0.12f, 0.12f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // set the camera projection
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // draw map background
        map.render(game.batch, virtualWidth, virtualHeight);

        // draw arena (walls / puddles)
        if (arena != null) {
            Renderer renderer = new LibGdxRenderer(game.batch);
            RenderVisitor renderVisitor = new RenderVisitor(renderer);

            for (VisitableVisual v : arena.getVisuals()) {
                v.accept(renderVisitor);
            }
        }

        long nowMs = System.currentTimeMillis();

        // spawn new powerup (each 15 sec)
        if (!matchOver) {
            WorldPowerUp spawned = powerUpSpawner.trySpawn(nowMs, p1, p2, worldPowerUps);
            if (spawned != null){
                worldPowerUps.add(spawned);
                log.info("Power-up spawned: type={}, pos=({}, {})",
                        spawned.getPowerUp().getClass().getSimpleName(),
                        spawned.getRect().x, spawned.getRect().y);
            }

            // render powerups
            for (WorldPowerUp pu : worldPowerUps) {
                pu.render(game.batch);
            }
        }

        // draw players
        p1.render(game.batch);
        p2.render(game.batch);

        // UI labels
        if (player1Label != null && player2Label != null) {
            float scale = 0.2f;

            float w1 = player1Label.getWidth() * scale;
            float h1 = player1Label.getHeight() * scale;

            float w2 = player2Label.getWidth() * scale;
            float h2 = player2Label.getHeight() * scale;

            float margin = 2f;
            float topMargin = 2f;

            // left top
            float x1 = margin;
            float y1 = virtualHeight - topMargin - h1;

            // right top
            float x2 = virtualWidth - margin - w2;
            float y2 = virtualHeight - topMargin - h2;

            game.batch.draw(player1Label, x1, y1, w1, h1);
            game.batch.draw(player2Label, x2, y2, w2, h2);
        }

        // P1 hearts
        drawHearts(game.batch, p1.getHp(), p1.getMaxHp(), 2, virtualHeight - 70);

        // P2 hearts
        drawHearts(game.batch, p2.getHp(), p2.getMaxHp(), virtualWidth - 2 - 10 * 24, virtualHeight - 70);

        // Buff bars
        drawBuffBar(game.batch, p1, 5, virtualHeight - 82);
        drawBuffBar(game.batch, p2, virtualWidth - 5 - 160, virtualHeight - 82);

        // rounds won icons
        Texture p1RoundsTex = getRoundsTexture(p1RoundsWon);
        Texture p2RoundsTex = getRoundsTexture(p2RoundsWon);

        float roundsScale = 0.25f;
        if (p1RoundsTex != null) {
            float w = p1RoundsTex.getWidth() * roundsScale;
            float h = p1RoundsTex.getHeight() * roundsScale;
            game.batch.draw(p1RoundsTex, 5, virtualHeight - 115, w, h);
        }
        if (p2RoundsTex != null) {
            float w = p2RoundsTex.getWidth() * roundsScale;
            float h = p2RoundsTex.getHeight() * roundsScale;
            game.batch.draw(p2RoundsTex, virtualWidth - 5 - w, virtualHeight - 115, w, h);
        }

        // match over overlay + animated winner banner
        if (matchOver) {
            // darken whole screen a bit
            game.batch.setColor(0f, 0f, 0f, 0.45f);
            game.batch.draw(whitePixel, 0, 0, virtualWidth, virtualHeight);
            game.batch.setColor(1f, 1f, 1f, 1f);

            // banner up/down
            winnerBannerY += winnerBannerVel * delta;
            if (winnerBannerY > winnerBannerMaxY) {
                winnerBannerY = winnerBannerMaxY;
                winnerBannerVel = -winnerBannerVel;
            } else if (winnerBannerY < winnerBannerMinY) {
                winnerBannerY = winnerBannerMinY;
                winnerBannerVel = -winnerBannerVel;
            }

            Texture win = (p1RoundsWon >= 2) ? matchWinnerP1 : matchWinnerP2;
            if (win != null) {
                float s = 2f;
                float w = win.getWidth() * s;
                float h = win.getHeight() * s;
                float x = (virtualWidth - w) / 2f;
                game.batch.draw(win, x, winnerBannerY, w, h);
            }
        }

        game.font.draw(game.batch, "WASD + SPACE (attack)", 10, virtualHeight - 120);
        game.font.draw(game.batch, "IJKL + Right ALT (attack)", virtualWidth - 170, virtualHeight - 120);
        game.font.draw(game.batch, "ESC - Back to menu", 10, virtualHeight - 140);

        game.batch.end();
    }

    private void handleInput(float dt) {
        // when match is over -> freeze gameplay
        if (matchOver) return;

        float dx1 = 0, dy1 = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy1 += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy1 -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx1 -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx1 += 1;

        float dx2 = 0, dy2 = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.I)) dy2 += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.K)) dy2 -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.J)) dx2 -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.L)) dx2 += 1;

        // movement through Player methods (input -> commands)
        p1.move(dx1, dy1, dt);
        p2.move(dx2, dy2, dt);

        // P1 attack
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            log.debug("Attack input: player=1");
            p1.attack(p2);
        }

        // P2 attack
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_RIGHT)) {
            log.debug("Attack input: player=2");
            p2.attack(p1);
        }
    }

    private void updateGameplay(float dt){
        // when match is over -> freeze gameplay
        if (matchOver) return;

        // clamp players to world bounds
        clampPlayerToWorld(p1);
        clampPlayerToWorld(p2);

        // arena logic: walls and puddles
        try {
            if (arena != null) {
                arena.applyLogic(p1);
                arena.applyLogic(p2);
            } else {
                log.error("Arena is null in updateGameplay()");
            }
        } catch (RuntimeException ex) {
            log.error("Arena logic failed", ex);
            throw ex;
        }

        long nowMs = System.currentTimeMillis();

        // update buffs
        p1.updateBuff(nowMs);
        p2.updateBuff(nowMs);

        // pickup logic
        for (int i = worldPowerUps.size - 1; i >= 0; i--) {
            WorldPowerUp pu = worldPowerUps.get(i);

            if (pu.getRect().overlaps(p1.getRect())) {
                log.info("Power-up picked up: player=1, type={}, pos=({}, {})",
                    pu.getPowerUp().getClass().getSimpleName(),
                    pu.getRect().x, pu.getRect().y);

                pu.getPowerUp().applyTo(p1, nowMs);

                log.debug("Power-up applied: player=1, type={}",
                    pu.getPowerUp().getClass().getSimpleName());

                pu.dispose();
                worldPowerUps.removeIndex(i);
                continue;
            }

            if (pu.getRect().overlaps(p2.getRect())) {
                log.info("Power-up picked up: player=2, type={}, pos=({}, {})",
                    pu.getPowerUp().getClass().getSimpleName(),
                    pu.getRect().x, pu.getRect().y);

                pu.getPowerUp().applyTo(p2, nowMs);

                log.debug("Power-up applied: player=2, type={}",
                    pu.getPowerUp().getClass().getSimpleName());

                pu.dispose();
                worldPowerUps.removeIndex(i);
            }
        }

        // detect fight end
        if (p1.isDead() && !p2.isDead()) {
            log.info("Fight ended. Winner=Player 2");
            onFightEnd(2);
        } else if (p2.isDead() && !p1.isDead()) {
            log.info("Fight ended. Winner=Player 1");
            onFightEnd(1);
        } else if (p1.isDead() && p2.isDead()) {
            log.error("Fight ended with both players dead (draw).");
        }
    }

    // clamp player inside virtual world
    private void clampPlayerToWorld(Player p) {
        float x = MathUtils.clamp(p.getX(), 0, virtualWidth - p.getWidth());
        float y = MathUtils.clamp(p.getY(), 0, virtualHeight - p.getHeight());
        p.setPosition(x, y);
    }

    // update viewport on window resize
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        log.info("Disposing GameScreen resources...");

        // dispose map and players
        if (map != null) map.dispose();
        if (p1 != null) p1.dispose();
        if (p2 != null) p2.dispose();

        // dispose arena textures
        if (arena != null) {
            DisposeVisitor disposeVisitor = new DisposeVisitor();
            for (VisitableVisual v : arena.getVisuals()) {
                v.accept(disposeVisitor);
            }
        }

        if (player1Label != null) player1Label.dispose();
        if (player2Label != null) player2Label.dispose();

        if (heartFull != null) heartFull.dispose();
        if (heartEmpty != null) heartEmpty.dispose();

        for (WorldPowerUp pu : worldPowerUps) {
            pu.dispose();
        }
        worldPowerUps.clear();

        if (whitePixel != null) whitePixel.dispose();

        if (matchWinnerP1 != null) matchWinnerP1.dispose();
        if (matchWinnerP2 != null) matchWinnerP2.dispose();

        if (rounds0 != null) rounds0.dispose();
        if (rounds1 != null) rounds1.dispose();
        if (rounds2 != null) rounds2.dispose();
    }
}
