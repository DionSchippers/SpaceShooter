package com.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Circle;

import java.util.concurrent.ThreadLocalRandom;

public class Powerup {
    protected Texture texture;
    protected Sprite sprite;
    protected TextureAtlas explosionTexture;
    protected Animation<TextureRegion> explosionAnimation;
    protected Sprite explosion;
    public Circle c_powerup;
    int xVALint;
    int yVALint;
    private float time;
    boolean exploding;
    Sound explosionSound;
    int type;

    public void setxVALint(int xVALint) {
        this.xVALint = xVALint;
    }

    public int getxVALint() {
        return xVALint;
    }

    public void redistribute() {
        int min = 1;
        int max = Gdx.graphics.getWidth() - 10;
        double xVAL = (Math.random() * ((max - min) + 1)) + min;
        xVALint = (int) xVAL;
        int screenTop = Gdx.graphics.getHeight();
        sprite.setPosition(xVALint, screenTop);
        c_powerup = new Circle();
        exploding = false;
    }

    public void spawn() {
        int min = 1000;
        int max = Gdx.graphics.getWidth() - 10;
        double xVAL = (Math.random() * ((max - min) + 1)) + min;
        double yVAL = (Math.random() * ((Gdx.graphics.getHeight() - 11) + 1)) + min;
        xVALint = (int) xVAL;
        yVALint = (int) yVAL;
        int screenTop = Gdx.graphics.getHeight();
        sprite.setPosition(xVALint, yVALint);
        c_powerup = new Circle();
        exploding = false;
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
        type = ThreadLocalRandom.current().nextInt(1, 2 + 1);
    }

    public Powerup() {

        texture = new Texture("Powerup.png");
        sprite = new Sprite(texture);
        explosionTexture = new TextureAtlas("Explosion.txt");
        explosionAnimation = new Animation(0.15f, explosionTexture.getRegions());
        explosion = new Sprite();
        this.spawn();
    }

    public void render(Batch batch) {
        if (exploding = true) {
            time += Gdx.graphics.getDeltaTime();
            batch.draw(explosionAnimation.getKeyFrame(time, false), explosion.getX(), explosion.getY());
            if (explosionAnimation.isAnimationFinished(time)) {
                exploding = false;
            }
        }
    }

    public void explode(float x, float y) {
        explosion.setPosition(x - 10, y - 10);
        exploding = true;
        time = 1;
        long id = explosionSound.play(0.5f);
        explosionSound.setPitch(id, 1);
        explosionSound.setLooping(id, false);

    }

    public void destroy() {

    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public float getY() {
        return sprite.getY();
    }

    public Sprite getExplosion() {
        return this.explosion;
    }
}
