package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.xingbo.live.R;

/**
 * Created by Terry on 2016/7/20.
 */
public class UserPayLogViewHolder {
    public TextView pay_log_order_num;
    public TextView pay_log_money;
    public TextView pay_log_ctime;
    public TextView pay_log_style;
    public View divider;

    public UserPayLogViewHolder(View itemView) {
        pay_log_order_num = (TextView) itemView.findViewById(R.id.pay_log_order_num);
        pay_log_money = (TextView) itemView.findViewById(R.id.pay_log_money);
        pay_log_ctime = (TextView) itemView.findViewById(R.id.pay_log_ctime);
        pay_log_style= (TextView) itemView.findViewById(R.id.pay_log_style);
        divider = itemView.findViewById(R.id.divider);
    }
}
