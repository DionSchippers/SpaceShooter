package com.spaceshooter.game;

import com.badlogic.gdx.*;
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

    AsteroidManager asteroidManager;
    BackgroundManager starManager;
    Player player;
    Player player2;


    @Override
    public void create() {
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(this);
        screen = "start";
//        font = new BitmapFont();
//        font = new BitmapFont(Gdx.files.internal("/fonts/Oxaniumfont.fnt"));
//        Label homescreen = new Label("Space Shooter", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("/fonts/Oxaniumfont.fnt")), Color.MAGENTA));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("fonts/Oxaniumfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        params.borderWidth = 1;
        params.borderColor = Color.WHITE;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        params.magFilter = Texture.TextureFilter.Nearest;
        params.minFilter = Texture.TextureFilter.Nearest;
        params.genMipMaps = true;
        params.size = 80;

        fonttitle = generator.generateFont(params);

        params.size = 40;
        font = generator.generateFont(params);

//        font.setColor(Color.WHITE);
        font.getData().setScale(1);
        score = 0;
        asteroidManager = new AsteroidManager(10);
        starManager = new BackgroundManager(10);

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
        starManager.render(batch, playing, score);

        if (screen == "game") {

            player.playerController(elapsedTime, playing);
            player2.playerController(elapsedTime, playing);

            asteroidManager.render(batch, playing, score);


            drawCenterText(font, Integer.toString(score), Gdx.graphics.getHeight()/2-20);
            score++;

            if (asteroidManager.colWithPlayer(player.c_player) || asteroidManager.colWithPlayer(player2.c_player)) {
                playing = false;
                screen = "gameover";
            }

            score += player.hitboxController(asteroidManager);
            score += player2.hitboxController(asteroidManager);

        } else if (screen == "gameover") {
            drawCenterText(font, "Je hebt " + Integer.toString(score) + " punten behaald!", 10);
            drawCenterText(font, "Game Over", 50);
            drawCenterText(font, "Press ENTER to restart", -80);

            if (select) {
                asteroidManager.reset();
                screen = "game";
                score = 0;
                playing = true;
                player.reset();
                player2.reset();
            }
        } else if (screen == "start") {
            drawCenterText(fonttitle, "Space Shooter", 50);
            drawCenterText(font, "Press ENTER to start", -80);

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

    public void drawCenterText(BitmapFont font, String text, int Height) {
        GlyphLayout layout = new GlyphLayout(font, text);
        float fontX = (Gdx.graphics.getWidth() - layout.width) / 2;
        float fontY = Height + (Gdx.graphics.getHeight() + layout.height) / 2;
        font.draw(batch, layout, fontX, fontY);
    }
}
