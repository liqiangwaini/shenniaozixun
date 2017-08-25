package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.msg.CommonMsg;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.view.FrescoImageView;

import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/18.
 */
public class SecretMsgNotifyAdapter extends BaseAdapter {
    private Context context;
    private List<CommonMsg> msgList;

    public SecretMsgNotifyAdapter(Context context, List<CommonMsg> msgList) {
        this.context = context;
        this.msgList = msgList;
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int i) {
        return msgList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(context, R.layout.item_secret_notify, null);
        }
        FrescoImageView avatar = (FrescoImageView) view.findViewById(R.id.item_secret_notify_avatar);
        //ImageView redPoint = (ImageView) view.findViewById(R.iditem_secret_notify_redpoint);
        TextView nick = (TextView) view.findViewById(R.id.item_secret_notify_nick);
        TextView content = (TextView) view.findViewById(R.id.item_secret_notify_content);
        TextView time = (TextView) view.findViewById(R.id.item_secret_notify_time);
        CommonMsg commonMsg = msgList.get(i);
        avatar.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + commonMsg.getData().getFuser().getAvatar()));
        nick.setText(commonMsg.getData().getFuser().getNick());
        content.setText(commonMsg.getData().getMsg());
        time.setText(commonMsg.getTime());

        return view;
    }
}
