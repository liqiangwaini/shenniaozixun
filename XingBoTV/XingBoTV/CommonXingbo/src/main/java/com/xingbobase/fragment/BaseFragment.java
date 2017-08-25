package com.xingbobase.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xingbobase.api.OnHttpStateCallback;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.widget.XingBoAlert;
import com.xingbobase.view.widget.XingBoDialog;
import com.xingbobase.view.widget.XingBoLoadingDialog;

import java.lang.ref.WeakReference;

/**
 * Created by WuJinZhou on 2015/8/8.
 */
public class BaseFragment extends Fragment implements OnHttpStateCallback {
    private static final String TAG="BaseFragment";
    protected final static String ARG="fragment_temp_arg";
    protected Activity act;
    protected Handler handler;
    protected XingBoDialog dialog;
    protected XingBoAlert alert;
    protected XingBoLoadingDialog loading;
    protected boolean isShow=false;//当前界面是否可见
    protected View rootView;
    protected String arg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act=getActivity();
        handler=new IHandler(this);
    }

    public void handleMsg(Message message){

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
    protected void showDoing(String TAG,DialogInterface.OnCancelListener listener){
        if(act==null||act.isFinishing()||!isShow){
            return;
        }
        if(loading==null){
            loading= XingBoUtil.showDoing(act, TAG, listener);
        }else{
            if(loading.isShowing()){
                loading.cancel();
            }
            loading.setTag(TAG);
            loading.setOnCancelListener(listener);
        }
        loading.show();
    }

    /**
     * 隐藏加载状态或执行状态
     */
    protected  void done(){
        if(loading!=null&&loading.isShowing()){
            loading.dismiss();
        }
    }


    /**
     * 警告提示
     */
    protected void alert(String msg){
        if(act==null||act.isFinishing()||!isShow){
            return;
        }
        if(alert==null){
            alert= XingBoUtil.alert(act, msg);
        }else {
            if(alert.isShowing()){
               alert.dismiss();
            }
            alert.setMsg(msg);
        }
        alert.show();
    }

    /**
     * 弹出对话框
     */
    protected void dialog(String title,String description,View.OnClickListener listener){
        if(act==null||act.isFinishing()||!isShow){
            return;
        }
        if (dialog == null) {
            dialog = XingBoUtil.dialog(act, title, description, listener);
        }else {
            if(dialog.isShowing()){
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
    protected void dialog(String ok,String cancel,int okColorId,int cancelColorId,String title,String description,View.OnClickListener listener){
        if(act==null||act.isFinishing()||!isShow){
            return;
        }
        if (dialog == null) {
            dialog = XingBoUtil.dialog(act, ok, cancel, okColorId, cancelColorId, title, description, listener);
        }else {
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            dialog.setTitle(title);
            dialog.setDescription(description);
            dialog.addListener(listener);
        }
        dialog.show();
    }

    /**
     * 显示当前界面
     */
    public void onShow(){

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow=isVisibleToUser;
        if(isShow){
           onShow();
        }
        if(!isVisibleToUser) {
            if (alert != null && alert.isShowing()) {
                alert.dismiss();
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (loading != null && loading.isShowing()) {
                loading.cancel();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    static class IHandler extends Handler{
        private WeakReference<BaseFragment> fragment;

        IHandler(BaseFragment fragment) {
            this.fragment = new WeakReference<BaseFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if(fragment!=null&&fragment.get()!=null) {
                fragment.get().handleMsg(msg);
            }
        }
    }
}
