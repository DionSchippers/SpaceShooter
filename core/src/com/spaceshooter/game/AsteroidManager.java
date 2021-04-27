package com.spaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class AsteroidManager {
    private int numAsteroids;
    private ArrayList<Asteroid> asteroids;

    public AsteroidManager(int numAsteroids) {
        this.numAsteroids = numAsteroids;

        this.asteroids = new ArrayList<Asteroid>();

        for (int i =0; i< numAsteroids; i++) {
            this.asteroids.add(new Asteroid());
        }
    }

    public void redestribute() {
        for (Asteroid asteroid: asteroids) {
            asteroid.redistribute();
        }
    }

    public boolean hasGoneOffscreen(Asteroid asteroid) {
        if (asteroid.getSprite().getY() < 0) {
            return true;
        }
        return false;
    }

    public void render(SpriteBatch batch) {
        for (Asteroid asteroid: asteroids) {
            if (hasGoneOffscreen(asteroid)) {
                asteroid.redistribute();
            }
            asteroid.getSprite().translateY(-3f);
            asteroid.getSprite().draw(batch);
        }
    }
}
