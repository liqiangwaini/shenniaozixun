package com.xingbobase;

import android.app.Activity;
import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xingbobase.util.XingBoUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by WuJinZhou on 2015/8/8.
 */
public class XingBoSystemApp extends Application {

    public static final String TAG = "XingBoSystemApp";

    public static float screenWidth=0;//屏幕宽px
    public static float screenHeight=0;//屏幕高px
    private static XingBoSystemApp instance;
    private List<Activity> activitys = null;

    public synchronized static XingBoSystemApp getInstance() {
        if (null == instance) {
            instance = new XingBoSystemApp();
        }
        screenWidth=instance.getResources().getDisplayMetrics().widthPixels;
        screenHeight=instance.getResources().getDisplayMetrics().heightPixels;
        return instance;
    }

    public XingBoSystemApp(){
        activitys = new LinkedList<Activity>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this).setDownsampleEnabled(true).build();
        XingBoUtil.log(TAG, "Fresco MainDiskCacheConfig path and system default cache dir:" + config.getMainDiskCacheConfig().getBaseDirectoryPathSupplier().get().getPath() + "-->" + getCacheDir());
        Fresco.initialize(this, config);
//        ImageLoaderConfiguration imageLoaderConfig = new ImageLoaderConfiguration.Builder(this).build();
//        ImageLoader.getInstance().init(imageLoaderConfig);
//        XingBoUtil.log(TAG, "Fresco small image disk cache config Base directory name :" + config.getSmallImageDiskCacheConfig().getBaseDirectoryName() + "---ImageLoader disk cache:" + ImageLoader.getInstance().getDiskCache().getDirectory().getPath());
        //JPushInterface.setDebugMode(true);
        //JPushInterface.init(this);
        instance = this;
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if(!activitys.contains(activity)){
                activitys.add(activity);
            }
        }else{
            activitys.add(activity);
        }

    }

    // 遍历所有Activity并finish
    public void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
        System.exit(0);
    }
}
