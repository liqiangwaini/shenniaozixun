package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;

import com.xingbo.live.R;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.eventbus.NotifycationEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.widget.XingBoAlert;

import java.util.Set;

import de.greenrobot.event.EventBus;
import io.vov.vitamio.utils.Log;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/29
 */
public class NotifycationAct extends BaseAct {
    private final static String TAG = "NotifycationAct";
    public final static String EXTRA_USER_RID = "extra_user_rid";
    public final static String NOTIFYCATION_TYPE = "type";
    public final static String LIVE_LOGO = "live_logo";
    private PushAgent mPushAgent;
    private SharedPreferences sp;
    private RelativeLayout loadingBox;

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_notifycation);
        //判断是否登录
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        loadingBox = (RelativeLayout) findViewById(R.id.loading_view_box);
        CommonUtil.log(TAG, "PX_USER_LOGIN_UID:  " + sp.getString(XingBo.PX_USER_LOGIN_UID, "0"));
        mPushAgent = PushAgent.getInstance(this);
        if (sp.getString(XingBo.PX_USER_SETTING_NOTIFYCATION_FLAG, "").equals("1")
                || sp.getString(XingBo.PX_USER_SETTING_IS_IN_MAINROOM, "").equals("1")
                || sp.getString(XingBo.PX_USER_SETTING_IS_ANCHORLIVE, "").equals("1")) {
            //关闭推送
//            mPushAgent.disable();
            EventBus.getDefault().post(new NotifycationEvent());
            finish();
        } else {
            if (sp.getString(XingBo.PX_USER_LOGIN_UID, "0").equals("0")) { //往登录界面
                Intent intent = new Intent(this, LoginAct.class);
                startActivity(intent);
                finish();
            } else {
                //请求进入直播间
                Bundle bun = getIntent().getExtras();
                if (bun != null) {
                    Set<String> keySet = bun.keySet();//获取键值集合
                    for (String key : keySet) {
                        if (key.equals("type")) {
                            String type = bun.getString(key);
                            Log.d(TAG, "type-->" + type);
                        }
                        if (key.equals("rid")) {
                            String rid = bun.getString(key);
                            Log.d(TAG, "类型startlive" + rid + "开播");
                            RequestParam param = RequestParam.builder(this);
                            param.put("device", "android");
                            param.put("rid", rid);
                            CommonUtil.request(this, HttpConfig.API_ENTER_ROOM, param, TAG,
                                    new XingBoResponseHandler<RoomModel>(this, RoomModel.class, this) {
                                        @Override
                                        public void phpXiuErr(int errCode, String msg) {
//                                alert(msg);
                                            if (loadingBox.getVisibility() == View.VISIBLE) {
                                                loadingBox.setVisibility(View.GONE);
                                            }
                                            alert("网络不给力,请检查网络状态");
                                        }

                                        @Override
                                        public void phpXiuSuccess(String response) {
                                            if (loadingBox.getVisibility() == View.VISIBLE) {
                                                loadingBox.setVisibility(View.GONE);
                                            }
                                            if (model.getD().getAnchor().getLivestatus().equals("0")) {
                                                Intent intent = new Intent(NotifycationAct.this, LiveFinishedAct.class);
                                                intent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, HttpConfig.FILE_SERVER + model.getD().getShare().getLogo());
                                                intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ID, model.getD().getAnchor().getId());
                                                intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ONLINES, model.getD().getAnchor().getLiveonlines());
                                                intent.putExtra(LiveFinishedAct.LIVE_ROOM_IS_FOLLOWED, model.getD().isFollowed());
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(NotifycationAct.this, MainRoom.class);
                                                intent.putExtra(MainRoom.EXTRA_MAIN_ROOM_INFO, model.getD());
                                                intent.putExtra(MainRoom.ANCHOR_LIVE_LOGO, HttpConfig.FILE_SERVER + model.getD().getShare().getLogo());//主播封面
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                } else {
                    alert("bundle is null");
                }
            }

        }
//        else {
//            //开启推送
//            mPushAgent.enable();
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return false;
        }
        return false;
    }
}
