package com.xingbo.live.api;


/**
 * Created by WuJinZhou on 2016/2/18.
 */
public class MsgBoxJsInterface {
    private MsgBoxJsCallBack callBack;

    public MsgBoxJsInterface(MsgBoxJsCallBack callBack) {
        this.callBack = callBack;
    }

    public void onReady(){
        callBack.onReady();
    }

    public void popUserWin(String uid){
        if(callBack!=null){
            callBack.popUserWin(uid);
        }
    }
}
