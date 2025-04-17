package com.obstacleavoid;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.obstacleavoid.screen.game.GameScreen;
import com.obstacleavoid.screen.loading.LoadingScreen;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ObstacleAvoidGame extends Game {
	//Game ->  is a class the also implements application listener . It helps us to have multiple screens e.g main menu screen
	//HighScore screen etc. game works with screens. There is an interface screen and we will also see class screen adapter


//dont make asset manager static, making it static could cause bugs and  memory leaks . If you make it static you should know what youy are
	//doing
	private AssetManager assetManager;
	private SpriteBatch batch;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		assetManager = new AssetManager();
		assetManager.getLogger().setLevel(Logger.DEBUG);

		batch = new SpriteBatch();

		setScreen(new LoadingScreen(this));
	}


	public AssetManager getAssetManager() {
		return assetManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		batch.dispose();
	}
}