package com.xingbobase.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xingbobase.R;

/**
 * 警告提示
 * Created by WuJinZhou on 2015/8/8.
 */
public class XingBoAlert extends Dialog implements View.OnClickListener {
    private TextView msgTv;
    private Button ok;
    private View.OnClickListener listener;
    /**
     * 构造对话框
     */
    public static XingBoAlert builder(Context context,int theme){
        return new XingBoAlert(context,theme);
    }

    public XingBoAlert(Context context) {
        super(context);
    }

    public XingBoAlert(Context context, int theme) {
        super(context, theme);
    }

    protected XingBoAlert(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(int layoutResId) {
        if(layoutResId!=0) {
            super.setContentView(layoutResId);
            msgTv = (TextView) findViewById(R.id.px_alert_msg);
            ok = (Button) findViewById(R.id.px_alert_ok);
            ok.setOnClickListener(this);
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }

    /**
     * 设置对话框视图
     * @param layoutResId 视图资源id
     */
    public XingBoAlert setView(int layoutResId){
        setContentView(layoutResId);
        return this;
    }

    /**
     * 点触其它区域，退出对话框
     * @param cancel true 触其它区域，退出对话框，false 触其它区域，不退出对话框
     */
    public XingBoAlert isCanceledOnTouchOutside(boolean cancel){
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 设置警告提示信息
     * @param msg 警告提示信息
     */
    public XingBoAlert setMsg(String msg) {
        if (msgTv!= null) {
            msgTv.setText(msg);
        }
        return this;
    }

    public XingBoAlert addOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if(listener!=null){
           setCanceledOnTouchOutside(false);
           listener.onClick(v);
        }
    }

}
