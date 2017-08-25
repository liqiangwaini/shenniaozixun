package com.xingbo.live.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.eventbus.SaveAvatarEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.view.widget.SaveAvatarMenu;
import com.xingbobase.view.FrescoImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/6
 */
public class UserAvatarPreviewAct extends BaseAct implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "UserAvatarPreviewAct";
    public static final String USER_AVATAR_URL = "user_avatar_url";
    //图片保存的地址
    public static final String APP_DOWNLOAD_IMAGE_PATH = Environment.getExternalStorageDirectory() + "/xingbo/XingboDownload/image/";
    private FrescoImageView avatarImageView;
    private TextView avatarSave;
    private String avatarUrl;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.user_avatar_preview_activity);
        avatarUrl = getIntent().getStringExtra(USER_AVATAR_URL);
        avatarImageView = (FrescoImageView) findViewById(R.id.avatar_preview_image);
        avatarImageView.setOnLongClickListener(this);
        avatarImageView.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + avatarUrl));
        avatarSave = (TextView) findViewById(R.id.avatar_save);
        avatarSave.setOnClickListener(this);
        findViewById(R.id.top_back_btn).setOnClickListener(this);

    }

    @Subscribe
    public void popSaveAvatar(SaveAvatarEvent event) {
        new Task().execute(HttpConfig.FILE_SERVER + avatarUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.avatar_save:
                avatarImageSave();
                break;
        }
    }

    /**
     * 头像图片保存至本地
     */
    private void avatarImageSave() {
        //下载图片
        new Task().execute(HttpConfig.FILE_SERVER + avatarUrl);

    }

    @Override
    public boolean onLongClick(View v) {
        new SaveAvatarMenu(this, 0).showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//        findViewById(R.id.avatar_save_menu_save).setOnClickListener(this);
        return true;
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
            Log.d("tag", "inputStream" + inputStream);
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
            Toast.makeText(UserAvatarPreviewAct.this, "保存成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            SavaImage(bitmap, APP_DOWNLOAD_IMAGE_PATH);
        }
    }

    @Override
    public void handleMsg(Message message) {
        super.handleMsg(message);
        switch (message.what) {
            case 0x123:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
