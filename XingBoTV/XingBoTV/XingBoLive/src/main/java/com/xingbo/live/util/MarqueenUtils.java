package com.xingbo.live.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.xingbo.live.SystemApp;

/**
 * Created by xingbo_szd on 2016/8/24.
 */
public class MarqueenUtils {

    private static Animator animatorX;
    private static AnimatorSet animatorSet;

    public static void show(View view,int textWidth){
        animatorX = ObjectAnimator.ofFloat(view, "translationX", SystemApp.screenWidth,-textWidth-10);
        animatorSet = new AnimatorSet();
        animatorSet.play(animatorX);
        animatorSet.setDuration(15000);
        animatorSet.start();
    }
}
