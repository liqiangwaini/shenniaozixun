package com.xingbo.live.entity;

import java.util.List;

/**
 * Created by Terry on 2016/7/21.
 */
public class UserPayLogListPage {
    List<UserPayLogItem> items;
    private int page;
    private int pagesize;
    private int pagetotal;
    private int total;
    private int prev;
    private int next;

    public List<UserPayLogItem> getItems() {
        return items;
    }

    public void setItems(List<UserPayLogItem> items) {
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
}
