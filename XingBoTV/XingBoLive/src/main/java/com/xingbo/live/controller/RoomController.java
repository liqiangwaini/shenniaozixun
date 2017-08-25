package com.xingbo.live.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.xingbo.live.R;
import com.xingbo.live.broadcast.InternetStateBroadcast;
import com.xingbo.live.entity.Anchor;
import com.xingbo.live.entity.Gift;
import com.xingbo.live.entity.model.CloseLiveModel;
import com.xingbo.live.entity.model.MessageCountModel;
import com.xingbo.live.entity.model.SendGiftModel;
import com.xingbo.live.eventbus.MFavoriteEvent;
import com.xingbo.live.eventbus.UpdateGiftBagNum;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import de.greenrobot.event.EventBus;


/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class RoomController {
    private static final String TAG = "MainroomController";

    public static final int SEND_MSG = 0;
    public static final int SEND_GIFT = 1;
    public static final int CLICK_FAVORATE = 2;
    public static final int GET_GIFT_LIST = 3;
    public static final int GET_MESSAGE_COUNT = 4;
    public static final int GET_CLOSE_LIVE_INFO = 5;
    public static final int CLOSE_PUSH_LIVE = 6;

    public RoomController() {
    }

    /**
     * 检测网络信号强度，可以定时器心跳检测
     */
    public static String checkNetState(Context context) {
        SharedPreferences sp = null;
        if (sp == null) {
            sp = context.getSharedPreferences(XingBo.PX_CONFIG_CACHE_FILE, Context.MODE_PRIVATE);
        }
        String netName = InternetStateBroadcast.NET_NO;//网终状态
        final TelephonyManager manager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo.State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
            //Toast.makeText(context, "手机网络连接成功！", Toast.LENGTH_SHORT).show();
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_MOBILE).commit();
            netName = InternetStateBroadcast.NET_MOBILE;
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED == wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            //Toast.makeText(context, "无线网络连接成功！", Toast.LENGTH_SHORT).show();
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_WIFI).commit();
            netName = InternetStateBroadcast.NET_WIFI;
            CommonUtil.log(TAG, "网络变为无线网络");
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED != mobileState) {
            //  Toast.makeText(context, "手机没有任何网络...", Toast.LENGTH_SHORT).show();
            sp.edit().putString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_NO).commit();
            CommonUtil.log(TAG, "网络已断开，无任何网络连接");
            netName = InternetStateBroadcast.NET_NO;
        }
        return netName;
    }

    //网络请求前，先检查网络
    public static void checkNet(Context context, BaseAct act) {
        String netName = checkNetState(context);
        if (netName.equals(InternetStateBroadcast.NET_NO)) {
            act.alert("当前网络已断开，请先连接网络！");
            return;
        } else if (netName.equals(InternetStateBroadcast.NET_MOBILE)) {
            act.dialog("继续访问网络", "停止访问", R.color.orange_FC563C, R.color.first_text_424242, "提示", "当前使用网络状态为移动网络，确认要继续访问吗？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.px_dialog_cancel) {
                        return;
                    }
                }
            });
        }
    }

    //送礼
    public void sendGift(final BaseAct act, Anchor anchor, final Gift currentGift, final int currentGiftNum) {
        if (currentGift == null) {
            act.alert("请先选择礼物");
            return;
        }
        RequestParam param = RequestParam.builder(act);
        param.put("livename", anchor.getLivename());
        param.put("rid", anchor.getId());
        param.put("uid", anchor.getId());
        param.put("gid", currentGift.getId());
        param.put("num", currentGiftNum + "");
        if (currentGift.isBag()) {
            param.put("usebag", "1");
        } else {
            param.put("usebag", "0");
        }
        CommonUtil.request(act, HttpConfig.API_APP_SEND_GIFT, param, TAG, new XingBoResponseHandler<SendGiftModel>(act, SendGiftModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                listener.onError(SEND_GIFT, msg);
                Log.d(TAG,msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                listener.onSuccess(SEND_GIFT, model.getD() + "##" + currentGift.isBag());
                if (currentGift.isBag()) {
                    EventBus.getDefault().post(new UpdateGiftBagNum(currentGift.getId(), currentGiftNum));
//                    EventBus.getDefault().post(new GiftBagPagerChange());
                } else {
//                    EventBus.getDefault().post(new GiftCoinChangeEvent());
                }

            }
        });
    }

    /**
     * 用户关注操作
     */
    public void favoriteUser(final BaseAct act, final boolean isFollwed, final String uid) {
        RequestParam param = RequestParam.builder(act);
//        param.put("livename", anchor.getLivename());
        param.put("uid", uid);
        String api = null;
        if (isFollwed) {
            api = HttpConfig.API_APP_CANCEL_FOLLOW;
        } else {
            api = HttpConfig.API_APP_ADD_FOLLOW;
        }
        CommonUtil.request(act, api, param, TAG, new XingBoResponseHandler(act, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                listener.onError(CLICK_FAVORATE, msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                listener.onSuccess(CLICK_FAVORATE, new MFavoriteEvent(!isFollwed, uid));
//
            }
        });
    }

    //禁播
    public void closePushLive(final BaseAct act, String rid) {
        RequestParam params = RequestParam.builder(act);
        params.put("rid", rid);  //主播ID
        CommonUtil.request(act, HttpConfig.API_APP_CLOSE_PUSH_LIVE, params, TAG, new XingBoResponseHandler<CloseLiveModel>(act, CloseLiveModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                listener.onError(CLOSE_PUSH_LIVE, msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                listener.onSuccess(CLOSE_PUSH_LIVE, model.getD());
            }
        });
    }

    //获取私聊消息未读数
    public void getMessageCount(final BaseAct act) {
        RequestParam param = RequestParam.builder(act);
        CommonUtil.request(act, HttpConfig.API_GET_MESSAGE_COUNT, param, TAG, new XingBoResponseHandler<MessageCountModel>(act, MessageCountModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                listener.onError(GET_MESSAGE_COUNT, msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.e(TAG, model.getD() + "");
                listener.onSuccess(GET_MESSAGE_COUNT, model.getD());

            }
        });
    }

    //请求开播结束数据
    public void getCloseLiveInfo(final BaseAct act) {
        RequestParam param = RequestParam.builder(act);
        //API_LIVE_CLOSE
        CommonUtil.request(act, HttpConfig.API_LIVE_CLOSE, param, TAG, new XingBoResponseHandler<CloseLiveModel>(act, CloseLiveModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                listener.onError(GET_CLOSE_LIVE_INFO, msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                listener.onSuccess(GET_CLOSE_LIVE_INFO, model.getD());
            }
        });
    }

    //显示软件盘
    public void showInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    //隐藏软件盘
    public void hideInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private MainRoomControllerListener listener;

    public interface MainRoomControllerListener {
        public void onError(int tag, String msg);

        public void onSuccess(int tag, Object modelD);
    }

    public void setMainroomControllerCallback(MainRoomControllerListener listener) {
        this.listener = listener;
    }


}
