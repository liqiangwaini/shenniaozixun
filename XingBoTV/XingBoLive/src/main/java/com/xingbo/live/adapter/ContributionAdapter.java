package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.Contribution;
import com.xingbo.live.http.HttpConfig;
import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbobase.view.FrescoImageView;

import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/12.
 */
public class ContributionAdapter extends BaseAdapter {

    private Context context;
    private List<Contribution> data;
    public ContributionAdapter(Context context, List<Contribution> data) {
        this.context=context;
        this.data=data;

    }

    public void setList(List<Contribution> data){
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ContributionViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context, R.layout.item_contribution_rank, null);
            viewHolder = new ContributionViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ContributionViewHolder) view.getTag();
        }
        final Contribution contribution=data.get(i);
        viewHolder.rank.setText(contribution.getRank());
        if(contribution.getRank()=="1"){
            viewHolder.rank.setTextColor(context.getResources().getColor(R.color.rank1));
        }else if(contribution.getRank()=="2"){
            viewHolder.rank.setTextColor(context.getResources().getColor(R.color.pink));
        }else if(contribution.getRank()=="3"){
            viewHolder.rank.setTextColor(context.getResources().getColor(R.color.rank3));
        }else{
            viewHolder.rank.setTextColor(context.getResources().getColor(R.color.c666666));
        }

        if(TextUtils.isEmpty(contribution.getAvatar())||contribution.getAvatar()==null){
//            viewHolder.avator.setBackgroundResource(R.mipmap.avatar);
        }else{
            viewHolder.avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + contribution.getAvatar()));
            viewHolder.avator.setTag(contribution.getFid());
        }
        viewHolder.nick.setText(contribution.getNick());
        viewHolder.richlvl.setImageResource(XingBoConfig.RICH_LV_ICONS[Integer.parseInt(contribution.getRichlvl())]);
        viewHolder.coin.setText(contribution.getTotal());
        return view;
    }

    class ContributionViewHolder {
        private TextView rank;
        private FrescoImageView avator;
        private TextView nick;
        private ImageView richlvl;
        private TextView coin;

        ContributionViewHolder(View convertView) {
            rank = (TextView) convertView.findViewById(R.id.item_contribution_rank);
            avator = (FrescoImageView) convertView.findViewById(R.id.item_contribution_avator);
            nick= (TextView) convertView.findViewById(R.id.item_contribution_nick);
            richlvl = (ImageView) convertView.findViewById(R.id.item_contribution_richlvl);
            coin = (TextView) convertView.findViewById(R.id.item_contribution_coin);
        }
    }
}
