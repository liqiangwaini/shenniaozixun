package com.xingbo.live.util;

import com.xingbo.live.emotion.EmotionEntity;
import com.xingbo.live.emotion.Emotions;
import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.entity.msg.MsgTUser;
import com.xingbo.live.http.HttpConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WuJinZhou on 2016/2/18.
 */
public class MsgHelper {
    public final static String TAG = "MsgHelper";
    public static final Pattern PATTERN = Pattern.compile(Emotions.EMOTICON_REGEX);

    private final static String BASE_FLOWER_ICON = "file:///android_asset/gift/flower_icon.png";
    private final static String BASE_RICH_LV_ICON_PREFIX = "file:///android_asset/lvIcons/rich_level_icon_";
    private final static String BASE_STAR_LV_ICON_PREFIX = "file:///android_asset/lvIcons/star_level_icon_";
    private final static String BASE_GUARD_LV_ICON_PREFIX = "file:///android_asset/lvIcons/room_guard_lvl_";
    private final static String BASE_VIP_LV_ICON_PREFIX = "file:///android_asset/lvIcons/vip_level_icon_";
    private final static String BASE_EMOTION_ICON_PREFIX = "file:///android_asset/";
    private final static String ICON_SUFFIX = ".png";
    public static Map<String, EmotionEntity> emotionMap = new HashMap<>();
    public final static String GIFT_ICON_URL = "gift_icon_url";
    public final static String MUSIC_ICON_URL = "music_icon_url";
    public final static String BROADCAST_ICON_URL = "broadcast_icon_url";
    public final static String DIV_CENTER_START = "<div align=\"center\">";

    public final static String LI_SYSTEM_MSG_START = "<li class=\"center\">";//系统信息
    public final static String LI_GIFT_MSG_START = "<li class=\"gift\">";
    public final static String LI_COMMON_MSG_START = "<li class=\"chat\">";
    public final static String LI_SYSTEM_NOTICE_START = "<li class=\"systen\" onclick=systemMsg(\"";//系统公告
    public final static String LI_SYSTEM_NOTICE_END = "\")>";

    public final static String SPAN_SYSTEM_MSG_START = "<span class=\"text\">";
    public final static String SPAN_NICK_CLICKABLE_START = "<span class=\"name\" onclick=\"popUserWin(";
    public final static String SPAN_NICK_CLICKABLE_END = ")\">";
    public final static String SPAN_NICK_START = "<span class=\"name\">";
    public final static String SPAN_MID_OPERA_N5_START = "<span class=\"name mr-5\" onclick=\"popUserWin(";
    public final static String SPAN_MID_OPERA_N5_END = ")\">";
    public final static String SPAN_MID_OPERA_T5_START = "<span class=\"text mr-5\">";

    public final static String SPAN_EMOTION_TEXT = "<span class=\"chat-text\">";
    public final static String IMAGE_GIFT_START = "<img class=\"gift-img\" src=\"";
    public final static String IMAGE_EMOTION_START = "<img class=\"emoji\" src=\"";
    public final static String IMAGE_EMOTION_NO_STYLE_START = "<img src=\"";
    public final static String SPAN_TEXT_CENTER_START = "<span style=\"text-align:center;\">";
    public final static String IMAGE_END = "\">";
    public final static String LI_END = "</li>";
    public final static String SPAN_END = "</span>";
    public final static String DIV_END = "</div>";

    MsgHelper(){}

    public static void registerEmotion(EmotionEntity emotionEntity) {
        emotionMap.put(emotionEntity.getCode(), emotionEntity);
    }

    public static String formatSystemMsg(String msg) {
        String tempMsg = LI_SYSTEM_MSG_START + SPAN_SYSTEM_MSG_START;
        tempMsg = tempMsg + msg + SPAN_END + LI_END;
        return tempMsg;
    }

    public static String formatSystemMsg(String prefix, String user, String msg) {
        String tempMsg = null;
        if (msg != null && !msg.equals("")) {
            tempMsg = LI_SYSTEM_MSG_START + SPAN_SYSTEM_MSG_START;
            if (prefix != null && !prefix.equals("")) {
                tempMsg = tempMsg + prefix;
            }
            if (user != null && !user.equals("")) {
                tempMsg = tempMsg + "“" + user + "”";
            }
            tempMsg = tempMsg + msg + SPAN_END + LI_END;
        }
        return tempMsg;
    }

    /**
     * 系统公告
     */
    public static String formatMSystemMsg(String msg, String href) {
        String tempMsg = null;
        if (msg != null && !msg.equals("")) {
            tempMsg = LI_SYSTEM_NOTICE_START + href + LI_SYSTEM_NOTICE_END + SPAN_NICK_START;
            tempMsg = tempMsg + "星广播：" + SPAN_END + SPAN_SYSTEM_MSG_START;
            tempMsg = tempMsg + msg + SPAN_END + LI_END;
        }
        return tempMsg;
    }

