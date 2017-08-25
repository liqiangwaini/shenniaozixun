package com.xingbo.live.entity.anim;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.util.Pair;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.xingbo.live.SystemApp;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by WuJinZhou on 2015/12/18.
 */
public final class GiftAnimatorSet {
    private final static long DURATION=2000;//动画播放(拼图过程)时间
    private Context context;
    public final List<FrescoImageView>ICONS;
    private RelativeLayout.LayoutParams iconLayoutParam;
    private String preIconUrl="-1";
    public AnimatorSet animatorSet;
    private GiftShape shape;
    private Random randomXY;
    private LinearInterpolator interpolator=new LinearInterpolator();
    private Animator.AnimatorListener listener;
    /**
     * @param key 坐标配置xml文件名
     */
    public GiftAnimatorSet(Context context,String key) {
        this.context=context;
        Pair<Integer, Integer> screen= XingBoUtil.getResolution(context);
        ICONS=new ArrayList<FrescoImageView>();
        shape=new GiftShape(key,GiftShape.parseXMLPointsFile(context, key));
        shape.initShape(context,screen);
        iconLayoutParam= new RelativeLayout.LayoutParams(shape.iconSize, shape.iconSize);
        randomXY=new Random();
        animatorSet= new AnimatorSet();
        initAnimatorSet(context);
    }

    public void initAnimatorSet(Context context){
        FrescoImageView icon;
        List<ObjectAnimator>list=new ArrayList<ObjectAnimator>();
        for (int index =0; index < shape.getPoints().size(); index++) {
            icon = new FrescoImageView(context);
            icon.setLayoutParams(iconLayoutParam);
            ObjectAnimator animatorX=new ObjectAnimator();
            animatorX.setTarget(icon);
            animatorX.setPropertyName("translationX");
            animatorX.setFloatValues(randomXY.nextInt((int)SystemApp.getInstance().screenWidth), shape.getPoints().get(index).getX());
            animatorX.setInterpolator(interpolator);
            list.add(animatorX);
            ObjectAnimator animatorY=new ObjectAnimator();
            animatorY.setTarget(icon);
            animatorY.setPropertyName("translationY");
            animatorY.setFloatValues(randomXY.nextInt((int)SystemApp.getInstance().screenHeight), shape.getPoints().get(index).getY());
            animatorY.setInterpolator(interpolator);
            list.add(animatorY);
            ICONS.add(icon);
        }
        ObjectAnimator [] animators=new ObjectAnimator[list.size()];
        int j=0;
        for (ObjectAnimator animator:list){
            animators[j]=animator;
            j++;
        }
        animatorSet.playTogether(animators);
        animatorSet.setDuration(DURATION);

    }

    /**
     * 礼物不同，拼成动画图片不同,前后两次图片url相同，则不重新load
     * @param iconUrl 礼物图片url;若是[鲜花x10]则iconUrl为空
     */
    public GiftAnimatorSet updateIconsSrc(String iconUrl){
        //第一次礼物
        if(preIconUrl.equals("-1")&&iconUrl!=null){
            for (FrescoImageView icon:ICONS){
                icon.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+iconUrl));
            }
            preIconUrl=iconUrl;
            return this;
        }
        //之前为鲜花x10
        if(preIconUrl.equals("0")&&iconUrl!=null){
            for (FrescoImageView icon:ICONS){
                icon.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+iconUrl));
            }
            preIconUrl=iconUrl;
            return this;
        }
        //鲜花x10
        if(preIconUrl.equals("-1")&&iconUrl==null){
            for (FrescoImageView icon:ICONS){
                icon.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+iconUrl));
            }
            preIconUrl="0";
            return this;
        }
        //礼物
        if(iconUrl!=null&&!preIconUrl.equals(iconUrl)){
            for (FrescoImageView icon:ICONS){
                icon.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+iconUrl));
            }
            preIconUrl=iconUrl;
        }
        //鲜花x10
        if(iconUrl==null&&!preIconUrl.equals("0")){
            for (FrescoImageView icon:ICONS){
                icon.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+iconUrl));
            }
            preIconUrl="0";
        }
        return this;
    }

    @Deprecated
    public void addIcons(RelativeLayout root){
        for (FrescoImageView icon:ICONS){
            try{
                root.addView(icon);
            }catch (IllegalStateException e){
                root.removeView(icon);
                root.addView(icon);
            }

        }
    }

    @Deprecated
    public boolean clearAnimIcons(RelativeLayout root){
        for (FrescoImageView icon:ICONS){
            root.removeView(icon);
        }
        return false;
    }

    /**
     * 添加监听
     */
    public void addListener(Animator.AnimatorListener listener) {
        this.listener = listener;
        if(listener!=null){
           animatorSet.addListener(listener);
        }else{
           animatorSet.removeAllListeners();
        }
    }
}
