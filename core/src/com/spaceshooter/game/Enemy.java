package com.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {
    SpriteBatch batch;
    TextureAtlas enemyTexture;
    Texture laserImg;
    Sprite enemySprite;
    Sprite laserSprite;
    protected TextureAtlas explosionTexture;
    protected Animation<TextureRegion> explosionAnimation;
    protected Sprite explosion;
    Animation<TextureRegion> enemyAnimation;
    ArrayList<Sprite> laserList;
    int laserTimer;
    Circle c_player;
    Rectangle r_laser;
    boolean shooting;
    boolean exploding;
    Sound explosionSound;
    private float time;


    public Enemy(String img) {
        batch = new SpriteBatch();
        enemyTexture = new TextureAtlas(img);
        laserImg = new Texture("EnemyLaser.png");
        enemyAnimation = new Animation(1f / 30f, enemyTexture.getRegions());
        laserList = new ArrayList<>();
        enemySprite = new Sprite();
        Random rand = new Random();
        int upperbound = Gdx.graphics.getWidth() - 80;
        int int_random = rand.nextInt(upperbound)+40;
        enemySprite.setPosition(int_random, Gdx.graphics.getHeight());
        createLaser(enemySprite.getX());
        laserTimer = 0;
        c_player = new Circle();
        r_laser = new Rectangle();
        shooting = false;
        exploding = false;
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));

        explosionTexture = new TextureAtlas("Explosion.txt");
        explosionAnimation = new Animation(0.15f, explosionTexture.getRegions());
        explosion = new Sprite();
    }

    public void enemyController(float elapsedTime, boolean playing) {
        batch.begin();
        for (Sprite sprite : laserList) {
            sprite.draw(batch);
        }

        if (enemySprite.getY() > Gdx.graphics.getHeight()-120) {
            enemySprite.translateY(-2f);
            shooting = false;
        } else if (enemySprite.getY() < -50) {
            shooting = false;
        } else {
            shooting = true;
        }

        batch.draw(enemyAnimation.getKeyFrame(elapsedTime, true), enemySprite.getX(), enemySprite.getY());
        if (shooting) {
            lasercontroller(playing);

            float x = enemySprite.getX();
            float y = enemySprite.getY();
            c_player.set(x + 48, y + 48, 32);
        }

        if (exploding = true) {
            time += Gdx.graphics.getDeltaTime();
            batch.draw(explosionAnimation.getKeyFrame(time, false), explosion.getX(), explosion.getY());
            if (explosionAnimation.isAnimationFinished(time)) {
                exploding = false;
            }
        }


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
        laserSprite.setPosition(playerSpriteX + 32-laserSprite.getWidth()/2, Gdx.graphics.getHeight()-100);
        laserList.add(laserSprite);
    }

    public boolean colWithLaser(Rectangle r_laser) {
        if (Intersector.overlaps(c_player, r_laser)) {
            shooting = false;
            enemyTexture.dispose();
            return true;
        }
        return false;
    }

    public void explode(float x, float y) {
        explosion.setPosition(x-10, y-10);
        exploding = true;
        time = 1;
        long id = explosionSound.play(0.5f);
        explosionSound.setPitch(id, 1);
        explosionSound.setLooping(id, false);

    }
}
