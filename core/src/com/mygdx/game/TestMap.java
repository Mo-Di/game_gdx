package com.mygdx.game;

/**
 * Created by qisheng on 2/9/2017.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class TestMap extends ApplicationAdapter implements InputProcessor {

    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    SpriteBatch sb;
    Texture texture;
    Player player;

    @Override public void create () {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();
        tiledMap = new TmxMapLoader().load("campus_map.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);
        sb = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("pik.png"));
        player = new Player(texture, (TiledMapTileLayer) tiledMap.getLayers().get(0));
        player.setCenter(w/2 + 50,h/2-50); //TODO: change position
    }

    @Override public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1); // set background color
        // Don't know what this line does - seems to work without it
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Show the boundary of the map
        camera.update(); // update the position of camera
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(); // draw the map on canvas combined with the previous line

        player.makeMove(player, camera, tiledMap);
        adjustBoundary(tiledMap, camera);
        sb.setProjectionMatrix(camera.combined); // Combine the character with the camera?
        sb.begin();
        player.draw(sb); // draw the character
        sb.end();
    }

    public void adjustBoundary(TiledMap tiledMap, OrthographicCamera cam) {
        // These values likely need to be scaled according to your world coordinates.
        // The left boundary of the map (x)
        MapProperties prop = tiledMap.getProperties();
        int mapWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        int mapHeight = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);

        float mapLeft = 0;
        float mapBottom = 0;

        // The camera dimensions, halved
        float cameraHalfWidth = cam.viewportWidth * .5f;
        float cameraHalfHeight = cam.viewportHeight * .5f;

        // Move camera after player as normal

        float cameraLeft = cam.position.x - cameraHalfWidth;
        float cameraRight = cam.position.x + cameraHalfWidth;
        float cameraBottom = cam.position.y - cameraHalfHeight;
        float cameraTop = cam.position.y + cameraHalfHeight;

        // Horizontal axis
        if(cameraLeft + player.getSpeed() <= mapLeft)
        {
            cam.position.x = mapLeft + cameraHalfWidth;
        }
        else if(cameraRight - player.getSpeed() >= mapWidth)
        {
            cam.position.x = mapWidth - cameraHalfWidth;
        }

        if(cameraBottom + player.getSpeed() <= mapBottom)
        {
            cam.position.y = mapBottom + cameraHalfHeight;
        }
        else if(cameraTop - player.getSpeed() >= mapHeight)
        {
            cam.position.y = mapHeight - cameraHalfHeight;
        }
    }

    // Called when a key was pressed
    @Override public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            player.setMovingRight(true);
        }
        if(keycode == Input.Keys.LEFT) {
            player.setMovingLeft(true);
        }
        if(keycode == Input.Keys.UP) {
            player.setMovingUp(true);
        }
        if(keycode == Input.Keys.DOWN) {
            player.setMovingDown(true);
        }
        return false;
    }

    // Called when a key was released
    @Override public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            player.setMovingRight(false);
        }
        if(keycode == Input.Keys.LEFT) {
            player.setMovingLeft(false);
        }
        if(keycode == Input.Keys.UP) {
            player.setMovingUp(false);
        }
        if(keycode == Input.Keys.DOWN) {
            player.setMovingDown(false);
        }
        return false;
    }

    @Override public boolean keyTyped(char character) {
        return false;
    }

    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {return true;}

    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override public boolean scrolled(int amount) {
        return false;
    }
}