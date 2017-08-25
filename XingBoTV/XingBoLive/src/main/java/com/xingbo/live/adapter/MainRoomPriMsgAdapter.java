package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.xingbo.live.R;
import com.xingbo.live.entity.MessagePrivate;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbobase.view.FrescoImageView;

import java.util.List;


public class MainRoomPriMsgAdapter extends XingBoBaseAdapter<MessagePrivate> {

    private Context mContext;

    public MainRoomPriMsgAdapter(Context context, List<MessagePrivate> data) {
        super(context, data);
        mContext=context;
    }

    public void setData(List<MessagePrivate> data){
        this.data=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(mContext,R.layout.item_main_room_pri_msg,null);
            viewHolder=new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.avatar= (FrescoImageView) convertView.findViewById(R.id.item_main_room_pri_msg_avatar);
            viewHolder.redPoint= (ImageView) convertView.findViewById(R.id.item_main_room_pri_msg_redpoint);
            viewHolder.nick= (TextView) convertView.findViewById(R.id.item_main_room_pri_msg_nick);
            viewHolder.msg= (TextView) convertView.findViewById(R.id.item_main_room_pri_msg_msg);
            viewHolder.time= (TextView) convertView.findViewById(R.id.item_main_room_pri_msg_time);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.avatar.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + data.get(position).getAvatar()));
        if(data.get(position).getReadflag()==0){
            viewHolder.redPoint.setVisibility(View.VISIBLE);
        }else  if(data.get(position).getReadflag()==1){
            viewHolder.redPoint.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.redPoint.setVisibility(View.INVISIBLE);
        }
        viewHolder.nick.setText(data.get(position).getNick());
        viewHolder.msg.setText(data.get(position).getLastmsg());
        viewHolder.time.setText(data.get(position).getCtime());
        return convertView;
    }

    class ViewHolder{
        private FrescoImageView avatar;
        private ImageView redPoint;
        private TextView nick;
        private TextView msg;
        private TextView time;
    }
}
