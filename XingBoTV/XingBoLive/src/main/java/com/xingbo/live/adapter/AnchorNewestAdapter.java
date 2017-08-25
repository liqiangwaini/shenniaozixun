package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.AnchorViewHolder;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.util.List;

/**
 * 直播-最新（手机开播），adapter
 */
public class AnchorNewestAdapter extends XingBoBaseAdapter<HomeAnchor> {

    public AnchorNewestAdapter(Context context, List<HomeAnchor> data) {
        super(context, data);
    }

    public void setData(List<HomeAnchor> data) {
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeAnchor anchor = data.get(position);
        AnchorViewHolder holder = null;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.home_anchor_case_item, null);
            holder = new AnchorViewHolder(convertView);
            convertView.setTag(holder);
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
        holder.pic.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchor.getPosterlogo()));
        holder.nick.setText(anchor.getNick());
        holder.online.setText(anchor.getLiveonlines());
        holder.addr.setText(anchor.getAddr());
        holder.usertitle.setText(anchor.getLivemood());
        return convertView;
    }
}
