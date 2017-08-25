package com.xingbo.live.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.eventbus.UserEditEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.view.widget.PicSelectorMenu;
import com.xingbobase.config.XingBo;
import com.xingbobase.eventbus.CropEvent;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.UploadFileResponseModel;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.XingBoUploadHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.vov.vitamio.utils.Log;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/7
 */
public class UserAvatarChangeAct extends BaseAct implements View.OnLongClickListener, View.OnClickListener {
    private static  final String TAG="UserAvatarChangeAct";
    public static final String USER_AVATAR_URL="user_avatar_url";

    private FrescoImageView userAvatarPreview;
    private PicSelectorMenu picSelector;
    private RelativeLayout loadingBox;
    private ImageView loadingImage;
    private TextView uploadPro;
    private AnimationDrawable uploadAni;
    private SharedPreferences sp;

    public final static int SOURCE_TAG_CODE_HEADER = 0x202;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_avatar_change_activity);
        EventBus.getDefault().register(this);
        String imageUrl = getIntent().getStringExtra(USER_AVATAR_URL);
        userAvatarPreview= (FrescoImageView) findViewById(R.id.avatar_preview_image);
        userAvatarPreview.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + imageUrl));
        userAvatarPreview.setOnLongClickListener(this);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        findViewById(R.id.avatar_save).setOnClickListener(this);

        loadingBox = (RelativeLayout) findViewById(R.id.loading_box);
        loadingImage = (ImageView) findViewById(R.id.loading_header_view);
        uploadPro = (TextView) findViewById(R.id.header_upload_pro);
        uploadAni = (AnimationDrawable) loadingImage.getBackground();
    }

    @Subscribe
    public void uploadFile(CropEvent event) {
        if (event.getSourceTagCode() == SOURCE_TAG_CODE_HEADER) {
            if (event.getPath() == null) {
                return;
            }

            String path = event.getPath();
//            uploadHeader(path);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = event.getPath();
            handler.sendMessage(msg);
        }
    }
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case 0:
                uploadHeader(msg.obj.toString());
            default:
                break;
        }
    }

    /**
     * 上传头像
     */
    public void uploadHeader(final String path) {
        XingBoUtil.log(TAG, "新头像图片路径:" + path);
        if (loadingBox.getVisibility() == View.GONE) {
            loadingBox.setVisibility(View.VISIBLE);
            uploadAni.start();
        }
        CommonUtil.uploadFile(this,path, new XingBoUploadHandler<UploadFileResponseModel>(UploadFileResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                    uploadAni.stop();
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                XingBoUtil.log(TAG, "上传结果" + response);
                Log.d("tag123", "上传结果:" + model.getUrl());
                sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(XingBo.PX_USER_LOGIN_AVATAR, model.getUrl());
                editor.commit();
                updateHeader(model.getUrl());
                EventBus.getDefault().post(new UserEditEvent());
                userAvatarPreview.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + model.getUrl()));
            }

            @Override
            public void phpXiuProgress(long bytesWritten, long totalSize) {
                XingBoUtil.log("上传进度", bytesWritten + "@@@@@" + totalSize);
                uploadPro.setText(((int) (bytesWritten / totalSize) * 100) + "%");
            }
        });
    }


    /**
     * 提交更新头像
     */
    private void updateHeader(final String url) {
        RequestParam param = RequestParam.builder(this);
        param.put("avatar", url);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                    uploadAni.stop();
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                    uploadAni.stop();
                }
                XingBoUtil.log(TAG, "提交保存头像结果" + response);
                Log.d("tag", "提交保存头像结果" + response);
                userAvatarPreview.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + url));
                //alert("头像修改成功");
                XingBoUtil.tip(UserAvatarChangeAct.this, "头像修改成功!", Gravity.BOTTOM );

//                user.setAvatar(url);
//                EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_HEADER, user));
            }
        });
    }


    @Override
    public boolean onLongClick(View v) {
        //选择图片更换图像
        CommonUtil.log(TAG, "点击了头像");
        if (picSelector == null) {
            picSelector = new PicSelectorMenu(this, SOURCE_TAG_CODE_HEADER);
        }
        picSelector.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.avatar_save:
                //弹出popupwindow
                CommonUtil.log(TAG, "点击了头像");
                if (picSelector == null) {
                    picSelector = new PicSelectorMenu(this, SOURCE_TAG_CODE_HEADER);
                }
                picSelector.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;
        }
    }
}
