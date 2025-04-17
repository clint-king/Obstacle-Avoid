package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.screen.menu.MenuScreen;


public class GameScreen implements Screen {
    private GameController controller;
    private GameRenderer renderer;

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;


    public GameScreen(ObstacleAvoidGame game){
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        //WE cant make GameController accept a GameRenderer parameter because that will cause circular dependancy (One class cannot live
        //without the other)

        controller = new GameController(game);

        renderer = new GameRenderer(game.getBatch(), assetManager, controller);
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        renderer.render(delta);

        //we are doing this here instead of doing it in GameController to prevent crushes
        if(controller.isGameOver()){
            game.setScreen(new MenuScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {

        renderer.resize(width , height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();

    }


}
