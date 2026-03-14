package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMap {
    private final int mapId;
    private Texture mapTexture;

    public GameMap(int mapId) {
        this.mapId = mapId;
        switch (mapId) {
            case 1:
                this.mapTexture = new Texture(Gdx.files.internal("map1.png"));
                break;
            case 2:
                mapTexture = new Texture(Gdx.files.internal("map2.png"));
                break;
            case 3:
                mapTexture = new Texture(Gdx.files.internal("map3.png"));
                break;
        }
    }

    // Рисуем карту на весь виртуальный экран
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
