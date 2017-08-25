package com.xingbo.live;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.drawable.ScalingUtils;
import com.xingbo.live.entity.UserPhotos;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.FrescoUtils;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;

/**
 * Created by xingbo_szd on 2016/9/9.
 */
public class ShowBigPhotoAdapter extends PagerAdapter {
    private ArrayList<UserPhotos> userPhotosList;

    public ShowBigPhotoAdapter(ArrayList<UserPhotos> userPhotosList) {
        this.userPhotosList = userPhotosList;
    }

    public void setPhotos(ArrayList<UserPhotos> userPhotosList) {
        this.userPhotosList = userPhotosList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return userPhotosList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(container.getContext(), R.layout.item_show_big_photo, null);
        FrescoImageView frescoImageView = (FrescoImageView) view.findViewById(R.id.image_item_show_big_photo);
        frescoImageView.setMaxHeight((int) SystemApp.screenHeight);
        frescoImageView.setMaxWidth((int) SystemApp.screenWidth);
        frescoImageView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
        frescoImageView.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + userPhotosList.get(position).getUrl()));
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(view);
        return view;
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
