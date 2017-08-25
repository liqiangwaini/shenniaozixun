package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xingbo.live.Constants;
import com.xingbo.live.entity.model.GetChannelIdModel;
import com.xingbo.live.entity.model.LoginUserModel;
import com.xingbo.live.eventbus.LoginEvent;
import com.xingbo.live.util.FastBlur;
import com.xingbo.live.util.GetMetaDataInfo;
import com.xingbobase.config.XingBo;
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

import io.vov.vitamio.utils.Log;


/**
 * Created by WuJinZhou on 2016/1/14.
 */
public class RegisterAct extends BaseAct implements View.OnClickListener {
    public final static String TAG = "RegisterAct";
    private final static int RECHECK_COUNTDOWN = 0x1;//重发验证码倒计时
    private EditText phoneInput, pwdInput, codeInput;
    private TimerTask task;
    private Timer timer = new Timer(true);//计时器
    private int second = 60;
    private Button checkPhone, register;
    private SharedPreferences sp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ImageView welcome = (ImageView)findViewById(R.id.login_other_bg_image);
   //     welcome.setImageBitmap(FastBlur.blurImageAmeliorate(BitmapFactory.decodeResource(getResources(), R.mipmap.welcome), 30));
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        phoneInput = (EditText) findViewById(R.id.account);
        pwdInput = (EditText) findViewById(R.id.password);
        codeInput = (EditText) findViewById(R.id.check_phone_code);
        checkPhone = (Button) findViewById(R.id.check_phone);
        checkPhone.setOnClickListener(this);
        register = (Button) findViewById(R.id.register_btn);
        register.setOnClickListener(this);
        findViewById(R.id.xingbo_lisence_text).setOnClickListener(this);
     //   requestChannalID();
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
            case R.id.register_btn:
                register();
                break;
            case R.id.xingbo_lisence_text:
                Intent intent= new Intent(this,UserProtocolAct.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
//    //获取渠道ID
//    public String requestChannalID(){
//        //获渠道名称
//        String channel_app_name = GetMetaDataInfo.getChannelName(this);
//        RequestParam param = RequestParam.builder(this);
//        if(!TextUtils.isEmpty(channel_app_name)){
//            param.put("channel_app_name",channel_app_name);
//        }
//        CommonUtil.request(this, HttpConfig.API_APP_USER_GET_CHANNEL_ID, param, TAG, new XingBoResponseHandler<GetChannelIdModel>(GetChannelIdModel.class) {
//            @Override
//            public void phpXiuErr(int errCode, String msg) {
//                alert("请求错误"+msg);
//            }
//            @Override
//            public void phpXiuSuccess(String response) {
//              channelId = model.getD();
//                Log.d(TAG,"channelId-->"+channelId);
//            }
//        });
//        return channelId;
//    }

    /**
     * 验证手机，请求验证码
     */
    private void requestCheckPhone() {
        XingBoUtil.log(TAG, "请求获取验证码...");
//        if(TextUtils.isEmpty(pwdInput.getText())||TextUtils.getTrimmedLength(pwdInput.getText())<6){
//            alert("请输入正确的密码格式(至少6位)");
//            return;
//        }
        if (!XingBoUtil.isMobileNo(phoneInput.getText().toString())) {
            alert("手机号码无效");
            return;
        }
        String phone = phoneInput.getText().toString();
        RequestParam param = RequestParam.builder(this);
        param.put("phone", phone);
        param.put("type", "register");
        checkPhone.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_USER_CHECK_PHONE, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuSuccess(String response) {
                CommonUtil.log(TAG, "请求获取验证码结果：" + response);
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
     * 提交注册
     */
    private void register() {
        if (TextUtils.isEmpty(phoneInput.getText())) {
            alert("请输入手机号");
            return;
        }
        if (!XingBoUtil.isMobileNo(phoneInput.getText().toString())) {
            alert("手机号码无效");
            return;
        }

        if (TextUtils.isEmpty(pwdInput.getText()) || TextUtils.getTrimmedLength(pwdInput.getText()) < 6) {
            alert("请输入正确的密码格式(至少6位)");
            return;
        }

        if (TextUtils.isEmpty(codeInput.getText())) {
            alert("请输入验证码");
            return;
        }
        String phone = phoneInput.getText().toString();
        String pwd = pwdInput.getText().toString();
        String code = codeInput.getText().toString();
        RequestParam param = RequestParam.builder(this);
        param.put("authtype", "phone");
        param.put("authtoken", phone.trim());
        param.put("authsecret", pwd.trim());
        param.put("authcode", code.trim());
        //设备类型
        param.put("from_device", Constants.LOGIN_DEVICE_TYPE);
        //渠道名称
        String channel_app_name = GetMetaDataInfo.getChannelName(this);
        if(!TextUtils.isEmpty(channel_app_name)){
         param.put("channel_app_name",channel_app_name);
//            param.put("channel_id",channelId);
        }
        register.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_USER_REGISTER, param, TAG, new XingBoResponseHandler<LoginUserModel>(this, LoginUserModel.class) {
            @Override
            public void phpXiuSuccess(String response) {
                register.setEnabled(true);
                CommonUtil.log(TAG, "注册结果：" + response);
//                if(!isFinishing()&&1==2){
//                    dialog("注册成功","恭喜您注册成功，确定离开注册界面？",new View.OnClickListener(){
//                        @Override
//                        public void onClick(View v) {
//                            setResult(RESULT_OK,null);
//                            finish();
//                        }
//                    });
//                }
//                setResult(RESULT_OK,null);

                sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(XingBo.PX_USER_LOGIN_UID, model.getD().getId());
                editor.putString(XingBo.PX_USER_LOGIN_NICK, model.getD().getNick());
                editor.putString(XingBo.PX_USER_LOGIN_AVATAR, model.getD().getAvatar());
                editor.putString(XingBo.PX_USER_LOGIN_LIVENAME, model.getD().getLivename());
                editor.commit();
                CommonUtil.log(TAG, "注册成功");
                CommonUtil.tip(RegisterAct.this, "注册成功,已登录", Gravity.BOTTOM);
                Intent intent = new Intent(RegisterAct.this, CompleteInfoAct.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                register.setEnabled(true);
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
