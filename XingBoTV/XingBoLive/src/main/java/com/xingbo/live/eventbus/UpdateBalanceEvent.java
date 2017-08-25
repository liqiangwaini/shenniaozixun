package com.xingbo.live.eventbus;

/**
 * 更新余额通知体
 * Created by WuJinZhou on 2016/1/27.
 */
public class UpdateBalanceEvent {
    private String currentCoin;

    public UpdateBalanceEvent(String currentCoin) {
        this.currentCoin = currentCoin;
    }

    public String getCurrentCoin() {
        return currentCoin;
    }

    public void setCurrentCoin(String currentCoin) {
        this.currentCoin = currentCoin;
    }
}
