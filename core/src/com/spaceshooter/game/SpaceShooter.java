package com.spaceshooter.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.spaceshooter.game.controller.SerialController;

import java.io.IOException;

public class SpaceShooter extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture enemyImg;
    Sprite enemySprite;
    boolean movingRight = false;
    boolean movingLeft = false;
    boolean playing = true;
    float elapsedTime = 0f;

    SerialController serialController = null;
    boolean useSerialInput = false;
    boolean useKeyboardInput = true;

    AsteroidManager asteroidManager;
    Player player;


    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);

        asteroidManager = new AsteroidManager(10);

        if (useSerialInput) {
            serialController = new SerialController();
            serialController.autoChooseCommPort();
        }

        player = new Player();
        player.create();
    }

    public void updateSerialInput() {
        if (serialController != null) {
            try {
                int direction = serialController.getDirection();
                if (direction == 1) {
                    movingRight = true;
                    movingLeft = false;
                }
                else if (direction == -1) {
                    movingRight = false;
                    movingLeft = true;
                }
                else {
                    movingRight = false;
                    movingLeft = false;
                }
            }
            catch (IOException e) {

            }
        }
    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();

        if (useSerialInput) {
            updateSerialInput();
        }
        player.move(movingRight, movingLeft, playing);

        Gdx.gl.glClearColor(28 / 255f, 35 / 255f, 46 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();


        for (Sprite sprite : player.laserList) {
            sprite.draw(batch);
        }
        batch.draw(player.playerAnimation.getKeyFrame(elapsedTime,true),player.playerSprite.getX(),player.playerSprite.getY());

        player.lasercontroller(playing);

        asteroidManager.render(batch);

        float x = player.playerSprite.getX();
        float y = player.playerSprite.getY();
        player.c_player.set(x+48, y+48, 48);
        if (asteroidManager.colWithPlayer(player.c_player)) {
            System.out.println("jinks");
        }
        if (asteroidManager.colWithLaser(player.r_laser)) {
            player.laserList.remove(player.laserSprite);
            player.laserSprite.setAlpha(0);
            System.out.println("appeltaart");
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (useKeyboardInput) {
            if (keycode == Input.Keys.A)
                movingLeft = true;

            if (keycode == Input.Keys.D)
                movingRight = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (useKeyboardInput) {
            if (keycode == Input.Keys.A) {
                movingLeft = false;
            }
            if (keycode == Input.Keys.D) {
                movingRight = false;
            }
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
