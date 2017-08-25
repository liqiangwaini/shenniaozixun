package com.xingbo.live.entity.msg;

/**
 * Created by xingbo_szd on 2016/7/20.
 */
public class SystemTypeMsg extends BaseMsg {
    private Mute data;

    public Mute getData() {
        return data;
    }

    public void setData(Mute data) {
        this.data = data;
    }

    public class Mute {
        private MsgFUser fuser;
        private MsgTUser tuser;

        public MsgFUser getFuser() {
            return fuser;
        }

        public void setFuser(MsgFUser fuser) {
            this.fuser = fuser;
        }

        public MsgTUser getTuser() {
            return tuser;
        }

        public void setTuser(MsgTUser tuser) {
            this.tuser = tuser;
        }
    }
}
