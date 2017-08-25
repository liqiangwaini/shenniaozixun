package com.xingbo.live.entity;

import com.xingbo.live.entity.msg.AnchorLvUpMsg;
import com.xingbo.live.entity.msg.CommonMsg;
import com.xingbo.live.entity.msg.GiftMsg;
import com.xingbo.live.entity.msg.GuardMsg;
import com.xingbo.live.entity.msg.JoinMsg;
import com.xingbo.live.entity.msg.LVMsg;
import com.xingbo.live.entity.msg.LuckyGiftRetAwardMsg;
import com.xingbo.live.entity.msg.SystemMsg;
import com.xingbo.live.entity.msg.SystemTypeMsg;
import com.xingbo.live.entity.msg.UserRichUpMsg;

/**
 * 房间消息
 */
public class RoomMessage {
    public static final int COMMON_MSG = 0x001;  //公聊
    public static final int JOIN_ROOM = 0x002;
    public static final int LEAVE_ROOM = 0x003;
    public static final int SEND_GIFT = 0x004;
    public static final int START_LIVE = 0x005;
    public static final int STOP_LIVE = 0x006;
    public static final int ADD_MUTE = 0x007;
    public static final int CANCEL_MUTE = 0x008;
    public static final int KICK_OUT = 0x009;
    public static final int ADD_ADMIN = 0x010;
    public static final int CENCEL_ADMIN = 0x011;
    public static final int ANCHOR_LVL_CHANGE = 0x012;
    public static final int ADD_DANMU = 0x013;
    public static final int SYSTEM_MESSAGE = 0x014;
    public static final int OPEN_GUARD = 0x015;
    public static final int ANCHOR_LVL_UP = 0x016;
    public static final int USER_LVL_UP = 0x017;
    public static final int GRREN_MESSAGE = 0x018;
    public static final int LUCKY_RETURN_AWARD = 0x019;

    private int type;  //0:系统消息  1:公聊
    private CommonMsg commonMsg;
    private GiftMsg giftMsg;
    private String content;
    private JoinMsg joinMsg;  //进入房间
    private SystemTypeMsg muteMsg;
    private GuardMsg guardMsg;
    private AnchorLvUpMsg anchorLvUpMsg;
    private UserRichUpMsg userRichUpMsg;
    private LVMsg lvMsg;
    private LuckyGiftRetAwardMsg luckyGiftRetAwardMsg;
    private SystemMsg systemMsg;

    RoomMessage() {
    }

    public RoomMessage(int type, LuckyGiftRetAwardMsg luckyGiftRetAwardMsg) {
        this.type = type;
        this.luckyGiftRetAwardMsg = luckyGiftRetAwardMsg;
    }

    public RoomMessage(int type, CommonMsg commonMsg) {
        this.type = type;
        this.commonMsg = commonMsg;
    }

    public RoomMessage(int type, GiftMsg giftMsg) {
        this.type = type;
        this.giftMsg = giftMsg;
    }

    public RoomMessage(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public RoomMessage(int type, JoinMsg joinMsg) {
        this.type = type;
        this.joinMsg = joinMsg;
    }

    public RoomMessage(int type, SystemTypeMsg muteMsg) {
        this.type = type;
        this.muteMsg = muteMsg;
    }

    public RoomMessage(int type, GuardMsg guardMsg) {
        this.type = type;
        this.guardMsg = guardMsg;
    }

    public RoomMessage(int type, AnchorLvUpMsg anchorLvUpMsg) {
        this.type = type;
        this.anchorLvUpMsg = anchorLvUpMsg;
    }

    public RoomMessage(int type, UserRichUpMsg userRichUpMsg) {
        this.type = type;
        this.userRichUpMsg = userRichUpMsg;
    }

    public RoomMessage(int type, LVMsg lvMsg) {
        this.type = type;
        this.lvMsg = lvMsg;
    }

    public RoomMessage(int type, SystemMsg systemMsg) {
        this.type = type;
        this.systemMsg = systemMsg;
    }

    public SystemTypeMsg getMuteMsg() {
        return muteMsg;
    }

    public void setMuteMsg(SystemTypeMsg muteMsg) {
        this.muteMsg = muteMsg;
    }

    public JoinMsg getJoinMsg() {
        return joinMsg;
    }

    public void setJoinMsg(JoinMsg joinMsg) {
        this.joinMsg = joinMsg;
    }

    public GiftMsg getGiftMsg() {
        return giftMsg;
    }

    public void setGiftMsg(GiftMsg giftMsg) {
        this.giftMsg = giftMsg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommonMsg getCommonMsg() {
        return commonMsg;
    }

    public void setCommonMsg(CommonMsg commonMsg) {
        this.commonMsg = commonMsg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GuardMsg getGuardMsg() {
        return guardMsg;
    }

    public void setGuardMsg(GuardMsg guardMsg) {
        this.guardMsg = guardMsg;
    }

    public AnchorLvUpMsg getAnchorLvUpMsg() {
        return anchorLvUpMsg;
    }

    public void setAnchorLvUpMsg(AnchorLvUpMsg anchorLvUpMsg) {
        this.anchorLvUpMsg = anchorLvUpMsg;
    }

    public UserRichUpMsg getUserRichUpMsg() {
        return userRichUpMsg;
    }

    public void setUserRichUpMsg(UserRichUpMsg userRichUpMsg) {
        this.userRichUpMsg = userRichUpMsg;
    }

    public LVMsg getLvMsg() {
        return lvMsg;
    }

    public void setLvMsg(LVMsg lvMsg) {
        this.lvMsg = lvMsg;
    }

    public SystemMsg getSystemMsg() {
        return systemMsg;
    }

    public void setSystemMsg(SystemMsg systemMsg) {
        this.systemMsg = systemMsg;
    }

    public LuckyGiftRetAwardMsg getLuckyGiftRetAwardMsg() {
        return luckyGiftRetAwardMsg;
    }

    public void setLuckyGiftRetAwardMsg(LuckyGiftRetAwardMsg luckyGiftRetAwardMsg) {
        this.luckyGiftRetAwardMsg = luckyGiftRetAwardMsg;
    }
}
