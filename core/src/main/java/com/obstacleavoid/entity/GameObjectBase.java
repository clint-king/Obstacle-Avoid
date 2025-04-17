package com.obstacleavoid.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.obstacleavoid.config.GameConfig;

public abstract class GameObjectBase {
    private float x;
    private float y;
    private float width = 1;
    private float height = 1;
    private Circle bounds;

    public GameObjectBase(float boundsRadius) {
        bounds = new Circle(x , y , boundsRadius);

    }


    public void drawDebug(ShapeRenderer renderer) {
        renderer.x(bounds.x , bounds.y , 0.1f);
        renderer.circle(bounds.x, bounds.y, bounds.radius, 30);

    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateBounds(); // this is to make sure the player/circle gets to move to a requested position
    }

    public void updateBounds(){
        float halfWidth = width/2f;
        float halfHeight = height/2f;

        bounds.setPosition(halfWidth+ x, halfHeight+ y );
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public void setY(float y) {
        updateBounds();
        this.y = y;
    }

    public void setSize(float width , float height){
        this.width =  width;
        this.height =  height;
        updateBounds();

    }


    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    public Circle getBounds() {
        return bounds;
    }


}
