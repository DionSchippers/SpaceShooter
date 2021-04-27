package com.spaceshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class SpaceShooter extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    TextureAtlas playerTexture;
    Texture asteroidImg;
    Texture enemyImg;
    Texture laserImg;
    Sprite playerSprite;
    Sprite asteroidSprite;
    Sprite playerSprite;
    Sprite enemySprite;
    Sprite laserSprite;
    ArrayList<Sprite> laserList;
    boolean movingRight = false;
    boolean movingLeft = false;
    boolean playing = true;
    int laserTimer;
    Animation<TextureRegion> playerAnimation;
    float elapsedTime = 0f;


    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTexture = new TextureAtlas("SpaceShip2.txt");
        playerAnimation = new Animation(1f/30f, playerTexture.getRegions());
        laserImg = new Texture("laserbeam1.png");
        playerSprite = new Sprite();
        playerSprite.setPosition(Gdx.graphics.getWidth() / 2 - playerSprite.getWidth() / 2, 20);
        laserList = new ArrayList<>();
        createLaser(playerSprite.getX());
        laserTimer = 0;
        Astroid asteroid = new Astroid();
        asteroidSprite = asteroid.getSprite();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (movingRight && playing) {
            if (playerSprite.getX() > Gdx.graphics.getWidth() - 96) {
                playerSprite.setX(Gdx.graphics.getWidth() - 96);
            } else {
                playerSprite.translateX(15f);
            }
        }
        if (movingLeft && playing) {
            if (playerSprite.getX() < 0) {
                playerSprite.setX(0);
            } else {
                playerSprite.translateX(-15f);
            }
        }

        Gdx.gl.glClearColor(28 / 255f, 35 / 255f, 46 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        for (Sprite sprite : laserList) {
            sprite.draw(batch);
        }
        batch.draw(playerAnimation.getKeyFrame(elapsedTime,true),playerSprite.getX(),playerSprite.getY());
        batch.end();
        if (laserSprite.getY() > Gdx.graphics.getHeight()) {
            laserList.remove(laserSprite);
            laserSprite.setAlpha(0);
        } else if (playing) {
            laserSprite.translateY(35f);
        }
        laserTimer++;
        while (laserTimer > 20) {
            createLaser(playerSprite.getX());
            laserTimer = 0;
        }

        int i = 1;
        if( i < 10){
            Astroid asteroid = new Astroid(speed);
            i++;
        }
        asteroidSprite.translateY(-3f);
        Gdx.gl.glClearColor(41 / 255f, 37 / 255f, 74 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(playerSprite, playerSprite.getX(), playerSprite.getY(), 96, 96);
        batch.draw(asteroidSprite, asteroidSprite.getX(), asteroidSprite.getY(), 64, 64);
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        laserImg.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A)
            movingLeft = true;

        if (keycode == Input.Keys.D)
            movingRight = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A) {
            movingLeft = false;
        }
        if (keycode == Input.Keys.D) {
            movingRight = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void createLaser(float playerSpriteX) {
        laserSprite = new Sprite(laserImg);
        laserSprite.setPosition(playerSpriteX + 32, 52);
        laserList.add(laserSprite);
    }
}
