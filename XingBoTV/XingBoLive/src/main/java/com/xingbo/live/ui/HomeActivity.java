package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.api.FragmentSelectedCallBack;
import com.xingbo.live.entity.model.UpdateInfoModel;
import com.xingbo.live.eventbus.LoginOutEvent;
import com.xingbo.live.fragment.FragmentLiveList;
import com.xingbo.live.fragment.FragmentPublish;
import com.xingbo.live.fragment.HomeFragment;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.service.UpdateService;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.CpuManager;
import com.xingbo.live.util.ToolFile;
import com.xingbo.live.view.FragmentTabHost;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;

import java.io.File;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


/**
 * Created by 123 on 2016/6/30.
 *
 * @package com.xingbo.live.ui
 * @description 描述 主页布局
 */
public class HomeActivity extends BaseAct {
    private FragmentTabHost mTabHost;
    private LayoutInflater layoutInflater;
    private final static int APP_INIT_FINISH = 0x1;
    //    private ProfileInfoHelper infoHelper;
//    private LoginHelper mLoginHelper;
    private final Class fragmentArray[] = {FragmentLiveList.class, FragmentPublish.class, HomeFragment.class};
    private int mImageViewArray[] = {R.drawable.tab_live, R.drawable.publish_home_icon_bg_selector, R.drawable.tab_profile};
    private String mTextviewArray[] = {"直播", "", "发现"};
    private static final String TAG = HomeActivity.class.getSimpleName();
    private String userUrl, useId, userliveName;
    private String uid;
    private FragmentSelectedCallBack fragmentSelectedCallBack;
    private View mContextViewmContextView;
    private String currentVersion;
    private String serverVersion;
    private RelativeLayout rlWelcome;
    private SharedPreferences sp;
    private String versionCheckFlag;
    public static final String APP_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/xingbo/XingboDownload/App/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemApp.getInstance().addActivity(this);
        setContentView(R.layout.home_layout);
        EventBus.getDefault().register(this);
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        userUrl = sp.getString(XingBo.PX_USER_LOGIN_AVATAR, "0");
        versionCheckFlag = sp.getString(XingBo.PX_USER_LOGIN_VERSION_CHECK, "0");
        CommonUtil.log(TAG, "头像结果" + userUrl);
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        layoutInflater = LayoutInflater.from(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentPanel);
        mContextViewmContextView = LayoutInflater.from(this).inflate(R.layout.home_layout, null, false);
        int fragmentCount = fragmentArray.length;
        for (int i = 0; i < fragmentCount; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().setDividerDrawable(null);
        }

