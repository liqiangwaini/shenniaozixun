package com.xingbobase.view.widget;

import android.app.Dialog;
import android.content.Context;

/**
 *
 * 加载或执行状态对话框
 * Created by WuJinZhou on 2016/1/7.
 */
public class XingBoLoadingDialog extends Dialog {
    private String tag;

    public XingBoLoadingDialog(Context context) {
        super(context);
    }

    public XingBoLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public XingBoLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
