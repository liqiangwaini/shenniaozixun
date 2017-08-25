package com.xingbo.live.popup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.GuardTimeAndPrice;
import com.xingbo.live.entity.model.BalanceModle;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.ui.GuardAct;
import com.xingbo.live.ui.RechargeAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class GuardPop extends PopupWindow implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "GuardPop";

    private GuardAct act;
    private String rid;
    private String uid;
    private String anchorNick;
    private String anchorAvatar;

    private Button button;
    private TextView lastData;
    private TextView coinNotEnough;
    private TextView coin;
    private RadioButton goldSeason;
    private RadioButton goldMonth;
    private RadioButton normalSeason;
    private RadioButton normalMonth;
    private TextView nick;
    private FrescoImageView avatar;
    private final View view;

    private int guardType;
    private List<GuardTimeAndPrice> months;
    private ImageView cancel;

    private int month = 1;  //选中的守护月份
    private RadioGroup radioGroup;

    public GuardPop(GuardAct act, String rid, String uid, String nick, String anchorAvatar) {
        this.act = act;
        this.rid = rid;
        this.uid = uid;
        this.anchorNick = nick;
        this.anchorAvatar = anchorAvatar;
        view = View.inflate(act, R.layout.popup_open_guard, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setMainUser( List<GuardTimeAndPrice> months) {
        this.months = months;
        init();
    }

    public void setGuardData(List<GuardTimeAndPrice> months) {
        this.months = months;
    }

    public void init() {
        avatar = (FrescoImageView) view.findViewById(R.id.open_guard_avatar);
        nick = (TextView) view.findViewById(R.id.tv_open_guard_nick);
        cancel = (ImageView) view.findViewById(R.id.open_guard_cancel);
        radioGroup = (RadioGroup) view.findViewById(R.id.ll_open_guard);
        normalMonth = (RadioButton) view.findViewById(R.id.open_guard_normal_month);
        normalSeason = (RadioButton) view.findViewById(R.id.open_guard_normal_season);
        goldMonth = (RadioButton) view.findViewById(R.id.open_guard_gold_month);
        goldSeason = (RadioButton) view.findViewById(R.id.open_guard_gold_season);
        coin = (TextView) view.findViewById(R.id.open_guard_coin);
        coinNotEnough = (TextView) view.findViewById(R.id.open_guard_coin_not_enough);
        lastData = (TextView) view.findViewById(R.id.open_guard_lastdate);
        button = (Button) view.findViewById(R.id.button_open_guard);
        //主播头像与昵称
        avatar.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchorAvatar));
        nick.setText(anchorNick);
//        coin.setText(coinNow + "星币");
        coin.setText(months.get(0).getPrice() + "星币");
        if(((Integer.parseInt(months.get(0).getCoin())-months.get(0).getPrice()))<0){
            coinNotEnough.setVisibility(View.VISIBLE);
        }
        lastData.setText(months.get(0).getExpire());
        normalMonth.setChecked(true);
        //click
        radioGroup.setOnCheckedChangeListener(this);
        cancel.setOnClickListener(this);
        normalMonth.setOnClickListener(this);
        normalSeason.setOnClickListener(this);
        goldMonth.setOnClickListener(this);
        goldSeason.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_guard_cancel:
                dismiss();
                break;
            case R.id.button_open_guard:
                sendGuard(act);
                break;
        }
    }

    public void sendGuard(final Context context) {  //开通守护
        RequestParam param = RequestParam.builder(context);
        param.put("rid", rid);
        param.put("uid", rid);
        param.put("month", month + "");
        CommonUtil.request(context, HttpConfig.API_APP_SEND_GUARD, param, TAG, new XingBoResponseHandler<BalanceModle>(act, BalanceModle.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                XingBoUtil.dialog(context, "去充值", "取消", R.color.pink, R.color.c333333, msg, "余额不足，请充值", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.px_dialog_ok:
                                Intent intent= new Intent(context, RechargeAct.class);
                                intent.putExtra(RechargeAct.EXTRA_USER_COIN_VALUE, Long.parseLong(months.get(0).getCoin()+""));
                                act.startActivity(intent);
                                break;
                            case R.id.px_dialog_cancel:
                                dismiss();
                                break;
                        }
                    }
                }).show();
            }

            @Override
            public void phpXiuSuccess(String response) {
//                coin.setText(model.getD() + "星币");
                dismiss();
                listener.onOpenGuardSuccess();
//                act.alert("开通守护成功");
            }
        });
    }




    private OnOpenGuardSuccess listener;


    public void setOpenGuardListener(OnOpenGuardSuccess listener){
        this.listener=listener;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.open_guard_normal_month:
                guardType = 0;
                month = 1;
                if ((Integer.parseInt(months.get(0).getCoin()) - months.get(0).getPrice()) < 0) {
                    coinNotEnough.setVisibility(View.VISIBLE);
                } else {
                    coinNotEnough.setVisibility(View.INVISIBLE);
                }
                coin.setText(months.get(0).getPrice() + "星币");
                lastData.setText(months.get(0).getExpire());
                break;
            case R.id.open_guard_normal_season:
                guardType = 1;
                month = 3;
                if ((Integer.parseInt(months.get(1).getCoin()) - months.get(1).getPrice()) < 0) {
                    coinNotEnough.setVisibility(View.VISIBLE);
                } else {
                    coinNotEnough.setVisibility(View.INVISIBLE);
                }
                coin.setText(months.get(1).getPrice() + "星币");
                lastData.setText(months.get(1).getExpire());
                break;
            case R.id.open_guard_gold_month:
                guardType = 2;
                month = 6;
                if ((Integer.parseInt(months.get(2).getCoin()) - months.get(2).getPrice()) < 0) {
                    coinNotEnough.setVisibility(View.VISIBLE);
                } else {
                    coinNotEnough.setVisibility(View.INVISIBLE);
                }
                coin.setText(months.get(2).getPrice()+ "星币");
                lastData.setText(months.get(2).getExpire());
                break;
            case R.id.open_guard_gold_season:
                guardType = 3;
                month = 12;
                if ((Integer.parseInt(months.get(3).getCoin()) - months.get(3).getPrice()) < 0) {
                    coinNotEnough.setVisibility(View.VISIBLE);
                } else {
                    coinNotEnough.setVisibility(View.INVISIBLE);
                }
                coin.setText(months.get(3).getPrice() + "星币");
                lastData.setText(months.get(3).getExpire());
                break;
        }
    }

    public interface OnOpenGuardSuccess{
        public void onOpenGuardSuccess();
    }
}