        //开播的按钮
        mTabHost.getTabWidget().getChildTabViewAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.CPU_ABI.equals("armeabi-v7a")) {
                    Intent liveCoverIntent = new Intent(HomeActivity.this, StartLiveCoverAct.class);
                    startActivity(liveCoverIntent);
                } else {
                    alert("当前手机机型不支持开播功能，更多支持，敬请期待！");
                }
            }
        });
        uploadUserData();
        if (versionCheckFlag.equals("0")) {
            upAppDate();
        }
    }

    public void uploadUserData() {
        //将用户的id和device_token传到服务器
        PushAgent mPushAgent = PushAgent.getInstance(HomeActivity.this);
        mPushAgent.enable();
        String device_token = UmengRegistrar.getRegistrationId(HomeActivity.this);
        uid = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID, "");
        RequestParam param = RequestParam.builder(this);
        param.put("device_token", device_token);
        param.put("device_type", "3");
        CommonUtil.request(this, HttpConfig.API_APP_GET_UMENG_DEVICE_BIND, param, TAG, new XingBoResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.d(TAG, response);
            }
        });
        Log.d(TAG, "device-->" + device_token + "\n" + "--->" + uid);
    }

    public void handleMsg(Message msg) {
        //此处不处理断网或超时情况
        switch (msg.what) {
            case APP_INIT_FINISH:
                break;
            default:
                break;
        }
    }

    private void upAppDate() {
        try {
            currentVersion = getVersionName();
            Log.d("tag", "currentVersion-->" + currentVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestParam param = RequestParam.builder(this);
        param.put("device", "android");
        CommonUtil.request(this, HttpConfig.API_APP_GET_AUTODATE_INSTALLPACKAGE, param, TAG, new XingBoResponseHandler<UpdateInfoModel>(UpdateInfoModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                Log.d(TAG, msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.d("tag", "123-->" + response);
                if (model.getD() == null) {
                    return;
                }
                serverVersion = model.getD().getPackage_version();//服务端版本号
                //检测网络状态有关的
                NetworkInfo.State wifiState = null;
                NetworkInfo.State mobileState = null;
                final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                Log.d("tag", "serverVersion-->" + serverVersion);
                //版本号不同提示用户更新
                if (!currentVersion.equals(serverVersion)) {
                    if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED == wifiState && NetworkInfo.State.CONNECTED != mobileState) {
                        //wifi状态下直接下载,完成后提示用户安装
                        //    String md5 = o.getCt_RomoteAppIsUpdateResult().get_ctUserAppUpdateInfo().getStrCtAppMd5();

//                        String filePath = APP_DOWNLOAD_PATH + "XingboTV_" + serverVersion + ".apk";
                        //  String localMd5 = ToolMD5.getFileMD5(new File(filePath));
//                        boolean isExsit = ToolFile.isExsit(filePath);
//                        //      boolean Md5File = (md5 == null ? "" : md5).equals(localMd5);
//                        if (isExsit) {
                        XingBoUtil.dialogCheck(HomeActivity.this, "立即更新", "稍后更新", R.color.pink, R.color.c333333, "版本更新", "星播TV" + model.getD().getPackage_desc(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.px_dialog_cancel:
                                        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString(XingBo.PX_USER_LOGIN_VERSION_CHECK, "1");
                                        editor.commit();
//                                        dialog.dismiss();
                                        break;
                                    case R.id.px_dialog_ok:
                                        String address = model.getD().getPackage_url();
                                        Intent mIntent = new Intent(HomeActivity.this, UpdateService.class);
                                        mIntent.putExtra("URL", address);
                                        mIntent.putExtra("NEWEST_VERCODE", serverVersion);
                                        Log.d("tag", "serverVersion-->2.0" + serverVersion);
                                        startService(mIntent);
                                        Message msg = handler.obtainMessage();
                                        msg.what = 1;
                                        handler.sendMessageDelayed(msg, 2000);
//                                            installApk(new File(APP_DOWNLOAD_PATH, "XingboTV_" + serverVersion + ".apk"));
                                        break;
                                }
                            }
                        }).show();
                    } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
                        XingBoUtil.dialogCheck(HomeActivity.this, "立即更新", "稍后更新", R.color.pink, R.color.c333333, "版本更新", "星播TV" + model.getD().getPackage_desc(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.px_dialog_cancel:
                                        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString(XingBo.PX_USER_LOGIN_VERSION_CHECK, "1");
                                        editor.commit();
                                        break;
                                    case R.id.px_dialog_ok:
                                        String address = model.getD().getPackage_url();
                                        Intent mIntent = new Intent(HomeActivity.this, UpdateService.class);
                                        mIntent.putExtra("URL", address);
                                        mIntent.putExtra("NEWEST_VERCODE", serverVersion);
                                        startService(mIntent);
                                        Message msg = handler.obtainMessage();
                                        msg.what = 1;
                                        handler.sendMessageDelayed(msg, 2000);
                                        break;
                                }
                            }
                        }).show();

                    }

                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    handler.sendMessageDelayed(msg, 2000);
                }
            }
        });
    }

    public View getTitleLayout() {
        return mContextViewmContextView.findViewById(R.id.rl_title_parent);
    }

    /**
     * 自动安装
     *
     * @param file
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent);
    }


    /**
     * 获取当前程序的版本号
     *
     * @return
     * @throws Exception
     */
    private String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionName;
    }

    /**
     * 隐藏所有显示的界面
     */
//    private void hideFragments(FragmentTransaction transaction) {
//        if (homeFrag != null) {
//            transaction.hide(homeFrag);
//        }
//        if (liveListFrag != null) {
//            transaction.hide(liveListFrag);
//        }
//
//    }
    @Override
    protected void onStart() {
//        .i(TAG, "HomeActivity onStart");
        super.onStart();
//        if (QavsdkControl.getInstance().getAVContext() == null) {//retry
//            InitBusinessHelper.initApp(getApplicationContext());
//            SxbLog.i(TAG, "HomeActivity retry login");
//            mLoginHelper = new LoginHelper(this);
//            mLoginHelper.imLogin(MySelfInfo.getInstance().getId(), MySelfInfo.getInstance().getUserSig());
//        }
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_content, null);
        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageResource(mImageViewArray[index]);
        TextView name = (TextView) view.findViewById(R.id.tab_text);
        name.setText(mTextviewArray[index]);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //点击两次退出应用重写Activity中onKeyDown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //写一个退出方法，名称就是onKeyDown中的exit（）
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            SystemApp.getInstance().exit();
            System.exit(0);
        }
    }
    @Subscribe
    public void loginOut(LoginOutEvent loginOutEvent){
        finish();
    }


//    @Override
//    public void updateProfileInfo(TIMUserProfile profile) {
//        SxbLog.i(TAG, "updateProfileInfo");
//        if (null != profile) {
//            MySelfInfo.getInstance().setAvatar(profile.getFaceUrl());
//            if (!TextUtils.isEmpty(profile.getNickName())) {
//                MySelfInfo.getInstance().setNickName(profile.getNickName());
//            } else {
//                MySelfInfo.getInstance().setNickName(profile.getIdentifier());
//            }
//        }
//    }

//    @Override
//    public void updateUserInfo(int reqid, List<TIMUserProfile> profiles) {
//    }
}
