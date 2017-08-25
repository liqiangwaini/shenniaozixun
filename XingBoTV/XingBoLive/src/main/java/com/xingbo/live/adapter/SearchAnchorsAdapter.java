package com.xingbo.live.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.SearchAnchorsViewHolder;
import com.xingbo.live.adapter.holder.UserFansViewHolder;
import com.xingbo.live.entity.SearchAnchors;
import com.xingbo.live.eventbus.HomeSearchEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.UserFansAct;
import com.xingbo.live.ui.UserHomepageAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbobase.api.OnRequestErrCallBack;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/12
 */
public class SearchAnchorsAdapter extends XingBoBaseAdapter<SearchAnchors> {

    private Context mContext;
    private List<SearchAnchors> mList;

    public SearchAnchorsAdapter(Context context, List<SearchAnchors> data) {
        super(context, data);
        mContext=context;
        mList=data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SearchAnchors searchAnchors = data.get(position);
        SearchAnchorsViewHolder holder= null;
        if (convertView==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_anchors, null);
            holder= new SearchAnchorsViewHolder(convertView);
            convertView.setTag(holder);
        }else {
           holder= (SearchAnchorsViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(searchAnchors.getAvatar())){
            holder.avatar.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+searchAnchors.getAvatar()));
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent home= new Intent(mContext, UserHomepageAct.class);
                    home.putExtra(UserHomepageAct.EXTRA_USER_ID,searchAnchors.getId());
                    mContext.startActivity(home);
                }
            });
        }
        if (!TextUtils.isEmpty(searchAnchors.getNick())){
            holder.nick.setText(searchAnchors.getNick());
        }
        if (!TextUtils.isEmpty(searchAnchors.getId())){
            holder.idNum.setText(searchAnchors.getId());
        }
        if (!TextUtils.isEmpty(searchAnchors.getFollowers())){
            holder.fansNum.setText(searchAnchors.getFollowers());
        }
        //主播搜索缺少个性签名
        if(!TextUtils.isEmpty(searchAnchors.getLivemood())){
            holder.personalSign.setText(searchAnchors.getLivemood());

        }
        //  是否关注
       boolean tr= searchAnchors.getRoominfo().isFollowed();
        if(searchAnchors.getRoominfo().isFollowed()){
            holder.btnFcous.setBackgroundResource(R.drawable.round_buttonn2);
            holder.btnFcous.setText("已关注");
            holder.btnFcous.setTextColor(mContext.getResources().getColor(R.color.cffffff));

        }else
        {
            holder.btnFcous.setBackgroundResource(R.drawable.round_button);
            holder.btnFcous.setText("关注");
            holder.btnFcous.setTextColor(mContext.getResources().getColor(R.color.cffffff));
        }

        final SearchAnchorsViewHolder finalHolder = holder;
        final SearchAnchorsViewHolder finalHolder1 = holder;
        final SearchAnchorsViewHolder finalHolder2 = holder;
        holder.btnFcous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.btnFcous.getText().toString().equals("已关注")){
                    RequestParam param= RequestParam.builder(mContext);

                    param.put("uid",searchAnchors.getId());
                    CommonUtil.request(mContext, HttpConfig.API_APP_CANCEL_FOLLOW, param, UserFansAct.TAG, new XingBoResponseHandler<BaseResponseModel>(new OnRequestErrCallBack() {
                        @Override
                        public void loginErr(int errCode, String msg) {

                        }

                        @Override
                        public void costErr(int errCode, String msg) {

                        }


                    }, BaseResponseModel.class) {
                        @Override
                        public void phpXiuErr(int errCode, String msg) {

                        }

                        @Override
                        public void phpXiuSuccess(String response) {
                            finalHolder1.btnFcous.setBackgroundResource(R.drawable.round_button);
                            finalHolder1.btnFcous.setText("关注");
                            finalHolder.btnFcous.setTextColor(mContext.getResources().getColor(R.color.cffffff));
                            EventBus.getDefault().post(new HomeSearchEvent());

                        }
                    });


                }else if (finalHolder.btnFcous.getText().toString().equals("关注")){
                    RequestParam param= RequestParam.builder(mContext);
                    param.put("uid",searchAnchors.getId());
                    CommonUtil.request(mContext, HttpConfig.API_APP_ADD_FOLLOW, param, UserFansAct.TAG, new XingBoResponseHandler<BaseResponseModel>(new OnRequestErrCallBack() {
                        @Override
                        public void loginErr(int errCode, String msg) {

                        }

                        @Override
                        public void costErr(int errCode, String msg) {

                        }


                    },BaseResponseModel.class) {
                        @Override
                        public void phpXiuErr(int errCode, String msg) {

                        }

                        @Override
                        public void phpXiuSuccess(String response) {
                            finalHolder2.btnFcous.setBackgroundResource(R.drawable.round_buttonn2);
                            finalHolder2.btnFcous.setText("已关注");
                            finalHolder.btnFcous.setTextColor(mContext.getResources().getColor(R.color.cffffff));
                            EventBus.getDefault().post(new HomeSearchEvent());

                        }
                    });

                }


            }
        });

        return convertView;
    }
}
