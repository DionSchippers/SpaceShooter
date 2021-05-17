package com.spaceshooter.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    boolean movingRight2 = false;
    boolean movingLeft2 = false;
    boolean select = false;
    boolean playing = false;
    float elapsedTime = 0f;
    BitmapFont font, fonttitle;
    int score;
    public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
    String screen;
    int selectedOption;
    boolean selecting;
    int playerAmount;
    Sound GameTheme;

    SerialController serialController = null;
    boolean useSerialInput = false;
    boolean useKeyboardInput = true;

    AsteroidManager asteroidManager;
    BackgroundManager starManager;
    Player player;
    Player player2;
    Enemy enemy;


    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
        screen = "start";
        selectedOption = 0;
        selecting = true;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("fonts/Oxaniumfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.borderColor = Color.WHITE;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        params.magFilter = Texture.TextureFilter.Nearest;
        params.minFilter = Texture.TextureFilter.Nearest;
        params.genMipMaps = true;
        params.size = 80;

        fonttitle = generator.generateFont(params);

        params.size = 40;
        font = generator.generateFont(params);

        font.setColor(Color.WHITE);
        font.getData().setScale(1);
        score = 0;
        asteroidManager = new AsteroidManager(8);
        starManager = new BackgroundManager(100);
        GameTheme = Gdx.audio.newSound(Gdx.files.internal("GameTheme.ogg"));

        player = new Player();
        player.create("SpaceShip2.txt");
        player2 = new Player();
        player2.create("SpaceShip4.txt");
        enemy = new Enemy();
        enemy.create("SpaceShip4.txt");
        asteroidManager = new AsteroidManager(10);

        if (useSerialInput) {
            serialController = new SerialController();
            serialController.autoChooseCommPort();
        }
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

        player.move(movingRight, movingLeft, playing);
        if (playerAmount == 2)
            player2.move(movingRight2, movingLeft2, playing);

        Gdx.gl.glClearColor(56 / 255f, 70 / 255f, 92 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        starManager.render(batch, playing, score);

        if (screen == "game") {

            player.playerController(elapsedTime, playing);
            if (playerAmount == 2)
                player2.playerController(elapsedTime, playing);
            enemy.enemyController(elapsedTime, playing);

            asteroidManager.render(batch, playing, score, elapsedTime);


            drawCenterText(font, Integer.toString(score), Gdx.graphics.getHeight()/2-20);
            score++;

            if (asteroidManager.colWithPlayer(player.c_player) || asteroidManager.colWithPlayer(player2.c_player)) {
                playing = false;
                screen = "gameover";
                GameTheme.stop();
            }

            enemy.hitboxController(player);
            enemy.hitboxController(player2);
            font.setColor(Color.RED);
            font.draw(batch, Integer.toString(player.hp), 30f, 60f);
            if (playerAmount == 2) {
                font.setColor(Color.BLUE);
                font.draw(batch, Integer.toString(player2.hp), Gdx.graphics.getWidth()-30f, 60f);
            }
            font.setColor(Color.WHITE);

            if (player.hp < 1 || player2.hp < 1) {
                playing = false;
                screen = "gameover";
                GameTheme.stop();
            }


            score += player.hitboxController(asteroidManager);
            if (playerAmount == 2)
                score += player2.hitboxController(asteroidManager);

        } else if (screen == "gameover") {
            drawCenterText(font, "Je hebt " + Integer.toString(score) + " punten behaald!", 10);
            drawCenterText(font, "Game Over", 50);
            menuSelector("Restart", "Menu");

        } else if (screen == "start") {
            drawCenterText(fonttitle, "Space Shooter", 50);
            menuSelector("1 speler", "2 spelers");
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        GameTheme.dispose();
        player.dispose();
        if (playerAmount == 2)
            player2.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (useKeyboardInput) {
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
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A) {
            movingLeft = false;
            selecting = true;
        }

        if (keycode == Input.Keys.D) {
            movingRight = false;
            selecting = true;
        }

        if (keycode == Input.Keys.DPAD_LEFT) {
            movingLeft2 = false;
            selecting = true;
        }

        if (keycode == Input.Keys.DPAD_RIGHT) {
            movingRight2 = false;
            selecting = true;
        }

        if (keycode == Input.Keys.ENTER) {
            select = false;
            selecting = true;
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

    public void drawCenterText(BitmapFont font, String text, int Height) {
        GlyphLayout layout = new GlyphLayout(font, text);
        float fontX = (Gdx.graphics.getWidth() - layout.width) / 2;
        float fontY = Height + (Gdx.graphics.getHeight() + layout.height) / 2;
        font.draw(batch, layout, fontX, fontY);
    }

    public void menuSelector(String option1, String option2) {
        font.setColor(Color.GRAY);
        if (movingLeft && selecting || movingLeft2 && selecting) {
            selectedOption--;
            selecting = false;
        }
        if (movingRight && selecting || movingRight2 && selecting) {
            selectedOption++;
            selecting = false;
        }
        if (selectedOption > 1) selectedOption = 0;
        if (selectedOption < 0) selectedOption = 1;

        if (selectedOption == 0)
            font.setColor(Color.WHITE);
        drawCenterText(font, option1, -40);
        font.setColor(Color.GRAY);

        if (selectedOption == 1)
            font.setColor(Color.WHITE);
        drawCenterText(font, option2, -80);
        font.setColor(Color.WHITE);

        if (select && selecting) {
            selecting = false;
            if (selectedOption == 0) {
                options(option1);
            } else if (selectedOption == 1) {
                options(option2);
            }
            selectedOption = 0;
        }

    }

    public void options(String option) {
        switch (option) {
            case "1 speler":
                playerAmount = 1;
                screen = "game";
                playing = true;
                playMusic();
                break;

            case "2 spelers":
                playerAmount = 2;
                screen = "game";
                playing = true;
                playMusic();
                break;
            case "Restart":
                asteroidManager.reset();
                screen = "game";
                score = 0;
                playMusic();
                playing = true;
                player.reset();
                if (playerAmount == 2)
                    player2.reset();
                break;
            case "Menu":
                screen = "start";
                player.reset();
                if (playerAmount == 2)
                    player2.reset();
                asteroidManager.reset();
                score = 0;
                break;
        }

    }
    public void playMusic() {
        long id = GameTheme.play(0.5f);
        GameTheme.setPitch(id, 1);
        GameTheme.setLooping(id, false);
    }
}
