package com.spaceshooter.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class EnemyManager {
    private int numEnemies;
    private ArrayList<Enemy> enemies;
    private int scoreAmount = 0;

    public EnemyManager(int numEnemies) {
        this.numEnemies = numEnemies;

        this.enemies = new ArrayList<Enemy>();

        for (int i =0; i< numEnemies; i++) {
            this.enemies.add(new Enemy("EnemyShip.txt"));
        }
    }


    public void render(boolean playing, float elapsedTime, int score, Player player, Player player2) {
        if (playing) {
            System.out.println(score);
            System.out.println(scoreAmount);
            if (scoreAmount < score) {
                System.out.println(scoreAmount);
                this.enemies.add(new Enemy("EnemyShip.txt"));
                scoreAmount += 20000;
            }
            for (Enemy enemy : enemies) {
                enemy.enemyController(elapsedTime, playing);
                enemy.hitboxController(player);
                enemy.hitboxController(player2);

            }
        }
    }

    public boolean colWithPlayer(Circle c_player) {
        for (Enemy enemy: this.enemies
        ) {
            if (Intersector.overlaps(c_player, enemy.c_player)) {
                return true;
            }
        }
        return false;
    }
    public boolean colWithLaser(Rectangle r_laser) {
        for (Enemy enemy : this.enemies) {
            if (Intersector.overlaps(enemy.c_player, r_laser)) {
                float x = enemy.enemySprite.getX();
                float y = enemy.enemySprite.getY();
                enemy.explode(x, y);
                enemy.enemySprite.setPosition(enemy.enemySprite.getX(), -100);
                enemy.laserSprite.setPosition(enemy.enemySprite.getX(), -100);
                enemy.c_player.setPosition(enemy.enemySprite.getX(), -100);
                return true;
            }
        }
        return false;
    }


}


