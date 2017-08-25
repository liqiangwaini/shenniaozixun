package com.xingbobase.http;

import android.util.Log;

import com.google.gson.JsonParseException;
import com.loopj.android.http.TextHttpResponseHandler;
import com.xingbobase.api.OnHttpStateCallback;
import com.xingbobase.api.OnRequestErrCallBack;
import com.xingbobase.config.XingBo;
import com.xingbobase.util.XingBoUtil;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.Header;

/**
 * 请求回调
 * Created by WuJinZhou on 2015/8/8.
 */
public abstract class XingBoResponseHandler<T extends BaseResponseModel> extends TextHttpResponseHandler {
    public Class<T> cls;
    private WeakReference<OnHttpStateCallback> httpStateCallBack;
    public T model;
    protected OnRequestErrCallBack errCallBack;

    protected XingBoResponseHandler(Class<T> cls) {
        this.cls = cls;
    }

    protected XingBoResponseHandler(OnRequestErrCallBack callBack, Class<T> cls, OnHttpStateCallback httpStateCallBack) {
        this.cls = cls;
        this.errCallBack = callBack;
        if (httpStateCallBack != null) {
            this.httpStateCallBack = new WeakReference<OnHttpStateCallback>(httpStateCallBack);
        }
    }

    protected XingBoResponseHandler(OnRequestErrCallBack callBack, Class<T> cls) {
        this.cls = cls;
        this.errCallBack = callBack;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (httpStateCallBack != null && httpStateCallBack.get() != null) {
            httpStateCallBack.get().requestStart();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (httpStateCallBack != null && httpStateCallBack.get() != null) {
            httpStateCallBack.get().requestFinish();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
        Log.e("tag",throwable.toString());
        if (throwable.getMessage()!=null&&throwable.getMessage().contains("UnknownHostException")) {
            phpXiuErr(XingBo.RESPONSE_CODE_ERR, "无法连接服务器");
            return;
        }
        phpXiuErr(XingBo.RESPONSE_CODE_ERR, "请求失败，请重试");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String response) {
        XingBoUtil.log("PHPXiuHttp", "解析前数据：" + response);
        BaseResponseModel baseModel = null;
        try {
            baseModel = XingBoUtil.gson.fromJson(response, BaseResponseModel.class);
        } catch (JsonParseException e) {
            phpXiuErr(XingBo.RESPONSE_CODE_BAD, "获取数据异常(JsonParseException)");
            return;
        }
        if (baseModel == null || baseModel.getC() == null || baseModel.getC().equals("")) {
            phpXiuErr(XingBo.RESPONSE_CODE_BAD, "获取数据异常(some field is null)");
            return;
        }
        if (baseModel.getC().equals("10000")) {//需要登录
            if (errCallBack != null) {
                errCallBack.loginErr(XingBo.REQUEST_CODE_LOGIN_ERR, baseModel.getM());
            } else {
                phpXiuErr(XingBo.REQUEST_CODE_LOGIN_ERR, baseModel.getM());
            }
            return;
        }
        if (baseModel.getC().equals("20000")) {//余额不足
            if (errCallBack != null) {
                errCallBack.costErr(XingBo.REQUEST_CODE_PAY_ERR, baseModel.getM());
            } else {
                phpXiuErr(XingBo.REQUEST_CODE_PAY_ERR, baseModel.getM());
            }
            return;
        }
        if (!baseModel.getC().equals("1")) {//业务请求失败
            phpXiuErr(XingBo.REQUEST_CODE_FAILURE, baseModel.getM());
            return;
        }

        try {
            model = XingBoUtil.gson.fromJson(response, cls);
        } catch (JsonParseException e) {
            phpXiuErr(XingBo.RESPONSE_CODE_BAD, "获取数据异常(JsonParseException)");
            return;
        }

        if (model == null || model.getC() == null || model.getC().equals("")) {
            phpXiuErr(XingBo.RESPONSE_CODE_BAD, "获取数据异常(some field is null)");
            return;
        }
        phpXiuSuccess(response);
    }

    /**
     * 错误处理
     */
    public abstract void phpXiuErr(int errCode, String msg);

    public abstract void phpXiuSuccess(String response);
}