    /**
     * 端午中奖消息
     * @param msg
     * @return
     */
    public static String formatSystemPubMsg(String msg) {
        String tempMsg = null;
        if (msg != null && !msg.equals("")) {
            tempMsg = LI_SYSTEM_NOTICE_START + "" + LI_SYSTEM_NOTICE_END + SPAN_NICK_START;
            tempMsg = tempMsg + "星广播：" + SPAN_END + SPAN_SYSTEM_MSG_START;
            tempMsg = tempMsg + msg + SPAN_END + LI_END;
        }
        return tempMsg;
    }

    public static String formatCommonMsg(MsgFUser user, String msg) {
        String tempMsg = "";
        if (user != null && msg != null) {
            tempMsg += LI_COMMON_MSG_START;
            tempMsg += formatNickSpan(user, null, 1);
            tempMsg = tempMsg + SPAN_EMOTION_TEXT;
            Matcher matcher = PATTERN.matcher(msg);
            while (matcher.find()) {
                String group = matcher.group();
                EmotionEntity emotionEntity = emotionMap.get(group);
                msg = msg.replace(group, createImage(IMAGE_EMOTION_START, BASE_EMOTION_ICON_PREFIX, emotionEntity.getSource()));
            }
            tempMsg = tempMsg + msg + SPAN_END + LI_END;
        }
        return tempMsg;
    }

    public static String formatCommonMsg(MsgFUser user, String msg, String anchor) {
        String tempMsg = "";
        if (user != null && msg != null) {
            tempMsg += LI_COMMON_MSG_START;
            tempMsg += formatNickSpan(user, null, 1);
            tempMsg = tempMsg + SPAN_EMOTION_TEXT;
            Matcher matcher = PATTERN.matcher(msg);
            while (matcher.find()) {
                String group = matcher.group();
                EmotionEntity emotionEntity = emotionMap.get(group);
                msg = msg.replace(group, createImage(IMAGE_EMOTION_START, BASE_EMOTION_ICON_PREFIX, emotionEntity.getSource()));
            }
            tempMsg = tempMsg + msg + SPAN_END + LI_END;
        }
        return tempMsg;
    }

    public static String formatCommonPrivateMsg(MsgFUser sender, MsgTUser receiver, String msg) {
        String tempMsg = "";
        if (sender != null && receiver != null && msg != null) {
            tempMsg += LI_COMMON_MSG_START;
            tempMsg += formatNickSpan(sender, null, 0);
            tempMsg += SPAN_SYSTEM_MSG_START + "对" + SPAN_END;
            tempMsg += formatNickSpan(null, receiver, 1);
            tempMsg += SPAN_EMOTION_TEXT;
            Matcher matcher = PATTERN.matcher(msg);
            while (matcher.find()) {
                String group = matcher.group();
                EmotionEntity emotionEntity = emotionMap.get(group);
                msg = msg.replace(group, createImage(IMAGE_EMOTION_START, BASE_EMOTION_ICON_PREFIX, emotionEntity.getSource()));
            }
            tempMsg = tempMsg + msg + SPAN_END + LI_END;
        }
        return tempMsg;
    }

    /**
     * 守护开通信息
     */
    public static String formatGuardMsg(MsgFUser sender, int lvl, String num) {
        String tempMsg = "";
        if (sender != null) {
            tempMsg += LI_GIFT_MSG_START;
            tempMsg += formatNickSpan(sender, null, 0);
            tempMsg += SPAN_MID_OPERA_T5_START + "开通" + SPAN_END;
            tempMsg += IMAGE_GIFT_START + BASE_GUARD_LV_ICON_PREFIX + lvl + ICON_SUFFIX + IMAGE_END;//createImage(IMAGE_GIFT_START, BASE_GUARD_LV_ICON_PREFIX,lvl+"");
            tempMsg += SPAN_SYSTEM_MSG_START + "x" + num + SPAN_END;
            tempMsg += LI_END;
        }
        return tempMsg;
    }

    public static String formatFlowerMsg(MsgFUser sender, String num) {
        String tempMsg = "";
        if (sender != null) {
            tempMsg += LI_GIFT_MSG_START;
            tempMsg += formatNickSpan(sender, null, 0);
            tempMsg = tempMsg + SPAN_MID_OPERA_T5_START + "送出" + SPAN_END;
            tempMsg = tempMsg + createImage(IMAGE_GIFT_START, "", BASE_FLOWER_ICON);
            tempMsg = tempMsg + SPAN_SYSTEM_MSG_START + "x" + num + SPAN_END;
            tempMsg = tempMsg + LI_END;
        }
        return tempMsg;
    }

