package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.assets.RegionNames;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.Background;
import com.obstacleavoid.entity.Obstacle;
import com.obstacleavoid.entity.Player;
import com.obstacleavoid.util.GdxUtils;
import com.obstacleavoid.util.ViewportUtils;
import com.obstacleavoid.util.debug.DebugCameraController;

public class GameRenderer implements Disposable {


    //==attributes ==
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    //IN ORDER to display our HUD we need a new camera and viewport
    private OrthographicCamera hudCamera;
    private Viewport hudViewport;


    private BitmapFont font;
    //layout of the font
    private final GlyphLayout layout = new GlyphLayout();
    private DebugCameraController debugCameraController;

    private final GameController controller;

    private TextureRegion playerRegion;
    private TextureRegion obstacleRegion;
    private TextureRegion backgroundRegion;

    private final AssetManager assetManager;
    private final SpriteBatch batch;
    //== constructors ==
    public GameRenderer(SpriteBatch batch , AssetManager assetManager , GameController controller) {
        this.batch = batch;
        this.assetManager = assetManager;
        this.controller = controller;
        init();
    }

    private void init(){
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH , GameConfig.WORLD_HEIGHT , camera);
        renderer = new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH , GameConfig.HUD_HEIGHT , hudCamera);

        font =assetManager.get(AssetDescriptors.FONT);

        //create debug camera controller
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X  , GameConfig.WORLD_CENTER_Y);

        TextureAtlas gamePlayAtlas =  assetManager.get(AssetDescriptors.GAME_PLAY);

        playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        obstacleRegion =gamePlayAtlas.findRegion(RegionNames.OBSTACLE);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
    }

    //==public ==
    public void render(float delta){

        batch.totalRenderCalls = 0;
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        if (Gdx.input.isTouched() && !controller.isGameOver()){
            Vector2 screenTouch = new Vector2(Gdx.input.getX() , Gdx.input.getY());
            //unproject , will project the screen coordinates to our worldUnits coordinates.
            //we used new Vector2(screenTouch) so that screenTouch is not Modified

            Vector2 worldTouch = viewport.unproject(new Vector2(screenTouch));

            System.out.println("screenTouch= "+ screenTouch);
            System.out.println("worldTouch= "+ worldTouch);

            Player player = controller.getPlayer();
            worldTouch.x = MathUtils.clamp(worldTouch.x , 0 , GameConfig.WORLD_WIDTH - player.getWidth());
            player.setX(worldTouch.x);
        }

        //Clear Screen
        GdxUtils.clearScreen();

        rendererGamePlay();

        //render UI
        renderUi();

        //Render debug graphics
        //renderDebug();


        //System.out.println("totalRenderCalls= "+ batch.totalRenderCalls);
    }


    public void resize(int width, int height) {
        //we need to tell for the viewport that there has been some resizing
        viewport.update(width , height , true);
        hudViewport.update(width , height , true);
        ViewportUtils.debugPixelPerUnit(viewport);
    }

    @Override
    public void dispose() {
        renderer.dispose();


    }

    private void rendererGamePlay(){
        viewport.apply();

        //it is telling  batch about our camera position , rotation and zoom levels
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //draw background
        Background background = controller.getBackground();
        batch.draw(backgroundRegion, background.getX() , background.getY() , background.getWidth() , background.getHeight() );


        //draw player
        Player player = controller.getPlayer();
        batch.draw(playerRegion , player.getX() , player.getY() , player.getWidth() , player.getHeight());

        //draw obstacle
        for(Obstacle obstacle : controller.getObstacles()){
            batch.draw(obstacleRegion,
                    obstacle.getX() , obstacle.getY() , obstacle.getWidth(), obstacle.getHeight());
        }
        batch.end();


    }


    //==private methods==
    private void renderUi(){

        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);//it is telling  batch about our camera position , rotation and zoom levels
        batch.begin();
        String livesText = "LIVES: " + controller.getLives();
        layout.setText(font ,livesText);
        font.draw(batch , livesText , 20 , GameConfig.HUD_HEIGHT - layout.height);


        String scoreText = "SCORE: "+ controller.getDisplayScore()  ;
        layout.setText(font , scoreText);

        font.draw(batch , scoreText , GameConfig.HUD_WIDTH- layout.width-20 , GameConfig.HUD_HEIGHT- layout.height);


        batch.end();
    }

    //for drawGrid we already have begin and end , but we still included it because to render we need begin and end
    private void renderDebug(){
        //this can prevent some problems that could arise in the future. when ever we use multiple viewports that have different sizes
        //we need to apply .also to projection to metrics
       viewport.apply();


        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        drawDebug();
        renderer.end();
        ViewportUtils.drawGrid(viewport , renderer);
    }

    private void drawDebug(){
        Player player = controller.getPlayer();
        player.drawDebug(renderer);

        Array<Obstacle> obstacles = controller.getObstacles();
        for(Obstacle obstacle: obstacles){
            obstacle.drawDebug(renderer);
        }

}
}
