package com.xingbo.live;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.duanqu.qupai.jni.ApplicationGlue;
import com.umeng.common.message.Log;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.xingbo.live.entity.anim.GiftAnimatorSet;
import com.xingbo.live.ui.NotifycationAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.view.widget.GiftFrescoImageView;
import com.xingbobase.XingBoSystemApp;
import com.xingbobase.config.XingBo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WuJinZhou on 2015/8/8.
 */
public class SystemApp extends XingBoSystemApp {
    public static final String TAG = "XingBoSystemApp.SystemApp";
    public final static Map<String, GiftAnimatorSet> GIFT_ANIM_MAP = new HashMap<String, GiftAnimatorSet>();//大礼物动画集
    public final static List<GiftFrescoImageView> FEW_GIFT_VIEWS = new ArrayList<GiftFrescoImageView>();//少量（少于10个）礼物动画显示资源池
    private SharedPreferences sp;
    private static Handler handler = new Handler();

    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        CommonUtil.log(TAG, "CPU_ABI: " + Build.CPU_ABI);
        //初始化云捕sdk
        com.netease.nis.bugrpt.CrashHandler.getInstance().init(this.getApplicationContext(), null);
        if (Build.CPU_ABI.equals("armeabi-v7a")) {
            System.loadLibrary("gnustl_shared");
            System.loadLibrary("qupai-media-thirdparty");
            System.loadLibrary("alivc-media-jni");
            ApplicationGlue.initialize(this);
        }
        //友盟配置
        PlatformConfig.setWeixin("wx1720159daed2476f", "");
        PlatformConfig.setSinaWeibo("390839445", "");
        PlatformConfig.setQQZone("101274319", "");

        PushAgent.getInstance(this).onAppStart();
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setPushIntentServiceClass(null);
        mPushAgent.setDebugMode(true);
        initYoumeng();
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        if (sp.getString(XingBo.PX_USER_SETTING_NOTIFYCATION_SHOKE, "").equals("0")) {
            mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
        } else {
        }
        if (sp.getString(XingBo.PX_USER_SETTING_NOTIFYCATION_VOICE, "").equals("0")) {
            mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
        } else {
        }
        if (sp.getString(XingBo.PX_USER_SETTING_NOTIFYCATION_NO, "").equals("0")) {
            mPushAgent.setNoDisturbMode(23, 0, 8, 0);
        } else {
            mPushAgent.setNoDisturbMode(0, 0, 0, 0);//关闭免打扰模式
        }

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);
                        return builder.build();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                Map<String, String> extra = msg.extra;
                String type = extra.get("type");//("type","startlive"); 打开activity类型 开播
                if (type.equals("startlive")) {
                    String rid = extra.get("rid");//("rid",$rid)主播ID
                    Log.d("tag", "appSystem--->" + rid);
                    int flagActivityNewTask = Intent.FLAG_ACTIVITY_NEW_TASK;
                    Intent intent = new Intent(context, NotifycationAct.class).addFlags(flagActivityNewTask);
                    intent.putExtra(NotifycationAct.NOTIFYCATION_TYPE, type);
                    intent.putExtra(NotifycationAct.EXTRA_USER_RID, rid);
                    context.startActivity(intent);
                }
//                openActivity(context,msg);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

////        初始化友盟
//       initYoumeng();
//     final  String device_token = UmengRegistrar.getRegistrationId(this);
//        CommonUtil.log(TAG, "设备唯一编码:" + device_token);
//        handler.post(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        CommonUtil.log(TAG, "异步初始化动画....");
//                        for (int i = 0; i < XingBoConfig.ANIM_KEYS.length; i++) {
//                            GiftAnimatorSet animatorSet = new GiftAnimatorSet(getApplicationContext(), XingBoConfig.ANIM_KEYS[i]);
//                            GIFT_ANIM_MAP.put(XingBoConfig.ANIM_KEYS[i], animatorSet);
//                        }
//                        for (int i = 0; i < 10; i++) {
//                            FEW_GIFT_VIEWS.add(new GiftFrescoImageView(getApplicationContext(), i, R.id.main_room_gift_image_view_));
//                        }
//                    }
//                }
//        );
//    }

    //消息推送
    private void initYoumeng() {
        //------------------友盟推送------------------
        mPushAgent = PushAgent.getInstance(this);

        mPushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String registrationId) {
                //onRegistered方法的参数registrationId即是device_token
                Log.d("device_token", "-->" + registrationId);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

}
