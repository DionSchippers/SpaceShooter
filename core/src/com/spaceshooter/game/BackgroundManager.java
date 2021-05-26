package com.spaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class BackgroundManager {
    private int numStars;
    private ArrayList<Star> stars;

    public BackgroundManager(int numStars) {
        this.numStars = numStars;

        this.stars = new ArrayList<Star>();

        for (int i =0; i< numStars; i++) {
            this.stars.add(new Star());
        }
    }

    public void redestribute() {
        for (Star star: stars) {
            star.redistribute();
        }
    }

    public boolean hasGoneOffscreen(Star star) {
        if (star.getSprite().getY() < 0) {
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch, boolean playing, int score) {
        for (Star star : stars) {
            if (hasGoneOffscreen(star)) {
                star.redistribute();
            }
            star.getSprite().translateY(-1f * (1f + (float)Math.sqrt((float)score/5000)));
            star.getSprite().draw(batch);
            float x = star.xVALint;
            float y = star.getY();
        }
    }

    public void reset() {
        for (Star star : stars) {
            star.redistribute();
        }
    }
}
