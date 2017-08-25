package com.xingbo.live.emotion;

import android.graphics.drawable.Drawable;


public class EmotionEntity {

    private String mSource;

    private int mResourceId;

    private Drawable mDrawable;

    private String mCode;

    public static EmotionEntity fromAssert(String code, String assertPath) {
        EmotionEntity emotionEntity = new EmotionEntity();
        emotionEntity.mCode = code;
        emotionEntity.mSource = assertPath;
        return emotionEntity;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public int getResourceId() {
        return mResourceId;
    }

    public void setResourceId(int mResourceId) {
        this.mResourceId = mResourceId;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    @Override
    public String toString() {
        return "EmotionEntity{" +
                "mSource='" + mSource + '\'' +
                ", mCode='" + mCode + '\'' +
                '}';
    }

}
