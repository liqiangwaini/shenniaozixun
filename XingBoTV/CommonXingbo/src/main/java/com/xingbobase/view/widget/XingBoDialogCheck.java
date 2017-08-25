package com.xingbobase.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.xingbobase.R;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/10
 */
public class XingBoDialogCheck extends Dialog implements View.OnClickListener {
    private boolean isOkDismiss=true;//点击确定按钮，是否马上退出窗口对话
    private TextView titleView, descView;
    private Button ok,cancel;
    private View.OnClickListener listener;
    private View.OnClickListener listenerCannel;

    /**
     * 构造对话框
     */
    public static XingBoDialogCheck builder(Context context,int theme){
        return new XingBoDialogCheck(context,theme);
    }

    public XingBoDialogCheck(Context context) {
        super(context);
    }

    public XingBoDialogCheck(Context context, int theme) {
        super(context, theme);
    }

    protected XingBoDialogCheck(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(int layoutResId) {
        if(layoutResId!=0) {
            super.setContentView(layoutResId);
            titleView = (TextView) findViewById(R.id.px_dialog_title);
            descView = (TextView) findViewById(R.id.px_dialog_description);
            cancel = (Button) findViewById(R.id.px_dialog_cancel);
            cancel.setOnClickListener(this);
            ok = (Button) findViewById(R.id.px_dialog_ok);
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
    public XingBoDialogCheck setView(int layoutResId){
        setContentView(layoutResId);
        return this;
    }

    /**
     * 点触其它区域，退出对话框
     * @param cancel true 触其它区域，退出对话框，false 触其它区域，不退出对话框
     */
    public XingBoDialogCheck isCanceledOnTouchOutside(boolean cancel){
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 设置对话框标题
     * @param title 对话框标题
     */
    public XingBoDialogCheck setTitle(String title) {
        if (titleView != null) {
            titleView.setText(title);
        }
        return this;
    }

    /**
     * 设置对话框提示信息
     * @param description 对话框提示信息
     */
    public XingBoDialogCheck setDescription(String description) {
        if (descView != null) {
            descView.setText(description);
        }
        return this;
    }

    /**
     * 设置对话框是否在点击确定按钮后退出
     * @param isOkDismiss 默认为true，点击确定后，对话框退出，false 点击确定后对话框不退出
     */
    public XingBoDialogCheck isOkDismiss(boolean isOkDismiss) {
        this.isOkDismiss = isOkDismiss;
        return this;
    }

    /**
     *设置确定按钮文字
     * 在设调用setView（layoutResId）之后调用
     */
    public XingBoDialogCheck setOKText(String okTxt){
        if(ok!=null){
            ok.setText(okTxt);
        }
        return this;
    }

    /**
     *设置取消按钮文字
     * 在设调用setView（layoutResId）之后调用
     */
    public XingBoDialogCheck setCancelText(String cancelTxt){
        if(cancel!=null){
            cancel.setText(cancelTxt);
        }
        return this;
    }

    /**
     * 设置对话框确定按钮文字颜色
     * @param colorId 对话框确定按钮文字颜色
     */
    public XingBoDialogCheck okBtnTxtColor(int colorId){
        if(colorId!=0) {
            ok.setTextColor(getContext().getResources().getColor(colorId));
        }
        return  this;
    }

    /**
     * 设置对话框取消按钮文字颜色
     * @param colorId 对话框取消按钮文字颜色
     */
    public XingBoDialogCheck cancelBtnTxtColor(int colorId){
        if(colorId!=0) {
            cancel.setTextColor(getContext().getResources().getColor(colorId));
        }
        return this;
    }

    /**
     * 对话框，确定按钮点击事件处理回调
     * @param l 监听回调
     */
    public XingBoDialogCheck addListener(View.OnClickListener l) {
        listener=l;
        return this;
    }

    /**
     * 对话框 取消按钮点击事件处理回调
     * @param l
     */
    public XingBoDialogCheck addCannelListener(View.OnClickListener l){
        listenerCannel=l;
        return this;

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.px_dialog_ok) {
            if(listener!=null){
                listener.onClick(v);
            }
            if(isOkDismiss) {
                dismiss();
            }
        }else{

            if (listenerCannel!=null){
                listenerCannel.onClick(v);
            }
            if (isOkDismiss){
                dismiss();
            }
//
        }
    }


}
