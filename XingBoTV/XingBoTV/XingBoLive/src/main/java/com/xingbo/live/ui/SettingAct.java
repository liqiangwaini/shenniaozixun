package com.xingbo.live.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.PersistentCookieStore;
import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.BaseUser;
import com.xingbo.live.entity.MainUser;
import com.xingbo.live.entity.model.MainUserModel;
import com.xingbo.live.eventbus.LoginOutEvent;
import com.xingbo.live.eventbus.PhoneBindingStateEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.CacheManager;
import com.xingbobase.util.XingBoUtil;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.vov.vitamio.utils.Log;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class SettingAct extends BaseAct implements View.OnClickListener {
    public final static String TAG="SettingAct";
    public final static String EXTRA_IS_LOGIN="extra_is_login";
    public final static String EXTRA_MUSER="extra_mure";
    public final static int HANDLER_MSG_SET_CACHE_MSG=0x1;

    private boolean isLogin=true;
    private Button loginOutBtn;
    private TextView cacheSizeTv;
    private RelativeLayout rl_password;

    private String uid;
    private BaseUser mUser;
    private TextView phoneBind;
    private  TextView phoneBindNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        EventBus.getDefault().register(this);
        mUser= (BaseUser) getIntent().getSerializableExtra(EXTRA_MUSER);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        uid= getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE,Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID,"");
        rl_password= (RelativeLayout) findViewById(R.id.user_password_box);
        phoneBindNum= (TextView) findViewById(R.id.phone_band_num);
        if (mUser.getAuthtype().equals("local")||mUser.getAuthtype().equals("phone")){
            rl_password.setVisibility(View.VISIBLE);
            findViewById(R.id.user_password_box).setOnClickListener(this);
        }else{
            rl_password.setVisibility(View.GONE);
        }
        //网络请求：
        request();
        phoneBind= (TextView) findViewById(R.id.phone_band);
        isLogin=getIntent().getBooleanExtra(EXTRA_IS_LOGIN,false);
        loginOutBtn=(Button)findViewById(R.id.login_out);
        if(isLogin){
           loginOutBtn.setVisibility(View.VISIBLE);
           loginOutBtn.setOnClickListener(this);
        }else{
           loginOutBtn.setVisibility(View.GONE);
        }
        cacheSizeTv=(TextView)findViewById(R.id.cache_size);
        cacheSizeTv.setText("正在计算...");
        Thread t=new Thread(new CalculateCacheSize());
        t.start();
        findViewById(R.id.cache_setting).setOnClickListener(this);
        findViewById(R.id.feedback_setting).setOnClickListener(this);
        findViewById(R.id.about_setting).setOnClickListener(this);

        findViewById(R.id.sending_box).setOnClickListener(this);

        findViewById(R.id.user_phone_box).setOnClickListener(this);
    }


    @Subscribe
    public void phoneBindingState(PhoneBindingStateEvent phoneBindingStateEvent){
        request();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void request() {
        RequestParam  param = RequestParam.builder(this);
        param.put("uid",uid);
        CommonUtil.request(this, HttpConfig.API_USER_GET_PROFILE, param, TAG, new XingBoResponseHandler<MainUserModel>(MainUserModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
            }
            @Override
            public void phpXiuSuccess(String response) {
                mUser=model.getD();
                if(mUser.getPhone().equals("")){
                    phoneBind.setText("点此绑定手机");
                }else {
                    phoneBind.setText("点此解除绑定");
                    String phone = mUser.getPhone();
                    phoneBindNum.setText(phone.substring(0,3)+"****"+phone.substring(7,phone.length()));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.sending_box:
                Intent phshIntent= new Intent(this,UserPushAct.class);
                startActivity(phshIntent);
                break;
            case R.id.login_out:
                dialog("退出登录","退出登陆会清除个人信息,确认退出？",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersistentCookieStore cookieStore=new PersistentCookieStore(SettingAct.this);
                        cookieStore.clear();
                        EventBus.getDefault().post(new LoginOutEvent());
                        //清除登录信息
                        SharedPreferences sp=getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor= sp.edit();
                        editor.remove(XingBo.PX_USER_LOGIN_UID);
                        editor.remove(XingBo.PX_USER_LOGIN_NICK);
                        editor.remove(XingBo.PX_USER_LOGIN_AVATAR);
                        editor.remove(XingBo.PX_USER_LOGIN_LIVENAME);
                        editor.commit();
                        Intent login=new Intent(SettingAct.this, LoginAct.class);
                        startActivity(login);
                        finish();
                    }
                });
                break;
            case R.id.cache_setting:
                dialog("清除缓存","确定清除所有缓存内容吗",new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearCache(SettingAct.this);

                        XingBoUtil.alert(SettingAct.this, "清理完成！");
                    }
                });
                break;
            case R.id.feedback_setting://意见反馈
                Intent feedBack=new Intent(this,FeedBack.class);
                startActivity(feedBack);
                break;
            case R.id.about_setting:
                Intent about=new Intent(this,AboutWeb.class);
                startActivity(about);
                break;
            case R.id.user_password_box://密码修改
                Intent password= new Intent(this,SetNewPwd.class);
                startActivity(password);
                break;
            case R.id.user_phone_box://手机绑定
                if(mUser.getPhone().equals("")) {
                    Intent phoneBind = new Intent(this, UserPhoneBindingAct.class);
                    startActivity(phoneBind);
                }else {
                    //解除绑定的操作
                    XingBoUtil.dialog(this, "解除绑定", "确定是否解除绑定，解除绑定后将无法通过手机找回密码？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.px_dialog_ok:
                                    Toast.makeText(SettingAct.this, "解除绑定", Toast.LENGTH_SHORT).show();
                                    cancelBinding();
                                    break;
                                case R.id.px_dialog_cancel:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    }).show();
                }
                break;
            default:
                break;
        }
    }

    private void cancelBinding() {
        RequestParam param = RequestParam.builder(this);
        param.put("nick",mUser.getNick());
        param.put("avatar",mUser.getAvatar());
        param.put("sex",mUser.getSex());
        param.put("addr",mUser.getAddr());
        param.put("birth",mUser.getBirth());
        param.put("intro",mUser.getIntro());
        param.put("phone","");
        CommonUtil.request(this, HttpConfig.API_USER_GET_PHONE_CANCELBIND, param, TAG, new XingBoResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                Toast.makeText(SettingAct.this, "解除绑定成功！", Toast.LENGTH_SHORT).show();
                phoneBind.setText("点此绑定手机");
                request();
            }
        });
    }


    @Override
    public void handleMsg(Message msg) {
        switch (msg.what){
            case HANDLER_MSG_SET_CACHE_MSG:
                cacheSizeTv.setText(msg.obj.toString());
                break;
        }
    }

    /**
     * 计算缓存
     */
    class  CalculateCacheSize implements Runnable{
        @Override
        public void run() {
            Message msg=handler.obtainMessage();
            msg.what=HANDLER_MSG_SET_CACHE_MSG;
            msg.obj=cacheSize();
            handler.sendMessage(msg);
        }
    }

    /**
     * 计算缓存
     * 1.Http请求缓存 H
     * 2.ImageLoader图片加载缓存
     * 3.Fresco 图片缓存
     * 4.webView缓存
     * 5.其它下载缓存文件
     */
    public String cacheSize(){
        long size=0;
        String cacheDir=getCacheDir().getPath();
        try {
            long innerCacheSize= CacheManager.getFolderSize(getCacheDir());
            size+=innerCacheSize;
            CommonUtil.log(TAG, "内部临时缓存:" + innerCacheSize);
            String fileDir=getFilesDir().getPath();
            long innerFileCache= CacheManager.getFolderSize(getFilesDir());
            size+=innerFileCache;
            CommonUtil.log(TAG, "内部文件缓存:" + innerFileCache);
            if (1==2&&Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String extraCacheDir=getExternalCacheDir().getPath();
                long extraCache= CacheManager.getFolderSize(getExternalCacheDir());
                size+=extraCache;
                CommonUtil.log(TAG, "外部临时缓存:" + extraCache);
                String extraFileDir_DCIM=getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath();
                long extraDCIMSize= CacheManager.getFolderSize(getExternalFilesDir(Environment.DIRECTORY_DCIM));
                size+=extraDCIMSize;
                CommonUtil.log(TAG, "外部DCIM文件缓存:" + extraDCIMSize);
                String extraFileDir_MOVIE=getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath();
                long extraMOVIESSize= CacheManager.getFolderSize(getExternalFilesDir(Environment.DIRECTORY_MOVIES));
                CommonUtil.log(TAG, "外部MOVIES文件缓存:" + extraMOVIESSize);
                size+=extraMOVIESSize;
                String extraFileDir_ALARMS=getExternalFilesDir(Environment.DIRECTORY_ALARMS).getPath();
                long extraALARMSSize= CacheManager.getFolderSize(getExternalFilesDir(Environment.DIRECTORY_ALARMS));
                CommonUtil.log(TAG, "外部ALARMS文件缓存:" + extraALARMSSize);
                size+=extraALARMSSize;
                String extraFileDir_DOC=getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath();
                long extraDOCSize= CacheManager.getFolderSize(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
                CommonUtil.log(TAG, "外部DOC文件缓存:" + extraDOCSize);
                size+=extraDOCSize;
                String extraFileDir_DOWN=getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath();
                long extraDOWNSize= CacheManager.getFolderSize(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
                CommonUtil.log(TAG, "外部DOWN文件缓存:" + extraDOWNSize);
                size+=extraDOWNSize;
                String extraFileDir_PIC=getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
                long extraPICSize= CacheManager.getFolderSize(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                CommonUtil.log(TAG, "外部PIC文件缓存:" + extraPICSize);
                size+=extraPICSize;
                String extraFileDir_MUSIC=getExternalFilesDir(Environment.DIRECTORY_MUSIC).getPath();
                long extraMUSICSize= CacheManager.getFolderSize(getExternalFilesDir(Environment.DIRECTORY_MUSIC));
                CommonUtil.log(TAG, "外部MUSIC文件缓存:" + extraMUSICSize);
                size+=extraMUSICSize;
                CommonUtil.log(TAG,"caheSize--》"+size);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CacheManager.getFormatSize(size);
    }

    /**
     * 清除缓存
     */
    public void clearCache(Context context){
        CommonUtil.log(TAG, "清除文件缓存....");
        CacheManager.deleteFolderFile(getCacheDir().getPath(), true);
        CacheManager.deleteFolderFile(getFilesDir().getPath(), true);
        CacheManager.deleteFolderFile(getExternalCacheDir().getPath(), true);
        Toast.makeText(SettingAct.this, "清除完成", Toast.LENGTH_SHORT).show();
        cacheSizeTv.setText("0.0M");
    }

}
