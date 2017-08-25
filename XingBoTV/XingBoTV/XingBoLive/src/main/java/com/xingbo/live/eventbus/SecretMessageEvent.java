package com.xingbo.live.eventbus;

import com.xingbo.live.entity.msg.CommonMsg;

/**
 * Created by WuJinZhou on 2015/12/6.
 */
public class SecretMessageEvent {

    private CommonMsg secretMsg;

    public SecretMessageEvent(CommonMsg secretMsg) {
        this.secretMsg = secretMsg;
    }

    public CommonMsg getSecretMsg() {
        return secretMsg;
    }

    public void setSecretMsg(CommonMsg secretMsg) {
        this.secretMsg = secretMsg;
    }
}
