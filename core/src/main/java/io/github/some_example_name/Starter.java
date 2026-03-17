package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.screen.MenuScreen;

public class Starter extends Game {
    public SpriteBatch batch; // рендер - рисует спрайты и текст
    public BitmapFont font; // шрифт

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        // Главное меню.
        setScreen(new MenuScreen(this));
    }

    // Освобождения ресурсов при закрытие.
    @Override
    public void dispose() {
        super.dispose();
        // Освобождаем ресурсы, связанные с рендером.
        batch.dispose();
        font.dispose();
    }
}
