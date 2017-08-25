package com.xingbo.live.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.ShowBigPhotoAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.UserPhotos;
import com.xingbo.live.eventbus.PhotoDeleteEvent;
import com.xingbo.live.eventbus.SaveAvatarEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.view.widget.SaveAvatarMenu;
import com.xingbobase.view.FrescoImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/19
 */
public class PhotosSaveAct extends BaseAct implements View.OnClickListener, ViewPager.OnPageChangeListener, View.OnLongClickListener {
    private static final String TAG = "PhotosSaveAct";
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
    private Bitmap bitmap;
    private int currentPosition;
    private String imageId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_photos_save);
        EventBus.getDefault().register(this);
        imageUptime = getIntent().getStringExtra(IMAGE_UPTIME);
        imageUrl = getIntent().getStringExtra(IMAGE_URL);
        currentPhotos = (ArrayList<UserPhotos>) getIntent().getSerializableExtra(IMAGE_PHOTOS);
        findViewById(R.id.photo_delete_top_back_btn).setOnClickListener(this);
        findViewById(R.id.photo_delete).setOnClickListener(this);
        photoUptime = (TextView) findViewById(R.id.model_name);
        photoUptime.setText(imageUptime);
        frescoImageView = (FrescoImageView) findViewById(R.id.photo_show);
        frescoImageView.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + imageUrl));
        frescoImageView.setOnLongClickListener(this);
        photoNumber = (TextView) findViewById(R.id.photo_delete_number);
        photoNumber.setText(getIntent().getStringExtra(IMAGE_POSITION) + "/" + currentPhotos.size());
        viewPager = (ViewPager) findViewById(R.id.photo_delete_viewpager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOnLongClickListener(this);
        mAdapter = new ShowBigPhotoAdapter(currentPhotos);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra(IMAGE_POSITION)) - 1);
    }

    @Subscribe
    public void popSaveAvatar(SaveAvatarEvent event) {
        new Task().execute(HttpConfig.FILE_SERVER + currentPhotos.get(currentPosition).getUrl());
    }

    private int curentItem;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_delete_top_back_btn:
                onBackPressed();
                break;
            case R.id.photo_delete:
                avatarImageSave(imageUrl);
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
            if ((curentItem + 1) > currentPhotos.size()) {
                curentItem--;
            }
            photoNumber.setText((curentItem + 1) + "/" + currentPhotos.size());
        }
        if (message.what == 0x123) {

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        photoNumber.setText((position + 1) + "/" + ((ArrayList<UserPhotos>) getIntent().getSerializableExtra(IMAGE_PHOTOS)).size());
        currentPosition=position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 头像图片保存至本地
     */
    private void avatarImageSave(String imageUrl) {
        //下载图片
        new Task().execute(HttpConfig.FILE_SERVER + imageUrl);

    }

    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            android.util.Log.d("tag", "inputStream" + inputStream);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public void SavaImage(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(path + "/" + System.currentTimeMillis() + "xingbo.png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.photo_delete_viewpager:
                new SaveAvatarMenu(this, 0).showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;
        }

        return true;
    }

    /**
     * 异步线程下载图片
     */
    class Task extends AsyncTask<String, Integer, Void> {
        protected Void doInBackground(String... params) {
            bitmap = GetImageInputStream((String) params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Message message = new Message();
            message.what = 0x123;
            handler.sendMessageDelayed(message, 1000);
            SavaImage(bitmap, XingBoConfig.APP_DOWNLOAD_IMAGE_PATH);
        }
    }


}
