package com.xingbo.live.eventbus;

/**
 * Created by xingbo_szd on 2016/7/22.
 */
public class ManagerEvent {
    private String id;
    private boolean isAdd;
    public ManagerEvent(){}
    public ManagerEvent(boolean isAdd,String id){
        this.isAdd=isAdd;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }
}
