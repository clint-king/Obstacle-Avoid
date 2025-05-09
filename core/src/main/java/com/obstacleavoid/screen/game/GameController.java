package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.common.GameManager;
import com.obstacleavoid.config.DifficultyLevel;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.Background;
import com.obstacleavoid.entity.Obstacle;
import com.obstacleavoid.entity.Player;

public class GameController {

    //== constants ==
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    //==attributes==
    private Player player;
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private Pool<Obstacle> obstaclePool;
    private final float startPlayerX = (GameConfig.WORLD_WIDTH - GameConfig.PLAYER_SIZE)/ 2f;
    private final float startPlayerY = 1 - GameConfig.PLAYER_SIZE / 2f;

    private Sound hit;
    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private Background background;

    //== constructor ==
    public GameController(ObstacleAvoidGame game){
        this.game = game;
        assetManager = game.getAssetManager();
        init();
    }


    private void init(){


        //create player
        player = new Player();
        //Calculate position



        //position player
        player.setPosition(startPlayerX , startPlayerY);

        //obstacle pool (with reflection pool you need to have no argument constructor)
        obstaclePool = Pools.get(Obstacle.class , 40);

        //  create background
        background = new Background();
        background.setPosition(0 , 0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        hit = assetManager.get(AssetDescriptors.HIT_SOUND);
    }


    //==public methods ==
    public void update(float delta){

        if(isGameOver()){
            return ;
        }
        updatePlayer();
        updateObstacles(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if(isPlayerCollidingWithObstacles()){
            log.debug("Collision detected");
            lives--;
            if(isGameOver()){
                log.debug("Game Over!!");
                GameManager.INSTANCE.updateHighScore(score);
            }else{
                restart();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public Background getBackground(){
        return background;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public boolean isGameOver(){ //new

        return lives <= 0;
    }


    //==private methods ==

    private void restart(){
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX , startPlayerY);
    }


    private boolean isPlayerCollidingWithObstacles(){

        for(Obstacle obstacle : obstacles){
            if(obstacle.isNotHit() &&  obstacle.isPlayerColliding(player)){
                hit.play();
                return true;
            }
        }
        return false;
    }

    private void updatePlayer(){

            float xSpeed = 0;

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                xSpeed = GameConfig.MAX_PLAYER_X_SPEED;

            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;
            }

            player.setX(player.getX()+ xSpeed);


        blockPlayerFromLeavingTheWorld();
    }


    public void blockPlayerFromLeavingTheWorld(){

        float playerX = MathUtils.clamp(player.getX() , 0, GameConfig.WORLD_WIDTH-player.getWidth());
//U CAN USE CLAMP OR THIS LOGIC
        //       float playerX = player.getX();
//        if(playerX < player.getWidth()/2){
//            playerX = player.getWidth()/2;
//        }else if(playerX > (GameConfig.WORLD_WIDTH -player.getWidth()/2) ){
//            playerX = (GameConfig.WORLD_WIDTH -player.getWidth()/2);
//        }

        player.setPosition(playerX , player.getY());

    }


    private void updateObstacles(float delta){

        for(Obstacle obstacle : obstacles){
            //here every obstacle that has been created is reduced its y value
            obstacle.update();
        }

        // for every allocated time an obstacle is created
        createNewObstacle(delta);
        removePassedObstacles();
    }

    private void createNewObstacle(float delta){
        obstacleTimer += delta;

        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
            float min = 0;
            float max = GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE;

            //selecting x variable
            float obstacleX = MathUtils.random(min , max);
            //selecting y variable
            float obstacleY = GameConfig.WORLD_HEIGHT;


            Obstacle obstacle = obstaclePool.obtain();
            DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
            obstacle.setPosition(obstacleX , obstacleY);
            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());

            obstacles.add(obstacle);
            obstacleTimer = 0f;
        }

    }
    private void removePassedObstacles(){
        if(obstacles.size > 0){
            Obstacle first = obstacles.first();
            //we did not set it as zero because it would have an effect of obstacles disappearing
            float minObstacleY = -GameConfig.OBSTACLE_SIZE;

            if(first.getY() < minObstacleY){
                obstacles.removeValue(first , true);
                obstaclePool.free(first);
            }
        }
    }

    private void updateScore(float delta){
        scoreTimer += delta;

        if(scoreTimer >= GameConfig.SCORE_MAX_TIME){
            score += MathUtils.random(1 , 5); //range from 1 to 4 (first parameter inclusive and second parameter exclusive)
            scoreTimer = 0.0f;

        }
    }

    private void updateDisplayScore(float delta){
        //display prevents the score numbers from jumping very high ,
        //delta usually is 1/60 , with 60 representing the number of screens per second
        if(displayScore < score){
            displayScore  = Math.min(score , displayScore + (int) (60 * delta));
        }
    }

}
