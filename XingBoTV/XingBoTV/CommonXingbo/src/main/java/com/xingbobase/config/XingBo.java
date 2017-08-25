package com.xingbobase.config;

/**
 * PHPXiu通用常量
 * Created by WuJinZhou on 2015/8/8.
 */
public class XingBo {
    public final static int REQUEST_OPERA_INIT=0x0;//请求初始化操作
    public final static int REQUEST_OPERA_REFRESH=0x1;//请求刷新
    public final static int REQUEST_OPERA_LOAD=0x2;//请求加载更多

    public final static int RESPONSE_CODE_ERR=0x3;//请求初始化操作
    public final static int RESPONSE_CODE_TIME_OUT=0x4;//请求刷新
    public final static int RESPONSE_CODE_BAD=0x5;//请求加载更多
    public final static int REQUEST_CODE_FAILURE=0x6;//请求失败
    public final static int REQUEST_CODE_LOGIN_ERR=0x7;//没有登录或已失效
    public final static int REQUEST_CODE_PAY_ERR=0x8;//余额不足
    public final static int RESPONSE_NO_NET=0x9;//没有网络链接
    /**
     * 配置信息缓存
     */
    public final static String PX_CONFIG_CACHE_FILE="px_config_cache_file";//缓存配置信息的文件
    public final static String PX_CONFIG_CACHE_IS_FIRST_RUN="px_config_cache_is_first_run";//第一次运行，卸载后再安装运行也默认为第一次运行
    public final static String PX_CONFIG_CACHE_DEVICE_ID="px_config_cache_device_id";//手机设备id
    public final static String PX_CONFIG_CACHE_NET_WORK="px_config_cache_net_work";//当前网络类型
    public final static String PX_CONFIG_CACHE_DEVICE_MODEL="px_config_cache_device_model";//手机设备型号

    /**
     * 登录用户信息缓存
     */
    public final static String PX_USER_CACHE_FILE="px_user_cache_file";
    public final static String PX_USER_CACHE_FILE_TOKEN="px_user_cache_token";//token

    public final static String PX_USER_LOGIN_CACHE="userloginfile";
    public final static String PX_USER_LOGIN_UID="px_user_login_uid";
    public final static String PX_USER_LOGIN_NICK="px_user_login_nick";
    public final static String PX_USER_LOGIN_AVATAR="px_user_login_avatar";
    public final static String PX_USER_LOGIN_LIVENAME="px_user_login_livename";
    public final static String PX_USER_LOGIN_VERSION_CHECK="version_check";
    //消息推送的缓存信息
    public final static String PX_USER_SETTING_NOTIFYCATION_FLAG="notifycation_is_send";
    public final  static  String PX_USER_SETTING_NOTIFYCATION_VOICE="notifycation_voice";
    public final  static  String PX_USER_SETTING_NOTIFYCATION_SHOKE="notifycation_shoke";
    public final  static  String PX_USER_SETTING_NOTIFYCATION_NO="notifycation_no";


    public final static  String PX_USER_SETTING_IS_IN_MAINROOM="is_in_mainroom";
    public final static  String PX_USER_SETTING_IS_ANCHORLIVE="is_anchorlive";

    //直播间信息缓存
    public final static String PX_ROOM_CACHE_FILE="px_room_cache_file";
    public final static String PX_ROOM_CURRENT_ANCHOR_ID="px_room_current_anchor_id";
    public final static String PX_ROOM_SLIDE="px_room_slide";
    public final static String PX_ROOM_POSTERLOGO="px_room_posterlogo";
}
