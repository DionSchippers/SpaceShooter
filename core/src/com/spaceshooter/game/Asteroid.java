package com.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class Asteroid {
    protected Texture texture;
    protected Sprite sprite;
    public Circle c_asteroid;
    int xVALint;

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
        c_asteroid = new Circle();
    }

    public Asteroid() {

        texture = new Texture("Asteroid.png");
        sprite = new Sprite(texture);
        this.redistribute();
    }

    public void destroy() {
        
    }

    public Sprite getSprite() {
        return this.sprite;
    }
    public float getY() { return sprite.getY();}
}
