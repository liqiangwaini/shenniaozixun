package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbobase.view.FrescoImageView;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class SearchResultViewHolder {
    private HomeAnchor anchor;
    public FrescoImageView header;
    public TextView nick;
    public ImageView onStateIcon;
    public TextView offStateIcon;
    public SearchResultViewHolder(View itemView) {
        header=(FrescoImageView)itemView.findViewById(R.id.anchor_header);
        nick=(TextView)itemView.findViewById(R.id.anchor_nick);
        onStateIcon=(ImageView)itemView.findViewById(R.id.live_state_on_icon);
        offStateIcon=(TextView)itemView.findViewById(R.id.live_state_off_icon);
    }

    public HomeAnchor getAnchor() {
        return anchor;
    }

    public void setAnchor(HomeAnchor anchor) {
        this.anchor = anchor;
    }
}
