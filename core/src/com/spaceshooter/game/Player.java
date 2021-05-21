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
import java.util.Iterator;

public class Player {
    SpriteBatch batch;
    TextureAtlas playerTexture;
    Sprite playerSprite;
    Animation<TextureRegion> playerAnimation;
    ArrayList<Laser> laserList;
    int laserTimer;
    Circle c_player;
    int hp;
    Sound damageSound;
    int score = 0;
    boolean dead = false;
    int powerup = 0;
    float bulletspeed = 50f;
    int tpr = 20;


    public void create(String img) {
        batch = new SpriteBatch();
        playerTexture = new TextureAtlas(img);
        playerAnimation = new Animation(1f / 30f, playerTexture.getRegions());
        laserList = new ArrayList<Laser>();
        playerSprite = new Sprite();
        playerSprite.setPosition(Gdx.graphics.getWidth() / 2 - playerSprite.getWidth() / 2, 20);
        laserTimer = 0;
        c_player = new Circle();
        hp = 3;
        damageSound= Gdx.audio.newSound(Gdx.files.internal("PlayerDmg.ogg"));
    }

    public void playerController(float elapsedTime, boolean playing) {
        batch.begin();
        if (!dead) {
            for (Laser laser : laserList) {
                laser.laserSprite.draw(batch);
            }

            batch.draw(playerAnimation.getKeyFrame(elapsedTime, true), playerSprite.getX(), playerSprite.getY());

            lasercontroller(playing);

            float x = playerSprite.getX();
            float y = playerSprite.getY();
            c_player.set(x + 48, y + 48, 48);
            if (powerup == 0) {
                bulletspeed = 50f;
            } else if (powerup == 1) {
                bulletspeed = 100f;
            } else if (powerup == 2) {
                bulletspeed = 10f;

            }
        }
        batch.end();
    }

    public int hitboxController(AsteroidManager asteroidManager, EnemyManager enemyManager, PowerupManager powerupManager) {
        int points = 0;
        for (Laser laser: laserList) {
            if (asteroidManager.colWithLaser(laser.r_laser)) {
                laserList.remove(laser.laserSprite);
                laser.reset();
                points += 1000;
            }
            if (enemyManager.colWithLaser(laser.r_laser)) {
                laserList.remove(laser.laserSprite);
                laser.reset();
                points += 2000;
            }
            if (powerupManager.colWithLaser(laser.r_laser, this)) {
                laserList.remove(laser.laserSprite);
                laser.reset();
                points += 2000;
            }
        }
        Iterator<Laser> iterator = laserList.iterator();
        for (int i = 0; i < laserList.size(); i++) {
        Laser laser = laserList.get(i);
            if (laser.laserSprite.getY() > Gdx.graphics.getHeight()) {
                laserList.remove(i--);
            }
        }
        return points;
    }

    public void dispose() {
        playerTexture.dispose();
        for (Laser laser: laserList) {
            laser.laserImg.dispose();
        }
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
        for (Laser laser: laserList) {
            laser.lasercontroller(playing);
        }
        while (laserTimer > tpr) {
            switch (powerup){
                case 0:
                    laserList.add(new Laser(0f, 50f, "laserBeam1.png", playerSprite.getX()));
                    tpr = 20;
                    break;
                case 1:
                    laserList.add(new Laser(0f, 50f, "laserBeam1.png", playerSprite.getX()));
                    tpr = 5;
                    break;
                case 2:
                    laserList.add(new Laser(2f, 20f, "laserBeam1.png", playerSprite.getX()));
                    laserList.add(new Laser(0f, 20f, "laserBeam1.png", playerSprite.getX()));
                    laserList.add(new Laser(-2f, 20f, "laserBeam1.png", playerSprite.getX()));
                    tpr = 1;
                    break;
            }
            laserTimer = 0;
        }
        laserTimer++;
    }


    public void reset() {
        playerSprite.setX(Gdx.graphics.getWidth() / 2 - 48);
        hp = 3;
        score = 0;
        dead = false;
        for (Laser laser: laserList) {
            laser.laserSprite.setY(10000);
        }
        powerup = 0;
        laserList.clear();
    }

    public boolean colWithLaser(Rectangle r_laser) {
        if (!dead) {
            if (Intersector.overlaps(c_player, r_laser)) {
                hp--;
                long id = damageSound.play(0.5f);
                damageSound.setPitch(id, 1);
                damageSound.setLooping(id, false);
                return true;
            }
        }
        return false;
    }



}
