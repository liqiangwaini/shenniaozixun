package com.xingbo.live.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼物分类结构
 * Created by WuJinZhou on 2015/12/9.
 */
public class GiftType {

    private static final int GIFT_NUMBER=8;
    private String id;
    private String name;
    private String display;
    private ArrayList<Gift> gifts;

    /**
     * 分页数据，每页GIFT_NUMBER个
     */
    public  ArrayList<ArrayList<Gift>> pagers(){

        int pageNum=gifts.size()/GIFT_NUMBER;
        int remainder=gifts.size()%GIFT_NUMBER;
        if(remainder!=0){
            pageNum=pageNum+1;
        }
        ArrayList<ArrayList<Gift>>pagers=new ArrayList<ArrayList<Gift>>();
        for(int i=0;i<pageNum;i++){
            pagers.add(i,new ArrayList<Gift>());
            pagers.get(i).addAll(gifts.subList(i*GIFT_NUMBER,Math.min(i*GIFT_NUMBER+GIFT_NUMBER,gifts.size())));
        }
        if(pagers.size()==0){
           pagers.add(new ArrayList<Gift>());
        }
        return pagers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }
}
