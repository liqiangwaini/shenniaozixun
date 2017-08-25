package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.adapter.holder.AnchorViewHolder;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.adapter.XingBoBaseStickyHeaderGridAdapter;

import java.util.List;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/9
 */
public class CommonAnchorListHotAdapter extends XingBoBaseStickyHeaderGridAdapter<HomeAnchor> {

    public final static String TAG="CommonAnchorListHotAdapter";
    private int mEmptyHeaderResId;
    private ViewGroup.LayoutParams headerViewParams;//轮播图区域高度
    private AbsListView.LayoutParams aParam;
    private FrameLayout.LayoutParams lastParam;
    private Context mContext;
    public CommonAnchorListHotAdapter(Context context, List<HomeAnchor> data) {
        super(context, data);
        mContext=context;
        mEmptyHeaderResId= R.layout.empty_view;
        int cw=(int) SystemApp.screenWidth- CommonUtil.dip2px(context, 30);
        aParam=new AbsListView.LayoutParams(cw/2,cw/2);
    }
    public void setData(List<HomeAnchor> data) {
        this.data = data;
    }

    public CommonAnchorListHotAdapter(Context context, List<HomeAnchor> data, int mEmptyHeaderHeight) {
        super(context, data);
        mContext=context;
        mEmptyHeaderResId= R.layout.empty_view;
//        int cw=(int) SystemApp.screenWidth- CommonUtil.dip2px(context, 30);
//        aParam=new AbsListView.LayoutParams(cw/2,cw/2);
        headerViewParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mEmptyHeaderHeight);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getId() != R.id.sticky_header_empty_view) {
            convertView = mInflater.inflate(mEmptyHeaderResId, parent, false);
            convertView.setLayoutParams(headerViewParams);
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeAnchor anchor = (HomeAnchor) data.get(position);
        AnchorViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_anchor_case_item, null);
            holder = new AnchorViewHolder(convertView);
            convertView.setTag(holder);
         //   convertView.setLayoutParams(aParam);
        } else {
            holder = (AnchorViewHolder) convertView.getTag();
        }
        holder.setAnchor(anchor);
        if (anchor.getLivestatus() != null) {
            if (anchor.getLivestatus().equals("1")) {
                if (holder.state.getVisibility() == View.INVISIBLE) {
                    holder.state.setVisibility(View.VISIBLE);
                }
            } else {
                if (holder.state.getVisibility() == View.VISIBLE) {
                    holder.state.setVisibility(View.INVISIBLE);
                }
            }
        }
        holder.heart.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchor.getAvatar()));
        if(TextUtils.isEmpty(anchor.getPosterlogo())){
            holder.pic.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchor.getAvatar()));
        }else{
            holder.pic.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchor.getPosterlogo()));
        }
        holder.nick.setText(anchor.getNick());
        holder.online.setText(anchor.getLiveonlines());
        holder.addr.setText(anchor.getAddr());
        holder.usertitle.setText(anchor.getLivemood());
        return convertView;
    }
}
