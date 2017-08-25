package com.xingbobase.http;

import android.content.Context;
import android.content.SharedPreferences;

import com.xingbobase.BuildConfig;
import com.xingbobase.config.XingBo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * 请求参数
 *
 * Created by WuJinZhou on 2015/9/8.
 */
public class RequestParam extends LinkedHashMap<String,String> {

    private final static  boolean CONTAIN_OPTIONAL_PARAM=false;//包含所有可选参数

    private final static Random RANDOM=new Random();

    public static RequestParam builder(Context context){
        SharedPreferences sp=context.getSharedPreferences(XingBo.PX_CONFIG_CACHE_FILE,context.MODE_PRIVATE);
        RequestParam param=new RequestParam();
        if(CONTAIN_OPTIONAL_PARAM){
            param.put("deviceid",sp.getString(XingBo.PX_CONFIG_CACHE_DEVICE_ID,"badId"));
            param.put("netspeed",sp.getString("net_speed","0"));
            param.put("network",sp.getString(XingBo.PX_CONFIG_CACHE_NET_WORK,"unKnow"));
            param.put("rand", (Math.abs(RANDOM.nextLong()) + "").substring(0, 8));
            param.put("time",(System.currentTimeMillis()/1000)+"");
            param.put("version", BuildConfig.VERSION_NAME);
            param.put("model",sp.getString(XingBo.PX_CONFIG_CACHE_DEVICE_MODEL,"unKnow"));
        }
        param.put("platform", "android");
        return param;
    }

    @Override
    public String put(String key, String value) {
        try {
            value= URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return super.put(key, value);
    }

    /**
     * 将map转换为http请求的url参数形式字符串
     */
    public static String httpFormat(RequestParam map){
        String param="";
        for (String paraName:map.keySet()){
            param+=paraName+"="+map.get(paraName)+"&";
        }
        if(param==null||param.equals("")){
            return null;
        }
        return param.substring(0,param.length()-1);
    }
}
