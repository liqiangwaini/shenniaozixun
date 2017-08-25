package com.xingbo.live.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserMsgSystemViewHolder;
import com.xingbo.live.entity.MessageSystem;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.util.List;

/**
 * Project：XingBoTV2.0
 * Author: Mengru Ren
 * Date:  2016/7/31
 */
public class UserMsgSystemAdapter extends XingBoBaseAdapter<MessageSystem> {
    private Context mContext;
    private int currentPosition=-1;
    private boolean flag;
    public UserMsgSystemAdapter(Context context, List<MessageSystem> data) {
        super(context, data);
        mContext=context;
    }
    public void setData(List<MessageSystem> data) {
        this.data = data;
    }

    public void setCurrentPosition(int position,boolean flag) {
        this.currentPosition=position;
        this.flag=flag;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageSystem items= data.get(position);
        UserMsgSystemViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.user_msg_system_list_item,null);
            holder= new UserMsgSystemViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (UserMsgSystemViewHolder)convertView.getTag();
        }
        holder.avatar.setImageURI(Uri.parse("res:///"+R.mipmap.system_msg_icon));
        holder.msg.setText(data.get(position).getMsg());

        if ((currentPosition==position+1)&&flag){
            holder.msg.setSingleLine(false);
            holder.msg.setTextColor(Color.BLACK);
        }else {
            holder.msg.setSingleLine(true);
            holder.msg.setTextColor(Color.GRAY);
        }
        holder.time.setText(data.get(position).getCtime());

        return convertView;
    }
}
