package com.xingbo.live.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.ShowBigPhotoAdapter;
import com.xingbo.live.entity.UserPhotos;
import com.xingbo.live.eventbus.PhotoDeleteEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/5
 */
public class PhotosDeleteAct extends BaseAct implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public final static String TAG = "PhotosDeleteAct";

    public final static String IMAGE_UPTIME = "image_uptime";
    public final static String IMAGE_URL = "iamge_url";
    public final static String IMAGE_POSITION = "image_position";
    public final static String IMAGE_PHOTOS = "image_photos";

    private TextView photoUptime;
    private FrescoImageView frescoImageView;
    private String imageUptime;
    private String imageUrl;
    private TextView photoNumber;
    private ViewPager viewPager;
    private ShowBigPhotoAdapter mAdapter;
    private ArrayList<UserPhotos> currentPhotos;
    private String imageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_photos_delete);
        imageUptime = getIntent().getStringExtra(IMAGE_UPTIME);
        imageUrl = getIntent().getStringExtra(IMAGE_URL);
        currentPhotos = (ArrayList<UserPhotos>) getIntent().getSerializableExtra(IMAGE_PHOTOS);
        findViewById(R.id.photo_delete_top_back_btn).setOnClickListener(this);
        findViewById(R.id.photo_delete).setOnClickListener(this);
        photoUptime = (TextView) findViewById(R.id.model_name);
        photoUptime.setText(imageUptime);
        frescoImageView = (FrescoImageView) findViewById(R.id.photo_show);
        frescoImageView.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + imageUrl));
        photoNumber = (TextView) findViewById(R.id.photo_delete_number);
        photoNumber.setText(getIntent().getStringExtra(IMAGE_POSITION) + "/" + currentPhotos.size());
        viewPager = (ViewPager) findViewById(R.id.photo_delete_viewpager);
        viewPager.addOnPageChangeListener(this);
        mAdapter = new ShowBigPhotoAdapter(currentPhotos);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra(IMAGE_POSITION))-1);
    }

    private int curentItem;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_delete_top_back_btn:
                onBackPressed();
                break;
            case R.id.photo_delete:
                imageId = currentPhotos.get(viewPager.getCurrentItem()).getId();
                XingBoUtil.dialog(this, "删除照片", "您确定要删除此照片么？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.px_dialog_ok:
                                RequestParam param = RequestParam.builder(PhotosDeleteAct.this);
                                param.put("pids", imageId);
                                CommonUtil.request(PhotosDeleteAct.this, HttpConfig.API_USER_GET_DELETEPHOTOS, param, TAG, new XingBoResponseHandler<BaseResponseModel>(PhotosDeleteAct.this, BaseResponseModel.class) {
                                    @Override
                                    public void phpXiuErr(int errCode, String msg) {
                                        alert(msg);
                                    }
                                    @Override
                                    public void phpXiuSuccess(String response) {
//                                        alert(model.getM());//
                                        Toast.makeText(PhotosDeleteAct.this,model.getM(),Toast.LENGTH_SHORT).show();
                                        for (int i = 0; i < currentPhotos.size(); i++) {
                                            if (imageId.equals(currentPhotos.get(i).getId())) {
                                                currentPhotos.remove(currentPhotos.get(i));
                                                break;
                                            }
                                        }
                                        if (currentPhotos.size() <= 0) {
                                            EventBus.getDefault().post(new PhotoDeleteEvent());
                                            finish();
                                        } else {
                                            curentItem = viewPager.getCurrentItem();
                                            handler.sendEmptyMessage(0);
                                        }
                                    }
                                });
                                break;
                            case R.id.px_dialog_cancel:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new PhotoDeleteEvent());
        super.onBackPressed();
    }

    @Override
    public void handleMsg(Message message) {
        if (message.what == 0) {
            mAdapter.setPhotos((ArrayList<UserPhotos>) currentPhotos);
            mAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(curentItem);
            if((curentItem+1)>currentPhotos.size()){
                curentItem--;
            }
            photoNumber.setText((curentItem+1) + "/" + currentPhotos.size());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        photoNumber.setText((position + 1) + "/" + ((ArrayList<UserPhotos>) getIntent().getSerializableExtra(IMAGE_PHOTOS)).size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
