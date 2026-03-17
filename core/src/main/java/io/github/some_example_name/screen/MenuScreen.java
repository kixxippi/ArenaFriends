package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.some_example_name.Starter;

public class MenuScreen extends ScreenAdapter {
    // "Виртуальный" размер мира (не меняется при ресайзе окна)
    private static final float VIRTUAL_WIDTH = 1408f;
    private static final float VIRTUAL_HEIGHT = 768f;

    private static final float TITLE_SCALE = 0.6f; // масштаб заголовка
    private static final float TITLE_TOP_MARGIN = 20f; // двигает заголовок вверх/вниз
    private static final float TITLE_SHAKE_AMPLITUDE = 5f; // насколько сильно качается по Y
    private static final float TITLE_SHAKE_SPEED = 4f; // скорость покачивания

    private final Starter game;
    private Texture background;    // картинка фона
    private Texture titleTexture;  // картинка с названием игры

    private OrthographicCamera camera; // камера 2D
    private Viewport viewport; // масштабирует мир под размер окна

    private float titleTime = 0f; // внутренний таймер для анимации покачивания

    public MenuScreen(Starter game) {
        this.game = game;
    }

    @Override
    public void show() {
        // создаём камеру и viewport с фиксированным виртуальным размером
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        // загружаем текстуры из assets
        background = new Texture(Gdx.files.internal("menu_bg.png"));
        titleTexture = new Texture(Gdx.files.internal("menu_title.png"));
    }

    @Override
    public void render(float delta) {
        // обработка ввода
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setScreen(new GameScreen(game, 1)); // карта 1
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.setScreen(new GameScreen(game, 2)); // карта 2
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            game.setScreen(new GameScreen(game, 3)); // карта 3
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }

        titleTime += delta; // накапливаем время для покачивания заголовка

        // очистка экрана
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // задаём батчу матрицу камеры (чтобы работал viewport)
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // фон: растягиваем на весь виртуальный экран
        game.batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        // заголовок по центру сверху
        float titleWidth = titleTexture.getWidth() * TITLE_SCALE;
        float titleHeight = titleTexture.getHeight() * TITLE_SCALE;

        float titleX = (VIRTUAL_WIDTH - titleWidth) / 2f;

        // базовая позиция по Y (без покачивания)
        float baseTitleY = VIRTUAL_HEIGHT - titleHeight - TITLE_TOP_MARGIN;

        // вертикальное покачивание по синусоиде
        float shakeOffset = (float) Math.sin(titleTime * TITLE_SHAKE_SPEED) * TITLE_SHAKE_AMPLITUDE;

        float titleY = baseTitleY + shakeOffset;

        // рисуем с заданным масштабом
        game.batch.draw(titleTexture, titleX, titleY, titleWidth, titleHeight);

        // текст пунктов меню
        game.font.draw(game.batch, "1 - Start (Map 1)", 60, 120);
        game.font.draw(game.batch, "2 - Start (Map 2)", 60, 90);
        game.font.draw(game.batch, "3 - Start (Map 3)", 60, 60);
        game.font.draw(game.batch, "ESC - Exit", 60, 30);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // пересчитываем viewport при изменении размера окна
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        // освобождаем текстуры
        if (background != null) background.dispose();
        if (titleTexture != null) titleTexture.dispose();
    }
}
