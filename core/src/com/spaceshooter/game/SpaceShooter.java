package com.spaceshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class SpaceShooter extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture playerImg;
    Texture astroidImg;
    Texture enemyImg;
    Sprite playerSprite;
    Sprite astroidSprite;
    Sprite enemySprite;
    boolean movingRight = false;
    boolean movingLeft = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerImg = new Texture("spaceship.png");
        playerSprite = new Sprite(playerImg);
        playerSprite.setPosition(Gdx.graphics.getWidth() / 2 - playerSprite.getWidth() / 2, 20);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        if (movingRight) {
            if (playerSprite.getX() > Gdx.graphics.getWidth()-96) {
                playerSprite.setX(Gdx.graphics.getWidth()-96);
            }else {
                playerSprite.translateX(15f);
            }
        }
        if (movingLeft) {
            if (playerSprite.getX() < 0) {
                playerSprite.setX(0);
            }else {
                playerSprite.translateX(-15f);
            }
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(playerSprite, playerSprite.getX(), playerSprite.getY(),96,96);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerImg.dispose();
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
}
