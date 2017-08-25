package com.xingbo.live.entity.msg;

/**
 * Created by WuJinZhou on 2015/12/8.
 */
public class BaseMsg {
    public final static String COMMON_MSG="sendpubmsg";
//    public final static String COMMON_MSG_FLOWER="sendflower";//送花
    public final static String COMMON_MSG_GIFT="sendgift";//发送礼物
    public final static String SYSTEM_JOIN_MSG="join";//进入房间通消息
    public final static String SYSTEM_LEAVE_MSG="leave";//离开房间消息
    public final static String SYSTEM_MSG_STOP_LIVE="stoplive";//停播
    public final static String SYSTEM_MSG_START_LIVE="startlive";//开播
    public final static String SYSTEM_MSG_ADD_MUTE="addmute";//禁言
    public final static String SYSTEM_MSG_CANCEL_MUTE="cancelmute";//取消禁言
    public final static String SYSTEM_MSG_MOVE_USER="addkick";//踢出聊天室
    public final static String SYSTEM_MSG_FOR_ADMIN="addadmin";//设置为管理员
    public final static String SYSTEM_MSG_CANCEL_ADMIN="canceladmin";//取消管理员
    public final static String SYSTEM_MSG_ANCHOR_LVL="anchorlvl";//主播等级变化通知
    public final static String PRIVATE_MSG="sendprimsg";//私聊信息
    public final static String BROAD_CAST_MSG="sendlaba";//广播信息
    public final static String FLY_WORD_MSG="sendflyword";//飞屏消息
//    public final static String AGREE_SONG_BOOK_MSG="agreesongorder";//主播同意了某人的点歌
    public final static String SCROLL_GIFT_MSG="scrollgift";//全站礼物通知
    public final static String M_SYSTEM_NOTICE="systemmessage";//系统公告
    public final static String SYSTEM_MSG_GUARD="sendguard";//开通守护
    public final static String SYSTEM_ONLINE_MSG="onlines"; //直播间观众列表

//    public final static String SYSTEM_REDPACKET_MSG="sendpacket";//更新红包消息
    public final static String SYSTEM_PUBPRI_MSG="sendPubprimsg"; //幸运礼物返奖信息

    public final static String ANCHOR_LV_UP_MSG="anchorlvlup";//主播升级通知
    public final static String USER_LV_UP_MSG="richlvlup";//用户升级通知
    public final static String TOU_TIAO_GIFT="toutiaogift";  //头条
    public final static String  CANCEL_USER_BAG_ITEM="canceluserbagitem";//消耗背包礼物

    private String type;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
