package com.xingbo.live.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.RequestParam;
import com.xingbo.live.R;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;

/**
 * Created by WuJinZhou on 2016/3/9.
 */
public class SetNewPwd extends BaseAct implements View.OnClickListener {
    public final static String TAG = "SetNewPwd";
    private final static int PASSWORD_NEW_OLD_EQUALLY = 0;//新旧密码
    private final  static  int PASSWORD_NEW_NEW_DIFFERENT=1;//两次输入的新密码不一样
    private final static int PASSWORD_OLD_ERROR = 2;//原密码输入错误
    private final static  int PASSWORD_NEW_OLD_RESPONSE=3;
    private final static  int PASSWORD_NEW_NEW_RESPONSE=4;
    private final static int PASSWORD_OLD_ERROR_RESPONSE=5;

    private EditText pwd_old, pwd_new1, pwd_new2;
    private TextView newNewError,newOldError,oldError;
    private Button submitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_pwd);
        initView();
    }

    private void initView() {
        pwd_old = (EditText) findViewById(R.id.password_old);
        pwd_new1 = (EditText) findViewById(R.id.password_new1);
        pwd_new2 = (EditText) findViewById(R.id.password_new2);
        submitBtn = (Button) findViewById(R.id.password_ensure);
        oldError= (TextView) findViewById(R.id.password_err_old);
        newOldError= (TextView) findViewById(R.id.password_err_new1);
        newNewError= (TextView) findViewById(R.id.password_err_new2);
        submitBtn.setOnClickListener(this);
        findViewById(R.id.top_back_btn_psw).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn_psw:
                onBackPressed();
                break;
            case R.id.password_ensure:
                onSubmit();
                break;
            default:
                break;
        }
    }

    /**
     * 提交修改
     */
    private void onSubmit() {
      //  submitBtn.setEnabled(false);
        if (TextUtils.isEmpty(pwd_old.getText())) {
            alert("请输入密码");
            return;
        }

        if (TextUtils.isEmpty(pwd_new1.getText())) {
            alert("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(pwd_new2.getText())) {
            alert("请再次输入新密码");
            return;
        }
        if (pwd_new1.getText().toString().equals(pwd_old.getText().toString())) {//新旧密码相同
            newOldError.setVisibility(View.VISIBLE);
            pwd_new1.setBackgroundResource(R.drawable.edit_frame_color);
            handler.sendEmptyMessageDelayed(PASSWORD_NEW_OLD_EQUALLY,2000);

        } else if(!(pwd_new1.getText().toString().equals(pwd_new2.getText().toString()))){//两次输入的密码不一样
            newNewError.setVisibility(View.VISIBLE);
            pwd_new2.setBackgroundResource(R.drawable.edit_frame_color);
            handler.sendEmptyMessageDelayed(PASSWORD_NEW_NEW_DIFFERENT,2000);
        }else{
            RequestParam param = RequestParam.builder(this);
            param.put("password", pwd_old.getText().toString());
            param.put("password1", pwd_new1.getText().toString());
            param.put("password2", pwd_new2.getText().toString());
            CommonUtil.request(this, HttpConfig.API_USER_UPDATE_PWD, param, TAG, new XingBoResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
                @Override
                public void phpXiuErr(int errCode, String msg) {
                   // submitBtn.setEnabled(true);
                    oldError.setVisibility(View.VISIBLE);
                    pwd_old.setVisibility(View.VISIBLE);
                    pwd_old.setBackgroundResource(R.drawable.edit_frame_color);
                    handler.sendEmptyMessageDelayed(PASSWORD_OLD_ERROR,3000);

                }
                @Override
                public void phpXiuSuccess(String response) {
                    String m = model.getM();
                    CommonUtil.tip(SetNewPwd.this,m+"，请重新登录", Gravity.CENTER);
                    Intent loginIntent= new Intent(SetNewPwd.this,LoginAct.class);
                    startActivity(loginIntent);
                    setResult(RESULT_OK, null);
                }
            });
        }
    }

    @Override
    public void handleMsg(Message message) {
        switch (message.what){
            case PASSWORD_NEW_OLD_EQUALLY://新旧密码相同处理
                newOldError.setVisibility(View.INVISIBLE);
               // pwd_old.setText("");
                pwd_new2.setText("");
                pwd_new1.setText("");
                pwd_new1.setBackgroundResource(R.drawable.edit_frame_color_null);
                break;
            case PASSWORD_NEW_NEW_DIFFERENT://两次输入的新密码不同
                newNewError.setVisibility(View.INVISIBLE);
//                pwd_old.setText("");
                pwd_new2.setText("");
                pwd_new1.setText("");
                pwd_new2.setBackgroundResource(R.drawable.edit_frame_color_null);

                break;
            case PASSWORD_OLD_ERROR://原密码输入错误
               oldError.setVisibility(View.INVISIBLE);
//                pwd_old.setText("");
//                pwd_new2.setText("");
//                pwd_new1.setText("");
                pwd_old.setBackgroundResource(R.drawable.edit_frame_color_null);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        super.onBackPressed();
    }
}
