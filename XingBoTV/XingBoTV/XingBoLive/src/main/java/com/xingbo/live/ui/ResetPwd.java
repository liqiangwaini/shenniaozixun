package com.xingbo.live.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xingbo.live.util.FastBlur;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.RequestParam;
import com.xingbo.live.R;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by WuJinZhou on 2016/3/9.
 */
public class ResetPwd extends BaseAct implements View.OnClickListener {
    public final static String TAG = "ResetPwd";
    private final static int RECHECK_COUNTDOWN = 0x1;//重发验证码倒计时
    private final static int UPDATE_PWD_REQUEST_CODE = 0x2;//重置密码跳转
    private EditText phoneInput, codeInput, passwordInput;
    private TimerTask task;
    private Timer timer = new Timer(true);//计时器
    private int second = 60;
    private Button checkPhone, nextBtn;
    private String bindingPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pwd);
//        FrescoImageView welcome=(FrescoImageView)findViewById(R.id.login_other_bg_image);
//        welcome.setImageBitmap(FastBlur.blurImageAmeliorate(BitmapFactory.decodeResource(getResources(), R.mipmap.welcome), 30));
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        phoneInput = (EditText) findViewById(R.id.account);
        codeInput = (EditText) findViewById(R.id.check_phone_code);
        passwordInput = (EditText) findViewById(R.id.password);
        checkPhone = (Button) findViewById(R.id.check_phone);
        checkPhone.setOnClickListener(this);
        nextBtn = (Button) findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.check_phone:
                requestCheckPhone();
                break;
            case R.id.next_btn:
                next();
                break;
            default:
                break;
        }
    }

    /**
     * 验证手机，请求验证码
     */
    private void requestCheckPhone() {
        XingBoUtil.log(TAG, "请求获取验证码...");
        if (TextUtils.isEmpty(phoneInput.getText())) {
            alert("请输入手机号");
            return;
        }
        if (!XingBoUtil.isMobileNo(phoneInput.getText().toString())) {
            alert("手机号码无效");
            return;
        }
        String phone = phoneInput.getText().toString();
        RequestParam param = RequestParam.builder(this);
        param.put("phone", phone);
        param.put("type", "findpassword");
        checkPhone.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_USER_CHECK_PHONE, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuSuccess(String response) {
                CommonUtil.log(TAG, "请求获取验证码结果：" + response);
                CommonUtil.tip(ResetPwd.this, "运营商将会给您发送短信验证码，请注意查收", Gravity.BOTTOM);
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
     * 提交验证
     */
    private void next() {
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
        if (TextUtils.isEmpty(passwordInput.getText())) {
            alert("请输入新密码");
            return;
        }
        if (passwordInput.getText().length() < 6 || passwordInput.getText().length() > 16) {
            alert("请输入6至16位新密码");
            return;
        }
        String phone = phoneInput.getText().toString();
        String code = codeInput.getText().toString();
        String password = passwordInput.getText().toString();
        RequestParam param = RequestParam.builder(this);
        param.put("authcode", code.trim());
        param.put("phone", phone.trim());
        param.put("password", password.trim());
        nextBtn.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_APP_RESET_PASSWORD2, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuSuccess(String response) {
                nextBtn.setEnabled(true);
                CommonUtil.log(TAG, "手机找密码验证结果：" + response);
                setResult(RESULT_OK, null);
                finish();
            }

            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                nextBtn.setEnabled(true);
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
            checkPhone.setText("发送验证码");
        }
        checkPhone.setEnabled(true);
    }

    public void handleMsg(Message msg) {
        switch (msg.what) {
            case RECHECK_COUNTDOWN:
                if ((int) msg.obj < 1) {
                    checkPhone.setText("发送验证码");
                    checkPhone.setEnabled(true);
                    if (task != null) {
                        task.cancel();
                        task = null;
                    }
                    break;
                }
                checkPhone.setText(second + "秒");
                second--;
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        super.onBackPressed();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //重置密码返回
        if (requestCode == UPDATE_PWD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                finish();
            }
        }
    }
}
