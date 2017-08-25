package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbobase.view.FrescoImageView;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/26
 */
public class UserContributionViewHolder {

    public TextView rank;
    public FrescoImageView avator;
    public TextView nick;
    public ImageView richlvl;
    public TextView coin;

   public UserContributionViewHolder(View convertView) {
        rank = (TextView) convertView.findViewById(R.id.item_contribution_rank);
        avator = (FrescoImageView) convertView.findViewById(R.id.item_contribution_avator);
        nick= (TextView) convertView.findViewById(R.id.item_contribution_nick);
        richlvl = (ImageView) convertView.findViewById(R.id.item_contribution_richlvl);
        coin = (TextView) convertView.findViewById(R.id.item_contribution_coin);
    }

}
