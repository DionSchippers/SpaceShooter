package com.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Player {
    TextureAtlas playerTexture;
    Texture laserImg;
    Sprite playerSprite;
    Sprite laserSprite;
    Animation<TextureRegion> playerAnimation;
    ArrayList<Sprite> laserList;
    int laserTimer;
    Circle c_player;
    Rectangle r_laser;

    public void create() {
        playerTexture = new TextureAtlas("SpaceShip2.txt");
        laserImg = new Texture("laserbeam1.png");
        playerAnimation = new Animation(1f/30f, playerTexture.getRegions());
        laserList = new ArrayList<>();
        playerSprite = new Sprite();
        playerSprite.setPosition(Gdx.graphics.getWidth() / 2 - playerSprite.getWidth() / 2, 20);
        createLaser(playerSprite.getX());
        laserTimer = 0;
        c_player = new Circle();
        r_laser = new Rectangle();
    }
    public void dispose() {
        playerTexture.dispose();
        laserImg.dispose();
    }

    public void move(boolean movingRight, boolean movingLeft, boolean playing) {
        if (movingRight && playing) {
            if (playerSprite.getX() > Gdx.graphics.getWidth() - 96) {
                playerSprite.setX(Gdx.graphics.getWidth() - 96);
            } else {
                playerSprite.translateX(15f);
            }
        }
        if (movingLeft && playing) {
            if (playerSprite.getX() < 0) {
                playerSprite.setX(0);
            } else {
                playerSprite.translateX(-15f);
            }
        }
    }

    public void lasercontroller(boolean playing) {
        if (laserSprite.getY() > Gdx.graphics.getHeight()) {
            laserList.remove(laserSprite);
            laserSprite.setAlpha(0);
        } else if (playing) {
            laserSprite.translateY(50f);
            r_laser.setPosition(laserSprite.getX(),laserSprite.getY());
        }
        laserTimer++;
        while (laserTimer > 20) {
            createLaser(playerSprite.getX());
            r_laser.set(laserSprite.getX()-laserSprite.getWidth()/2, laserSprite.getY()-laserSprite.getHeight()/2, laserSprite.getWidth(), laserSprite.getHeight());
            laserTimer = 0;
        }
    }

    public void createLaser(float playerSpriteX) {
        laserSprite = new Sprite(laserImg);
        laserSprite.setPosition(playerSpriteX + 32, 52);
        laserList.add(laserSprite);
    }

}
