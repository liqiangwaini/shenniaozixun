package com.xingbo.live.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xingbo.live.ui.UserHomepageAct;
import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.adapter.holder.AnchorViewHolder;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/12.
 */
public class CommonAnchorListAdapter extends XingBoBaseAdapter<HomeAnchor> {
    private int sw;
    private AbsListView.LayoutParams aParam;
    private FrameLayout.LayoutParams lastParam;
    private Context mContext;
    public CommonAnchorListAdapter(Context context, List<HomeAnchor> data) {
        super(context, data);
        mContext=context;
        sw=(int)SystemApp.screenWidth;
        int dp30=CommonUtil.dip2px(context,10);
        int cw=sw- dp30;
        aParam=new AbsListView.LayoutParams(cw/2,cw/2);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HomeAnchor anchor=data.get(position);
        AnchorViewHolder holder=null;

        if(convertView==null){
            convertView=mInflater.inflate(R.layout.home_anchor_grid_item,null);
            holder=new AnchorViewHolder(convertView);
            convertView.setTag(holder);
            convertView.setLayoutParams(aParam);
        }else{
            holder=(AnchorViewHolder)convertView.getTag();
        }
        holder.setAnchor(anchor);
        if(anchor.getLivestatus()!=null){
            if(anchor.getLivestatus().equals("1")){
                if(holder.state.getVisibility()==View.INVISIBLE) {
                    holder.state.setVisibility(View.VISIBLE);
                }
            }else{
                if(holder.state.getVisibility()==View.VISIBLE) {
                    holder.state.setVisibility(View.INVISIBLE);
                }
            }
        }

        if (!TextUtils.isEmpty(anchor.getPosterlogo())) {
            holder.pic.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchor.getPosterlogo()));
        }else{
            holder.pic.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchor.getAvatar()));
        }
        if (!TextUtils.isEmpty(anchor.getAvatar())) {
            holder.avatarPic.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchor.getAvatar()));
        }
        holder.avatarPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = anchor.getId();
                if (id != null) {
                    Intent home = new Intent(mContext, UserHomepageAct.class);
                    home.putExtra(UserHomepageAct.EXTRA_USER_ID, id);
                    //  home.putExtra(UserHomeAct.EXTRA_MODEL_NAME,"个人档案");
                    mContext.startActivity(home);
                } else {
                    Toast.makeText(mContext, "用户不存在！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.nick.setText(anchor.getNick());
     //   holder.addr.setText(anchor.getAddr());
        holder.online.setText(anchor.getLiveonlines() + "在看");
       /* if(lastParam==null) {
            lastParam = (FrameLayout.LayoutParams) holder.nickContainer.getLayoutParams();
        }
        if(data.size()>2) {
            if(data.size()%2!=0) {
                if (position == data.size() - 1) {
                    lastParam.bottomMargin = 15;
                    holder.nickContainer.setLayoutParams(lastParam);
                } else {
                    lastParam = (FrameLayout.LayoutParams) holder.nickContainer.getLayoutParams();
                    if (lastParam.bottomMargin == 15) {
                        lastParam.bottomMargin = 0;
                        holder.nickContainer.setLayoutParams(lastParam);
                    }
                }
            }else{
                if (position == data.size() - 1 || position == data.size() - 2) {
                    lastParam.bottomMargin = 15;
                    holder.nickContainer.setLayoutParams(lastParam);
                } else {
                    lastParam = (FrameLayout.LayoutParams) holder.nickContainer.getLayoutParams();
                    if (lastParam.bottomMargin == 15) {
                        lastParam.bottomMargin = 0;
                        holder.nickContainer.setLayoutParams(lastParam);
                    }
                }
            }
        } else {
            lastParam = (FrameLayout.LayoutParams) holder.nickContainer.getLayoutParams();
            if (lastParam.bottomMargin == 15) {
                lastParam.bottomMargin = 0;
                holder.nickContainer.setLayoutParams(lastParam);
            }
        }*/
        return convertView;
    }
}
