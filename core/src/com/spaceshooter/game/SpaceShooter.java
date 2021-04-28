package com.spaceshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

import java.util.ArrayList;

public class SpaceShooter extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    TextureAtlas playerTexture;
    Texture asteroidImg;
    Texture enemyImg;
    Texture laserImg;
    Sprite playerSprite;
    Sprite asteroidSprite;
    Sprite enemySprite;
    Sprite laserSprite;
    ArrayList<Sprite> laserList;
    boolean movingRight = false;
    boolean movingLeft = false;
    boolean playing = true;
    int laserTimer;
    Animation<TextureRegion> playerAnimation;
    float elapsedTime = 0f;

    AsteroidManager asteroidManager;


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
        Asteroid asteroid = new Asteroid();
        asteroidSprite = asteroid.getSprite();
        Gdx.input.setInputProcessor(this);


        asteroidManager = new AsteroidManager(3);
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
        asteroidSprite.translateY(-3f);
        batch.draw(asteroidSprite, asteroidSprite.getX(), asteroidSprite.getY(), 64, 64);
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

        asteroidManager.render(batch);
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
