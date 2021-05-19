package com.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    int hp;
    Sound damageSound;


    public void create(String img) {
        batch = new SpriteBatch();
        playerTexture = new TextureAtlas(img);
        laserImg = new Texture("laserbeam1.png");
        playerAnimation = new Animation(1f / 30f, playerTexture.getRegions());
        laserList = new ArrayList<>();
        playerSprite = new Sprite();
        playerSprite.setPosition(Gdx.graphics.getWidth() / 2 - playerSprite.getWidth() / 2, 20);
        createLaser(playerSprite.getX());
        laserTimer = 0;
        c_player = new Circle();
        r_laser = new Rectangle();
        hp = 3;
        damageSound= Gdx.audio.newSound(Gdx.files.internal("PlayerDmg.ogg"));
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

    public int hitboxController(AsteroidManager asteroidManager, EnemyManager enemyManager) {
        if (asteroidManager.colWithLaser(r_laser)) {
            laserList.remove(laserSprite);
            laserSprite.setAlpha(0);
            laserSprite.setPosition(laserSprite.getX(), 10000f);
            return 1000;
        }
        if (enemyManager.colWithLaser(r_laser)) {
            laserList.remove(laserSprite);
            laserSprite.setAlpha(0);
            laserSprite.setPosition(laserSprite.getX(), 10000f);
            return 2000;
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
                playerSprite.translateX(7f);
            }
        }
        if (movingLeft && playing) {
            if (playerSprite.getX() < 0) {
                playerSprite.setX(0);
            } else {
                playerSprite.translateX(-7f);
            }
        }
    }

    public void lasercontroller(boolean playing) {
        if (laserSprite.getY() > Gdx.graphics.getHeight()) {
            laserList.remove(laserSprite);
            laserSprite.setAlpha(0);
        } else if (playing) {
            laserSprite.translateY(50f);
            r_laser.setPosition(laserSprite.getX(), laserSprite.getY());
        }
        laserTimer++;
        while (laserTimer > 20) {
            createLaser(playerSprite.getX());
            r_laser.set(laserSprite.getX() - laserSprite.getWidth() / 2, laserSprite.getY() - laserSprite.getHeight() / 2, laserSprite.getWidth(), laserSprite.getHeight());
            laserTimer = 0;
        }
    }

    public void createLaser(float playerSpriteX) {
        laserSprite = new Sprite(laserImg);
        laserSprite.setPosition(playerSpriteX + 32, 52);
        laserList.add(laserSprite);
    }

    public void reset() {
        playerSprite.setX(Gdx.graphics.getWidth() / 2 - 48);
        laserSprite.setY(10000);
        hp = 3;
    }

    public boolean colWithLaser(Rectangle r_laser) {
        if (Intersector.overlaps(c_player, r_laser)) {
            hp--;
            long id = damageSound.play(0.5f);
            damageSound.setPitch(id, 1);
            damageSound.setLooping(id, false);
            return true;
        }
        return false;
    }



}
