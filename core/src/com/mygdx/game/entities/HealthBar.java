package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


/**
 * Created by Zhaoqi on 2017/4/3.
 */
public class HealthBar extends Group {

    private Image barImg;
    private Image healthBarImg;
    private Label lifeLabel;
    private CampusMap map;
    private final float HORIZONTAL_MARGIN = 250;
    private final float VERTICAL_MARGIN = 70;

    public HealthBar(Texture bar, Texture healthBar, Label life_label, CampusMap map) {
        this.barImg = new Image(bar);
        this.healthBarImg = new Image(healthBar);
        this.lifeLabel = life_label;
        this.map = map;
    }

    public void initBar() {
        lifeLabel.setFontScale(3);
        barImg.setPosition(0, 0);
        healthBarImg.setPosition(19, 6);
        lifeLabel.setPosition(50, -15);
        this.addActor(lifeLabel);
        this.addActor(barImg);
        this.addActor(healthBarImg);
    }

    public void updateBar(Player player, OrthographicCamera camera) {
        lifeLabel.setText("Life: "+Player.health);
        healthBarImg.setWidth(177*Player.health/Player.TOTALHEALTH);
        healthBarImg.setHeight(21);
        this.setPosition(player.getX()+camera.viewportWidth/2-HORIZONTAL_MARGIN,
                player.getY()+camera.viewportHeight/2-VERTICAL_MARGIN);
        this.setOrigin(barImg.getWidth()/2,barImg.getHeight()/2);
    }

    //TODO: Refactor
    public void adjustBoundary(Player player, OrthographicCamera camera) {

        boolean outOfBound = false;

        float mapLeft = 0;
        float mapBottom = 0;
        float mapWidth = map.mapWidth;
        float mapHeight = map.mapHeight;

        // The camera dimensions, halved
        float cameraHalfWidth = camera.viewportWidth * .5f;
        float cameraHalfHeight = camera.viewportHeight * .5f;

        float cameraLeft = camera.position.x - cameraHalfWidth;
        float cameraRight = camera.position.x + cameraHalfWidth;
        float cameraBottom = camera.position.y - cameraHalfHeight;
        float cameraTop = camera.position.y + cameraHalfHeight;

        if (cameraRight + Player.SPEED >= mapWidth) {
            this.setX(mapWidth - HORIZONTAL_MARGIN);
            outOfBound = true;
        }
        if (cameraTop + Player.SPEED >= mapHeight) {
            this.setY(mapHeight - VERTICAL_MARGIN);
            outOfBound = true;
        }
        if (cameraLeft - Player.SPEED <= mapLeft) {
            this.setX(cameraHalfWidth * 2 - HORIZONTAL_MARGIN);
            outOfBound = true;
        }
        if (cameraBottom - Player.SPEED <= mapBottom) {
            this.setY(cameraHalfHeight * 2 - VERTICAL_MARGIN);
            outOfBound = true;
        }
        if (!outOfBound) {
            this.setPosition(player.getX()+camera.viewportWidth/2-HORIZONTAL_MARGIN,
                    player.getY()+camera.viewportHeight/2-VERTICAL_MARGIN);
        }
    }
}

//    public void adjustBoundary(OrthographicCamera cam) {
//
//        float mapLeft = 0;
//        float mapBottom = 0;
//
//        // The camera dimensions, halved
//        float cameraHalfWidth = cam.viewportWidth * .5f;
//        float cameraHalfHeight = cam.viewportHeight * .5f;
//
//        float cameraLeft = cam.position.x - cameraHalfWidth;
//        float cameraRight = cam.position.x + cameraHalfWidth;
//        float cameraBottom = cam.position.y - cameraHalfHeight;
//        float cameraTop = cam.position.y + cameraHalfHeight;
//
//        if(cameraLeft + Player.SPEED <= mapLeft) {
//            cam.position.x = mapLeft + cameraHalfWidth;
//        }
//        else if(cameraRight - Player.SPEED >= mapWidth) {
//            cam.position.x = mapWidth - cameraHalfWidth;
//        }
//
//        if(cameraBottom + Player.SPEED <= mapBottom) {
//            cam.position.y = mapBottom + cameraHalfHeight;
//        }
//        else if(cameraTop - Player.SPEED >= mapHeight) {
//            cam.position.y = mapHeight - cameraHalfHeight;
//        }
//}