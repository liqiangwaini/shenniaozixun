package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.SearchResultViewHolder;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbo.live.http.HttpConfig;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class HomeSearchResultListAdapter extends XingBoBaseAdapter<HomeAnchor> {
    private LayoutInflater mInflater;
    public HomeSearchResultListAdapter(Context context, List<HomeAnchor> data) {
        super(context, data);
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeAnchor anchor=data.get(position);
        SearchResultViewHolder holder=null;
        if(convertView==null){
           convertView=mInflater.inflate(R.layout.home_search_result_item,null);
           holder=new SearchResultViewHolder(convertView);
           convertView.setTag(holder);
        }else{
           holder=(SearchResultViewHolder)convertView.getTag();
        }
        holder.header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+anchor.getLivelogo()));
        holder.nick.setText(anchor.getNick());
        if(anchor.getLivestatus()!=null){
           if(anchor.getLivestatus().equals("1")){
              if(holder.offStateIcon.getVisibility()==View.VISIBLE){
                  holder.offStateIcon.setVisibility(View.INVISIBLE);
              }
              if(holder.onStateIcon.getVisibility()==View.INVISIBLE){
                  holder.onStateIcon.setVisibility(View.VISIBLE);
              }
           }else{
               if(holder.offStateIcon.getVisibility()==View.INVISIBLE){
                   holder.offStateIcon.setVisibility(View.VISIBLE);
               }
               if(holder.onStateIcon.getVisibility()==View.VISIBLE){
                   holder.onStateIcon.setVisibility(View.INVISIBLE);
               }
           }
        }
        holder.setAnchor(anchor);
        return convertView;
    }
}
