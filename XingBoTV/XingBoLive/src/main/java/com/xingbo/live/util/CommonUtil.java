package com.xingbo.live.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.xingbo.live.broadcast.InternetStateBroadcast;
import com.xingbo.live.controller.RoomController;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.FileRequestParam;
import com.xingbobase.http.RequestParam;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.XingBoUploadHandler;
import com.xingbobase.util.XingBoUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;

/**
 * PHPXiu工具类
 * <p/>
 * Created by WuJinZhou on 2015/8/8.
 */
public class CommonUtil extends XingBoUtil {
    private static final String TAG = "CommonUtil";

    /**
     * 请求网络数据
     *
     * @param api                 业务接口
     * @param param               请求参数
     * @param tag                 请求标识
     * @param httpResponseHandler 响应回调
     */
    public static void request(Context context, String api, RequestParam param, String tag, XingBoResponseHandler httpResponseHandler) {
        if (RoomController.checkNetState(context).equals(InternetStateBroadcast.NET_NO)) {
            httpResponseHandler.phpXiuErr(XingBo.RESPONSE_NO_NET,"无网络连接状态");
            return;
        }
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);//取cookie
        client.setCookieStore(cookieStore);//加cookie
        String url = HttpConfig.SERVER + api + "?" + RequestParam.httpFormat(param);//拼接url和参数
        CommonUtil.log(tag, "请求：" + url);
        List<Cookie> cookies = cookieStore.getCookies();
        //client.setProxy("192.168.0.17",8888);//测试抓包时设置
        httpResponseHandler.setTag(tag);
        client.get(url, httpResponseHandler);
    }

    /**
     * 文件上传
     */
    public static void uploadFile(Context context,String path, XingBoUploadHandler httpResponseHandler) {
        if (RoomController.checkNetState(context).equals(InternetStateBroadcast.NET_NO)) {
            httpResponseHandler.phpXiuErr(XingBo.RESPONSE_NO_NET,"无网络连接状态");
            return;
        }
        FileRequestParam param = new FileRequestParam();
        try {
            File file = new File(path);
            param.put("file", file);
        } catch (FileNotFoundException e) {

        }
        client.post(HttpConfig.FILE_UPLOAD_API, param, httpResponseHandler);
    }

    /**
     * 创建弹幕解析器
     */
    public static BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }
        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);
        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }
}
