package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserContributionViewHolder;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.Contribution;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/26
 */
public class UserContributionAdapter extends XingBoBaseAdapter<Contribution> {

     private Context mContext;
    private WeakReference<View.OnClickListener> listenerRef;
    public UserContributionAdapter(Context context, List<Contribution> data,View.OnClickListener listener) {
        super(context, data);
        mContext=context;
        listenerRef = new WeakReference<View.OnClickListener>(listener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contribution contribution = data.get(position);
        UserContributionViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_contribution_rank,null);
            holder= new UserContributionViewHolder(convertView);
            convertView.setTag(holder);

        }else {
            holder= (UserContributionViewHolder) convertView.getTag();
        }
        holder.rank.setText(contribution.getRank());
        if(contribution.getRank()=="1"){
            holder.rank.setTextColor(context.getResources().getColor(R.color.rank1));
        }else if(contribution.getRank()=="2"){
            holder.rank.setTextColor(context.getResources().getColor(R.color.pink));
        }else if(contribution.getRank()=="3"){
            holder.rank.setTextColor(context.getResources().getColor(R.color.rank3));
        }else{
            holder.rank.setTextColor(context.getResources().getColor(R.color.c666666));
        }

        if(TextUtils.isEmpty(contribution.getAvatar())||contribution.getAvatar()==null){
//            viewHolder.avator.setBackgroundResource(R.mipmap.avatar);
        }else{
            holder.avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + contribution.getAvatar()));
            holder.avator.setTag(contribution.getFid());
        }

        if (listenerRef != null && listenerRef.get() != null) {
            holder.avator.setOnClickListener(listenerRef.get());
        }
        holder.nick.setText(contribution.getNick());
        holder.richlvl.setImageResource(XingBoConfig.RICH_LV_ICONS[Integer.parseInt(contribution.getRichlvl())]);
        holder.coin.setText(contribution.getTotal());

        return convertView;
    }
}
