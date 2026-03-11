package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// Простая карта: одна текстура фона, без стен и тайлов.
public class GameMap {
    private final int mapId;          // номер карты (1, 2, 3)
    private Texture mapTexture; // картинка карты

    public GameMap(int mapId) {
        this.mapId = mapId;
        // выбираем текстуру в зависимости от mapId
        switch (mapId) {
            case 1:
                this.mapTexture = new Texture(Gdx.files.internal("map1.png"));
                break;
            case 2:
                //mapTexture = new Texture(Gdx.files.internal("map2.png"));
                break;
            case 3:
                //mapTexture = new Texture(Gdx.files.internal("map3.png"));
                break;
        }
    }

    // Рисуем карту на весь виртуальный экран (ширина/высота задаются снаружи).
    public void render(SpriteBatch batch, float worldWidth, float worldHeight) {
        batch.draw(mapTexture, 0, 0, worldWidth, worldHeight);
    }

    public void dispose() {
        mapTexture.dispose();
    }

    public int getMapId() {
        return mapId;
    }
}
