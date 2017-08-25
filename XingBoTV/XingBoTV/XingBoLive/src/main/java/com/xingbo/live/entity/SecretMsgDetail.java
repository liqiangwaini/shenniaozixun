package com.xingbo.live.entity;

import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/29.
 */
public class SecretMsgDetail {
    private List<SecretMsgItem> items;
    private String page;
    private String pagesieze;
    private String pagetotal;
    private String total;
    private String prev;
    private String next;
    private SecretMsgUserDetail owner;
    private SecretMsgUserDetail user;

    public List<SecretMsgItem> getItems() {
        return items;
    }

    public void setItems(List<SecretMsgItem> items) {
        this.items = items;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPagesieze() {
        return pagesieze;
    }

    public void setPagesieze(String pagesieze) {
        this.pagesieze = pagesieze;
    }

    public String getPagetotal() {
        return pagetotal;
    }

    public void setPagetotal(String pagetotal) {
        this.pagetotal = pagetotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public SecretMsgUserDetail getOwner() {
        return owner;
    }

    public void setOwner(SecretMsgUserDetail owner) {
        this.owner = owner;
    }

    public SecretMsgUserDetail getUser() {
        return user;
    }

    public void setUser(SecretMsgUserDetail user) {
        this.user = user;
    }
}
