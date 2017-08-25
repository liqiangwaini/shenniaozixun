package com.xingbobase.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.xingbobase.api.OnHttpStateCallback;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.widget.XingBoAlert;
import com.xingbobase.view.widget.XingBoDialog;
import com.xingbobase.view.widget.XingBoLoadingDialog;

import java.lang.ref.WeakReference;

/**
 * 基本Act
 * Created by WuJinZhou on 2015/8/8.
 */
public class XingBoBaseAct extends FragmentActivity implements OnHttpStateCallback {
    protected Handler handler;
    protected XingBoDialog dialog;
    public XingBoAlert alert;
    public XingBoLoadingDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new IHandler(this);

    }

    public void handleMsg(Message message) {

    }

    /**
     * http请求开始回调
     */
    @Override
    public void requestStart() {

    }

    /**
     * http请求结束回调
     */
    @Override
    public void requestFinish() {

    }

    /**
     * 显示执行进度状态
     */
    protected void showDoing(String TAG, DialogInterface.OnCancelListener listener) {
        if (isFinishing()) {
            return;
        }
        if (loading == null) {
            loading = XingBoUtil.showDoing(this, TAG, listener);
        } else {
            if (loading.isShowing()) {
                loading.cancel();
            }
            loading.setTag(TAG);
            loading.setOnCancelListener(listener);
        }
        loading.show();
    }

    /**
     * 警告提示
     */
    public void alert(String msg) {
        if (isFinishing()) {
            return;
        }
        if (alert == null) {
            alert = XingBoUtil.alert(this, msg);
        } else {
            if (alert.isShowing()) {
                alert.dismiss();
            }
            alert.setMsg(msg);
        }
        alert.show();
    }


    /**
     * 弹出对话框
     */
    protected void dialog(String title, String description, View.OnClickListener listener) {
        if (isFinishing()) {
            return;
        }
        if (dialog == null) {
            dialog = XingBoUtil.dialog(this, title, description, listener);
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog.setTitle(title);
            dialog.setDescription(description);
            dialog.addListener(listener);
        }
        dialog.show();
    }

    /**
     * 弹出对话框
     */
    public void dialog(String ok, String cancel, int okColorId, int cancelColorId, String title, String description, View.OnClickListener listener) {
        if (isFinishing()) {
            return;
        }
        if (dialog == null) {
            dialog = XingBoUtil.dialog(this, ok, cancel, okColorId, cancelColorId, title, description, listener);
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog.setOKText(ok);
            dialog.setCancelText(cancel);
            dialog.setTitle(title);
            dialog.setDescription(description);
            dialog.addListener(listener);
        }
        dialog.show();
    }

    /**
     * 隐藏加载状态或执行状态
     */
    protected void done() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (IllegalStateException e) {
            finish();
        }
    }

    @Override
    public void finish() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    static class IHandler extends Handler {
        private WeakReference<XingBoBaseAct> act;

        IHandler(XingBoBaseAct act) {
            this.act = new WeakReference<XingBoBaseAct>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            act.get().handleMsg(msg);
        }
    }

}
