package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.xingbo.live.R;

/**
 * Created by Terry on 2016/7/20.
 */
public class UserSendLogViewHolder {
    public TextView send_log_title;
    public TextView send_log_ctime;
    public TextView send_log_consume;

    public UserSendLogViewHolder(View itemView) {
        send_log_title = (TextView) itemView.findViewById(R.id.send_log_title);
        send_log_ctime = (TextView) itemView.findViewById(R.id.send_log_ctime);
        send_log_consume = (TextView) itemView.findViewById(R.id.send_log_consume);
    }
}
