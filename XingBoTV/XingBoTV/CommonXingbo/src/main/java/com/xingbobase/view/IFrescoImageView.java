package com.xingbobase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by WuJinZhou on 2016/2/3.
 */
public class IFrescoImageView extends SimpleDraweeView {
    public IFrescoImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public IFrescoImageView(Context context) {
        super(context);
    }

    public IFrescoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IFrescoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public IFrescoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}
