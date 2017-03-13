package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Shuni on 2/25/17.
 */
public class Player extends Sprite {

    private TiledMapTileLayer collisionLayer;
    private boolean movingRight;
    private boolean movingLeft;
    private boolean movingUp;
    private boolean movingDown;
    private float speed = 3f;
//    Skin skin;
//
//    Dialog dialog = new Dialog("Warning", skin) {
//        public void result(Object obj) {
//            System.out.println("result "+obj);
//        }
//    };

    public Player(Texture texture, TiledMapTileLayer collisionLayer) {
        super(texture);
        this.collisionLayer = collisionLayer;
    }

    public void makeMove(Player player, TiledMap tiledMap) {
        MapLayer layer = tiledMap.getLayers().get("CollisionBoxes");
        MapObjects boxes = layer.getObjects();
        Array<Rectangle> collisionRects = new Array<Rectangle>();
        for (MapObject box : boxes) {
            if (box instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) box).getRectangle();
                collisionRects.add(rect);
            }
        }
        if(movingRight) {
            player.translateX(speed);
            if(checkArrayOverlap(player,collisionRects)) {
                popUpMessage(player,tiledMap);
                player.translateX(-speed);
            }
        }
        else if(movingLeft) {
            player.translateX(-speed);
            if(checkArrayOverlap(player,collisionRects)) {
                popUpMessage(player,tiledMap);
                player.translateX(speed);
            }
        }
        else if(movingUp) {
            player.translateY(speed);
            if(checkArrayOverlap(player,collisionRects)) {
                popUpMessage(player,tiledMap);
                player.translateY(-speed);
            }
        }
        else if(movingDown) {
            player.translateY(-speed);
            if(checkArrayOverlap(player,collisionRects)) {
                popUpMessage(player,tiledMap);
                player.translateY(speed);
            }
        }
    }
    public boolean checkArrayOverlap(Player player, Array<Rectangle> rects) {
        for (Rectangle rect : rects) {
            if (checkOverlap(player, rect)){
                return true;
            }
        }
        return false;
    }

    public boolean checkOverlap(Player player, Rectangle rec2) {
        float p1x = player.getX();
        float p1y = player.getY() + player.getHeight();
        float p2x = player.getX() + player.getWidth();
        float p2y = player.getY();

        float p3x = rec2.getX();
        float p3y = rec2.getY() + rec2.getHeight();
        float p4x = rec2.getX() + rec2.getWidth();
        float p4y = rec2.getY();
        return (! ( (p2x < p3x) || (p1y < p4y) || (p1x > p4x) || (p2y > p3y)));
    }

    public void popUpMessage(Player player, TiledMap tiledMap) {
        MapLayer layer = tiledMap.getLayers().get("Doors");
        MapObjects boxes = layer.getObjects();
        Array<Rectangle> doorRects = new Array<Rectangle>();
        for (MapObject box : boxes) {
            if (box instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) box).getRectangle();
                doorRects.add(rect);
            }
        }
        if(checkArrayOverlap(player,doorRects)) {
//            dialog.text("Are you sure you want to quit?");
//            dialog.button("Yes", true); //sends "true" as the result
//            dialog.button("No", false);  //sends "false" as the result
//            dialog.show();
            System.out.println("Overlap!");
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }
}
