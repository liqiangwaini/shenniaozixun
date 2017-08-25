package com.xingbobase.api;

import android.annotation.TargetApi;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by WuJinZhou on 2016/1/6.
 */
@TargetApi(17)
public class PXAndroidJsSDK17Interface {
    private Context context;
    public PXAndroidJsSDK17Interface() {
    }

    public PXAndroidJsSDK17Interface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void helloPXAndroid(){

    }

    @JavascriptInterface
    public void helloPXAndroid(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


}
