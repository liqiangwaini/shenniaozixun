package com.xingbo.live.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
public class UserBirthAct extends BaseAct implements View.OnClickListener {
    private final static String TAG = "UserBirthAct";
    public final static String EXTRA_USER_BIRTH = "extra_user_birth";

    private TextView userBirth;
    private Button submit;
    private String birth;

    private int year = 2016;
    private int monthOfYear = 01;
    private int dayOfMonth = 01;
private DatePickerDialog mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_birth);

        findViewById(R.id.top_back_btn).setOnClickListener(this);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);

        userBirth = (TextView) findViewById(R.id.user_birth);
        userBirth.setOnClickListener(this);

        birth = getIntent().getStringExtra(EXTRA_USER_BIRTH);
        if (birth != null) {
            userBirth.setText(birth);
        }
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
            case R.id.user_birth:
                showDateDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 选择生日
     */
    private void showDateDialog() {
        if (null == mDialog) {
            mDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int month = monthOfYear + 1;
                    birth = year + "-" + month + "-" + dayOfMonth;
                    userBirth.setText(birth);

                }
            }, year, monthOfYear, dayOfMonth);
        }
        mDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDialog.show();
    }

    /**
     * 保存
     */
    public void submit() {

        final String birth = userBirth.getText().toString();
        RequestParam param = RequestParam.builder(this);
        param.put("birth", birth);
        submit.setEnabled(false);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
//                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {

                XingBoUtil.tip(UserBirthAct.this, "生日修改成功!", Gravity.CENTER);
                XingBoUtil.log(TAG, "提交保存生日结果" + response);
                Intent intent = new Intent();
                intent.putExtra("user_birth", userBirth.getText().toString());
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

}
