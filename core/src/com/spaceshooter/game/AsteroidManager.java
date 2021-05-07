package com.spaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

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
            float x = asteroid.xVALint;
            float y = asteroid.getY();

//            ss.circle(x+16, y+16, 16);
            asteroid.c_asteroid.set(x+16, y+16, 16);
        }
    }

    public boolean colWithPlayer(Circle c_player) {
        for (Asteroid asteroid: this.asteroids
             ) {
            if (Intersector.overlaps(c_player, asteroid.c_asteroid)) {
                return true;
            }
        }
        return false;
    }
    public boolean colWithLaser(Rectangle r_laser) {
        for (Asteroid asteroid : this.asteroids) {
            if (Intersector.overlaps(asteroid.c_asteroid, r_laser)) {
                asteroid.destroy();
                return true;
            }
        }
        return false;
    }


}
