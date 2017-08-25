package com.xingbo.live.entity.anim;

/**
 * Created by WuJinZhou on 2015/12/19.
 */
public class GiftShapePoint {
    private float x;
    private float y;

    public GiftShapePoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void scale(float scale){
        this.x=this.x/scale;
        this.y=this.y/scale;
    }

    public void conversionY(float startY){
        this.y=startY+this.y;
    }

    public void conversionX(float startX){
        this.x=this.x+startX;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
