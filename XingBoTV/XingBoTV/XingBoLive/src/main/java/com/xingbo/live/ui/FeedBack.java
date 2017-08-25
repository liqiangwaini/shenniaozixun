package com.xingbo.live.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
 */
public class FeedBack extends BaseAct implements View.OnClickListener,TextWatcher {
    public final static String TAG="FeedBack";
    private EditText feedInput;
    private EditText phoneNum;
    private Button submit;
    private TextView msg;
    public final static String msgPre="还能再输入";
    public final static String msgEnd="个字";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        feedInput=(EditText)findViewById(R.id.user_feed);
        feedInput.addTextChangedListener(this);
        phoneNum= (EditText) findViewById(R.id.phone_qq_num);
        msg=(TextView)findViewById(R.id.user_feed_length_msg);
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
     * 提交
     */
    public void submit(){
        if(TextUtils.isEmpty(feedInput.getText())){
           return;
        }
        String word=feedInput.getText().toString();
        RequestParam param= RequestParam.builder(this);
        param.put("word",word);
        submit.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_APP_USER_FEED_BACK, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                submit.setEnabled(true);
            }

            @Override
            public void phpXiuSuccess(String response) {
                alert("非常感谢您的宝贵意见，您的信息我们已收到！");
                XingBoUtil.log(TAG, "反馈结果" + response);
                submit.setEnabled(true);
                phoneNum.setText("");
                feedInput.setText("");
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        msg.setText(msgPre+(200-s.toString().length())+msgEnd);
    }
}
