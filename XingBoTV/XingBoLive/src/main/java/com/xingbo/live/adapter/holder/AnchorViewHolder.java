package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbobase.view.FrescoImageView;
import com.xingbobase.view.IFrescoImageView;

/**
 * Created by WuJinZhou on 2016/1/15.
 */
public class AnchorViewHolder {
    public FrescoImageView heart;//头像
    public TextView nick;//昵称
    public TextView addr;//地址
    public TextView online;//观看人数
    private HomeAnchor anchor;
    public FrescoImageView pic;//主播封面
    public TextView  usertitle;//直播主题
    public ImageView state;//直播状态
    public RelativeLayout nickContainer;
    public IFrescoImageView avatarPic;

    public AnchorViewHolder(View itemView) {
        heart=(FrescoImageView)itemView.findViewById(R.id.header);
        nick=(TextView)itemView.findViewById(R.id.nick);
        addr= (TextView) itemView.findViewById(R.id.user_add);
        online=(TextView)itemView.findViewById(R.id.online_count_txt);
        pic=(FrescoImageView)itemView.findViewById(R.id.anchor_pic);
        usertitle  = (TextView) itemView.findViewById(R.id.user_title);
        nickContainer=(RelativeLayout)itemView.findViewById(R.id.nick_container);
        online=(TextView)itemView.findViewById(R.id.online_count_txt);
        avatarPic= (IFrescoImageView) itemView.findViewById(R.id.user_header);
        state=(ImageView)itemView.findViewById(R.id.live_state_icon);
    }

    public HomeAnchor getAnchor() {
        return anchor;
    }

    public void setAnchor(HomeAnchor anchor) {
        this.anchor = anchor;
    }
}
