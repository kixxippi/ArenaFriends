package io.github.some_example_name.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameMap {
    private final int mapId;
    private Texture mapTexture;

    public GameMap(int mapId) {
        this(mapId, true);
    }

    public GameMap(int mapId, boolean loadTexture) {
        this.mapId = mapId;

        if (!loadTexture) {
            mapTexture = null;
            return;
        }

        switch (mapId) {
            case 1:
                mapTexture = new Texture(Gdx.files.internal("map/map1.png"));
                break;
            case 2:
                mapTexture = new Texture(Gdx.files.internal("map/map2.png"));
                break;
            case 3:
                mapTexture = new Texture(Gdx.files.internal("map/map3.png"));
                break;
            default:
                mapTexture = null;
        }
    }

    public void render(SpriteBatch batch, float worldWidth, float worldHeight) {
        if (mapTexture == null) return;
        batch.draw(mapTexture, 0, 0, worldWidth, worldHeight);
    }

    public void dispose() {
        if (mapTexture != null) mapTexture.dispose();
    }

    public int getMapId() {
        return mapId;
    }
}
