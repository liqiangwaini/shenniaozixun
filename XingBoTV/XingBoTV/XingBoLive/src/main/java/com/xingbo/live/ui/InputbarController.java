package com.xingbo.live.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.xingbo.live.R;
import com.xingbo.live.entity.model.DanmuModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.SoftInputUtils;
import com.xingbo.live.view.InputBarWithBoard;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

/**
 * Created by xingbo_szd on 2016/7/21.
 */
public class InputbarController {
    private static final String TAG = "InputbarController";

    private int msgChannel = MSG_CHANNEL_COMMON;//0,1,2;公共频道、公共私聊频道、私聊频道
    public final static int MSG_CHANNEL_COMMON = 0;//发送消息到公共频道
    public final static int MSG_CHANNEL_DANMU = 1;//发送弹幕信息

    private boolean isSendingMsg = false;
    private String replayText;
    private BaseAct act;

    public InputbarController(BaseAct act) {
        this.act = act;
    }

   /* @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.inputbar_act);
        livename = getIntent().getStringExtra(ANCHOR_LIVENAME);
        anchorId = getIntent().getStringExtra(ANCHOR_ID);
        replayText = getIntent().getStringExtra(REPLAY_UID);
        View blankView = findViewById(R.id.view_inputbar_blank);
        inputBarWithBoard = (InputBarWithBoard) findViewById(R.id.input_bar_act);
        inputBarWithBoard.setInputBarWithBoardListener(this);
        if(!TextUtils.isEmpty(replayText)){
            inputBarWithBoard.mInput.setText(replayText);
            inputBarWithBoard.mInput.setSelection(replayText.length());
        }
    }*/

    public void onSend(String msg, String anchorId) {
        MainRoom mainRoom= (MainRoom) act;
        mainRoom.hideEmotionContainer();
        if (isSendingMsg) {
            return;
        }
        isSendingMsg = true;
        if (msgChannel == MSG_CHANNEL_DANMU) {  //发送弹幕
            SharedPreferences sp = act.getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
            sendDanmu(anchorId, sp.getString(XingBo.PX_USER_LOGIN_UID, ""), msg);
//            EventBus.getDefault().post(new DanmuEvent(msg));
        } else if (msgChannel == MSG_CHANNEL_COMMON) {  //发送公聊
            sendMessage(anchorId, msg);
        }
        isSendingMsg = false;
    }

    public void sendDanmu(String rid, String uid, String word) {
        RequestParam param = RequestParam.builder(act);
        param.put("rid", rid);
        param.put("uid", uid);
        param.put("word", word);
        CommonUtil.request(act, HttpConfig.API_APP_SEND_FLY_WORD, param, TAG, new XingBoResponseHandler<DanmuModel>(act, DanmuModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                int lastCoin = model.getD();
                listener.sendMessageSuccess(lastCoin);
            }
        });

    }

    public void onDanmuStateChange(boolean isChecked) {
        if (isChecked) {
            if (msgChannel == MSG_CHANNEL_COMMON) {
                msgChannel = MSG_CHANNEL_DANMU;
            }
        } else {
            msgChannel = MSG_CHANNEL_COMMON;
        }
    }

    //公聊
/*    public void publicChat() {
        inputBarWithBoard.setVisibility(View.VISIBLE);
        inputBarWithBoard.mInput.setFocusable(true);// 获取焦点
        inputBarWithBoard.mInput.setFocusableInTouchMode(true);
        inputBarWithBoard.mInput.requestFocus();//获取焦点 光标出现
//        showInput(this, inputBarWithBoard.mInput);
        inputBarWithBoard.showSoftInput(inputBarWithBoard.mInput);
    }

    public void setChatMsg(String msg) {
        inputBarWithBoard.mInput.setText(msg);
        inputBarWithBoard.mInput.setSelection(msg.length());
        SoftInputUtils.showInput(act, inputBarWithBoard.mInput);
    }*/

    public void sendMessage(String anchorId, String msg) {
        RequestParam param = RequestParam.builder(act);
        param.put("rid", anchorId);
        //公共聊天
        param.put("uid", "0");
        param.put("msg", msg);
        CommonUtil.request(act, HttpConfig.API_SEND_COMMON_MSG, param, TAG, new XingBoResponseHandler<BaseResponseModel>(act, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                listener.sendMessageSuccess(-1);
            }
        });
    }

    private OnSendSuccessListener listener;

    public void setOnSendSuccess(OnSendSuccessListener listener) {
        this.listener = listener;
    }

    public interface OnSendSuccessListener {
        public void sendMessageSuccess(int coin);
    }
}
