package com.xingbo.live.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.model.IgnoreAllPriMsgModel;
import com.xingbo.live.entity.msg.PriMsgDetailMsg;
import com.xingbo.live.fragment.MainRoomPriMsgFragment;
import com.xingbo.live.fragment.MainRoomSysMsgFragment;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.fragment.BaseFragment;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

/**
 * Created by xingbo_szd on 2016/8/8.
 */
public class PrivateMsgDialog extends DialogFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAG = "PrivateMsgDialog";
    private BaseFragment fragment = null;

    private TextView systemMsg;
    private View view;
    private RadioButton privateMsg;
    private RadioButton systemMsg1;
    private TextView ignoreAll;
    private RadioGroup radioGroup;
    private FrameLayout framePriMsg;
    private FragmentTransaction transaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_room_pri_msg, null);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = XingBoConfig.MAIN_ROOM_PRI_MSG_HEIGHT;
        getDialog().getWindow().setAttributes(lp);
        initView();
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setCanceledOnTouchOutside(true);
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.style_popup);
        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.width = (int) SystemApp.screenWidth;
        window.setAttributes(wlp);
        window.setBackgroundDrawableResource(R.color.transparent);
        return dialog;
    }

    public void initView() {
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_titlebar);
        privateMsg = (RadioButton) view.findViewById(R.id.radiobutton_private_msg);
        systemMsg1 = (RadioButton) view.findViewById(R.id.radiobutton_system_msg);
        ignoreAll = (TextView) view.findViewById(R.id.ignore_all_msg);
        framePriMsg = (FrameLayout) view.findViewById(R.id.frame_pri_msg);
        //initdata
        fragment = MainRoomPriMsgFragment.newInstance(0);
        MainRoomPriMsgFragment mainRoomPriMsgFragment = (MainRoomPriMsgFragment) fragment;
        mainRoomPriMsgFragment.isDialog(true);
        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_pri_msg, fragment);
        transaction.commit();
        //click
        radioGroup.setOnCheckedChangeListener(this);
        ignoreAll.setOnClickListener(this);

    }

    private boolean isPriMsgShow=true;

    public boolean isPriMsgShow() {
        return isPriMsgShow;
    }

    public void setIsPriMsgShow(boolean isPriMsgShow) {
        this.isPriMsgShow = isPriMsgShow;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.radiobutton_private_msg:
                transaction = getChildFragmentManager().beginTransaction();
                fragment = MainRoomPriMsgFragment.newInstance(0);
                MainRoomPriMsgFragment mainRoomPriMsgFragment = (MainRoomPriMsgFragment) fragment;
                mainRoomPriMsgFragment.isDialog(true);
                transaction.replace(R.id.frame_pri_msg, fragment);
                transaction.commit();
                isPriMsgShow=true;
                break;
            case R.id.radiobutton_system_msg:
                transaction = getChildFragmentManager().beginTransaction();
                fragment = MainRoomSysMsgFragment.newInstance(1);
                transaction.replace(R.id.frame_pri_msg, fragment);
                transaction.commit();
                isPriMsgShow=false;
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

    public void ignoreAll(){
        RequestParam param=RequestParam.builder(getActivity());
        CommonUtil.request(getActivity(), HttpConfig.API_USER_IGNORE_ALL_MSG, param, TAG, new XingBoResponseHandler<IgnoreAllPriMsgModel>((MainRoom)getActivity(),IgnoreAllPriMsgModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                ((MainRoom) getActivity()).alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
//                ((MainRoom) getActivity()).alert("忽略所有消息成功");
                Toast.makeText(getActivity(),"所有消息已忽略",Toast.LENGTH_SHORT).show();
                if(fragment instanceof MainRoomPriMsgFragment){
                    ((MainRoomPriMsgFragment) fragment).request();
                }
            }
        });
    }

    //获取到发送来的私信消息
    public void refreshPriMsgList(PriMsgDetailMsg priMsgDetail){
        if(fragment instanceof MainRoomPriMsgFragment){
//            ((MainRoomPriMsgFragment) fragment).refreshPriMsgList(priMsgDetail);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
    }
}

