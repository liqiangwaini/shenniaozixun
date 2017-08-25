package com.xingbo.live.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.eventbus.UpdateBalanceEvent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class UserAccountAct extends BaseAct implements View.OnClickListener {

    private final static String TAG = "UserAccountAct";

    public final static String EXTRA_USER_COIN="extra_user_coin";
    //充值记录框，消费记录框
    private RelativeLayout userAccount,userCostRecord;
    //用户星币数文本框
    private TextView userAccountCoin;
    //充值按钮
    private Button recharge_btn;
    //用户星币数
    private String userCoinValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_user_account);
        //接收参数
        userCoinValue = getIntent().getStringExtra(EXTRA_USER_COIN);
        if(userCoinValue==null||userCoinValue.equals("")){
            userCoinValue = "0";
        }

        initView();
    }

    private void initView(){
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        //点击转到充值记录，消费记录
        userAccount = (RelativeLayout) findViewById(R.id.user_pay_log);
        userAccount.setOnClickListener(this);
        userCostRecord = (RelativeLayout) findViewById(R.id.user_cost_record);
        userCostRecord.setOnClickListener(this);
        //用户星币显示
        userAccountCoin = (TextView) findViewById(R.id.user_account_coin);
        userAccountCoin.setText(userCoinValue);
        //充值按钮
        recharge_btn = (Button) findViewById(R.id.recharge_btn);
        recharge_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.user_pay_log:
                goPayLog();
                break;
            case R.id.user_cost_record:
                goSendLog();
                break;
            case R.id.recharge_btn:
                Intent recharge=new Intent(this, RechargeAct.class);
                recharge.putExtra(RechargeAct.EXTRA_USER_COIN_VALUE,Long.parseLong(userCoinValue));
                startActivity(recharge);
                break;
            default:
                break;
        }
    }

    /**
     * 转到充值记录列表
     */
    private void goPayLog(){
        Intent payLogAct = new Intent(this, UserPayLogAct.class);
        startActivity(payLogAct);
    }

    /**
     * 转到消费记录列表
     */
    private void goSendLog(){
        Intent sendLogAct = new Intent(this, UserSendLogAct.class);
        startActivity(sendLogAct);
    }

    /**
     * 更新余额
     */
    @Subscribe
    public void updateCoin(UpdateBalanceEvent event){
        try{
            userCoinValue=event.getCurrentCoin();
            userAccountCoin.setText(userCoinValue);
        }catch (Exception e){

        }
    }
}
