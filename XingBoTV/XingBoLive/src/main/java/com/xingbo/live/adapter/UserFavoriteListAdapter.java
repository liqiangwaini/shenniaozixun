package com.xingbo.live.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.UserFavoriteViewHolder;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MainUser;
import com.xingbo.live.entity.User;
import com.xingbo.live.entity.UserFavorite;
import com.xingbo.live.entity.model.UserHomeModel;
import com.xingbo.live.eventbus.ConcernBtnClickEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.UserFavoriteAct;
import com.xingbo.live.ui.UserHomepageAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.adapter.XingBoBaseAdapter;
import com.xingbobase.api.OnRequestErrCallBack;
import com.xingbobase.extra.pulltorefresh.internal.Utils;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.lang.ref.WeakReference;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserFavoriteListAdapter extends XingBoBaseAdapter<UserFavorite> {
    private LayoutInflater mInflater;
    private WeakReference<View.OnClickListener> listenerRef;
    private View.OnClickListener mListener;
    private Context mContext;
    private Activity mActivity;



    public UserFavoriteListAdapter(Activity activity, List<UserFavorite> data, View.OnClickListener listener) {
        super(activity, data);
        mActivity = activity;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        listenerRef = new WeakReference<View.OnClickListener>(listener);
        mListener = listener;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserFavorite item = data.get(position);
        UserFavoriteViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.user_favorite_list_item, null);
            holder = new UserFavoriteViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (UserFavoriteViewHolder) convertView.getTag();
        }
        holder.setFavorite(item);
        holder.header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + item.getAvatar()));
        //点击头像进入该用户的主页信息
//        holder.header.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent homepageIntent= new Intent(mActivity, UserHomepageAct.class);
//                homepageIntent.putExtra(UserHomepageAct.EXTRA_USER_ID,item.getId());
//                mActivity.startActivity(homepageIntent);
//            }
//        });
        holder.header.setTag(item.getId());
        if (listenerRef != null && listenerRef.get() != null) {
            holder.header.setOnClickListener(listenerRef.get());
        }
        holder.nick.setText(item.getNick());
        int rlv = 0;
        try {
            rlv = Integer.parseInt(item.getRichlvl());
        } catch (NumberFormatException e) {
            rlv = 0;
        }
        if (rlv < 35) {
            holder.richIcon.setImageResource(XingBoConfig.RICH_LV_ICONS[rlv]);
        } else {
            holder.richIcon.setImageResource(XingBoConfig.RICH_LV_ICONS[34]);
        }
        holder.setUid(item.getId());
        //个性签名
        if (!TextUtils.isEmpty(item.getIntro())) {

            holder.personalSign.setText(item.getIntro());
        }

        //设置性别的图片
        if (item.getSex().equals("女")) {
            holder.uSex.setImageResource(R.mipmap.female);
        } else {
            holder.uSex.setImageResource(R.mipmap.male);
        }

            holder.btnFcous.setBackgroundResource(R.drawable.round_buttonn2);
            holder.btnFcous.setText("已关注");
            holder.btnFcous.setTextColor(mActivity.getResources().getColor(R.color.cffffff));
//            id = item.getId();//关注用户的id  获取详细的信息 得到boolean的变量isFollowed；

        //关注功能设置监听事件
        holder.btnFcous.setTag(position);
        final UserFavoriteViewHolder finalHolder = holder;
        final UserFavoriteViewHolder finalHolder1 = holder;
        holder.btnFcous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (finalHolder1.btnFcous.getText().toString().equals("已关注")) {
                    RequestParam param = RequestParam.builder(mActivity);
                    param.put("uid", item.getId());
                    CommonUtil.request(mActivity, HttpConfig.API_APP_CANCEL_FOLLOW, param, UserFavoriteAct.TAG, new XingBoResponseHandler<BaseResponseModel>(new OnRequestErrCallBack() {
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

                            finalHolder.btnFcous.setBackgroundResource(R.drawable.round_button);
                            finalHolder.btnFcous.setText("关注");
                            finalHolder.btnFcous.setTextColor(mContext.getResources().getColor(R.color.cffffff));
                            EventBus.getDefault().post(new ConcernBtnClickEvent());

                        }
                    });
                } else if (finalHolder1.btnFcous.getText().toString().equals("关注")) {
                    RequestParam param = RequestParam.builder(mActivity);
                    param.put("uid", item.getId());
                    CommonUtil.request(mActivity, HttpConfig.API_APP_ADD_FOLLOW, param, UserFavoriteAct.TAG, new XingBoResponseHandler<BaseResponseModel>(new OnRequestErrCallBack() {
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
                            finalHolder.btnFcous.setBackgroundResource(R.drawable.round_buttonn2);
                            finalHolder.btnFcous.setText("已关注");
                            finalHolder.btnFcous.setTextColor(mActivity.getResources().getColor(R.color.cffffff));
                            EventBus.getDefault().post(new ConcernBtnClickEvent());
                        }
                    });
                }


            }
        });

        return convertView;
    }
}


