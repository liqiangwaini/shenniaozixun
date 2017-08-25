package com.xingbobase.http;

import com.google.gson.JsonParseException;
import com.loopj.android.http.TextHttpResponseHandler;
import com.xingbobase.api.OnHttpStateCallback;
import com.xingbobase.config.XingBo;
import com.xingbobase.util.XingBoUtil;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by WuJinZhou on 2016/2/3.
 */
public abstract class XingBoUploadHandler<T extends UploadFileResponseModel> extends TextHttpResponseHandler {
    public Class<T> cls;
    private WeakReference<OnHttpStateCallback> httpStateCallBack;
    public T model;
    protected XingBoUploadHandler(Class<T> cls) {
        this.cls = cls;
    }

    protected XingBoUploadHandler(Class<T> cls, OnHttpStateCallback httpStateCallBack) {
        this.cls = cls;
        this.httpStateCallBack =new WeakReference<OnHttpStateCallback>(httpStateCallBack);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(httpStateCallBack!=null&&httpStateCallBack.get()!=null){
            httpStateCallBack.get().requestStart();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if(httpStateCallBack!=null&&httpStateCallBack.get()!=null){
            httpStateCallBack.get().requestFinish();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

        if(throwable instanceof UnknownHostException){
            phpXiuErr(XingBo.RESPONSE_CODE_ERR,"无法连接服务器");
            return;
        }
        phpXiuErr(XingBo.RESPONSE_CODE_ERR, "上传失败:onFailure(" + statusCode + ")");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String response) {
        XingBoUtil.log("PHPXiuHttp", "解析前数据：" + response);
        UploadFileResponseModel baseModel=null;
        try{
            baseModel= XingBoUtil.gson.fromJson(response,UploadFileResponseModel.class);
        }catch (JsonParseException e){
            phpXiuErr(XingBo.RESPONSE_CODE_BAD,"获取数据异常(JsonParseException)");
            return;
        }
        if(baseModel==null||baseModel.getState()==null||baseModel.getState().equals("")){
            phpXiuErr(XingBo.RESPONSE_CODE_BAD,"获取数据异常(some field is null)");
            return;
        }

        if(!baseModel.getState().equals("SUCCESS")){
            phpXiuErr(XingBo.REQUEST_CODE_FAILURE,"上传失败");
            return;
        }

        try{
            model= XingBoUtil.gson.fromJson(response,cls);
        }catch (JsonParseException e){
            phpXiuErr(XingBo.RESPONSE_CODE_BAD,"获取数据异常(JsonParseException)");
            return;
        }

        if(model==null){
            phpXiuErr(XingBo.RESPONSE_CODE_BAD,"获取数据异常(some field is null)");
            return;
        }
        phpXiuSuccess(response);
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
        phpXiuProgress(bytesWritten,totalSize);
    }

    /**
     * 错误处理
     */
    public abstract void phpXiuErr(int errCode,String msg);

    public abstract void phpXiuSuccess(String response);

    public abstract void phpXiuProgress(long bytesWritten, long totalSize);
}
