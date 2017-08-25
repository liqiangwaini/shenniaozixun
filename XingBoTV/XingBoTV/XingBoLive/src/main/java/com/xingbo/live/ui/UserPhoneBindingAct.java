package com.xingbo.live.ui;

;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.emotion.EmotionEditText;
import com.xingbo.live.entity.model.LoginUserModel;
import com.xingbo.live.eventbus.PhoneBindingStateEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import io.vov.vitamio.utils.Log;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/10
 */
public class UserPhoneBindingAct extends BaseAct implements View.OnClickListener {
    private static final String TAG = "UserPhoneBindingAct";
    private final static int RECHECK_COUNTDOWN = 0x1;//重发验证码倒计时
    private TimerTask task;
    private Timer timer = new Timer(true);//计时器
    private int second = 60;
    private SharedPreferences sp;
    private EmotionEditText phoneInput;
    private EmotionEditText codeInput;
    private TextView phoneBindingHint;
    private String uid;
    private Button getSecode;//获取验证码按钮
    private Button phoneBindEnsure;//确定按钮


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_phone_binding);
        uid= getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID,"");
        initView();
    }

    private void initView() {
        phoneInput = (EmotionEditText) findViewById(R.id.user_phone_num_edit);
        codeInput = (EmotionEditText) findViewById(R.id.user_phone_binding_code_edit);
        getSecode = (Button) findViewById(R.id.get_code);
        getSecode.setOnClickListener(this);
        phoneBindEnsure = (Button) findViewById(R.id.phone_binding_sure);
        phoneBindEnsure.setOnClickListener(this);
        phoneBindingHint= (TextView) findViewById(R.id.user_phone_binding_hint);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                requestCheckPhone();
                break;
            case R.id.phone_binding_sure:
                phoneBinding();
                break;
            case R.id.top_back_btn:
                onBackPressed();
                break;
        }
    }

    /**
     * 验证手机，请求验证码
     */
    private void requestCheckPhone() {
        XingBoUtil.log(TAG, "请求获取验证码...");
        if (!XingBoUtil.isMobileNo(phoneInput.getText().toString())) {
            alert("手机号码无效");
            return;
        }
        String phone = phoneInput.getText().toString();
        RequestParam param = RequestParam.builder(this);
        param.put("phone", phone);
        param.put("type","bindphone");
        getSecode.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_USER_CHECK_PHONE, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuSuccess(String response) {
                CommonUtil.log(TAG, "请求获取验证码结果：" + response);
                phoneBindingHint.setVisibility(View.VISIBLE);
                showCheckState();
            }

            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                reGetCode();
            }
        });
    }

    /**
     * 提交绑定
     */
    private void phoneBinding() {
        if (TextUtils.isEmpty(phoneInput.getText())) {
            alert("请输入手机号");
            return;
        }
        if (!XingBoUtil.isMobileNo(phoneInput.getText().toString())) {
            alert("手机号码无效");
            return;
        }
        if (TextUtils.isEmpty(codeInput.getText())) {
            alert("请输入验证码");
            return;
        }
        String phone = phoneInput.getText().toString();
        String code = codeInput.getText().toString();

        //提交进行手机绑定
        RequestParam param = RequestParam.builder(this);
        param.put("uid",uid);
        param.put("authcode", code.trim());
        param.put("phone", phone.trim());
        phoneBindEnsure.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_USER_GET_PHONE_BINDING, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuSuccess(String response) {
                phoneBindEnsure.setEnabled(true);
                CommonUtil.log(TAG, "绑定：" + response);
                Log.d("ereere", response);
                Toast.makeText(UserPhoneBindingAct.this, "手机号绑定成功！", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new PhoneBindingStateEvent());
                finish();
            }
            @Override
            public void phpXiuErr(int errCode, String msg) {
                Log.d("tag","phoneBinding-->"+msg);
                alert(msg);
                phoneBindEnsure.setEnabled(true);
            }
        });
    }

    /**
     * 显示验证码获取状态信息
     */
    public void showCheckState() {
        second = 60;
        task = new TimerTask() {
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = RECHECK_COUNTDOWN;
                msg.obj = second;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    /**
     * 重置获取验证码状态
     */
    public void reGetCode() {
        if (task != null) {
            task.cancel();
            getSecode.setText("发送验证码");
        }
        getSecode.setEnabled(true);
        phoneBindingHint.setVisibility(View.INVISIBLE);
    }

    public void handleMsg(Message msg) {
        switch (msg.what) {
            case RECHECK_COUNTDOWN:
                if ((int) msg.obj < 1) {
                    getSecode.setText("发送验证码");
                    getSecode.setEnabled(true);
                    if (task != null) {
                        task.cancel();
                        task = null;
                    }
                    break;
                }
                getSecode.setText(second + "秒");
                second--;
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        CommonUtil.cancelRequest(TAG);
        handler.removeCallbacksAndMessages(null);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        super.finish();
    }
}
