package master.flame.danmaku.danmaku.model;

import android.graphics.drawable.Drawable;

/**
 * 滚动通知消息
 * Created by WuJinZhou on 2016/1/28.
 */
public class ScrollNoticeWord {
    public final static int SCROLL_NOTICE_BROAD_CAST=0x1;//喇叭广播
    public final static int SCROLL_NOTICE_SEND_GIFT=0x2;//发送礼物
    public final static int SCROLL_NOTICE_AGREE_SONG=0x3;//同意点歌

    private int id;
    private String msg;
    private int type=SCROLL_NOTICE_BROAD_CAST;
    private boolean isEmotion=false;//内容含表情
    private String source;
    private String destination;
    //extra for song
    private String songName;//歌名
    //extra gift
    private String giftIcon;//source uri
    private int num=1;
    /**
     * 点歌通知
     */
    public ScrollNoticeWord(int id,int type,String source, String destination,String name) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.destination=destination;
        this.songName=name;
    }

    /**
     * 礼物通知
     */
    public ScrollNoticeWord(int id,int type,String source, String destination,String giftIconUri,int num) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.destination=destination;
        this.giftIcon=giftIconUri;
        this.num=num;
    }

    /**
     * 广播通知
     */
    public ScrollNoticeWord(int id,int type,String source,String msg,boolean isEmotion) {
        this.id = id;
        this.msg =msg;
        this.type = type;
        this.source = source;
        this.isEmotion=isEmotion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEmotion() {
        return isEmotion;
    }

    public void setEmotion(boolean isEmotion) {
        this.isEmotion = isEmotion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getGiftIcon() {
        return giftIcon;
    }

    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
