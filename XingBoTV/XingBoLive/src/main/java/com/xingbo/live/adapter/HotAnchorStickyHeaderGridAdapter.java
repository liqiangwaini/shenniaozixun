package com.xingbo.live.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xingbo.live.ui.UserHomepageAct;
import com.xingbobase.adapter.XingBoBaseStickyHeaderGridAdapter;
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
public class HotAnchorStickyHeaderGridAdapter extends XingBoBaseStickyHeaderGridAdapter<HomeAnchor> {
    public final static String TAG="HotAnchorStickyHeaderGridAdapter";
    private int mEmptyHeaderResId;
    private ViewGroup.LayoutParams headerViewParams;//轮播图区域高度
    private AbsListView.LayoutParams aParam;
    private FrameLayout.LayoutParams lastParam;
    private Context mContext;
    public HotAnchorStickyHeaderGridAdapter(Context context, List<HomeAnchor> data) {
        super(context, data);
        mContext=context;
        mEmptyHeaderResId=R.layout.empty_view;
        int cw=(int)SystemApp.screenWidth- CommonUtil.dip2px(context,10);
        aParam=new AbsListView.LayoutParams(cw/2,cw/2);
    }

    public HotAnchorStickyHeaderGridAdapter(Context context, List<HomeAnchor> data,int mEmptyHeaderHeight) {
        this(context, data);
        headerViewParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mEmptyHeaderHeight);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getId() != R.id.sticky_header_empty_view) {
            convertView = mInflater.inflate(mEmptyHeaderResId, parent, false);
            convertView.setLayoutParams(headerViewParams);
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HomeAnchor anchor=(HomeAnchor)data.get(position);
        AnchorViewHolder holder=null;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.hot_anchor_sticky_header_item,null);
            holder=new AnchorViewHolder(convertView);
            convertView.setTag(holder);
            convertView.setLayoutParams(aParam);
        }else{
            holder=(AnchorViewHolder)convertView.getTag();
        }
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
        holder.setAnchor(anchor);

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
                if (id!=null) {
                    Intent home = new Intent(mContext, UserHomepageAct.class);
                    home.putExtra(UserHomepageAct.EXTRA_USER_ID, id);
                    //  home.putExtra(UserHomeAct.EXTRA_MODEL_NAME,"个人档案");
                    mContext.startActivity(home);
                }else
                {
                    Toast.makeText(mContext, "用户不存在！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.nick.setText(anchor.getNick());
        /*if(lastParam==null) {
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
        holder.online.setText(anchor.getLiveonlines() + "在看");
        return convertView;
    }
}
