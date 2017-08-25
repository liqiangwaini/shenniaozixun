package com.xingbo.live.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.model.LoginUserModel;
import com.xingbo.live.eventbus.LocalStateEvent;
import com.xingbo.live.eventbus.LoginEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import de.greenrobot.event.EventBus;

/**
 * Created by WuJinZhou on 2015/10/21.
 */
public class LoginLocalAct extends BaseAct implements View.OnClickListener, DialogInterface.OnCancelListener {
    private final static String TAG = "LoginLocalAct";
    private final static int REGISTER_REQUEST_CODE = 0x1;
    private final static int RESET_PWD_REQUEST_CODE = 0x2;
    private EditText accountInput, pwdInput;
    private Button loginBtn;
    private TextView resetPwd, registerBtn;
    private SharedPreferences sp;
    private String netName;
    private int flg = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_local);
        ImageView welcome = (ImageView) findViewById(R.id.login_other_bg_image);
//        welcome.setImageBitmap(FastBlur.blurImageAmeliorate(BitmapFactory.decodeResource(getResources(),R.mipmap.welcome),30));
        accountInput = (EditText) findViewById(R.id.account);
        accountInput.setFocusable(true);
        accountInput.setFocusableInTouchMode(true);
        accountInput.requestFocus();
        pwdInput = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
        resetPwd = (TextView) findViewById(R.id.reset_pwd_link);
        resetPwd.setOnClickListener(this);
        registerBtn = (TextView) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);
        findViewById(R.id.top_back_btn).setOnClickListener(this);

        findViewById(R.id.xingbo_lisence_text).setOnClickListener(this);

//        FrescoImageView welcome=(FrescoImageView)findViewById(R.id.login_other_bg_image);

//        Uri uri=Uri.parse("res:///"+ R.mipmap.welcome);
//        welcome.setImageURI(uri);

    }

    @Override
    public void onClick(View v) {
//        if(v.getId()!=R.id.top_back_btn){
//            netName=sp.getString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_NO);
//            if(InternetStateBroadcast.NET_NO.equals(netName)){
//                alert("当前没有网络连接!");
//                return;
//            }
//        }
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.reset_pwd_link:
                EventBus.getDefault().post(new LocalStateEvent(1));
                resetPwd();
                break;
            case R.id.register_btn:
                EventBus.getDefault().post(new LocalStateEvent(1));
                goRegister();
                break;
            case R.id.top_back_btn:
                EventBus.getDefault().post(new LocalStateEvent(1));
                onBackPressed();
                break;
            case R.id.xingbo_lisence_text:
                Intent intent = new Intent(this, UserProtocolAct.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  //返回事件
            EventBus.getDefault().post(new LocalStateEvent(1));
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goRegister() {
        Intent register = new Intent(this, RegisterAct.class);
        startActivityForResult(register, 10);
    }

    private void resetPwd() {

        Intent resetPwd = new Intent(this, ResetPwd.class);
        startActivityForResult(resetPwd, RESET_PWD_REQUEST_CODE);
    }

    /**
     * 官方账号登录
     */
    private void login() {
        flg = 0;
        Intent intent = new Intent();
        intent.putExtra("flg", flg + "");
        setResult(RESULT_OK, intent);
        if (TextUtils.isEmpty(accountInput.getText())) {
            alert("请输入登录账号");
            return;
        }
        if (TextUtils.isEmpty(pwdInput.getText())) {
            alert("请输入登录密码");
            return;
        }
        String authtoken = accountInput.getText().toString().trim();
        String authsecret = pwdInput.getText().toString().trim();
        if (authtoken.equals("")) {
            alert("星播账号不能为空");
        } else if (authsecret.equals("")) {
            alert("密码不能为空");
        }
        if (!authtoken.equals("") && !authsecret.equals("")) {
            RequestParam param = RequestParam.builder(this);
            param.put("authtype", "phone");
            param.put("authtoken", authtoken);
            param.put("authsecret", authsecret);
            loginBtn.setEnabled(false);
            login(param);
        }
    }

    private void login(RequestParam param) {
        requestStart();
        loginBtn.setBackgroundResource(R.drawable.local_login_btn_select);
        CommonUtil.request(this, HttpConfig.API_USER_LOGIN, param, TAG, new XingBoResponseHandler<LoginUserModel>(this, LoginUserModel.class) {
            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
                loginBtn.setBackgroundResource(R.drawable.local_login_btn);
                CommonUtil.log(TAG, "登录结果：" + response);
                done();
                loginBtn.setEnabled(true);
                EventBus.getDefault().post(new LoginEvent());
                CommonUtil.tip(LoginLocalAct.this, "登录成功", Gravity.BOTTOM);
                sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(XingBo.PX_USER_LOGIN_UID, model.getD().getId());
                editor.putString(XingBo.PX_USER_LOGIN_NICK, model.getD().getNick());
                editor.putString(XingBo.PX_USER_LOGIN_AVATAR, model.getD().getAvatar());
                editor.putString(XingBo.PX_USER_LOGIN_LIVENAME, model.getD().getLivename());
                editor.commit();
                EventBus.getDefault().post(new LocalStateEvent(0));
                goHome();
            }

            @Override
            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                loginBtn.setBackgroundResource(R.drawable.local_login_btn);
                alert(msg);
                EventBus.getDefault().post(new LocalStateEvent(1));
                done();
                loginBtn.setEnabled(true);
            }
        });
    }

    /**
     * 前往主界面
     */
    public void goHome() {
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        if (sp.getString(XingBo.PX_USER_LOGIN_UID, "0").equals("0")) { //往登录界面
            Intent intent = new Intent(this, LoginLocalAct.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void showSoftInput(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册返回
        if (requestCode == REGISTER_REQUEST_CODE) {
            registerBtn.setEnabled(true);
            if (resultCode == RESULT_OK) {
                CommonUtil.log(TAG, "注册成功");
                EventBus.getDefault().post(new LoginEvent());
                CommonUtil.tip(this, "注册成功,已登录", Gravity.BOTTOM);
                finish();
            }
        } else if (requestCode == RESET_PWD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                alert("密码已重置成功");
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}


