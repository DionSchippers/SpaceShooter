package com.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Enemy {
    SpriteBatch batch;
    TextureAtlas enemyTexture;
    Texture laserImg;
    Sprite enemySprite;
    Sprite laserSprite;
    Animation<TextureRegion> enemyAnimation;
    ArrayList<Sprite> laserList;
    int laserTimer;
    Circle c_player;
    Rectangle r_laser;


    public void create(String img) {
        batch = new SpriteBatch();
        enemyTexture = new TextureAtlas(img);
        laserImg = new Texture("laserbeam1.png");
        enemyAnimation = new Animation(1f / 30f, enemyTexture.getRegions());
        laserList = new ArrayList<>();
        enemySprite = new Sprite();
        enemySprite.setPosition(Gdx.graphics.getWidth() / 2 - enemySprite.getWidth() / 2, Gdx.graphics.getHeight()-120);
        createLaser(enemySprite.getX());
        laserTimer = 0;
        c_player = new Circle();
        r_laser = new Rectangle();
        enemySprite.rotate(180f);
    }

    public void enemyController(float elapsedTime, boolean playing) {
        batch.begin();
        for (Sprite sprite : laserList) {
            sprite.draw(batch);
        }

        batch.draw(enemyAnimation.getKeyFrame(elapsedTime, true), enemySprite.getX(), enemySprite.getY());

        lasercontroller(playing);

        float x = enemySprite.getX();
        float y = enemySprite.getY();
        c_player.set(x + 48, y + 48, 48);


        batch.end();
    }

    public void hitboxController(Player player) {
        if (player.colWithLaser(r_laser)) {
            laserList.remove(laserSprite);
            laserSprite.setAlpha(0);
            laserSprite.setPosition(laserSprite.getX(), -10000f);
        }
    }

    public void lasercontroller(boolean playing) {
        if (laserSprite.getY() > Gdx.graphics.getHeight()) {
            laserList.remove(laserSprite);
            laserSprite.setAlpha(0);
        } else if (playing) {
            laserSprite.translateY(-30f);
            r_laser.setPosition(laserSprite.getX(), laserSprite.getY());
        }
        laserTimer++;
        while (laserTimer > 40) {
            createLaser(enemySprite.getX());
            r_laser.set(laserSprite.getX() - laserSprite.getWidth() / 2, laserSprite.getY() - laserSprite.getHeight() / 2, laserSprite.getWidth(), laserSprite.getHeight());
            laserTimer = 0;
        }
    }

    public void createLaser(float playerSpriteX) {
        laserSprite = new Sprite(laserImg);
        laserSprite.setPosition(playerSpriteX + 32, Gdx.graphics.getHeight()-100);
        laserList.add(laserSprite);
    }
}
