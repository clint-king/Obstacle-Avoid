package com.obstacleavoid.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.config.DifficultyLevel;


public class GameManager {
    public static final GameManager INSTANCE  = new GameManager();

    private static final String HIGH_SCORE_KEY = "highscore";
    private static final String DIFFICULTY_KEY  = "difficulty";


    //used to store light wait things in the game
    private Preferences PREFS;

    private int highScore ;
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM;



    private GameManager(){
       // getPreferences parameter is the name of the file , which in this case is the name of our game
        PREFS = Gdx.app.getPreferences(ObstacleAvoidGame.class.getSimpleName());
        highScore = PREFS.getInteger(HIGH_SCORE_KEY , 0);

        String difficultyName  = PREFS.getString(DIFFICULTY_KEY , DifficultyLevel.MEDIUM.name());
        difficultyLevel = DifficultyLevel.valueOf(difficultyName);
    }

    public void updateHighScore(int score){
        if(score < highScore){
            return ;
        }

        highScore = score;
        PREFS.putInteger(HIGH_SCORE_KEY , highScore);

        //to save
        PREFS.flush();
    }

    public  String getHighScoreString(){
        return String.valueOf(highScore);
    }

    public DifficultyLevel getDifficultyLevel(){
        return difficultyLevel;
    }

    public void updateDifficulty(DifficultyLevel newDifficultyLevel){
        if(difficultyLevel == newDifficultyLevel){
            return;
        }

        difficultyLevel = newDifficultyLevel;
        PREFS.putString(DIFFICULTY_KEY , difficultyLevel.name());
        PREFS.flush();
    }
}
