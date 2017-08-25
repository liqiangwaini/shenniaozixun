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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
public class UserSexAct extends BaseAct implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private final static String TAG = "UserSexAct";
    public final static String EXTRA_SEX = "extra_sex";
    private RadioButton male, female;
    private RadioGroup sex;
    private Button submit;
    private String userSex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sex);
        userSex = getIntent().getStringExtra(EXTRA_SEX);


        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        sex = (RadioGroup) findViewById(R.id.sex_radio);
        sex.setOnCheckedChangeListener(this);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        if (userSex.equals("") || userSex.equals("男")) {
            male.setChecked(true);

        } else {
            female.setChecked(true);
        }
        findViewById(R.id.top_back_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
    public void submit() {

        CommonUtil.log(TAG, "xingbie" + userSex);

        RequestParam param = RequestParam.builder(this);
        param.put("sex", userSex);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                //alert("昵称修改成功");
                XingBoUtil.tip(UserSexAct.this, "性别修改成功!", Gravity.CENTER);
                XingBoUtil.log(TAG, "提交保存性别修改结果" + response);
                Intent intent = new Intent();
                intent.putExtra("user_sex", userSex);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == male.getId()) {
            userSex = "男";
        } else {
            userSex = "女";
        }
    }
}
