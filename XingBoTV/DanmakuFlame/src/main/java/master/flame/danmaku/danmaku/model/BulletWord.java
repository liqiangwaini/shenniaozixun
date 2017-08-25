package master.flame.danmaku.danmaku.model;

/**
 * 弹幕内容
 * Created by WuJinZhou on 2016/1/22.
 */
public class BulletWord {
    private int id;
    private int tag;
    private CharSequence msg;
    public BulletWord(int id, int tag, CharSequence msg) {
        this.id = id;
        this.tag = tag;
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public CharSequence getMsg() {
        return msg;
    }

    public void setMsg(CharSequence msg) {
        this.msg = msg;
    }
}
