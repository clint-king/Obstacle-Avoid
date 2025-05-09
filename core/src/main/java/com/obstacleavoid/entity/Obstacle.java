package com.obstacleavoid.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.obstacleavoid.config.GameConfig;

public class
Obstacle extends GameObjectBase implements Pool.Poolable{

    private float ySpeed =  GameConfig.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;


    public Obstacle() {
        super(GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE , GameConfig.OBSTACLE_SIZE);
    }


    public void  update() {
        setPosition(getX() , getY()-ySpeed);
        updateBounds();
    }



    public  boolean isPlayerColliding(Player player){
        Circle playerBounds = player.getBounds();

        //if player bounds overlap obstacle bounds

       boolean overlaps = Intersector.overlaps(playerBounds, this.getBounds());
//       if(overlaps){
//           hit = true;
//       }
       //better way
       hit = overlaps;
       return overlaps;
    }

    public boolean isNotHit(){
        return !hit;
    }

    public  void setYSpeed(float obstacleSpeed){
        this.ySpeed = obstacleSpeed;
    }

    public void reset(){
        hit = false;
    }

}
