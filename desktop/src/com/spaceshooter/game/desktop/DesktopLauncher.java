package com.spaceshooter.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.spaceshooter.game.SpaceShooter;

public class DesktopLauncher {

	public static void main (String[] arg) {
//		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//		config.setWindowSizeLimits(800, 600, 1920, 1080);
//		new Lwjgl3Application(new SpaceShooter(), config);
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setMaximized(true);
		config.setResizable(false);
		new Lwjgl3Application(new SpaceShooter(), config);

	}
}
