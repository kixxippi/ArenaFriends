package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.some_example_name.map.GameMap;
import io.github.some_example_name.model.Player;
import io.github.some_example_name.Starter;
import io.github.some_example_name.combat.Sword;
import io.github.some_example_name.arena.Arena;
import io.github.some_example_name.arena.ArenaFactory;
import io.github.some_example_name.arena.ArenaRenderer;
import io.github.some_example_name.arena.WallsOnlyArena;
import io.github.some_example_name.arena.PuddlesOnlyArena;
import io.github.some_example_name.arena.MixedArena;

// background map + two players
public class GameScreen extends ScreenAdapter {
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
    private ArenaRenderer arenaRenderer;

    private Texture player1Label;
    private Texture player2Label;

    private Texture heartFull;
    private Texture heartEmpty;

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

    @Override
    public void show() {
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
        p1 = new Player(80, 80, 64, 64,
            "players/player1_right.png",
            "players/player1_left.png",
            sword1, 1);

        p2 = new Player(virtualWidth - 140, virtualHeight - 140, 64, 64,
            "players/player2_right.png",
            "players/player2_left.png",
            sword2, 2);

        // create arena and renderer
        arena = ArenaFactory.createArena(mapId, virtualWidth, virtualHeight);
        arenaRenderer = new ArenaRenderer();

        player1Label = new Texture(Gdx.files.internal("ui/player1.png"));
        player2Label = new Texture(Gdx.files.internal("ui/player2.png"));

        heartFull = new Texture(Gdx.files.internal("ui/heart_full.png"));
        heartEmpty = new Texture(Gdx.files.internal("ui/heart_empty.png"));
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

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
        if (arenaRenderer != null && arena != null) {
            if (arena instanceof WallsOnlyArena) {
                WallsOnlyArena wallsArena = (WallsOnlyArena) arena;
                arenaRenderer.renderWalls(game.batch, wallsArena.getWallVisuals());
            } else if (arena instanceof PuddlesOnlyArena) {
                PuddlesOnlyArena puddlesArena = (PuddlesOnlyArena) arena;
                arenaRenderer.renderPuddles(game.batch, puddlesArena.getPuddleVisuals());
            } else if (arena instanceof MixedArena) {
                MixedArena mixedArena = (MixedArena) arena;
                arenaRenderer.renderWalls(game.batch, mixedArena.getWallVisuals());
                arenaRenderer.renderPuddles(game.batch, mixedArena.getPuddleVisuals());
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

        game.font.draw(game.batch, "WASD + SPACE (attack)", 10, virtualHeight - 70);
        game.font.draw(game.batch, "IJKL + Right ALT (attack)", virtualWidth - 170, virtualHeight - 70);
        game.font.draw(game.batch, "ESC - Back to menu", 10, virtualHeight - 90);

        game.batch.end();
    }

    private void handleInput(float dt) {
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

        // movement through Player methods
        p1.move(dx1, dy1, dt);
        p2.move(dx2, dy2, dt);

        // clamp players to world bounds
        clampPlayerToWorld(p1);
        clampPlayerToWorld(p2);

        // arena logic: walls and puddles
        if (arena != null) {
            arena.handleWalls(p1);
            arena.handleWalls(p2);

            arena.handlePuddles(p1);
            arena.handlePuddles(p2);
        }

        // P1 attack
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            p1.attack(p2);
        }

        // P2 attack
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_RIGHT)) {
            p2.attack(p1);
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
        // dispose map and players
        if (map != null) map.dispose();
        if (p1 != null) p1.dispose();
        if (p2 != null) p2.dispose();

        // dispose arena textures
        if (arenaRenderer != null && arena != null) {
            if (mapId == 1) {
                WallsOnlyArena wallsArena = (WallsOnlyArena) arena;
                arenaRenderer.disposeWalls(wallsArena.getWallVisuals());
            } else if (mapId == 2) {
                PuddlesOnlyArena puddlesArena = (PuddlesOnlyArena) arena;
                arenaRenderer.disposePuddles(puddlesArena.getPuddleVisuals());
            } else if (mapId == 3) {
                MixedArena mixedArena = (MixedArena) arena;
                arenaRenderer.disposeWalls(mixedArena.getWallVisuals());
                arenaRenderer.disposePuddles(mixedArena.getPuddleVisuals());
            }
        }

        if (player1Label != null) player1Label.dispose();
        if (player2Label != null) player2Label.dispose();

        if (heartFull != null) heartFull.dispose();
        if (heartEmpty != null) heartEmpty.dispose();
    }
}
