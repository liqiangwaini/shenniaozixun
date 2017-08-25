package com.xingbo.live.entity;

import java.util.List;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/1
 */
public class MessagePrivateDetailPage {
    private List<MessagePrivateDetail> items;
    private int page;
    private int pagesize;
    private int pagetotal;
    private int total;
    private  int prev;
    private int next;
    private MessageUser  user;
    private MessageOwner owner;

    public List<MessagePrivateDetail> getItems() {
        return items;
    }

    public void setItems(List<MessagePrivateDetail> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPagetotal() {
        return pagetotal;
    }

    public void setPagetotal(int pagetotal) {
        this.pagetotal = pagetotal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public MessageUser getUser() {
        return user;
    }

    public void setUser(MessageUser user) {
        this.user = user;
    }

    public MessageOwner getOwner() {
        return owner;
    }

    public void setOwner(MessageOwner owner) {
        this.owner = owner;
    }
}