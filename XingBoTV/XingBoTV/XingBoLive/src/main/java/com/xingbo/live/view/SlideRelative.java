package com.xingbo.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.xingbo.live.SystemApp;

/**
 * Created by xingbo_szd on 2016/7/29.
 */
public class SlideRelative extends RelativeLayout {
    public SlideRelative(Context context) {
        super(context);
    }

    public SlideRelative(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int startX=0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX= (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX= (int) event.getRawX();
                if((endX-startX)>5){
                    TranslateAnimation toRightAnimation=new TranslateAnimation(0,SystemApp.screenWidth,0,0);
                    toRightAnimation.setDuration(500);
                    this.setAnimation(toRightAnimation);
                    setVisibility(INVISIBLE);
                }else if((startX-endX)>5){
                    TranslateAnimation toLeftAnimation=new TranslateAnimation(SystemApp.screenWidth,0,0,0);
                    toLeftAnimation.setDuration(500);
                    this.setAnimation(toLeftAnimation);
                    setVisibility(VISIBLE);
                }
                break;
        }
        return true;
    }
}
