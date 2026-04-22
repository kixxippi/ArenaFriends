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
    private static final float virtualWidth = 1408f;
    private static final float virtualHeight = 768f;

    private final Starter game;
    private Texture background;

    private OrthographicCamera camera;
    private Viewport viewport;

    private MenuTitle menuTitle;

    public MenuScreen(Starter game) {
        this.game = game;
    }

    @Override
    public void show() {
        // create camera and viewport with fixed virtual size
        camera = new OrthographicCamera();
        viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        viewport.apply();
        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        camera.update();

        background = new Texture(Gdx.files.internal("menu/menu_bg.png"));

        // create title
        menuTitle = new MenuTitle(true);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            game.setScreen(new GameScreen(game, 1));
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            game.setScreen(new GameScreen(game, 2));
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            game.setScreen(new GameScreen(game, 3));
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }

        // update title animation
        if (menuTitle != null) {
            menuTitle.update(delta);
        }

        // clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // set the camera projection
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // draw background on whole virtual screen
        game.batch.draw(background, 0, 0, virtualWidth, virtualHeight);

        // draw title
        if (menuTitle != null) {
            menuTitle.draw(game.batch, virtualWidth, virtualHeight);
        }

        game.font.draw(game.batch, "1 - Start (Map 1)", 60, 120);
        game.font.draw(game.batch, "2 - Start (Map 2)", 60, 90);
        game.font.draw(game.batch, "3 - Start (Map 3)", 60, 60);
        game.font.draw(game.batch, "ESC - Exit", 60, 30);

        game.batch.end();
    }


    // update viewport on window resize
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    // clear
    @Override
    public void dispose() {
        if (background != null) background.dispose();
        if (menuTitle != null) menuTitle.dispose();
    }
}
