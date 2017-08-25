package com.xingbobase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.xingbobase.entity.XingBoBaseItem;
import com.xingbobase.extra.sticky.StickyGridHeadersSimpleAdapter;

import java.util.List;

/**
 * Created by WuJinZhou on 2015/8/12.
 */
public abstract class XingBoBaseStickyHeaderGridAdapter<T extends XingBoBaseItem> extends XingBoBaseAdapter implements StickyGridHeadersSimpleAdapter {

    protected XingBoBaseStickyHeaderGridAdapter(Context context, List<T> data) {
        super(context, data);
    }

    @Override
    public long getHeaderId(int position) {
        int type=((XingBoBaseItem)data.get(position)).getItemType();
        return type;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
