package com.obstacleavoid.config;

//enums by deafult they are public static final
public enum DifficultyLevel {

    EASY(GameConfig.EASY_OBSTACLE_SPEED),
    MEDIUM(GameConfig.MEDIUM_OBSTACLE_SPEED),
    HARD(GameConfig.HARD_OBSTACLE_SPEED);

    private final float obstacleSpeed;

    //The constructor cannot be public
    DifficultyLevel(float obstcleSpeed){
        this.obstacleSpeed = obstcleSpeed;
    }

    public float getObstacleSpeed(){
       return obstacleSpeed;
    }

    public boolean isEasy(){
      return  this == EASY;
    }

    public boolean isMedium(){
        return  this == MEDIUM;
    }

    public boolean isHard(){
        return  this == HARD;
    }
}
