package com.xingbo.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserPayLogViewHolder;
import com.xingbo.live.entity.UserPayLogItem;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.util.List;

/**
 * Created by Terry on 2016/7/21.
 */
public class UserPayLogListAdapter extends XingBoBaseAdapter<UserPayLogItem> {
    private static final String TAG = "UserPayLogListAdapter";

    public UserPayLogListAdapter(Context context, List<UserPayLogItem> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserPayLogItem item = data.get(position);
        UserPayLogViewHolder holder = null;
        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_user_pay_log,null);
            holder = new UserPayLogViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(UserPayLogViewHolder)convertView.getTag();
        }
        holder.pay_log_order_num.setText(item.getOrderno());
        holder.pay_log_money.setText("充值 " + item.getMoney() + " 元");
        holder.pay_log_ctime.setText(item.getCtime());
        holder.pay_log_style.setText(item.getMethod());

        if(position > 0){
            if(holder.divider.getVisibility() == View.VISIBLE){
                holder.divider.setVisibility(View.GONE);
            }
        }else{
            if(holder.divider.getVisibility() == View.GONE){
                holder.divider.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }
}
