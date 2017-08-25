package com.xingbo.live.entity;

/**
 * Created by WuJinZhou on 2016/1/26.
 */
public class RoomExtraModelItem {
    private int functionId;
    private String modelName;

    public RoomExtraModelItem(int functionId, String modelName) {
        this.functionId = functionId;
        this.modelName = modelName;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
