package com.xingbobase.entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.xingbobase.config.XingBo;

import java.io.Serializable;

/**
 * Created by WuJinZhou on 2015/8/8.
 */
public class XingBoUser extends XingBoBaseItem implements Serializable {
    public final static String PX_USER_CACHE_FILE_RECORD_ID="px_user_cache_record_id";
    public final static String PX_USER_CACHE_FILE_USER_ID="px_user_cache_uid";
    public final static String PX_USER_CACHE_FILE_USER_NICK="px_user_cache_user_nick";
    private String _id;
    private String token;
    private String uid;//用户id
    private String nick;//昵称

    /**
     * 读取用户登录时缓存的信息
     */
    public static XingBoUser readCache(Context context){
        XingBoUser u=new XingBoUser();
        SharedPreferences sp=context.getSharedPreferences(XingBo.PX_USER_CACHE_FILE,Context.MODE_PRIVATE);
        if(sp!=null&&sp.getString(XingBo.PX_USER_CACHE_FILE_TOKEN,null)==null){
            return u;
        }else if(sp!=null&&sp.getString(XingBo.PX_USER_CACHE_FILE_TOKEN,null)!=null){
            u.setToken(sp.getString(XingBo.PX_USER_CACHE_FILE_TOKEN,null));
            u.set_id(sp.getString(PX_USER_CACHE_FILE_RECORD_ID,null));
            u.setUid(sp.getString(PX_USER_CACHE_FILE_USER_ID,null));
            u.setNick(sp.getString(PX_USER_CACHE_FILE_USER_NICK,null));
        }
        return u;
    }
    /**
     * 更新用户登录时缓存的信息
     */
    public void updateCache(Context context){
        SharedPreferences sp=context.getSharedPreferences(XingBo.PX_USER_CACHE_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(PX_USER_CACHE_FILE_RECORD_ID,get_id());
        editor.putString(XingBo.PX_USER_CACHE_FILE_TOKEN,getToken());
        editor.putString(PX_USER_CACHE_FILE_USER_ID,getUid());
        editor.putString(PX_USER_CACHE_FILE_USER_NICK,getNick());
        editor.commit();
    }

    /**
     * 更新用户信息某个字段的缓存
     */
    public static void updateCache(Context context,String key,String value){
        SharedPreferences sp=context.getSharedPreferences(XingBo.PX_USER_CACHE_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 读取某一用户配置的缓存
     */
    public static String readCacheByKey(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences(XingBo.PX_USER_CACHE_FILE,Context.MODE_PRIVATE);
        return sp.getString(key,null);
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
