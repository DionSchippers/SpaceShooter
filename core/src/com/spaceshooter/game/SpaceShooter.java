package com.spaceshooter.game;

import java.io.*;
import java.nio.file.Files;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.spaceshooter.game.controller.SerialController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
    EnemyManager enemyManager;
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
        enemyManager = new EnemyManager(0);
        starManager = new BackgroundManager(100);
        GameTheme = Gdx.audio.newSound(Gdx.files.internal("GameTheme.ogg"));

        player = new Player();
        player.create("SpaceShip2.txt");
        player2 = new Player();
        player2.create("SpaceShip4.txt");
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
                } else if (direction == -1) {
                    movingRight = false;
                    movingLeft = true;
                } else {
                    movingRight = false;
                    movingLeft = false;
                }
            } catch (IOException e) {

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

            enemyManager.render(playing, elapsedTime, score, player, player2);

            asteroidManager.render(batch, playing, score, elapsedTime);


            drawCenterText(font, Integer.toString(score), Gdx.graphics.getHeight() / 2 - 20);
            score++;

            if (asteroidManager.colWithPlayer(player.c_player) || asteroidManager.colWithPlayer(player2.c_player)) {
                playing = false;
                screen = "gameover";
                GameTheme.stop();
                leaderboardWrite();
                leaderboardRead();
            }
            font.setColor(Color.RED);
            font.draw(batch, Integer.toString(player.hp), 30f, 60f);
            if (playerAmount == 2) {
                font.setColor(Color.BLUE);
                font.draw(batch, Integer.toString(player2.hp), Gdx.graphics.getWidth() - 30f, 60f);
            }
            font.setColor(Color.WHITE);

            if (player.hp < 1 || player2.hp < 1) {
                playing = false;
                screen = "gameover";
                GameTheme.stop();
                leaderboardWrite();
                leaderboardRead();
            }


            score += player.hitboxController(asteroidManager, enemyManager);
            if (playerAmount == 2)
                score += player2.hitboxController(asteroidManager, enemyManager);

        } else if (screen == "gameover") {
            drawCenterText(font, "Je hebt " + Integer.toString(score) + " punten behaald!", 10);
            drawCenterText(font, "Game Over", 50);
            menuSelector("Restart", "Menu", "Leaderboard");

        } else if (screen == "start") {
            drawCenterText(fonttitle, "Space Shooter", 50);
            menuSelector("1 speler", "2 spelers", "Afsluiten");
        } else if (screen == "leaderboard") {
            drawCenterText(fonttitle, "Leaderboard", 190);
            menuSelector("Afsluiten", "Restart");
            leaderboardRead();
        }
        batch.end();
    }

    public void leaderboardWrite() {
        JSONArray leaderboardArray = getLeaderboard(new JSONParser());
        if (leaderboardArray == null) {
            return;
        }

        // Store the index of the current passed score.
        int index = -1;

        // Loop through the 5 highscores stored in Leaderboard.json
        for (int i = 0; i < leaderboardArray.size(); i++) {

            // Check if the score exists in the Leaderboard.json
            if (leaderboardArray.get(i) != null) {
                JSONObject score = (JSONObject) leaderboardArray.get(i);

                // Check if the current game score is higher than the stored high score
                // If True store the high score array index in the index variable
                if (this.score > (long) score.get("score"))
                    index = i;
            } else {
                break;
            }
        }
        try {
            Files.write(Paths.get("Leaderboard.json"),
                    leaderboardArray.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // einde functie leaderboardWrite

    public void leaderboardRead() {
        JSONArray leaderboardArray = getLeaderboard(new JSONParser());
        for (int i = leaderboardArray.size(); i > 0; i--) {
            if (leaderboardArray.get(i - 1) != null) {
                JSONObject score = (JSONObject) leaderboardArray.get(i - 1);
                drawCenterText(font, String.format("Score %d: %s", leaderboardArray.size() - i, score.get("score")), 120 - (50 * i));
            } else {
                break;
            }
        }
    } //einde functie leaderboardRead

    private JSONArray getLeaderboard(JSONParser jsonParser) {
        try (FileReader reader = new FileReader("Leaderboard.json")) {
            return (JSONArray) jsonParser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public void menuSelector(String option1, String option2, String option3) {
        font.setColor(Color.GRAY);
        if (movingLeft && selecting || movingLeft2 && selecting) {
            selectedOption--;
            selecting = false;
        }
        if (movingRight && selecting || movingRight2 && selecting) {
            selectedOption++;
            selecting = false;
        }
        if (selectedOption > 2) selectedOption = 0;
        if (selectedOption < 0) selectedOption = 2;

        if (selectedOption == 0)
            font.setColor(Color.WHITE);
        drawCenterText(font, option1, -40);
        font.setColor(Color.GRAY);

        if (selectedOption == 1)
            font.setColor(Color.WHITE);
        drawCenterText(font, option2, -80);
        font.setColor(Color.GRAY);

        if (selectedOption == 2)
            font.setColor(Color.WHITE);
        drawCenterText(font, option3, -120);
        font.setColor(Color.WHITE);

        if (select && selecting) {
            selecting = false;
            if (selectedOption == 0) {
                options(option1);
            } else if (selectedOption == 1) {
                options(option2);
            } else if (selectedOption == 2) {
                options(option3);
            }
            selectedOption = 0;
        }

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
        drawCenterText(font, option1, -130);
        font.setColor(Color.GRAY);

        if (selectedOption == 1)
            font.setColor(Color.WHITE);
        drawCenterText(font, option2, -180);
        font.setColor(Color.WHITE);

        if (select && selecting) {
            selecting = false;
            if (selectedOption == 0) {
                options(option1);
            }else if (selectedOption == 1){
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
                enemyManager.resetEnemy();
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
                enemyManager.resetEnemy();
                break;
            case "Afsluiten":
                screen = "game";
                System.exit(0);
                break;
            case "Leaderboard":
                screen = "leaderboard";
                break;
        }
    }

    public void playMusic() {
        long id = GameTheme.play(0.5f);
        GameTheme.setPitch(id, 1);
        GameTheme.setLooping(id, false);
    }
}
