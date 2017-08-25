package com.xingbo.live.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.umeng.message.UmengRegistrar;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.api.OnRequestErrCallBack;
import com.umeng.message.PushAgent;
import com.xingbo.live.R;
import com.xingbobase.http.RequestParam;
import com.xingbobase.ui.XingBoBaseAct;
import com.umeng.analytics.MobclickAgent;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.widget.XingBoLoadingDialog;

import io.vov.vitamio.utils.Log;

/**
 * Created by WuJinZhou on 2016/1/14.
 */
public class BaseAct extends XingBoBaseAct implements OnRequestErrCallBack,DialogInterface.OnCancelListener {
    private static final String TAG="BaseAct";

    public View rootView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在baseActivity中添加
        //开启日活,此方法与统计分析sdk中统计日活的方法无关！请务必调用此方法！
        PushAgent.getInstance(this).onAppStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void loginErr(int errCode, String msg) {
        dialog("登录", "取消", R.color.orange_FC563C, R.color.first_text_424242, "尚未登录", "请先登录后再试！", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(BaseAct.this, LoginAct.class);
                startActivity(login);
            }
        });
    }

    @Override
    public void costErr(int errCode, String msg) {
        dialog("去充值", "取消", R.color.pink, R.color.first_text_424242, "余额不足", "余额不足，请充值！", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recharge = new Intent(BaseAct.this, RechargeAct.class);
                recharge.putExtra(RechargeAct.EXTRA_USER_COIN_FLAG,true);
                startActivity(recharge);
            }
        });
    }



    public void setRootView(View rootView){
        this.rootView=rootView;
    }

    public void showPopupBg(){
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void requestStart() {
        showDoing(TAG, this);
    }

    @Override
    public void requestFinish() {
        done();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        CommonUtil.cancelRequest(((XingBoLoadingDialog) dialog).getTag());
    }

}
