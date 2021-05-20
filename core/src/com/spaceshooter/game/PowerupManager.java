package com.spaceshooter.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class PowerupManager {
    private int numPowerups;
    private ArrayList<Powerup> powerups;
    private int scoreAmount = 5000;

    public PowerupManager(int numPowerups) {
        this.numPowerups = numPowerups;

        this.powerups = new ArrayList<Powerup>();

        for (int i =0; i< numPowerups; i++) {
            this.powerups.add(new Powerup());
        }
    }

    public void redestribute() {
        for (Powerup powerup: powerups) {
            powerup.redistribute();
        }
    }

    public void render(SpriteBatch batch, boolean playing, int score, float time) {
        if (playing) {
            if (scoreAmount < score) {
                this.powerups.add(new Powerup());
                scoreAmount += 5000;
            }
            for (Powerup powerup : powerups) {
                powerup.getSprite().translateY(-3f * (1f + score/10000f));
                powerup.getSprite().draw(batch);
                powerup.render(batch);
                float x = powerup.xVALint;
                float y = powerup.getY();

                powerup.c_powerup.set(x + 16, y + 16, 16);
            }
        }
    }

    public void reset() {
        powerups.clear();
        scoreAmount = 5000;
    }

    public boolean colWithPlayer(Circle c_player) {
        for (Powerup powerup: this.powerups
        ) {
            if (Intersector.overlaps(c_player, powerup.c_powerup)) {
                return true;
            }
        }
        return false;
    }
    public boolean colWithLaser(Rectangle r_laser, Player player) {
        for (Powerup powerup : this.powerups) {
            if (Intersector.overlaps(powerup.c_powerup, r_laser)) {
                float x = powerup.sprite.getX();
                float y = powerup.sprite.getY();
                powerup.explode(x, y);
                powerup.sprite.setPosition(powerup.sprite.getX(), -10);
                player.powerup = powerup.type;
                System.out.println(powerup.type);
                return true;
            }
        }
        return false;
    }
}
