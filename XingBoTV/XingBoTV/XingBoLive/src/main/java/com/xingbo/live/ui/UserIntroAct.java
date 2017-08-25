package com.xingbo.live.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;

/**
 * Created by WuJinZhou on 2015/10/23.
 */
public class UserIntroAct extends BaseAct implements View.OnClickListener,TextWatcher {
    private final static String TAG="UserIntroAct";
    public final static String EXTRA_USER_INTRO="extra_user_intro";
    private EditText introInput;
    private Button submit;
    private TextView msg;
    private String intro;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_intro);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        introInput=(EditText)findViewById(R.id.user_intro);
        introInput.addTextChangedListener(this);
        intro=getIntent().getStringExtra(EXTRA_USER_INTRO);
        msg=(TextView)findViewById(R.id.intro_length_msg);
        if(intro!=null){
           introInput.setText(intro);
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.top_back_btn:
                    onBackPressed();
                    break;
                case R.id.submit:
                    submit();
                    break;
                default:
                    break;
            }
    }

    /**
     * 保存
     */
    public void submit(){
        String intro=introInput.getText().toString().trim();
        RequestParam param= RequestParam.builder(this);
        param.put("intro",intro);
        submit.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                //alert("说说修改成功");
                XingBoUtil.tip(UserIntroAct.this, "个人资料修改成功!", Gravity.CENTER);
                XingBoUtil.log(TAG, "提交保存说说结果" + response);
                Intent intent = new Intent();
                intent.putExtra("user_intro", introInput.getText().toString());
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s.length()>30){
            alert("最多输入30个字");
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length()>30){
            alert("最多输入30个字");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s != null) {
            msg.setText(s.toString().length() + "/30");
        }
    }
}
