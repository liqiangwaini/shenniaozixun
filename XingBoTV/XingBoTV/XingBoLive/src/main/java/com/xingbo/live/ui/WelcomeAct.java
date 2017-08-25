package com.xingbo.live.ui;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import com.xingbobase.config.XingBo;
import com.xingbo.live.R;
import com.xingbo.live.broadcast.InternetStateBroadcast;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.ui.XingBoBaseAct;
import com.xingbobase.util.XingBoUtil;

/**
 * 欢迎界面
 * Created by WuJinZhou on 2015/8/8.
 */
public class WelcomeAct extends XingBoBaseAct{
    protected final static String TAG="WelcomeAct";
    private final static int APP_INIT_FINISH=0x1;
    private SharedPreferences sp;
    private String uid;
    private Boolean isLogin=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences(XingBo.PX_USER_CACHE_FILE,Context.MODE_PRIVATE);
        if(sp.getBoolean(XingBo.PX_CONFIG_CACHE_IS_FIRST_RUN,true)){//第一次运行
            Intent guide=new Intent(this,Guide.class);
            startActivity(guide);
            sp.edit().putBoolean(XingBo.PX_CONFIG_CACHE_IS_FIRST_RUN,false).commit();//默认已运行
            finish();
            return;
        }
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setContentView(R.layout.xingbo_welcome);
        handler.sendEmptyMessageDelayed(APP_INIT_FINISH, 3000);//默认播放3秒图片动画后再检测是否可以跳转
        initApp();
    }

//    private void CheckVersionTask() {
//        try {
//            currentVersion = getVersionName();
//            Log.d("tag","currentVersion-->"+currentVersion);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        RequestParam param = RequestParam.builder(this);
//        param.put("device", "android");
//        CommonUtil.request(this, HttpConfig.API_APP_GET_AUTODATE_INSTALLPACKAGE, param, TAG, new XingBoResponseHandler<UpdateInfoModel>(UpdateInfoModel.class) {
//            @Override
//            public void phpXiuErr(int errCode, String msg) {
//                alert(msg);
//            }
//
//            @Override
//            public void phpXiuSuccess(String response) {
//                serverVersion = model.getD().getPackage_name();
//                Log.d("tag", "serverVersion" + serverVersion);
//
//            }
//        });
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化应用
     */
    public void initApp(){
        sp=getSharedPreferences(XingBo.PX_CONFIG_CACHE_FILE, Context.MODE_PRIVATE);
        //检测当前网络类型
        //checkNetType();
        checkInternet();
        if(sp.getBoolean(XingBo.PX_CONFIG_CACHE_IS_FIRST_RUN,true)) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();
            XingBoUtil.log(TAG, "deviceId:" + deviceId);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(XingBo.PX_CONFIG_CACHE_DEVICE_ID, deviceId == null || deviceId.equals("") ? "badId" : deviceId);
            editor.putString(XingBo.PX_CONFIG_CACHE_DEVICE_MODEL, android.os.Build.MODEL);
            editor.putBoolean(XingBo.PX_CONFIG_CACHE_IS_FIRST_RUN, false);
            editor.commit();
        }
    }

    public void handleMsg(Message msg) {
        //此处不处理断网或超时情况
        switch (msg.what){
            case APP_INIT_FINISH:
                goHome();
                break;
            default:
                break;
        }
    }

    /**
     * 检测网络连接状态
     */
    private void checkInternet(){
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
            //Toast.makeText(context, "手机网络连接成功！", Toast.LENGTH_SHORT).show();
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_MOBILE).commit();
            CommonUtil.log(TAG,"当前是手机网络");
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED == wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            //Toast.makeText(context, "无线网络连接成功！", Toast.LENGTH_SHORT).show();
            CommonUtil.log(TAG,"当前是无线wifi网络");
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK,InternetStateBroadcast.NET_WIFI).commit();
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            //Toast.makeText(context, "手机没有任何网络...", Toast.LENGTH_SHORT).show();
            CommonUtil.log(TAG,"当前没有任何网络");
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK,InternetStateBroadcast.NET_NO).commit();
        }
    }

    /**
     * 前往主界面
     */
    public void goHome(){
        sp=getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        CommonUtil.log(TAG,"PX_USER_LOGIN_UID:  " + sp.getString(XingBo.PX_USER_LOGIN_UID, "0"));
        if(sp.getString(XingBo.PX_USER_LOGIN_UID,"0").equals("0")){ //往登录界面
            Intent intent = new Intent(this, LoginAct.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }



    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
