package com.spaceshooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.spaceshooter.game.controller.BaseController;
import com.spaceshooter.game.controller.SerialController;
import com.spaceshooter.game.controller.SocketController;
import com.spaceshooter.game.controller.SerialController;


import java.io.IOException;

public class SpaceShooter extends ApplicationAdapter implements InputProcessor {
    final String socketControllerIp = "192.168.178.110";
    final int socketControllerPort = 42420;

    SpriteBatch batch;
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
    boolean versus = false;
    Sound GameTheme;


    SerialController serialController = null;
    boolean useSerialInput = false;
    boolean useKeyboardInput = true;
    BaseController controller1;
    BaseController controller2;
    InputMethod inputMethod = InputMethod.KEYBOARD;

    AsteroidManager asteroidManager;
    EnemyManager enemyManager;
    PowerupManager powerupManager;
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
        enemyManager = new EnemyManager(0);
        starManager = new BackgroundManager(100);
        GameTheme = Gdx.audio.newSound(Gdx.files.internal("GameTheme.ogg"));

        player = new Player();
        player.create("SpaceShip2.txt");
        player2 = new Player();
        player2.create("SpaceShip4.txt");
        asteroidManager = new AsteroidManager(10);
        powerupManager = new PowerupManager(0);

        this.initializeController();
//
//        // Skip to single player mode
//        playerAmount = 1;
//        screen = "game";
//        playing = true;
//        versus = false;
//        player.dead = false;
    }

    public void initializeController() {
        switch (inputMethod) {
            case SERIAL:
                // Initialize the serial controller
                SerialController serialController = new SerialController();
                serialController.autoChooseCommPort();

                // The program may now use the controller's getDirection()
                controller1 = serialController;
                break;

            case SOCKET:
                // The program may now use the controller's getDirection()
                controller2 = new SocketController(socketControllerIp, socketControllerPort);
                break;

            default:
                return;
        }
    }

    public void updateControllerInput() {
        if (controller1 != null) {
            try {
                int direction = this.controller1.getDirection();
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
        if (controller2 != null) {
            try {
                int direction = this.controller2.getDirection();
                if (direction == 1) {
                    movingRight2 = true;
                    movingLeft2 = false;
                } else if (direction == -1) {
                    movingRight2 = false;
                    movingLeft2 = true;
                } else {
                    movingRight2 = false;
                    movingLeft2 = false;
                }
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void render() {
        elapsedTime += Gdx.graphics.getDeltaTime();

        if (inputMethod.equals(InputMethod.SOCKET) || inputMethod.equals(InputMethod.SERIAL)) {
            updateControllerInput();
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
            powerupManager.render(batch, playing, score, elapsedTime);

            if (!versus) {
                drawCenterText(font, Integer.toString(score), Gdx.graphics.getHeight() / 2 - 20);
                score++;
                score += player.hitboxController(asteroidManager, enemyManager, powerupManager);
                if (playerAmount == 2)
                    score += player2.hitboxController(asteroidManager, enemyManager, powerupManager);

                if (player.hp < 1 || player2.hp < 1) {
                    playing = false;
                    screen = "gameover";
                    GameTheme.stop();
                }
            } else {
                font.draw(batch, Integer.toString(player.score), 20f, Gdx.graphics.getHeight()-20f);
                GlyphLayout layout = new GlyphLayout(font, Integer.toString(player2.score));
                float fontX = (Gdx.graphics.getWidth() - layout.width)-20f;
                float fontY = (Gdx.graphics.getHeight()-20f);
                font.draw(batch, layout, fontX, fontY);
                if (!player.dead) {
                    player.score++;
                    player.score += player.hitboxController(asteroidManager, enemyManager, powerupManager);
                }
                if (!player2.dead) {
                    player2.score++;
                    player2.score += player2.hitboxController(asteroidManager, enemyManager, powerupManager);
                }
                if (!player.dead && !player2.dead) {
                    score = (player.score + player2.score) / 2;
                } else if (!player.dead && player2.dead) {
                    score = (player.score);
                } else if (player.dead && !player2.dead) {
                    score = (player2.score);
                }
                if (player.hp < 1 && player2.hp < 1) {
                    playing = false;
                    screen = "gameover";
                    GameTheme.stop();
                }
                if (player.hp < 1) {
                    player.dead = true;
                }
                if (player2.hp < 1) {
                    player2.dead = true;
                }
            }

            if (asteroidManager.colWithPlayer(player.c_player) || powerupManager.colWithPlayer(player.c_player)) {
                player.hp = 0;
            }
            if (asteroidManager.colWithPlayer(player2.c_player) || powerupManager.colWithPlayer(player2.c_player)) {
                player2.hp = 0;
            }

            font.setColor(Color.RED);
            font.draw(batch, Integer.toString(player.hp), 30f, 60f);
            if (playerAmount == 2) {
                font.setColor(Color.BLUE);
                font.draw(batch, Integer.toString(player2.hp), Gdx.graphics.getWidth() - 30f, 60f);
            }
            font.setColor(Color.WHITE);

        } else if (screen == "gameover") {
            if (!versus) {
                drawCenterText(font, "Je hebt " + Integer.toString(score) + " punten behaald!", 10);
            } else {
                if (player.score > player2.score) {
                    drawCenterText(font, "Speler 1 heeft gewonnen met " + Integer.toString(player.score) + " punten!", 50);
                    drawCenterText(font, "Speler 2 heeft " + Integer.toString(player2.score) + " punten gehaald!", 10);
                } else if (player.score < player2.score) {
                    drawCenterText(font, "Speler 2 heeft gewonnen met " + Integer.toString(player2.score) + " punten!", 50);
                    drawCenterText(font, "Speler 1 heeft " + Integer.toString(player.score) + " punten gehaald!", 10);
                }
            }
            drawCenterText(font, "Game Over", 100);
            String arr[] = {"Restart", "Menu", "Afsluiten"};
            menuSelector(arr);
        } else if (screen == "start") {
            drawCenterText(fonttitle, "Space Shooter", 50);
            String arr[] = {"1 speler", "2 spelers", "Afsluiten"};
            menuSelector(arr);
        } else if (screen == "multiselect") {
            drawCenterText(font, "Select gamemode", 50);
            String arr[] = {"Co-op", "Versus", "Menu"};
            menuSelector(arr);
        } else {
            drawCenterText(font, "Er is helaas iets fout gegaan", 50);
            drawCenterText(font, "Druk op ENTER om terug te gaan naar het menu", 10);
            String arr[] = {"Menu"};
            menuSelector(arr);
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

    public void menuSelector(String[] options) {
        font.setColor(Color.GRAY);
        if (movingLeft && selecting || movingLeft2 && selecting) {
            selectedOption--;
            selecting = false;
        }
        if (movingRight && selecting || movingRight2 && selecting) {
            selectedOption++;
            selecting = false;
        }
        if (selectedOption > options.length-1) selectedOption = 0;
        if (selectedOption < 0) selectedOption = options.length-1;

        int i = 0;
        for (String option: options) {
            if (selectedOption == i)
                font.setColor(Color.WHITE);
            drawCenterText(font, option, -40 + i * -40);
            font.setColor(Color.GRAY);
            i++;
        }

        if (select && selecting) {
            selecting = false;
            options(options[selectedOption]);
            selectedOption = 0;
        }
        font.setColor(Color.WHITE);
    }

    public void options(String option) {
        switch (option) {
            case "1 speler":
                playerAmount = 1;
                screen = "game";
                playing = true;
                versus = false;
                player.dead = false;
                playMusic();
                break;
            case "2 spelers":
                screen = "multiselect";
                break;
            case "Co-op":
                playerAmount = 2;
                screen = "game";
                playing = true;
                versus = false;
                player.dead = false;
                playMusic();
                break;
            case "Versus":
                playerAmount = 2;
                screen = "game";
                playing = true;
                versus = true;
                playMusic();
                player.dead = false;
                break;
            case "Restart":
                asteroidManager.reset();
                powerupManager.reset();
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
                powerupManager.reset();
                score = 0;
                enemyManager.resetEnemy();
                break;
            case "Afsluiten":
                screen = "game";
                System.exit(0);
                break;
            case "Endless":
                screen = "endlessselect";
                break;
            case "Campaign":
                screen = "campaignselect";
                break;
        }
    }

    public void playMusic() {
        long id = GameTheme.play(0.5f);
        GameTheme.setPitch(id, 1);
        GameTheme.setLooping(id, false);
    }
}