    public static String formatGiftMsg(MsgFUser sender, String giftIcon, String num) {
        String tempMsg = "";
        if (sender != null) {
            tempMsg += LI_GIFT_MSG_START;
            tempMsg += formatNickSpan(sender, null, 0);
            tempMsg = tempMsg + SPAN_MID_OPERA_T5_START + "送出" + SPAN_END;
            tempMsg = tempMsg + createImage(IMAGE_GIFT_START, HttpConfig.FILE_SERVER, giftIcon);
            tempMsg = tempMsg + SPAN_SYSTEM_MSG_START + "x" + num + SPAN_END;
            tempMsg = tempMsg + LI_END;
        }
        return tempMsg;
    }

    public static String formatGiftMsg(MsgFUser sender, MsgTUser receiver, String giftIcon, String num) {
        String tempMsg = null;
        if (sender != null) {
            tempMsg = LI_GIFT_MSG_START;
            tempMsg += formatNickSpan(sender, null, 0);
            tempMsg = tempMsg + SPAN_SYSTEM_MSG_START + "送给" + SPAN_END;
            tempMsg += formatGiftReceiverNickSpan(receiver);
            tempMsg = tempMsg + createImage(IMAGE_GIFT_START, HttpConfig.FILE_SERVER, giftIcon);
            tempMsg = tempMsg + SPAN_SYSTEM_MSG_START + "x" + num + SPAN_END;
            tempMsg = tempMsg + LI_END;
        }
        return tempMsg;
    }

    public static String formatGiftSeriesMsg(MsgFUser sender, MsgTUser receiver, String giftIcon, String num,String seriesNum) {
        String tempMsg = null;
        if (sender != null) {
            tempMsg = LI_GIFT_MSG_START;
            tempMsg += formatNickSpan(sender, null, 0);
            tempMsg = tempMsg + SPAN_SYSTEM_MSG_START + "送给" + SPAN_END;
            tempMsg += formatGiftReceiverNickSpan(receiver);
            tempMsg = tempMsg + createImage(IMAGE_GIFT_START, HttpConfig.FILE_SERVER, giftIcon);
            tempMsg = tempMsg + SPAN_SYSTEM_MSG_START + "x" + num + "连送 x"+seriesNum+SPAN_END;
            tempMsg = tempMsg + LI_END;
        }
        return tempMsg;
    }

    public static String formatPrivetMsg(String uid, String nick, String msg, boolean isReceiver) {
        String tempMsg = null;
        if (uid != null && !uid.equals("") && msg != null) {
            if (isReceiver) {
                tempMsg = LI_COMMON_MSG_START + SPAN_NICK_CLICKABLE_START;
                tempMsg = tempMsg + uid + SPAN_NICK_CLICKABLE_END;
                tempMsg = tempMsg + nick + SPAN_END;
                tempMsg = tempMsg + SPAN_SYSTEM_MSG_START + "对" + SPAN_END;
//                tempMsg = tempMsg + SPAN_NICK_START + "你：" + SPAN_END;
                tempMsg = tempMsg + SPAN_NICK_START + "我：" + SPAN_END;

                tempMsg = tempMsg + SPAN_EMOTION_TEXT;
            } else {
                tempMsg = LI_COMMON_MSG_START + SPAN_NICK_START;
//                tempMsg = tempMsg + "你" + SPAN_END;
                tempMsg = tempMsg + "我" + SPAN_END;
                tempMsg = tempMsg + SPAN_SYSTEM_MSG_START + "对" + SPAN_END;
                tempMsg = tempMsg + SPAN_NICK_CLICKABLE_START + uid + SPAN_NICK_CLICKABLE_END;
                tempMsg = tempMsg + nick + "：" + SPAN_END;
                tempMsg = tempMsg + SPAN_EMOTION_TEXT;
            }
            Matcher matcher = PATTERN.matcher(msg);
            while (matcher.find()) {
                String group = matcher.group();
                EmotionEntity emotionEntity = emotionMap.get(group);
                msg = msg.replace(group, createImage(IMAGE_EMOTION_START, BASE_EMOTION_ICON_PREFIX, emotionEntity.getSource()));
            }
            tempMsg = tempMsg + msg + LI_END;
        }
        return tempMsg;
    }

