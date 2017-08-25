package com.xingbo.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserIncomeViewHolder;
import com.xingbo.live.entity.UserIncomeListItem;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.util.List;

/**
 * Created by Terry on 2016/7/19.
 */
public class UserIncomeListAdapter extends XingBoBaseAdapter<UserIncomeListItem> {

    private static final String TAG = "UserIncomeListAdapter";
    private static final int TYPE_COUNT = 2;//item类型的总数
    private static final int TYPE_MONTH_TAG = 0;//月起始类型
    private static final int TYPE_DAY_TAG = 1;//月内数据类型
    private String lastMonthTag = "";
    private int currentType;//当前item类型

    public UserIncomeListAdapter(Context context, List<UserIncomeListItem> data) {
        super(context, data);
    }

    @Override
    public int getItemViewType(int position) {
        String itemTimeTag = data.get(position).getTimetag();
        if (!lastMonthTag.equals(itemTimeTag) || position == 0) {
            return TYPE_MONTH_TAG;// 消费类型
        } else {
            return TYPE_DAY_TAG;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserIncomeListItem item = data.get(position);
            currentType = getItemViewType(position);
            UserIncomeViewHolder monthBeginHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_user_income,null);
                monthBeginHolder = new UserIncomeViewHolder(convertView);
                convertView.setTag(monthBeginHolder);
            } else {
                monthBeginHolder = (UserIncomeViewHolder) convertView.getTag();
            }
            monthBeginHolder.income_month_tag.setText(item.getTimetag());
            monthBeginHolder.income_date.setText(item.getDd().replaceAll("-", "\\."));
            monthBeginHolder.income_value.setText("收入 " + item.getTotal() + "星钻");
            if (currentType == TYPE_MONTH_TAG) {
                if(monthBeginHolder.income_month_tag.getVisibility() == View.GONE){
                    monthBeginHolder.income_month_tag.setVisibility(View.VISIBLE);
                }
            } else {
                if(monthBeginHolder.income_month_tag.getVisibility() == View.VISIBLE){
                    monthBeginHolder.income_month_tag.setVisibility(View.GONE);
                }
            }
        lastMonthTag = item.getTimetag();

        return convertView;
    }
}
