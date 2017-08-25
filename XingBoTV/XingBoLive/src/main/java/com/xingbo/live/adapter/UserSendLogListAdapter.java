package com.xingbo.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserSendLogViewHolder;
import com.xingbo.live.entity.UserSendLogItem;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.util.List;

/**
 * Created by Terry on 2016/7/21.
 */
public class UserSendLogListAdapter extends XingBoBaseAdapter<UserSendLogItem> {
    private static final String TAG = "UserSendLogListAdapter";

    public UserSendLogListAdapter(Context context, List<UserSendLogItem> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserSendLogItem item = data.get(position);
        UserSendLogViewHolder holder = null;
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_user_send_log,null);
            holder = new UserSendLogViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(UserSendLogViewHolder)convertView.getTag();
        }
        holder.send_log_title.setText(item.getTitle());
        holder.send_log_consume.setText(item.getConsume());
        holder.send_log_ctime.setText(item.getCtime());
        return convertView;
    }
}
