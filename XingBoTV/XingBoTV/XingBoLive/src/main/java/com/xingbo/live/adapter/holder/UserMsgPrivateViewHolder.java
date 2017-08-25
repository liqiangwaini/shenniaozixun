package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.User;
import com.xingbobase.view.FrescoImageView;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/7/29
 */
public class UserMsgPrivateViewHolder {
   private String sessionid;
   public FrescoImageView  avator;//头像图片
   public TextView nick;
   public TextView content;//消息内容
   public TextView time;//时间
   public ImageView sex;//性别
   public ImageView richlvel;//等级
    public TextView  unreadCount;//未读条数显示

    public UserMsgPrivateViewHolder (View itemView){
        avator= (FrescoImageView) itemView.findViewById(R.id.item_secret_notify_avatar);
        nick= (TextView)itemView.findViewById(R.id.item_secret_notify_nick);
        content = (TextView) itemView.findViewById(R.id.item_secret_notify_content);
        time= (TextView)itemView.findViewById(R.id.item_secret_notify_time);
        sex= (ImageView) itemView.findViewById(R.id.item_secret_notify_sex);
        richlvel= (ImageView) itemView.findViewById(R.id.item_secret_notify_rich_icon);
        unreadCount= (TextView) itemView.findViewById(R.id.user_msg_unread);
    }
    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }



}
