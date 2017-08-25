package com.xingbo.live.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by WuJinZhou on 2016/3/17.
 */
public class ListBoxRelativeLayout extends RelativeLayout{
    private OnTouchDownListener onPressListener;

    public ListBoxRelativeLayout(Context context) {
        super(context);
    }

    public ListBoxRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListBoxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ListBoxRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
           if(onPressListener!=null) {
               onPressListener.onPress(ev);
           }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public OnTouchDownListener getOnPressListener() {
        return onPressListener;
    }

    public void setOnPressListener(OnTouchDownListener onPressListener) {
        this.onPressListener = onPressListener;
    }

    /**
     * 对外接口
     */
    public interface OnTouchDownListener {
        void onPress(MotionEvent ev);
    }
}
