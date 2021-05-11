package com.spaceshooter.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SpaceShooter extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture enemyImg;
    Sprite enemySprite;
    boolean movingRight = false;
    boolean movingLeft = false;
    boolean movingRight2 = false;
    boolean movingLeft2 = false;
    boolean select = false;
    boolean playing = false;
    float elapsedTime = 0f;
    BitmapFont font;
    int score;
    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
    String screen;

    AsteroidManager asteroidManager;
    Player player;
    Player player2;


    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
        screen = "start";
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        score = 0;
        asteroidManager = new AsteroidManager(10);

        player = new Player();
        player.create("SpaceShip2.txt");
        player2 = new Player();
        player2.create("SpaceShip4.txt");
    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();

        player.move(movingRight, movingLeft, playing);
        player2.move(movingRight2, movingLeft2, playing);

        Gdx.gl.glClearColor(56 / 255f, 70 / 255f, 92 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        if (screen == "game") {

            player.playerController(elapsedTime, playing);
            player2.playerController(elapsedTime, playing);

            asteroidManager.render(batch, playing, score);

        font.draw(batch, Integer.toString(score), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-20);
        score++;

            if (asteroidManager.colWithPlayer(player.c_player) || asteroidManager.colWithPlayer(player2.c_player)) {
                playing = false;
                screen = "gameover";
            }

            score += player.hitboxController(asteroidManager);
            score += player2.hitboxController(asteroidManager);

        } else if (screen == "gameover") {
            font.draw(batch, "Je hebt " + Integer.toString(score) + " punten behaald!", Gdx.graphics.getWidth()/2-150, Gdx.graphics.getHeight()/2-30);
            font.draw(batch, "Game Over", Gdx.graphics.getWidth()/2-40, Gdx.graphics.getHeight()/2);
            font.draw(batch, "Press ENTER to restart", Gdx.graphics.getWidth()/2-110, Gdx.graphics.getHeight()/2-200);

            if (select) {
                asteroidManager.reset();
                screen = "game";
                score = 0;
                playing = true;
                player.reset();
                player2.reset();
            }
        } else if (screen == "start") {
            font.draw(batch, "Space Shooter", Gdx.graphics.getWidth() / 2 - 40, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "Press ENTER to start", Gdx.graphics.getWidth() / 2 - 80, Gdx.graphics.getHeight() / 2 - 200);

            if (select) {
                screen = "game";
                playing = true;
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        player2.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.A)
            movingLeft = true;

        if (keycode == Input.Keys.D)
            movingRight = true;

        if (keycode == Input.Keys.DPAD_LEFT)
            movingLeft2 = true;

        if (keycode == Input.Keys.DPAD_RIGHT)
            movingRight2 = true;

        if (keycode == Input.Keys.ENTER)
            select = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A)
            movingLeft = false;

        if (keycode == Input.Keys.D)
            movingRight = false;

        if (keycode == Input.Keys.DPAD_LEFT)
            movingLeft2 = false;

        if (keycode == Input.Keys.DPAD_RIGHT)
            movingRight2 = false;

        if (keycode == Input.Keys.ENTER)
            select = false;
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
