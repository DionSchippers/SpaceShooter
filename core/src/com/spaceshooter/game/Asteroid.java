package com.spaceshooter.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Asteroid {
    protected Texture texture;
    protected Sprite sprite;
    int xVALint;

    public void setxVALint(int xVALint) {
        this.xVALint = xVALint;
    }

    public int getxVALint() {
        return xVALint;
    }

    public void redistribute() {
        int min = 1;
        int max = 1750;
        double xVAL = (Math.random() * ((max - min) + 1)) + min;
        xVALint = (int) xVAL;
        sprite.setPosition(xVALint, 600);
    }

    public Asteroid() {

        texture = new Texture("Asteroid.png");
        sprite = new Sprite(texture);
        this.redistribute();
    }

    public Sprite getSprite() {
        return this.sprite;
    }
}
