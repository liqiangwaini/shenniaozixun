package com.xingbo.live.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
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
public class UserNickAct extends BaseAct implements View.OnClickListener,TextWatcher {
    private final static String TAG="UserNickAct";
    public final static String EXTRA_NICK="extra_nick";
    private EditText nameInput;
    private TextView msg;
    private Button submit;
    private String nick;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_nick);
        nick=getIntent().getStringExtra(EXTRA_NICK);
        nameInput=(EditText)findViewById(R.id.user_nick);
        msg=(TextView)findViewById(R.id.nick_length_msg);
        nameInput.addTextChangedListener(this);
        nameInput.setText(nick);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
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
        if(TextUtils.isEmpty(nameInput.getText())){
           CommonUtil.log(TAG, "昵称不能为空");
           alert("昵称不能为空");
           return;
        }
        if(TextUtils.getTrimmedLength(nameInput.getText())<2){
            CommonUtil.log(TAG, "昵称至少两个字符");
            return;
        }
        //昵称最多输入为12个字符
        if(TextUtils.getTrimmedLength(nameInput.getText())>12){
           CommonUtil.log(TAG, "昵称最多12个字符");
           return;
        }
        String nick=nameInput.getText().toString();
        RequestParam param= RequestParam.builder(this);
        param.put("nick",nick);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                //alert("昵称修改成功");
                XingBoUtil.tip(UserNickAct.this, "昵称修改成功!", Gravity.CENTER);
                XingBoUtil.log(TAG, "提交保存昵称结果" + response);
                Intent intent = new Intent();
                intent.putExtra("nick_name", nameInput.getText().toString());
                setResult(RESULT_OK, intent);
                finish();

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
        msg.setText(s.toString().length()+"/12");
    }
}
