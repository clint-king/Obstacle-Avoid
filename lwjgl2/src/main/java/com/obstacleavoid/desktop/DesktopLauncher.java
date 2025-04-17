package com.obstacleavoid.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.config.GameConfig;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
	public static void main(String[] args) {
		createApplication();
	}

	private static LwjglApplication createApplication() {
		return new LwjglApplication(new ObstacleAvoidGame(), getDefaultConfiguration());
	}

	private static LwjglApplicationConfiguration getDefaultConfiguration() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "obstacle-avoid";
		config.width = (int) GameConfig.WIDTH;
		config.height = (int) GameConfig.HEIGHT;



		// This prevents a confusing error that would appear after exiting normally.
		config.forceExit = false;

		for (int size : new int[] { 128, 64, 32, 16 }) {
			config.addIcon("libgdx" + size + ".png", FileType.Internal);
		}
		return config;
	}
}