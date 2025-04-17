package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.assets.AssetPaths;
import com.obstacleavoid.config.DifficultyLevel;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.Obstacle;
import com.obstacleavoid.entity.Player;
import com.obstacleavoid.util.GdxUtils;
import com.obstacleavoid.util.ViewportUtils;
import com.obstacleavoid.util.debug.DebugCameraController;

//making this class deprecated , deprecated means it should not be used
@Deprecated
public class GameScreenOld implements Screen {
//*updated
    private static final Logger log = new Logger(GameScreenOld.class.getName(), Logger.DEBUG);
  private OrthographicCamera camera;
  private Viewport viewport;
  private ShapeRenderer renderer;

  //IN ORDER to display our HUD we need a new camera and viewport
    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private SpriteBatch batch;
    private BitmapFont font;
    //layout of the font
    private final GlyphLayout layout = new GlyphLayout();
    private DebugCameraController debugCameraController;

    private Player player;
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private float obstacleTimer;
    private float scoreTimer;
    private int lives = GameConfig.LIVES_START;
    private int score;
    private int displayScore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.HARD;







    @Override
    public void show() { // show is like create we use it to initialise our game and load resources

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH , GameConfig.WORLD_HEIGHT , camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH , GameConfig.HUD_HEIGHT , hudCamera);
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(AssetPaths.UI_FONT));


        //create debug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X  , GameConfig.WORLD_CENTER_Y);


        //create player
        player = new Player();


        //Calculate position
        float startPlayerX = GameConfig.WORLD_WIDTH / 2f;
        float startPlayerY = 1;

        //position player
        player.setPosition(startPlayerX , startPlayerY);

    }

    @Override
    public void render(float delta ) {

        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);
        //update world


              update(delta);



        //Clear Screen
        GdxUtils.clearScreen();

        //render UI
        renderUi();

        //Render debug graphics
        renderDebug();
    }

    @Deprecated
    private void update(float delta){ //updated

        if(isGameOver()){
            log.debug("Game over !!!");
            return;
        }
        updatePlayer();
        updateObstacles(delta);
        updateScore(delta);
        updateDisplayScore(delta);

        if(isPlayerCollidingWithObstacles()){
           log.debug("Collision detected");
           lives--;
        }
    }

    private boolean isGameOver(){ //new
        return lives <= 0;
    }

    private boolean isPlayerCollidingWithObstacles(){

        for(Obstacle obstacle : obstacles){
            if(obstacle.isNotHit() &&  obstacle.isPlayerColliding(player)){
                return true;

            }
        }
        return false;
    }

    private void updatePlayer(){
       // player.update();
        blockPlayerFromLeavingTheWorld();
    }


    public void blockPlayerFromLeavingTheWorld(){

        float playerX = MathUtils.clamp(player.getX() , player.getWidth() /2f , GameConfig.WORLD_WIDTH-player.getWidth() /2f );
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
    }

    private void createNewObstacle(float delta){
        obstacleTimer += delta;
        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME){
            float min = 0f;
            float max = GameConfig.WORLD_WIDTH;
            //selecting x variable
            float obstacleX = MathUtils.random(min , max);
            //selecting y variable
            float obstacleY = GameConfig.WORLD_HEIGHT;

            Obstacle obstacle = new Obstacle();
            obstacle.setPosition(obstacleX , obstacleY);
            obstacle.setYSpeed(difficultyLevel.getObstacleSpeed());

            obstacles.add(obstacle);
            obstacleTimer = 0f;
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

    private void renderUi(){

                        batch.setProjectionMatrix(hudCamera.combined);//it is telling  batch about our camera position , rotation and zoom levels
                        batch.begin();
                        String livesText = "LIVES: " + lives;
                        layout.setText(font ,livesText);
                         font.draw(batch , livesText , 20 , GameConfig.HUD_HEIGHT - layout.height);

                        String scoreText = "SCORE: "+ displayScore ;
                        layout.setText(font , scoreText);

                        font.draw(batch , scoreText , GameConfig.HUD_WIDTH- layout.width-20 , GameConfig.HUD_HEIGHT- layout.height);


                        batch.end();
    }

    //for drawGrid we already have begin and end , but we still included it because to render we need begin and end
    private void renderDebug(){
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();
        renderer.end();
        ViewportUtils.drawGrid(viewport , renderer);
    }

    private void drawDebug(){
        player.drawDebug(renderer);
        for(Obstacle obstacle: obstacles){
            obstacle.drawDebug(renderer);
        }

    }




    @Override
    public void dispose() { // remember that dispose is not called automatically
     renderer.dispose();
     batch.dispose();
     font.dispose();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width , height , true);
        hudViewport.update(width , height , true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() { // Used when we hide our screen e.g when we go from one screen to another
        // whenever we hide our screen we should dispose , however it is not always appropriate to dispose when we hide
        dispose();
    }
}
