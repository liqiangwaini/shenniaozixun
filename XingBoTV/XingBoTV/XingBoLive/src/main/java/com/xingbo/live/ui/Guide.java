package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xingbobase.config.XingBo;
import com.xingbo.live.R;
import com.xingbo.live.adapter.GuideViewPagerAdapter;
import com.xingbo.live.broadcast.InternetStateBroadcast;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.util.XingBoUtil;

/**
 * Created by WuJinZhou on 2016/2/28.
 */
public class Guide extends BaseAct implements ViewPager.OnPageChangeListener {
    public final static String TAG = "Guide";
    private ViewPager guideGroup;
    private GuideViewPagerAdapter mAdapter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        initApp();
        initView();
    }

    public void initView() {
        guideGroup = (ViewPager) findViewById(R.id.guide_view_pager);
        mAdapter = new GuideViewPagerAdapter(Guide.this, this);
        guideGroup.addOnPageChangeListener(this);
        guideGroup.setAdapter(mAdapter);
    }

    /**
     * 初始化应用
     */
    public void initApp() {
        sp = getSharedPreferences(XingBo.PX_CONFIG_CACHE_FILE, Context.MODE_PRIVATE);
        //检测当前网络类型
        //checkNetType();
        checkInternet();
        if (sp.getBoolean(XingBo.PX_CONFIG_CACHE_IS_FIRST_RUN, true)) {
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

    /**
     * 检测网络连接状态
     */
    private void checkInternet() {
        NetworkInfo.State wifiState = null;
        NetworkInfo.State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm==null){
            return;
        }
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
            //Toast.makeText(context, "手机网络连接成功！", Toast.LENGTH_SHORT).show();
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_MOBILE).commit();
            CommonUtil.log(TAG, "当前是手机网络");
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED == wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            //Toast.makeText(context, "无线网络连接成功！", Toast.LENGTH_SHORT).show();
            CommonUtil.log(TAG, "当前是无线wifi网络");
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_WIFI).commit();
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            //Toast.makeText(context, "手机没有任何网络...", Toast.LENGTH_SHORT).show();
            CommonUtil.log(TAG, "当前没有任何网络");
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_NO).commit();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        if (position == 3 &&positionOffset>0.1) {
//            handler.sendEmptyMessageDelayed(GUIDE_FINISHED, 1000);
//            Intent loginIntent = new Intent(this, LoginAct.class);
//            startActivity(loginIntent);
//        }
    }

    private static final int GUIDE_FINISHED = 0x01;  //关闭引导页

    @Override
    public void handleMsg(Message message) {
        if (message.what == GUIDE_FINISHED) {
            finish();
        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.e(TAG,state+"");
    }
}
