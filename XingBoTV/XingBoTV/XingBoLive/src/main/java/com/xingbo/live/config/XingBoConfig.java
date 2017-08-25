package com.xingbo.live.config;

import android.os.Environment;

import com.xingbobase.config.XingBo;
import com.xingbo.live.R;

/**
 * Created by WuJinZhou on 2016/1/8.
 */
public class XingBoConfig extends XingBo {
    public static final int COMMON_PAGE_SIZE = 10;
    public static final int MAIN_ROOM_PRI_MSG_HEIGHT = 450;

    public static final String MALE = "男";
    public static final String FEMALE = "女";
    //图片保存地址
    public static final String APP_DOWNLOAD_IMAGE_PATH= Environment.getExternalStorageDirectory() + "/xingbo/XingboDownload/image/";

    //直播间各状态
    public static final int START_VIDEO = 0x1;//启动播放器
    public static final int STOP_VIDEO = 0x2;//停止直播
    public static final int OPEN_OR_HIDE_GIFT_PAN = 0x3;//关闭或打开礼物面板
    public static final int VIDEO_CANCEL_IMAGE = 0x4;//隐藏视频播放上覆盖图片
    public static final int REFRESH_FANS_LIST = 0x5;  //刷新粉丝列表
    public static final int REFRESH_MESSAGE_LIST = 0x6;  //刷新消息列表
    public static final int REFRESH_PRI_MSG_COUNT = 0x7;  //刷新私信数量
    public static final int REFRESH_SCROLL_GIFT = 0x8;  //轨道礼物
    public static final int REFRESH_JOIN_ROOM = 0x9;  //进入房间
    public static final int REFRESH_LEAVE_ROOM = 0x10;  //离开房间
    public static final int REFRESH_ONLINES = 0x11;   //在线人数
    public static final int CLEAR_JOINS = 0x12;    //清除在线人数
    public static final int REFRESH_SCROLL_LASTEST = 0x13;  //滑动到最后一项
    public static final int CLEAR_SCROLL_GIFT = 0x14;//不显示跑道
    public static final int REFRESH_SCROLL_STATE = 0x15; //更新是否正在滑动的状态
    public static final int SHOW_EDIT_GIFT_NUMBER = 0x16; //显示编辑送礼数量布局
    public static final int SHOW_SCROLL_GIFT = 0x17;  //显示轨道礼物
    public static final int CANCEL_SCROLL_GIFT = 0x18;   //隐藏轨道礼物
    public static final int REFRESH_ANCHOR_EXP = 0x19;   //更新主播经验值
    public static final int ON_SUCCESS = 0x20;    //心跳包  成功
    public static final int ON_FAILER = 0x21;     //心跳包 失败
    public static final int COMMON_MSG=0X22;   //普通消息
    /**
     * VIP等级图标
     */
    public final static int[] VIP_LV_RES_IDS = {
            R.mipmap.vip_level_icon_1,
            R.mipmap.vip_level_icon_2,
            R.mipmap.vip_level_icon_3,
            R.mipmap.vip_level_icon_4,
            R.mipmap.vip_level_icon_5,
            R.mipmap.vip_level_icon_6,
            R.mipmap.vip_level_icon_7
    };

    /**
     * 守护等级图标
     */
    public final static int[] GUARD_LV_RES_IDS = {
            R.mipmap.room_guard_lvl_1,
            R.mipmap.room_guard_lvl_2,
            R.mipmap.room_guard_lvl_3,
            R.mipmap.room_guard_lvl_4,
            R.mipmap.room_guard_lvl_5,
            R.mipmap.room_guard_lvl_6,
            R.mipmap.room_guard_lvl_7
    };

    /**
     * 财富等级图标
     */
    public final static int[] RICH_LV_ICONS = {
            R.mipmap.rich_level_icon_0,
            R.mipmap.rich_level_icon_1,
            R.mipmap.rich_level_icon_2,
            R.mipmap.rich_level_icon_3,
            R.mipmap.rich_level_icon_4,
            R.mipmap.rich_level_icon_5,
            R.mipmap.rich_level_icon_6,
            R.mipmap.rich_level_icon_7,
            R.mipmap.rich_level_icon_8,
            R.mipmap.rich_level_icon_9,
            R.mipmap.rich_level_icon_10,
            R.mipmap.rich_level_icon_11,
            R.mipmap.rich_level_icon_12,
            R.mipmap.rich_level_icon_13,
            R.mipmap.rich_level_icon_14,
            R.mipmap.rich_level_icon_15,
            R.mipmap.rich_level_icon_16,
            R.mipmap.rich_level_icon_17,
            R.mipmap.rich_level_icon_18,
            R.mipmap.rich_level_icon_19,
            R.mipmap.rich_level_icon_20,
            R.mipmap.rich_level_icon_21,
            R.mipmap.rich_level_icon_22,
            R.mipmap.rich_level_icon_23,
            R.mipmap.rich_level_icon_24,
            R.mipmap.rich_level_icon_25,
            R.mipmap.rich_level_icon_26,
            R.mipmap.rich_level_icon_27,
            R.mipmap.rich_level_icon_28,
            R.mipmap.rich_level_icon_29,
            R.mipmap.rich_level_icon_30,
            R.mipmap.rich_level_icon_31,
            R.mipmap.rich_level_icon_32,
            R.mipmap.rich_level_icon_33,
    };

