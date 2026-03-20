package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    public GameScreen(Starter game, int mapId) {
        this.game = game;
        this.mapId = mapId;
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
            sword1);

        p2 = new Player(virtualWidth - 140, virtualHeight - 140, 64, 64,
            "players/player2_right.png",
            "players/player2_left.png",
            sword2);

        // create arena and renderer
        arena = ArenaFactory.createArena(mapId, virtualWidth, virtualHeight);
        arenaRenderer = new ArenaRenderer();
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

        game.font.draw(game.batch, "Map: " + mapId, 10, virtualHeight - 10);
        game.font.draw(game.batch, "P1 HP: " + p1.getHp() + "/" + p1.getMaxHp(), 10, virtualHeight - 30);
        game.font.draw(game.batch, "P2 HP: " + p2.getHp() + "/" + p2.getMaxHp(), 10, virtualHeight - 50);
        game.font.draw(game.batch, "P1: WASD + SPACE (attack)", 10, virtualHeight - 70);
        game.font.draw(game.batch, "P2: IJKL + Right ALT (attack)", 10, virtualHeight - 90);
        game.font.draw(game.batch, "ESC - Back to menu", 10, virtualHeight - 110);

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
    }
}
