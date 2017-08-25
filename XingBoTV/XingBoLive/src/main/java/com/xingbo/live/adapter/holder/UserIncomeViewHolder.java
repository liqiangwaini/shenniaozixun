package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.xingbo.live.R;

/**
 * Created by Terry on 2016/7/20.
 */
public class UserIncomeViewHolder {
    public TextView income_month_tag;
    public TextView income_date;
    public TextView income_value;

    public UserIncomeViewHolder() {
    }

    public UserIncomeViewHolder(View itemView) {
        income_month_tag = (TextView) itemView.findViewById(R.id.income_month_tag);
        income_date = (TextView) itemView.findViewById(R.id.income_date);
        income_value = (TextView) itemView.findViewById(R.id.income_value);
    }
}
