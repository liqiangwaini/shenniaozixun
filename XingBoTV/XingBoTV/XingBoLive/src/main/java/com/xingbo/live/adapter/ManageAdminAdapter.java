package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.view.FrescoImageView;

import java.util.List;

/**
 * Created by xingbo_szd on 2016/8/17.
 */
public class ManageAdminAdapter extends BaseAdapter {
    private Context context;
    private List<MsgFUser> list;

    public ManageAdminAdapter(Context context,List<MsgFUser> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<MsgFUser> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_manage_admin, null);
            viewHolder = new ViewHolder();
            viewHolder.avator = (FrescoImageView) convertView.findViewById(R.id.item_manage_admin_avator);
            viewHolder.nick = (TextView) convertView.findViewById(R.id.item_manage_admin_nick);
            viewHolder.id = (TextView) convertView.findViewById(R.id.item_manage_admin_id);
            viewHolder.sex = (FrescoImageView) convertView.findViewById(R.id.item_manage_admin_sex);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.item_manage_admin_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(list.get(position)!=null){
            viewHolder.avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + list.get(position).getAvatar()));
            viewHolder.nick.setText(list.get(position).getNick());
            viewHolder.id.setText(list.get(position).getId());
        }
//        viewHolder.sex.setImageURI(Uri.parse("res://"+context.getPackageName()+"/"+(list.get(position).get)));
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteAdmin(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private FrescoImageView avator;
        private TextView nick;
        private TextView id;
        private FrescoImageView sex;
        private ImageView delete;
    }

    private DeleteAdminListener listener;

    public void setAdminDeleteListener(DeleteAdminListener listener) {
        this.listener = listener;
    }

    public interface DeleteAdminListener {
        public void deleteAdmin(int position);
    }
}
