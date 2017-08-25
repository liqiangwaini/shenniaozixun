package com.xingbo.live.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.RotateAnimation;

import com.xingbo.live.SystemApp;
import com.xingbo.live.eventbus.ScrollGiftEvent;

import de.greenrobot.event.EventBus;

/**
 * Created by xingbo_szd on 2016/8/31.
 */
public class AnimatorUtils {
    public static void showScrollGift(View view,int textWidth){
        Animator animatorX = ObjectAnimator.ofFloat(view, "translationX", SystemApp.screenWidth, -textWidth);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX);
        animatorSet.setDuration(15000);
        animatorSet.start();
    }

    public static void rotate90(View view){
        RotateAnimation rotateAnimation=new RotateAnimation(0,90,0.5f,0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.start();
    }

}