    /***
     * 明星等级图标
     */
    public final static int[] STAR_LV_ICONS = {
            R.mipmap.star_level_icon_0,
            R.mipmap.star_level_icon_1,
            R.mipmap.star_level_icon_2,
            R.mipmap.star_level_icon_3,
            R.mipmap.star_level_icon_4,
            R.mipmap.star_level_icon_5,
            R.mipmap.star_level_icon_6,
            R.mipmap.star_level_icon_7,
            R.mipmap.star_level_icon_8,
            R.mipmap.star_level_icon_9,
            R.mipmap.star_level_icon_10,
            R.mipmap.star_level_icon_11,
            R.mipmap.star_level_icon_12,
            R.mipmap.star_level_icon_13,
            R.mipmap.star_level_icon_14,
            R.mipmap.star_level_icon_15,
            R.mipmap.star_level_icon_16,
            R.mipmap.star_level_icon_17,
            R.mipmap.star_level_icon_18,
            R.mipmap.star_level_icon_19,
            R.mipmap.star_level_icon_20,
            R.mipmap.star_level_icon_21,
            R.mipmap.star_level_icon_22,
            R.mipmap.star_level_icon_23,
            R.mipmap.star_level_icon_24,
            R.mipmap.star_level_icon_25,
            R.mipmap.star_level_icon_26,
            R.mipmap.star_level_icon_27,
            R.mipmap.star_level_icon_28,
            R.mipmap.star_level_icon_29,
            R.mipmap.star_level_icon_30,
            R.mipmap.star_level_icon_31,
            R.mipmap.star_level_icon_32,
            R.mipmap.star_level_icon_33,
            R.mipmap.star_level_icon_34,
            R.mipmap.star_level_icon_35,
            R.mipmap.star_level_icon_36,
            R.mipmap.star_level_icon_37,
            R.mipmap.star_level_icon_38,
            R.mipmap.star_level_icon_39,
            R.mipmap.star_level_icon_40
    };

    /**
     * 点击用户弹出窗口底部权限菜单预定义
     */
    public final static String ROOM_POP_WINDOW_BAR_MENU_SEND_PUB = "sendPub";
    public final static String ROOM_POP_WINDOW_BAR_MENU_SEND_PRIVATE_MSG = "sendPri";//私聊
    public final static String ROOM_POP_WINDOW_BAR_MENU_SEND_GIFT = "sendGift";
    public final static String ROOM_POP_WINDOW_BAR_MENU_ADD_FOLLOW = "addFollow";//关注
    public final static String ROOM_POP_WINDOW_BAR_MENU_CANCEL_FOLLOW = "cancelFollow";
    public final static String ROOM_POP_WINDOW_BAR_MENU_REPORT = "report";//举报
    public final static String ROOM_POP_WINDOW_BAR_MENU_ADD_ADMIN = "addAdmin";//设为管理员
    public final static String ROOM_POP_WINDOW_BAR_MENU_CANCEL_ADMIN = "cancelAdmin";
    public final static String ROOM_POP_WINDOW_BAR_MENU_ADD_MUTE = "addMute";//禁言
    public final static String ROOM_POP_WINDOW_BAR_MENU_CANCEL_MUTE = "cancelMute";
    public final static String ROOM_POP_WINDOW_BAR_MENU_ADD_KICK = "addKick";//踢人
    public final static String ROOM_POP_WINDOW_BAR_MENU_CANCEL_KICK = "cancelKick";

    /**
     * 大礼物动画配置
     */
    public final static String[] ANIM_KEYS = {"love", "smile", "v", "v520", "v1314", "xin"};

    /**
     * 城市列表
     */
    public static final int[] ARRAY_CITY = new int[]{R.array.beijin_province_item,
            R.array.tianjin_province_item, R.array.heibei_province_item,
            R.array.shanxi1_province_item, R.array.neimenggu_province_item,
            R.array.liaoning_province_item, R.array.jilin_province_item,
            R.array.heilongjiang_province_item, R.array.shanghai_province_item,
            R.array.jiangsu_province_item, R.array.zhejiang_province_item,
            R.array.anhui_province_item, R.array.fujian_province_item,
            R.array.jiangxi_province_item, R.array.shandong_province_item,
            R.array.henan_province_item, R.array.hubei_province_item,
            R.array.hunan_province_item, R.array.guangdong_province_item,
            R.array.guangxi_province_item, R.array.hainan_province_item,
            R.array.chongqing_province_item, R.array.sichuan_province_item,
            R.array.guizhou_province_item, R.array.yunnan_province_item,
            R.array.xizang_province_item, R.array.shanxi2_province_item,
            R.array.gansu_province_item, R.array.qinghai_province_item,
            R.array.ningxia_province_item, R.array.xinjiang_province_item,
            R.array.hongkong_province_item, R.array.aomen_province_item,
            R.array.taiwan_province_item};
}
