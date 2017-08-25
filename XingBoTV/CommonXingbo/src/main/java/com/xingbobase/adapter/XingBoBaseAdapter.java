package com.xingbobase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.xingbobase.entity.XingBoBaseItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/8.
 */
public abstract class XingBoBaseAdapter<T extends XingBoBaseItem> extends BaseAdapter {

    protected Context context;
    protected List<T> data=new ArrayList<T>();
    protected LayoutInflater mInflater;

    public XingBoBaseAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
