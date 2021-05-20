package com.spaceshooter.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Laser {
    Texture laserImg;
    Sprite laserSprite;
    Rectangle r_laser;
    int laserTimer;
    Vector2 movement;

    public Laser(float xMov, float yMov, String texture, float playerX) {
        laserImg = new Texture(texture);
        r_laser = new Rectangle();
        laserTimer = 0;
        movement = new Vector2(xMov, yMov);
        laserSprite = new Sprite(laserImg);
        laserSprite.setPosition(playerX + 32, 52);
    }

    public void lasercontroller(boolean playing) {
        if (playing) {
            laserSprite.translate(movement.x, movement.y);
            r_laser.set(laserSprite.getX() - laserSprite.getWidth() / 2, laserSprite.getY() - laserSprite.getHeight() / 2, laserSprite.getWidth(), laserSprite.getHeight());
        }
        laserTimer++;
    }

    public void createLaser(float playerSpriteX) {
        laserSprite = new Sprite(laserImg);
        laserSprite.setPosition(playerSpriteX + 32, 52);
    }

    public void reset() {
        laserSprite.setAlpha(0);
        laserSprite.setY(10000);
    }
}
