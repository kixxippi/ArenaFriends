package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.some_example_name.DebugReflector;
import io.github.some_example_name.Starter;
import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.arena.ArenaFactory;
import io.github.some_example_name.arena.PuddleVisual;
import io.github.some_example_name.arena.WallVisual;
import io.github.some_example_name.arena.visitor.StatsCollectorVisitor;
import io.github.some_example_name.arena.visitor.Shape;
import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.effect.Effect;
import io.github.some_example_name.map.GameMap;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.powerup.PowerUpSpawner;
import io.github.some_example_name.powerup.WorldPowerUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameScreen extends ScreenAdapter {

    private static final Logger log = LoggerFactory.getLogger(GameScreen.class);

    private static final float virtualWidth  = 1408f;
    private static final float virtualHeight = 768f;

    private final Starter game;
    private final int mapId;
    private final boolean testMode;

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

    private Texture matchWinnerP1;
    private Texture matchWinnerP2;

    private Texture rounds0;
    private Texture rounds1;
    private Texture rounds2;

    private final float p1SpawnX = 80;
    private final float p1SpawnY = 80;
    private final float p2SpawnX = virtualWidth - 140;
    private final float p2SpawnY = virtualHeight - 140;

    private GameRoundController round;

    public GameScreen(Starter game, int mapId) {
        this(game, mapId, false);
    }

    public GameScreen(Starter game, int mapId, boolean testMode) {
        this.game     = game;
        this.mapId    = mapId;
        this.testMode = testMode;
    }

    // -------------------------------------------------------------------------
    // show
    // -------------------------------------------------------------------------

    @Override
    public void show() {
        log.info("Round init: mapId={}, virtualSize={}x{}", mapId, virtualWidth, virtualHeight);

        round = new GameRoundController(
            virtualWidth, virtualHeight,
            p1SpawnX, p1SpawnY,
            p2SpawnX, p2SpawnY
        );

        // Observer: подписываем логгер событий матча
        round.getEventBus().addListener(new LoggingGameEventListener());

        if (testMode) {
            Sword sword1 = new Sword(10, 500);
            Sword sword2 = new Sword(10, 500);

            p1 = new Player(p1SpawnX, p1SpawnY, 64, 64, null, null, sword1, 1, false);
            p2 = new Player(p2SpawnX, p2SpawnY, 64, 64, null, null, sword2, 2, false);

            arena = ArenaFactory.createArena(mapId, virtualWidth, virtualHeight, false);
            map   = new GameMap(mapId, false);

            powerUpSpawner = new PowerUpSpawner(virtualWidth, virtualHeight, arena, 15000, false);
            return;
        }

        camera = new OrthographicCamera();
        viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();
        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        camera.update();

        map = new GameMap(mapId, true);

        Sword sword1 = new Sword(10, 500);
        Sword sword2 = new Sword(10, 500);

        p1 = new Player(p1SpawnX, p1SpawnY, 64, 64,
            "players/player1_right.png", "players/player1_left.png", sword1, 1);
        p2 = new Player(p2SpawnX, p2SpawnY, 64, 64,
            "players/player2_right.png", "players/player2_left.png", sword2, 2);
        log.debug("Players spawned: p1 at ({},{}), p2 at ({},{})",
            p1SpawnX, p1SpawnY, p2SpawnX, p2SpawnY);

        try {
            arena = ArenaFactory.createArena(mapId, virtualWidth, virtualHeight);
            log.info("Arena created: {}", arena.getClass().getSimpleName());
        } catch (RuntimeException ex) {
            log.error("Arena creation failed for mapId={}", mapId, ex);
            throw ex;
        }

        // Visitor: собираем статистику арены
        StatsCollectorVisitor stats = new StatsCollectorVisitor();
        for (Shape v : arena.getVisuals()) {
            v.accept(stats);
        }
        log.info("Arena stats: {}", stats);

        log.debug("Arena dump:\n{}", DebugReflector.dump(arena));
        log.debug("P1 dump:\n{}", DebugReflector.dump(p1));
        log.debug("P2 dump:\n{}", DebugReflector.dump(p2));

        player1Label = new Texture(Gdx.files.internal("ui/player1.png"));
        player2Label = new Texture(Gdx.files.internal("ui/player2.png"));
        heartFull    = new Texture(Gdx.files.internal("ui/heart_full.png"));
        heartEmpty   = new Texture(Gdx.files.internal("ui/heart_empty.png"));

        powerUpSpawner = new PowerUpSpawner(virtualWidth, virtualHeight, arena, 15000, true);

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1, 1, 1, 1);
        pm.fill();
        whitePixel = new Texture(pm);
        pm.dispose();

        matchWinnerP1 = new Texture(Gdx.files.internal("ui/match_winner_p1.png"));
        matchWinnerP2 = new Texture(Gdx.files.internal("ui/match_winner_p2.png"));

        rounds0 = new Texture(Gdx.files.internal("ui/rounds_0.png"));
        rounds1 = new Texture(Gdx.files.internal("ui/rounds_1.png"));
        rounds2 = new Texture(Gdx.files.internal("ui/rounds_2.png"));
    }

    // -------------------------------------------------------------------------
    // render
    // -------------------------------------------------------------------------

    @Override
    public void render(float delta) {
        handleInput(delta);

        if (round != null && arena != null && p1 != null && p2 != null) {
            round.updateGameplay(delta, arena, p1, p2, worldPowerUps);
            round.updateWinnerBanner(delta);
        }

        if (testMode) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        Gdx.gl.glClearColor(0.12f, 0.12f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        map.render(game.batch, virtualWidth, virtualHeight);

        // Рендеринг арены — прямые вызовы render() на каждом visual-объекте
        // (RenderVisitor и LibGdxRenderer удалены)
        if (arena != null) {
            for (Shape v : arena.getVisuals()) {
                if (v instanceof WallVisual)   ((WallVisual)   v).render(game.batch);
                else if (v instanceof PuddleVisual) ((PuddleVisual) v).render(game.batch);
            }
        }

        long nowMs = System.currentTimeMillis();

        if (!round.isMatchOver()) {
            WorldPowerUp spawned = powerUpSpawner.trySpawn(nowMs, p1, p2, worldPowerUps);
            if (spawned != null) {
                worldPowerUps.add(spawned);
                log.info("Power-up spawned: type={}, pos=({}, {})",
                    spawned.getPowerUp().getClass().getSimpleName(),
                    spawned.getRect().x, spawned.getRect().y);
            }
            for (WorldPowerUp pu : worldPowerUps) {
                pu.render(game.batch);
            }
        }

        p1.render(game.batch);
        p2.render(game.batch);

        if (player1Label != null && player2Label != null) {
            float scale = 0.2f;
            float w1 = player1Label.getWidth()  * scale;
            float h1 = player1Label.getHeight() * scale;
            float w2 = player2Label.getWidth()  * scale;
            float h2 = player2Label.getHeight() * scale;
            float margin    = 2f;
            float topMargin = 2f;
            game.batch.draw(player1Label, margin, virtualHeight - topMargin - h1, w1, h1);
            game.batch.draw(player2Label, virtualWidth - margin - w2, virtualHeight - topMargin - h2, w2, h2);
        }

        drawHearts(game.batch, p1.getHp(), p1.getMaxHp(), 2, virtualHeight - 70);
        drawHearts(game.batch, p2.getHp(), p2.getMaxHp(),
            virtualWidth - 2 - 10 * 24, virtualHeight - 70);

        drawBuffBar(game.batch, p1, 5, virtualHeight - 82);
        drawBuffBar(game.batch, p2, virtualWidth - 5 - 160, virtualHeight - 82);

        Texture p1RoundsTex = getRoundsTexture(round.getP1RoundsWon());
        Texture p2RoundsTex = getRoundsTexture(round.getP2RoundsWon());
        float roundsScale = 0.25f;
        if (p1RoundsTex != null) {
            float w = p1RoundsTex.getWidth()  * roundsScale;
            float h = p1RoundsTex.getHeight() * roundsScale;
            game.batch.draw(p1RoundsTex, 5, virtualHeight - 115, w, h);
        }
        if (p2RoundsTex != null) {
            float w = p2RoundsTex.getWidth()  * roundsScale;
            float h = p2RoundsTex.getHeight() * roundsScale;
            game.batch.draw(p2RoundsTex, virtualWidth - 5 - w, virtualHeight - 115, w, h);
        }

        if (round.isMatchOver()) {
            game.batch.setColor(0f, 0f, 0f, 0.45f);
            game.batch.draw(whitePixel, 0, 0, virtualWidth, virtualHeight);
            game.batch.setColor(1f, 1f, 1f, 1f);

            Texture win = (round.getP1RoundsWon() >= 2) ? matchWinnerP1 : matchWinnerP2;
            if (win != null) {
                float s = 2f;
                float w = win.getWidth()  * s;
                float h = win.getHeight() * s;
                float x = (virtualWidth - w) / 2f;
                game.batch.draw(win, x, round.getWinnerBannerY(), w, h);
            }
        }

        game.font.draw(game.batch, "WASD + SPACE (attack)", 10, virtualHeight - 120);
        game.font.draw(game.batch, "IJKL + Right ALT (attack)", virtualWidth - 170, virtualHeight - 120);
        game.font.draw(game.batch, "ESC - Back to menu", 10, virtualHeight - 140);

        game.batch.end();
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

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
        pct = com.badlogic.gdx.math.MathUtils.clamp(pct, 0f, 1f);

        float barW = 160f;
        float barH = 10f;

        batch.setColor(0f, 0f, 0f, 0.6f);
        batch.draw(whitePixel, x, y, barW, barH);

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

    private void handleInput(float dt) {
        if (testMode) return;
        if (round != null && round.isMatchOver()) return;

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

        p1.move(dx1, dy1, dt);
        p2.move(dx2, dy2, dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            log.debug("Attack input: player=1");
            p1.attack(p2);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_RIGHT)) {
            log.debug("Attack input: player=2");
            p2.attack(p1);
        }
    }

    // ---- TEST HELPERS ----
    public GameRoundController _testRound()                          { return round; }
    public Player               _testP1()                           { return p1; }
    public Player               _testP2()                           { return p2; }
    public void                 _testAddWorldPowerUp(WorldPowerUp pu) { worldPowerUps.add(pu); }
    public int                  _testWorldPowerUpsCount()            { return worldPowerUps.size; }

    // -------------------------------------------------------------------------
    // resize / dispose
    // -------------------------------------------------------------------------

    @Override
    public void resize(int width, int height) {
        if (viewport != null) viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        log.info("Disposing GameScreen resources...");

        if (map  != null) map.dispose();
        if (p1   != null) p1.dispose();
        if (p2   != null) p2.dispose();

        // Dispose арены — прямые вызовы dispose() на каждом visual-объекте
        // (DisposeVisitor удалён)
        if (arena != null) {
            for (Shape v : arena.getVisuals()) {
                if (v instanceof WallVisual)        ((WallVisual)   v).dispose();
                else if (v instanceof PuddleVisual) ((PuddleVisual) v).dispose();
            }
        }

        if (player1Label != null) player1Label.dispose();
        if (player2Label != null) player2Label.dispose();
        if (heartFull    != null) heartFull.dispose();
        if (heartEmpty   != null) heartEmpty.dispose();

        for (WorldPowerUp pu : worldPowerUps) pu.dispose();
        worldPowerUps.clear();

        if (whitePixel    != null) whitePixel.dispose();
        if (matchWinnerP1 != null) matchWinnerP1.dispose();
        if (matchWinnerP2 != null) matchWinnerP2.dispose();
        if (rounds0       != null) rounds0.dispose();
        if (rounds1       != null) rounds1.dispose();
        if (rounds2       != null) rounds2.dispose();
    }
}
