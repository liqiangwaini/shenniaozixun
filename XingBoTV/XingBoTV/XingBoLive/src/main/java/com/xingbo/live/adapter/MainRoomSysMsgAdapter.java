package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserMsgSystemViewHolder;
import com.xingbo.live.entity.MessageSystem;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbobase.view.FrescoImageView;

import java.util.List;

import master.flame.danmaku.ui.widget.DanmakuTextureView;


public class MainRoomSysMsgAdapter extends XingBoBaseAdapter<MessageSystem> {
    private Context mContext;
    private int currentPosition=-1;

    public MainRoomSysMsgAdapter(Context context, List<MessageSystem> data) {
        super(context, data);
        mContext = context;
    }

    public void setData(List<MessageSystem> data) {
        this.data = data;
    }

    public void setCurrentPosition(int position) {
        this.currentPosition=position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_main_room_sys_msg, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.avatar = (FrescoImageView) convertView.findViewById(R.id.item_main_room_sys_msg_avator);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.item_main_room_sys_msg_msg);
            viewHolder.time = (TextView) convertView.findViewById(R.id.item_main_room_sys_msg_time);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.avatar.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + data.get(position).getHref()));
        viewHolder.msg.setText(data.get(position).getMsg());
        if(currentPosition==position+1&&!data.get(position).isShow()){
            viewHolder.msg.setSingleLine(false);
            data.get(position).setIsShow(true);
        }else{
            viewHolder.msg.setSingleLine(true);
            data.get(position).setIsShow(false);
        }
        viewHolder.time.setText(data.get(position).getCtime());
        return convertView;
    }

    class ViewHolder {
        private FrescoImageView avatar;
        private TextView msg;
        private TextView time;
    }
}
