package com.spaceshooter.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

public class SpaceShooter extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture enemyImg;
    Sprite enemySprite;
    boolean movingRight = false;
    boolean movingLeft = false;
    boolean playing = true;
    float elapsedTime = 0f;
    BitmapFont font;

    AsteroidManager asteroidManager;
    Player player;


    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
        BitmapFont font = new BitmapFont();

        asteroidManager = new AsteroidManager(10);

        player = new Player();
        player.create();
    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();

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



//        font.draw(batch, "Hello World!", 10, 10);

        if (asteroidManager.colWithPlayer(player.c_player)) {
            playing = false;
            System.out.println("jinks");

        }
        if (asteroidManager.colWithLaser(player.r_laser)) {
            player.laserList.remove(player.laserSprite);
            player.laserSprite.setAlpha(0);
            player.laserSprite.setPosition(player.laserSprite.getX(), 10000f);
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
