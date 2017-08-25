package com.xingbo.live.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.xingbo.live.ui.RechargeAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.api.OnRequestErrCallBack;
import com.xingbobase.fragment.BaseFragment;
import com.xingbo.live.R;
import com.xingbo.live.ui.LoginAct;
import com.xingbobase.view.widget.XingBoLoadingDialog;

/**
 * Created by WuJinZhou on 2016/2/22.
 */
public class MBaseFragment extends BaseFragment implements OnRequestErrCallBack,DialogInterface.OnCancelListener {
    private static final String TAG="MBaseFragment";
    @Override
    public void loginErr(int errCode, String msg) {
        dialog("登录","取消", R.color.pink,R.color.first_text_424242,"尚未登录","请先登录后再试！",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(getActivity(), LoginAct.class);
                startActivity(login);
            }
        });
    }

    @Override
    public void costErr(int errCode, String msg) {
        dialog("去充值","取消",R.color.pink,R.color.first_text_424242,"余额不足","请先充值后再试！",new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recharge=new Intent(getActivity(), RechargeAct.class);
                startActivity(recharge);
            }
        });
    }


    public void removeWebView(){

    }

    /**
     * http请求开始回调
     */
    @Override
    public void requestStart() {
        showDoing(TAG, this);
    }

    /**
     * http请求结束回调
     */
    @Override
    public void requestFinish() {
        done();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        CommonUtil.cancelRequest(((XingBoLoadingDialog) dialog).getTag());
    }
}
