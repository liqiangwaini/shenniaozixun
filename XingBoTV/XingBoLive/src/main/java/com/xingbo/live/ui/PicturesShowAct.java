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


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.PictureShowPagerAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MainUser;
import com.xingbo.live.entity.UserPhotos;
import com.xingbo.live.entity.model.UserHomeModel;
import com.xingbo.live.fragment.MBaseFragment;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/1
 */
public class PicturesShowAct extends BaseAct implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private final static String TAG = "PicturesShowAct";
    public final static String EXTRA_USER_ID = "extra_user_id";
    public final static String PHOTOS_LIST__FLAG = "photolist";
    public final static String CURRENT_POSITION = "current_position";

    private String currentPosition;
    private String uid;
    private ImageButton imageButton;//返回按钮
    private TextView pictureCurrentNum;//当前数
    private TextView pictureTotalNum;//总图片数
    private TextView nick;
    private List<MBaseFragment> fragments = new ArrayList<>();
    private PictureShowPagerAdapter mAdapter;
    private ViewPager viewPager;
    private MainUser mUser;
    private  int currPosition;
    private Bitmap bitmap;
    private List<UserPhotos> photolist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_show_activity);
        Intent intent = getIntent();
        photolist = (List<UserPhotos>) intent.getSerializableExtra(PHOTOS_LIST__FLAG);
        uid = intent.getStringExtra(EXTRA_USER_ID);
        currentPosition = intent.getStringExtra(CURRENT_POSITION);
        viewPager = (ViewPager) findViewById(R.id.picture_show_viewpage);
        viewPager.setOnPageChangeListener(this);
        imageButton = (ImageButton) findViewById(R.id.top_back_btn);
        imageButton.setOnClickListener(this);
        pictureCurrentNum = (TextView) findViewById(R.id.picture_current_num);
        pictureTotalNum = (TextView) findViewById(R.id.picture_total_num);
        pictureTotalNum.setText(photolist.size() + "");
        nick = (TextView) findViewById(R.id.model_name);
        findViewById(R.id.pic_save).setOnClickListener(this);
        request();

        //载入图片资源url
        for (int i = 0; i < photolist.size(); i++) {

//            Bundle bundle = new Bundle();
//            bundle.putString(PictureShowFragment.IMAGE_URL, photolist.get(i).getUrl());
//            pictureShowFragment.setArguments(bundle);
            mAdapter = new PictureShowPagerAdapter(getSupportFragmentManager(), fragments);
            viewPager.setAdapter(mAdapter);
        }
        viewPager.setCurrentItem(Integer.parseInt(currentPosition));
        Log.d("tag", "phptolist-->" + photolist.size() + "......" + "position-->" + currentPosition + "||" + photolist.get(0));
    }

    private void request() {
        RequestParam param = RequestParam.builder(this);
        param.put("uid", uid);
        CommonUtil.request(this, HttpConfig.API_USER_GET_USER_INFO, param, TAG, new XingBoResponseHandler<UserHomeModel>(UserHomeModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.d(TAG, "用户中心初始化结果" + response);
                mUser = model.getD();
                if (!TextUtils.isEmpty(mUser.getNick())) {
                    nick.setText(mUser.getNick()+"的相册");
                }

            }


        });
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        pictureCurrentNum.setText((position + 1) + "");
        currPosition=position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "state-->" + state);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.pic_save:
                avatarImageSave(photolist.get(currPosition).getUrl());
                break;
        }
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


    @Override
    public void handleMsg(Message message) {
        super.handleMsg(message);
        switch (message.what) {
            case 0x123:
                break;
        }
    }
}

