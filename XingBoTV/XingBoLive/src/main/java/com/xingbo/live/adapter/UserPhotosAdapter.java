package com.xingbo.live.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserPhotosViewHolder;
import com.xingbo.live.entity.UserPhotos;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/1
 */
public class UserPhotosAdapter extends XingBoBaseAdapter<UserPhotos> {
    private Context mContext;

    private List<String> photoUrl;
    private Map<String,List<String>> itemsMap;


    public UserPhotosAdapter(Context context, List<UserPhotos> data) {
        super(context, data);
        photoUrl = new ArrayList<>();
        itemsMap= new HashMap<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserPhotos item= data.get(position);
        UserPhotosViewHolder holder= null;
      if(convertView==null)
      {
          convertView= LayoutInflater.from(mContext).inflate(R.layout.user_photos_view_item,null);
          holder=new UserPhotosViewHolder(convertView);
          convertView.setTag(holder);

      }else{
          holder= (UserPhotosViewHolder) convertView.getTag();
      }

        itemsMap.put(item.getUptime(),photoUrl);


        if (!TextUtils.isEmpty(item.getUptime())){
            holder.upLoadTime.setText(item.getUptime());
        }

        


        photoUrl= new ArrayList<>();


        return convertView;
    }
}
