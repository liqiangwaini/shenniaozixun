package com.xingbo.live.controller;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.entity.model.IgnoreAllPriMsgModel;
import com.xingbo.live.entity.msg.PriMsgDetailMsg;
import com.xingbo.live.fragment.MainRoomPriMsgFragment;
import com.xingbo.live.fragment.MainRoomSysMsgFragment;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.fragment.BaseFragment;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

/**
 * Created by xingbo_szd on 2016/8/8.
 */
public class PrivateMsgController implements RadioGroup.OnCheckedChangeListener, View.OnClickListener/*,MainRoomPriMsgFragment.OnPriMsgItemClick*/ {
    private static final String TAG = "PrivateMsgDialog";

    private BaseFragment fragment = null;
    private BaseAct act;
    private View rootView;
    private TextView systemMsg;
    private RadioButton privateMsg;
    private RadioButton systemMsg1;
    private TextView ignoreAll;
    private RadioGroup radioGroup;
    private FragmentTransaction transaction;

    public PrivateMsgController(BaseAct act, View rootView) {
        this.act = act;
        this.rootView = rootView;
    }

    public void init() {
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group_titlebar);
        privateMsg = (RadioButton) rootView.findViewById(R.id.radiobutton_private_msg);
        systemMsg1 = (RadioButton) rootView.findViewById(R.id.radiobutton_system_msg);
        ignoreAll = (TextView) rootView.findViewById(R.id.ignore_all_msg);
        //init data
        privateMsg.setChecked(true);
        fragment = MainRoomPriMsgFragment.newInstance(0);
        MainRoomPriMsgFragment mainRoomPriMsgFragment = (MainRoomPriMsgFragment) fragment;
        transaction = act.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_pri_msg, fragment);
        transaction.commit();
        //click
        radioGroup.setOnCheckedChangeListener(this);
        ignoreAll.setOnClickListener(this);
    }

    private boolean isPriMsgShow = true;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.radiobutton_private_msg:
                transaction = act.getSupportFragmentManager().beginTransaction();
                fragment = MainRoomPriMsgFragment.newInstance(0);
                MainRoomPriMsgFragment mainRoomPriMsgFragment = (MainRoomPriMsgFragment) fragment;
                mainRoomPriMsgFragment.isDialog(true);
                transaction.replace(R.id.frame_pri_msg, fragment);
                transaction.commit();
                isPriMsgShow = true;
                break;
            case R.id.radiobutton_system_msg:
                transaction = act.getSupportFragmentManager().beginTransaction();
                fragment = MainRoomSysMsgFragment.newInstance(1);
                transaction.replace(R.id.frame_pri_msg, fragment);
                transaction.commit();
                isPriMsgShow = false;
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ignore_all_msg:
                ignoreAll();
                break;
        }
    }

    public void ignoreAll() {
        RequestParam param = RequestParam.builder(act);
        CommonUtil.request(act, HttpConfig.API_USER_IGNORE_ALL_MSG, param, TAG, new XingBoResponseHandler<IgnoreAllPriMsgModel>(act, IgnoreAllPriMsgModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
//                ((MainRoom) getActivity()).alert("忽略所有消息成功");
                Toast.makeText(act, "所有消息已忽略", Toast.LENGTH_SHORT).show();
                if (fragment instanceof MainRoomPriMsgFragment) {
                    ((MainRoomPriMsgFragment) fragment).setCurrentPage(1);
                    ((MainRoomPriMsgFragment) fragment).request();
                }
            }
        });
    }

    //获取到发送来的私信消息
    public void refreshPriMsgList(PriMsgDetailMsg priMsgDetail) {
        if (fragment instanceof MainRoomPriMsgFragment) {
            ((MainRoomPriMsgFragment) fragment).refreshPriMsgList(priMsgDetail);
        }
    }

    private OnPriMessageItemClickListener listener;

    public void setOnPriMessageItemClickListener(OnPriMessageItemClickListener listener) {
        this.listener = listener;
    }

   /* @Override
    public void setOnItemClick(String nick, String uid) {
        listener.setOnItemClickListener(nick,uid);
    }*/

    public interface OnPriMessageItemClickListener {
        public void setOnItemClickListener(String nick, String uid);
    }

}

