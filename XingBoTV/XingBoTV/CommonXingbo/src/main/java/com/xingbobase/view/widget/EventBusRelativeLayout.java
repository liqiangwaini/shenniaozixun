package com.xingbobase.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.xingbobase.eventbus.InterceptTouchEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by WuJinZhou on 2016/1/14.
 */
public class EventBusRelativeLayout extends RelativeLayout {
    public EventBusRelativeLayout(Context context) {
        super(context);
    }

    public EventBusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventBusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(21)
    public EventBusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            EventBus.getDefault().post(new InterceptTouchEvent(getId(),ev.getX(),ev.getY()));
            //XingBoUtil.log(TAG,"EventBus has post event to object which register EventBus,the source id is:"+getId());
        }
        return super.onInterceptTouchEvent(ev);
    }
}
