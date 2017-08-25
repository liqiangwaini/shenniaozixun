package com.xingbo.live.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.xingbo.live.eventbus.OnInternetChangeEvent;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;

import de.greenrobot.event.EventBus;

/**
 * Created by WuJinZhou on 2016/3/10.
 */
public class InternetStateBroadcast extends BroadcastReceiver {
    public final static String TAG = "InternetStateBroadcast";
    private SharedPreferences sp;
    public final static String NET_NO = "NO";
    public final static String NET_WIFI = "WIFI";
    public final static String NET_MOBILE = "MOBILE";
    private State wifiState = null;
    private State mobileState = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (sp == null) {
            sp = context.getSharedPreferences(XingBo.PX_CONFIG_CACHE_FILE, Context.MODE_PRIVATE);
        }
        // TODO Auto-generated method stub
        //获取手机的连接服务管理器，这里是连接管理器类
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED == mobileState) {
            //Toast.makeText(context, "手机网络连接成功！", Toast.LENGTH_SHORT).show();
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_MOBILE).commit();
            EventBus.getDefault().post(new OnInternetChangeEvent(NET_MOBILE));
            CommonUtil.log(TAG, "网络变为移动网络");
        } else if (wifiState != null && mobileState != null && State.CONNECTED == wifiState && State.CONNECTED != mobileState) {
            //Toast.makeText(context, "无线网络连接成功！", Toast.LENGTH_SHORT).show();
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_WIFI).commit();
            EventBus.getDefault().post(new OnInternetChangeEvent(NET_WIFI));
            CommonUtil.log(TAG, "网络变为无线网络");
        } else if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED != mobileState) {
            //  Toast.makeText(context, "手机没有任何网络...", Toast.LENGTH_SHORT).show();
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_NO).commit();
            EventBus.getDefault().post(new OnInternetChangeEvent(NET_NO));
            CommonUtil.log(TAG, "网络已断开，无任何网络连接");

        }
    }

}
