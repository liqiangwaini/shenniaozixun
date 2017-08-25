package com.xingbo.live.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserFansViewHolder;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.UserFans;
import com.xingbo.live.eventbus.ConcernBtnClickEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.UserFansAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbobase.api.OnRequestErrCallBack;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.lang.ref.WeakReference;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserFansListAdapter extends XingBoBaseAdapter<UserFans> {
    private LayoutInflater mInflater;
    private WeakReference<View.OnClickListener>listenerRef;
    private View.OnClickListener mListener;

    private Activity mActivity;

    public UserFansListAdapter(Activity activity, List<UserFans> data, View.OnClickListener listener) {
        super(activity, data);
        mInflater=LayoutInflater.from(context);
        mActivity= activity;
        listenerRef=new WeakReference<View.OnClickListener>(listener);
        mListener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserFans item=data.get(position);
        UserFansViewHolder holder=null;
        if(convertView==null){
           convertView=mInflater.inflate(R.layout.user_fans_list_item,null);
           holder=new UserFansViewHolder(convertView);
           convertView.setTag(holder);
        }else{
           holder=(UserFansViewHolder)convertView.getTag();
        }
        holder.header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+item.getAvatar()));
        holder.header.setTag(item.getId());
        if(listenerRef!=null&&listenerRef.get()!=null) {
            holder.header.setOnClickListener(listenerRef.get());
        }
        holder.nick.setText(item.getNick());
        int rlv=0;
        try {
            rlv=Integer.parseInt(item.getRichlvl());
        }catch (NumberFormatException e){
            rlv=0;
        }
        if(rlv<35) {
            holder.richIcon.setImageResource(XingBoConfig.RICH_LV_ICONS[rlv]);
        }else{
            holder.richIcon.setImageResource(XingBoConfig.RICH_LV_ICONS[33]);
        }
        holder.setUid(item.getId());

        if(item.getSex().equals("女"))
        {
            holder.uSex.setImageResource(R.mipmap.female);

        }else {
            holder.uSex.setImageResource(R.mipmap.male);
        }
        if (!TextUtils.isEmpty(item.getIntro())){
            holder.personalSign.setText(item.getIntro());
        }

        if (item.getFollowed()){
            holder.btnFcous.setBackgroundResource(R.drawable.round_buttonn2);
            holder.btnFcous.setText("已关注");
            holder.btnFcous.setTextColor(mActivity.getResources().getColor(R.color.cffffff));
        }else {
            holder.btnFcous.setBackgroundResource(R.drawable.round_button);
            holder.btnFcous.setText("关注");
            holder.btnFcous.setTextColor(mActivity.getResources().getColor(R.color.cffffff));
        }

        final UserFansViewHolder finalHolder = holder;
        final UserFansViewHolder finalHolder1 = holder;
        final UserFansViewHolder finalHolder2 = holder;
        holder.btnFcous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.btnFcous.getText().toString().equals("已关注")){
                    RequestParam param= RequestParam.builder(mActivity);
                    param.put("uid",item.getId());
                    CommonUtil.request(mActivity, HttpConfig.API_APP_CANCEL_FOLLOW, param, UserFansAct.TAG, new XingBoResponseHandler<BaseResponseModel>(new OnRequestErrCallBack() {
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
                            finalHolder1.btnFcous.setBackgroundResource(R.drawable.round_button);
                            finalHolder1.btnFcous.setText("关注");
                            finalHolder.btnFcous.setTextColor(mActivity.getResources().getColor(R.color.cffffff));
                            EventBus.getDefault().post(new ConcernBtnClickEvent());

                        }
                    });


                }else if (finalHolder.btnFcous.getText().toString().equals("关注")){
                    RequestParam param= RequestParam.builder(mActivity);
                    param.put("uid",item.getId());
                    CommonUtil.request(mActivity, HttpConfig.API_APP_ADD_FOLLOW, param, UserFansAct.TAG, new XingBoResponseHandler<BaseResponseModel>(new OnRequestErrCallBack() {
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
                            finalHolder.btnFcous.setTextColor(mActivity.getResources().getColor(R.color.cffffff));
                            EventBus.getDefault().post(new ConcernBtnClickEvent());

                        }
                    });

                }

            }

        });
//        if(item.getLivestatu.s().equals("1")){
//           if(holder.roomLink.getVisibility()==View.GONE){
//              holder.roomLink.setVisibility(View.VISIBLE);
//              holder.roomLink.setTag(item);
//              if(listenerRef!=null&&listenerRef.get()!=null) {
//                  holder.roomLink.setOnClickListener(listenerRef.get());
//              }
//           }
//        }else{
//           if(holder.roomLink.getVisibility()==View.VISIBLE){
//              holder.roomLink.setVisibility(View.GONE);
//           }
//        }
        return convertView;
    }
}
