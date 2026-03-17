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

// Экран игры: фоновая картинка (карта) + два игрока.
public class GameScreen extends ScreenAdapter {
    private static final float VIRTUAL_WIDTH = 1408f;
    private static final float VIRTUAL_HEIGHT = 768f;

    private final Starter game;
    private final int mapId;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Player p1;
    private Player p2;

    private GameMap map;

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

        // создаём карту по номеру
        map = new GameMap(mapId);

        // создаём мечи для игроков
        Sword sword1 = new Sword(10, 500); // 10 урона, 500 мс кулдаун
        Sword sword2 = new Sword(10, 500);

        // ставим игроков на стартовые позиции, передаём пути к картинкам и меч
        p1 = new Player(80, 80, 64, 64,
            "player1_right.png",
            "player1_left.png",
            sword1);

        p2 = new Player(480, 320, 64, 64,
            "player2_right.png",
            "player2_left.png",
            sword2);
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

        // задаём матрицу для batch
        game.batch.setProjectionMatrix(camera.combined);

        // рисуем карту и игроков
        game.batch.begin();
        // фон карты
        map.render(game.batch, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        // игроки
        p1.render(game.batch);
        p2.render(game.batch);

        // HUD
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

        // движение через методы Player
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

    // Ограничиваем игрока границами мира
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
        if (map != null) map.dispose();
        if (p1 != null) p1.dispose();
        if (p2 != null) p2.dispose();
    }
}
