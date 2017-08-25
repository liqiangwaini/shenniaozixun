package com.xingbo.live.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.xingbo.live.R;
import com.xingbo.live.entity.UserPhotos;
import com.xingbobase.config.XingBo;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/17
 */
public class UserPushAct extends BaseAct implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private final String TAG = "UserPushAct";

    private CheckBox pushAll;
    private CheckBox pushShock;
    private CheckBox pushVoice;
    private CheckBox pushNo;
    private PushAgent mPushAgent;
    private SharedPreferences sp;
    private String notifycationFlag;
    private String notifycationVoice;
    private String notifycationShoke;
    private String notifycationNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_warn_activity);
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        notifycationFlag = sp.getString(XingBo.PX_USER_SETTING_NOTIFYCATION_FLAG, "0");
        notifycationNo = sp.getString(XingBo.PX_USER_SETTING_NOTIFYCATION_NO, "");
        notifycationShoke = sp.getString(XingBo.PX_USER_SETTING_NOTIFYCATION_SHOKE, "");
        notifycationVoice = sp.getString(XingBo.PX_USER_SETTING_NOTIFYCATION_VOICE, "");
        mPushAgent = PushAgent.getInstance(UserPushAct.this);
        mPushAgent.enable();
        initView();
        pushAll.setOnCheckedChangeListener(this);
        pushVoice.setOnCheckedChangeListener(this);
        pushShock.setOnCheckedChangeListener(this);
        pushNo.setOnCheckedChangeListener(this);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
    }

    private void initView() {
        pushAll = (CheckBox) findViewById(R.id.push_all);
        pushShock = (CheckBox) findViewById(R.id.push_shock);
        pushVoice = (CheckBox) findViewById(R.id.push_voice);
        pushNo = (CheckBox) findViewById(R.id.push_no);
        if (notifycationFlag.equals("0")) {
            pushAll.setChecked(true);
        } else {
            pushAll.setChecked(false);
        }
        if (notifycationVoice.equals("0")) {
            pushVoice.setChecked(true);
        } else {
            pushVoice.setChecked(false);
        }
        if (notifycationShoke.equals("0")) {
            pushShock.setChecked(true);
        } else {
            pushShock.setChecked(false);
        }
        if (notifycationNo.equals("0")) {
            pushNo.setChecked(true);
        } else {
            pushNo.setChecked(false);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = sp.edit();
        switch (buttonView.getId()) {
            case R.id.push_all:
                if (!isChecked) {
                    editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_FLAG, "1");
                    pushShock.setChecked(false);
                    pushShock.setEnabled(false);
                    pushVoice.setChecked(false);
                    pushVoice.setEnabled(false);
                    pushNo.setChecked(false);
                    pushNo.setEnabled(false);
                } else {
                    editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_FLAG, "0");
                    pushShock.setEnabled(true);
                    pushVoice.setEnabled(true);
                    pushNo.setEnabled(true);
                }
                break;
            case R.id.push_shock:
                if (isChecked) {
                    editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_SHOKE, "0");
                    //震动模式
                    mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
                } else {
                    editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_SHOKE, "1");
                }
                break;
            case R.id.push_voice:
                if (isChecked) {
                    editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_VOICE, "0");
                    //声音模式
                    mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
                } else {
                    editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_VOICE, "1");
                }
                break;
            case R.id.push_no:
                if (isChecked) {
                    editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_NO, "0");
                    //免打扰模式
                    mPushAgent.setNoDisturbMode(23, 0, 8, 0);
                } else {
                    editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_NO, "1");
                    mPushAgent.setNoDisturbMode(0, 0, 0, 0);//关闭免打扰模式
                }
                break;
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                onBackPressed();
                break;
        }

    }
}
