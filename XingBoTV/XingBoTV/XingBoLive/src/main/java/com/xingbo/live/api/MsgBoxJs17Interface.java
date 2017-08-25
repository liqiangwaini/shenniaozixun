package com.xingbo.live.api;

import android.webkit.JavascriptInterface;


/**
 * Created by WuJinZhou on 2016/2/18.
 */
public class MsgBoxJs17Interface {
    private MsgBoxJsCallBack callBack;

    public MsgBoxJs17Interface(MsgBoxJsCallBack callBack) {
        this.callBack = callBack;
    }

    @JavascriptInterface
    public void onReady(){
        callBack.onReady();
    }

    @JavascriptInterface
    public void popUserWin(String uid){
        if(callBack!=null){
           callBack.popUserWin(uid);
        }
    }

}
