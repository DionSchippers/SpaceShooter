package com.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Player {
    SpriteBatch batch;
    TextureAtlas playerTexture;
    Texture laserImg;
    Sprite playerSprite;
    Sprite laserSprite;
    Animation<TextureRegion> playerAnimation;
    ArrayList<Sprite> laserList;
    int laserTimer;
    Circle c_player;
    Rectangle r_laser;


    public void create(String img) {
        batch = new SpriteBatch();
        playerTexture = new TextureAtlas(img);
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

    public void playerController(float elapsedTime, boolean playing) {
        batch.begin();
        for (Sprite sprite : laserList) {
            sprite.draw(batch);
        }

        batch.draw(playerAnimation.getKeyFrame(elapsedTime, true), playerSprite.getX(), playerSprite.getY());

        lasercontroller(playing);

        float x = playerSprite.getX();
        float y = playerSprite.getY();
        c_player.set(x + 48, y + 48, 48);


        batch.end();
    }

    public int hitboxController(AsteroidManager asteroidManager) {
        if (asteroidManager.colWithLaser(r_laser)) {
            laserList.remove(laserSprite);
            laserSprite.setAlpha(0);
            laserSprite.setPosition(laserSprite.getX(), 10000f);
            return 1000;
        }
        return 0;
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
                playerSprite.translateX(8f);
            }
        }
        if (movingLeft && playing) {
            if (playerSprite.getX() < 0) {
                playerSprite.setX(0);
            } else {
                playerSprite.translateX(-8f);
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

    public void reset() {
        playerSprite.setX(Gdx.graphics.getWidth()/2 - 48);
        laserSprite.setY(10000);
    }

}
