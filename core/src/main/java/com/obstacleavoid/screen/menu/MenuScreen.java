package com.obstacleavoid.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.assets.RegionNames;
import com.obstacleavoid.screen.game.GameScreen;

import org.w3c.dom.Text;

public class MenuScreen extends MenuScreenBase {

    private static final Logger log = new Logger(MenuScreen.class.getName(), Logger.DEBUG);

    //private Sound introSound;

    public MenuScreen(ObstacleAvoidGame game){
       super(game);
    }

    protected Actor createUi(){
        Table table = new Table();

        TextureAtlas gamePlayAtlas =  assetManager.get(AssetDescriptors.GAME_PLAY);
        Skin uiSkin = assetManager.get(AssetDescriptors.UI_SKIN);
        TextureRegion backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        table.setBackground(new TextureRegionDrawable(backgroundRegion));
        //introSound = assetManager.get(AssetDescriptors.INTRO_SOUND);

        //introSound.loop();

        //play button


        TextButton playButton = new TextButton("PLAY" , uiSkin);
        playButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        play();
                    }
                }
        );


        //highScore button
        TextButton highScoreButton = new TextButton("HIGHSCORE" , uiSkin);

        highScoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHighScore();
            }
        });
        //options button

        TextButton optionsButton = new TextButton("OPTIONS" , uiSkin);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showOptions();
            }
        });
        //quit button

        TextButton quitButton = new TextButton("QUIT" , uiSkin);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                quit();
            }
        });


        //setup table
        Table buttonTable = new Table(uiSkin);
        buttonTable.defaults().pad(20);
        buttonTable.setBackground(RegionNames.PANEL);

        buttonTable.add(playButton).row();
        buttonTable.add(highScoreButton).row();
        buttonTable.add(optionsButton).row();
        buttonTable.add(quitButton).row();

        buttonTable.center();

        table.add(buttonTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;



    }



    private void play(){
        log.debug("play()");
        game.setScreen(new GameScreen(game));
       // introSound.stop();

    }

    private void showHighScore(){
        log.debug("showHighScore()");
        game.setScreen(new HighScoreScreen(game));

        //introSound.stop();
    }

    private void showOptions(){
        log.debug("showOptions()");

        game.setScreen(new OptionsScreen(game));
        //introSound.stop();
    }


    private void quit(){
        log.debug("quit()");
        Gdx.app.exit();
    }
}