    /***
     * 解析喇叭消息为HTML
     */
    public static String formatScrollBroadcastMsg(String nick, String msg) {
        //<span style="text-align:center;width:200px;background-color:yellow;">abc</span>
        //<div align="center"><span>abc</span></div>
        String tempMsg = null;
        tempMsg = SPAN_TEXT_CENTER_START + IMAGE_EMOTION_NO_STYLE_START + BROADCAST_ICON_URL + IMAGE_END + nick + "说：";
        Matcher matcher = PATTERN.matcher(msg);
        while (matcher.find()) {
            String group = matcher.group();
            EmotionEntity emotionEntity = emotionMap.get(group);
            msg = msg.replace(group, createImage(IMAGE_EMOTION_NO_STYLE_START, "", emotionEntity.getSource()));
        }
        tempMsg = tempMsg + msg + SPAN_END + SPAN_END;
        return tempMsg;
    }

    public static String createImage(String base, String prefix, String source) {
        return base + prefix + source + IMAGE_END;
    }

    /**
     * 构造昵称块
     */
    public static String formatNickSpan(MsgFUser sender, MsgTUser receiver, int isEnd) {
        String span = "<span class=\"name\" onclick=\"popUserWin(" + (sender == null ? receiver.getId() : sender.getId()) + ")\">";
        //如果发送者的id 和主播id相同就是主播只显示麦克图标

        try {
            int richLv = Integer.parseInt(sender == null ? receiver.getRichlvl() : sender.getRichlvl());
            span += "<img class=\"user-icon user-rich-icon\" src=\"file:///android_asset/lvIcons/rich_level_icon_" + richLv + ".png\">";
        } catch (NumberFormatException e) {

        }
        /**
         * 解析主播等级, 显示主播等级图标
         */

        try{
                int starLv=Integer.parseInt(sender==null?receiver.getAnchorlvl():sender.getAnchorlvl());
            String leavename = sender == null ? receiver.getLivename() : sender.getLivename();
            if (starLv >= 0 && !"0".equals(leavename)) { //其它主播，显示start
                span += "<img class=\"user-icon user-star-icon\" src=\"file:///android_asset/lvIcons/star_level_icon_" + starLv + ".png\">";
            }

        }catch (NumberFormatException e){

        }

        try {
            int guardLv = Integer.parseInt(sender == null ? receiver.getGuardlvl() : sender.getGuardlvl());
            if (guardLv > 0) {
                if (guardLv == 1) {
                    span += "<img class=\"user-icon user-guard-icon\" src=\"file:///android_asset/lvIcons/room_guard_lvl_1.png\">";
                } else {
                    span += "<img class=\"user-icon user-guard-icon\" src=\"file:///android_asset/lvIcons/room_guard_lvl_2.png\">";
                }
            }
        } catch (NumberFormatException e) {

        }

        try {
            int vipLv = Integer.parseInt(sender == null ? receiver.getViplvl() : sender.getViplvl());
            if (vipLv > 0) {
                span += "<img class=\"user-icon user-vip-icon\" src=\"file:///android_asset/lvIcons/vip_level_icon_" + vipLv + ".png\">";
            }
        } catch (NumberFormatException e) {

        }

        if (isEnd == 1) {//带冒号
            span += (sender == null ? receiver.getNick() : sender.getNick()) + "：</span>";
        } else {
            span += (sender == null ? receiver.getNick() : sender.getNick()) + "</span>";
        }

        return span;
    }


    /**
     * 构造接收礼物主播昵称块
     */
    public static String formatGiftReceiverNickSpan(MsgTUser receiver) {
        String span = "<span class=\"name mr-5\" onclick=\"popUserWin(" + receiver.getId() + ")\">";
        try {
            int richLv = Integer.parseInt(receiver.getRichlvl());
            span += "<img class=\"user-icon user-rich-icon\" src=\"file:///android_asset/lvIcons/rich_level_icon_" + richLv + ".png\">";
        } catch (NumberFormatException e) {

        }

        try {
            int starLv = Integer.parseInt(receiver.getAnchorlvl());
            span += "<img class=\"user-icon user-star-icon\" src=\"file:///android_asset/lvIcons/star_level_icon_" + starLv + ".png\">";
        } catch (NumberFormatException e) {

        }

        try {
            int guardLv = Integer.parseInt(receiver.getGuardlvl());
            if (guardLv > 0) {
                if (guardLv == 1) {
                    span += "<img class=\"user-icon user-guard-icon\" src=\"file:///android_asset/lvIcons/room_guard_lvl_1.png\">";
                } else {
                    span += "<img class=\"user-icon user-guard-icon\" src=\"file:///android_asset/lvIcons/room_guard_lvl_2.png\">";
                }
            }
        } catch (NumberFormatException e) {

        }

        try {
            int vipLv = Integer.parseInt(receiver.getViplvl());
            if (vipLv > 0) {
                span += "<img class=\"user-icon user-vip-icon\" src=\"file:///android_asset/lvIcons/vip_level_icon_" + vipLv + ".png\">";
            }
        } catch (NumberFormatException e) {

        }

        span += receiver.getNick() + "</span>";
        return span;
    }

}
