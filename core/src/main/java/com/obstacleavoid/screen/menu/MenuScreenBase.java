package com.obstacleavoid.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.util.GdxUtils;

public abstract class MenuScreenBase extends ScreenAdapter {


    protected final ObstacleAvoidGame game;
    protected final AssetManager assetManager;


    public MenuScreenBase(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    private Viewport viewport;
    private Stage stage;

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH , GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport , game.getBatch());


        Gdx.input.setInputProcessor(stage);

        stage.addActor(createUi());

    }

    protected  abstract Actor createUi();

    @Override
    public void resize(int width, int height) {
        viewport.update(width , height  , true);
    }

    @Override
    public void render(float delta) {

        GdxUtils.clearScreen();

        stage.act();

        stage.draw();



    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}
