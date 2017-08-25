package com.xingbobase.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by WuJinZhou on 2016/1/7.
 */
public class XingBoEditText extends EditText {
    public XingBoEditText(Context context) {
        super(context);
    }

    public XingBoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XingBoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public XingBoEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}
