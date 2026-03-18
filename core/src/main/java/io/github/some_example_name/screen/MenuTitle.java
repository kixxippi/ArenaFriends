package io.github.some_example_name.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuTitle {

    // настройки заголовка
    private static final float titleScale = 0.6f;
    private static final float titleTopMargin = 20f;
    private static final float titleShakeAmplitude = 5f;
    private static final float titleShakeSpeed = 4f;

    private final Texture titleTexture;
    private float time = 0f;

    public MenuTitle() {
        titleTexture = new Texture(Gdx.files.internal("menu/menu_title.png"));
    }

    public void update(float delta) {
        time += delta;
    }

    public void draw(SpriteBatch batch, float virtualWidth, float virtualHeight) {
        float titleWidth = titleTexture.getWidth() * titleScale; // new size
        float titleHeight = titleTexture.getHeight() * titleScale;

        float titleX = (virtualWidth - titleWidth) / 2f; // in the centre of the screen along the X-axis

        float baseTitleY = virtualHeight - titleHeight - titleTopMargin; // origin position along the Y-axis
        float shakeOffset = (float) Math.sin(time * titleShakeSpeed) * titleShakeAmplitude;
        float titleY = baseTitleY + shakeOffset; // final position along the Y-axis

        batch.draw(titleTexture, titleX, titleY, titleWidth, titleHeight);
    }

    // clear
    public void dispose() {
        titleTexture.dispose();
    }
}
