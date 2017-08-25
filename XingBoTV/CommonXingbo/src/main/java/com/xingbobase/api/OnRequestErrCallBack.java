package com.xingbobase.api;

/**
 * Created by WuJinZhou on 2016/2/22.
 */
public interface OnRequestErrCallBack {
    void loginErr(int errCode,String msg);
    void costErr(int errCode,String msg);

}
