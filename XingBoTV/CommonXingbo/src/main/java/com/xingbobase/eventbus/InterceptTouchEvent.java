package com.xingbobase.eventbus;

/**
 * Created by WuJinZhou on 2016/1/14.
 */
public class InterceptTouchEvent {
    //事件源视图id
    private int sourceId;
    private float x;
    private float y;

    public InterceptTouchEvent(int sourceId, float x, float y) {
        this.sourceId = sourceId;
        this.x = x;
        this.y = y;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
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
