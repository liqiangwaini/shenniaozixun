package com.xingbo.live.ui;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.mm.sdk.modelbiz.AddCardToWXCardPackage;
import com.xingbo.live.R;
import com.xingbo.live.entity.model.CloseLiveModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.FastBlur;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.FrescoImageView;

/**
 * Created by xingbo_szd on 2016/8/17.
 */
public class CloseLiveAct extends BaseAct {
    private static final String TAG = "CloseLiveAct";
    public static final String ANCHOR_LIVE_PROFIT = "anchor_live_profit";
    public static final String ANCHOR_LIVE_LOGO = "anchor_live_logo";

    private View rootView;
    private TextView visitors;
    private TextView profits;
    private TextView back;
    private SpannableStringBuilder spannableStringBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.close_live_act, null);
        setContentView(rootView);
        visitors = (TextView) rootView.findViewById(R.id.close_live_visitors);
        profits = (TextView) rootView.findViewById(R.id.close_live_profits);
        back = (TextView) rootView.findViewById(R.id.close_live_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bitmap bitmap=getIntent().getParcelableExtra(ANCHOR_LIVE_LOGO);
        request();
    }

    public void showProfit() {
        String text1 = "收获";
        String text2 = getIntent().getIntExtra(ANCHOR_LIVE_PROFIT, 0) + "";
        String text3 = "星币";
        spannableStringBuilder = new SpannableStringBuilder(text1 + text2 + text3);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), text1.length(), text1.length() + text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text1.length() + text2.length(), (text1 + text2 + text3).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        profits.setText(spannableStringBuilder);
        text1 = null;
        text2 = null;
        text3 = null;
    }

    //请求开播结束数据
    public void request() {
        RequestParam param = RequestParam.builder(this);
        //API_LIVE_CLOSE
        CommonUtil.request(this, HttpConfig.API_LIVE_CLOSE, param, TAG, new XingBoResponseHandler<CloseLiveModel>(this, CloseLiveModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                showProfit();
                showVisitors(model.getD().getWatch_num());
            }
        });
    }

    public void showVisitors(int num) {
        String text1 = num + "";
        String text2 = "人观看";
        spannableStringBuilder = new SpannableStringBuilder(text1 + text2);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text1.length(), text1.length() + text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        visitors.setText(spannableStringBuilder);
        text1 = null;
        text2 = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        spannableStringBuilder = null;
    }
}
