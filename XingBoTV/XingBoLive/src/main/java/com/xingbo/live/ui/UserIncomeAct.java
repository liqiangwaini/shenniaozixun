package com.xingbo.live.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.model.UserIncomeModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;

public class UserIncomeAct extends BaseAct implements View.OnClickListener {

    private final static String TAG = "UserIncomeAct";
    private boolean isNeedInit = true;//是否需要初始化
    private RelativeLayout userIncome;
    private TextView today_income, total_income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_income);
        //初始化View
        this.initView();
    }

    private void initView(){
        findViewById(R.id.top_back_btn).setOnClickListener(this);

        //点击转到收益记录列表
        findViewById(R.id.user_income_tip).setOnClickListener(this);

        //换成提现功能
        userIncome = (RelativeLayout)findViewById(R.id.user_income);
        userIncome.setOnClickListener(this);

        //收益信息
        today_income = (TextView) findViewById(R.id.today_income);
        total_income = (TextView) findViewById(R.id.total_income);

        if (isNeedInit) {
            request();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.user_income:
               alert("此功能暂未开通，敬请期待哦！");
                break;
            case R.id.user_income_tip:
                showIncomeList();
                break;
            default:
                break;
        }
    }

    /**
     * 请求初始化
     */
    private void request() {
        RequestParam param = RequestParam.builder(this);
        CommonUtil.request(this, HttpConfig.API_APP_GET_USER_INCOME, param, TAG, new XingBoResponseHandler<UserIncomeModel>(UserIncomeModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                XingBoUtil.log(TAG, "用户收益结果：" + response);
                today_income.setText(model.getD().getTodayGain());
                total_income.setText(model.getD().getTotalGain());
                isNeedInit = false;
            }
        });
    }

    /**
     * 转到收益列表页面
     */
    private void showIncomeList(){
        Intent intent = new Intent(this, UserIncomeListAct.class);
        startActivity(intent);
//        finish();
    }
}
