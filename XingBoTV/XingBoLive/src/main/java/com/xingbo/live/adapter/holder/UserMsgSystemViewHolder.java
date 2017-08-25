package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbobase.view.FrescoImageView;

/**
 * Project：XingBoTV2.0
 * Author: Mengru Ren
 * Date:  2016/7/31
 */
public class UserMsgSystemViewHolder {

    public FrescoImageView avatar;
    public TextView msg;
    public TextView time;

    public UserMsgSystemViewHolder(View itemView){
        avatar= (FrescoImageView) itemView.findViewById(R.id.item_sys_msg_avator);
        msg= (TextView) itemView.findViewById(R.id.item_sys_msg_msg);
        time= (TextView) itemView.findViewById(R.id.item_sys_msg_time);
    }

}
