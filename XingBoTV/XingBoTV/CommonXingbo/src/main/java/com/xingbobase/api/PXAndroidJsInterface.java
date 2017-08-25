package com.xingbobase.api;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by WuJinZhou on 2016/1/6.
 */
public class PXAndroidJsInterface {
    private Context context;
    public PXAndroidJsInterface() {
    }

    public PXAndroidJsInterface(Context context) {
        this.context = context;
    }

    public void helloPXAndroid(){

    }

    public void helloPXAndroid(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
