package com.xingbo.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.sina.weibo.sdk.utils.UIUtils;
import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserMsgPrivateViewHolder;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MessagePrivate;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.util.List;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/7/29
 */
public class UserMsgPrivateAdapter extends XingBoBaseAdapter<MessagePrivate> {

    private Context mContext;

    private  int width;

    public UserMsgPrivateAdapter(Context context, List<MessagePrivate> data) {
        super(context, data);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessagePrivate item = data.get(position);
        UserMsgPrivateViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_secret_notify, null);
            holder = new UserMsgPrivateViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserMsgPrivateViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(item.getAvatar())){
            holder.avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + item.getAvatar()));
        }else{
            holder.avator.setImageURI(Uri.parse("res://"+context.getPackageName()+"/"+R.mipmap.avatar_placeholder));
        }
        if (!TextUtils.isEmpty(item.getLastmsg())) {
            holder.content.setText(item.getLastmsg());
            holder.content.setSingleLine(true);
        }
        if (!TextUtils.isEmpty(item.getCtime())) {
            holder.time.setText(item.getCtime());
        }
        if (!TextUtils.isEmpty(item.getNick())) {

            // 进行计算屏幕宽度，动态显示
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            holder.nick.setMaxWidth(width -getPixels(TypedValue.COMPLEX_UNIT_DIP, 220));
            holder.nick.setText(item.getNick());
        }

        if (item.getSex().equals("女")) {
            holder.sex.setImageResource(R.mipmap.female);

        } else {
            holder.sex.setImageResource(R.mipmap.male);
        }

        int rlv = 0;
        try {
            rlv = Integer.parseInt(item.getRichlevel());
        } catch (NumberFormatException e) {
            rlv = 0;
        }
        if (rlv < 35) {
            holder.richlvel.setImageResource(XingBoConfig.RICH_LV_ICONS[rlv]);
        } else {
            holder.richlvel.setImageResource(XingBoConfig.RICH_LV_ICONS[34]);
        }

        if (item.getReadflag() == 0) {
            holder.unreadCount.setVisibility(View.VISIBLE);
            holder.unreadCount.setText(item.getNoreadcnt());
        } else if (item.getReadflag() == 1) {
            holder.unreadCount.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    //使用的px转换类
    public static int getPixels(int Unit, float size) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(Unit, size, metrics);
    }
    //获取某控件的宽度
    public   int getTextViewWidth(final TextView textView){
        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
               width=textView.getMeasuredWidth();
                return true;
            }
        });
        return width;
    }


}
