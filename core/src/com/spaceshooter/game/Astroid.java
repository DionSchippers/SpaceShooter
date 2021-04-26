package com.spaceshooter.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Astroid {
    Texture astroidImg;
    Sprite astroidSprite;
    int xVALint;

    public void setxVALint(int xVALint) {
        this.xVALint = xVALint;
    }

    public int getxVALint() {
        return xVALint;
    }

    public Astroid() {
        int min = 1;
        int max = 1750;
        double xVAL = (Math.random() * ((max - min) + 1)) + min;
        xVALint = (int) xVAL;
        astroidImg = new Texture("Astroid.png");
        astroidSprite = new Sprite(astroidImg);
        astroidSprite.setPosition(xVALint, 1080);
        astroidSprite.translateY(-3f);
    }

    public Sprite getSprite() {
        return this.astroidSprite;
    }
}
