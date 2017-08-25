package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.MessageOwner;
import com.xingbo.live.entity.MessagePrivateDetail;
import com.xingbo.live.entity.MessageUser;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbobase.view.FrescoImageView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/1
 */
public class UserMsgPrivateDetailAdapter extends XingBoBaseAdapter<MessagePrivateDetail> {
    private final static String TAG = "UserMsgPrivateDetailAdapter";

    private final static int TYPE_COUNT = 2;
    private final static int TYPE_LEFT = 1;
    private final static int TYPE_RIGHT = 0;
    private int currentType;

    private Context context;
    private MessageOwner owners;
    private MessageUser users;


    public UserMsgPrivateDetailAdapter(Context context, List<MessagePrivateDetail> data, MessageOwner owners, MessageUser users) {
        super(context, data);
        this.context = context;
        this.owners = owners;
        this.users = users;

    }

    public void setData(List<MessagePrivateDetail> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        String lastsender = data.get(position).getLastsender();
        if (lastsender.equals(users.getUid())) {
            return TYPE_LEFT;
        } else if (lastsender.equals(owners.getUid())) {
            return TYPE_RIGHT;
        } else {
            return TYPE_RIGHT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessagePrivateDetail items = data.get(position);
        currentType = getItemViewType(position);
        ViewHolder viewHolder=null;
        switch (currentType) {
            case TYPE_LEFT:
                if (convertView == null || !(convertView.getTag() instanceof LeftViewHolder)) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.user_msg_chat_left, null);
                    viewHolder = new LeftViewHolder();
                    viewHolder.flTime = (FrameLayout) convertView.findViewById(R.id.user_msg_chat_ctime_fl_left);
                    viewHolder.avator = (FrescoImageView) convertView.findViewById(R.id.user_msg_chatavatar_left);
                    viewHolder.content = (TextView) convertView.findViewById(R.id.user_msg_chatsender_left);
                    viewHolder.ctime = (TextView) convertView.findViewById(R.id.user_msg_chat_ctime_left);
                } else {
                    viewHolder = (LeftViewHolder) convertView.getTag();
                }
                viewHolder.avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + users.getAvatar()));
                break;
            case TYPE_RIGHT:
                if (convertView == null || !(convertView.getTag() instanceof RightViewHolder)) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.user_msg_chat_right, null);
                    viewHolder = new RightViewHolder();
                    viewHolder.flTime = (FrameLayout) convertView.findViewById(R.id.user_msg_chat_ctime_fl_right);
                    viewHolder.content = (TextView) convertView.findViewById(R.id.user_msg_chatuser_right);
                    viewHolder.avator = (FrescoImageView) convertView.findViewById(R.id.user_msg_chatavatar_right);
                    viewHolder.ctime = (TextView) convertView.findViewById(R.id.user_msg_chat_ctime_right);
                } else {
                    viewHolder = (RightViewHolder) convertView.getTag();
                }
                viewHolder.avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + owners.getAvatar()));
                break;
        }

        if (items.getLastsender().equals(users.getUid())) {
            viewHolder.avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + users.getAvatar()));
        } else if (items.getLastsender().equals(owners.getUid())) {
            viewHolder.avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + owners.getAvatar()));
        }
        if (!TextUtils.isEmpty(items.getLastmsg())) {
            viewHolder.content.setText(items.getLastmsg());
        }
        if (position != 0 && !getDates(items.getCtime(), data.get(position - 1).getCtime())) {
            viewHolder.flTime.setVisibility(View.GONE);
        } else {
            viewHolder.flTime.setVisibility(View.VISIBLE);
            if (items.getCtime() != null) {
                viewHolder.ctime.setText(items.getCtime());
            }
        }
        return convertView;
    }


    public boolean getDates(String ctime, String ctime1) {
        boolean isTrue = false;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            Date d1 = df.parse(ctime);
            Date d2 = df.parse(ctime1);
            Calendar c = Calendar.getInstance();
            c.setTime(d1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(d2);
            long time = c.getTimeInMillis();  // ctime 到现在的毫秒数
            long time1 = c1.getTimeInMillis();
            if ((time - time1) > 60000) {
                isTrue = true;
            } else {
                isTrue = false;
            }
        } catch (Exception e) {

        }
        return isTrue;
    }

    public class LeftViewHolder extends ViewHolder {
    }

    public class RightViewHolder extends ViewHolder {
    }

    public class ViewHolder {
        public FrescoImageView avator;//头像图片
        public TextView content;//消息内容
        public TextView ctime;//发送时间
        public FrameLayout flTime;//时间容器
    }
}
