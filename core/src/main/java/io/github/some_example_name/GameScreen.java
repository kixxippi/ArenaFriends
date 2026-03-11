package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Экран игры: фоновая картинка (карта) + два игрока.
public class GameScreen extends ScreenAdapter {
    private static final float VIRTUAL_WIDTH = 640f;
    private static final float VIRTUAL_HEIGHT = 480f;

    private final Starter game;
    private final int mapId;

    private OrthographicCamera camera;
    private Viewport viewport;

    private ShapeRenderer shape;

    private Player p1;
    private Player p2;

    private GameMap map; // простая карта-картинка

    public GameScreen(Starter game, int mapId) {
        this.game = game;
        this.mapId = mapId;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        shape = new ShapeRenderer();

        // создаём карту по номеру
        map = new GameMap(mapId);

        // ставим игроков на стартовые позиции
        p1 = new Player(80, 80, 24, 24);
        p2 = new Player(480, 320, 24, 24);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        // выход в меню
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        Gdx.gl.glClearColor(0.12f, 0.12f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // задаём матрицу для batch и shape
        game.batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        // рисуем карту (фон)
        game.batch.begin();
        map.render(game.batch, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        game.batch.end();

        // рисуем игроков
        shape.begin(ShapeRenderer.ShapeType.Filled);

        // игрок 1
        shape.setColor(0f, 0.8f, 1f, 1f);
        shape.rect(p1.getX(), p1.getY(), p1.getWidth(), p1.getHeight());

        // игрок 2
        shape.setColor(1f, 0.3f, 0.3f, 1f);
        shape.rect(p2.getX(), p2.getY(), p2.getWidth(), p2.getHeight());

        shape.end();

        // HUD
        game.batch.begin();
        game.font.draw(game.batch, "Map: " + mapId, 10, VIRTUAL_HEIGHT - 10);
        game.font.draw(game.batch, "P1 HP: " + p1.getHp() + "/" + p1.getMaxHp(), 10, VIRTUAL_HEIGHT - 30);
        game.font.draw(game.batch, "P2 HP: " + p2.getHp() + "/" + p2.getMaxHp(), 10, VIRTUAL_HEIGHT - 50);
        game.font.draw(game.batch, "P1: WASD + SPACE (attack)", 10, VIRTUAL_HEIGHT - 70);
        game.font.draw(game.batch, "P2: IJKL + Right ALT (attack)", 10, VIRTUAL_HEIGHT - 90);
        game.font.draw(game.batch, "ESC - Back to menu", 10, VIRTUAL_HEIGHT - 110);
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

        p1.move(dx1, dy1, dt);
        p2.move(dx2, dy2, dt);

        // ограничиваем поле движения экраном
        clampPlayerToWorld(p1);
        clampPlayerToWorld(p2);

        // атака P1
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            p1.attack(p2);
        }

        // атака P2
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_RIGHT)) {
            p2.attack(p1);
        }
    }

    // Ограничиваем игрока границами "мира".
    private void clampPlayerToWorld(Player p) {
        float x = MathUtils.clamp(p.getX(), 0, VIRTUAL_WIDTH - p.getWidth());
        float y = MathUtils.clamp(p.getY(), 0, VIRTUAL_HEIGHT - p.getHeight());
        p.setPosition(x, y);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        shape.dispose();
        if (map != null) map.dispose();
    }
}
