package com.xingbo.live.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.PersistentCookieStore;
import com.xingbo.live.R;
import com.xingbo.live.entity.model.UpdateInfoModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.service.UpdateService;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.api.PXAndroidJsInterface;
import com.xingbobase.api.PXAndroidJsSDK17Interface;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * Created by WuJinZhou on 2016/2/25.
 */
public class AboutWeb extends BaseAct implements View.OnClickListener {
    public final static String TAG = "AboutWeb";
    public final static String WEB_CACHE_DIRNAME = "web.cache";
    private WebView webView;
    private FrescoImageView frescoImageView;
    private TextView versionCode, verService, verControl;
    private String currentVersion;
    private String serverVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_web);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        initView();
        webView = (WebView) findViewById(R.id.about_web_view);
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);// 设置webView可以与js交互
        initJs();
        synCookies(this, HttpConfig.FILE_SERVER);
    }

    private void initView() {
        frescoImageView = (FrescoImageView) findViewById(R.id.xingbo_icon);
        frescoImageView.setImageURI(Uri.parse("res:///" + R.drawable.about_icon));
        versionCode = (TextView) findViewById(R.id.version_code);
        try {
            String versionName = getVersionName();
            versionCode.setText(versionName + "V");
        } catch (Exception e) {

        }

        verService = (TextView) findViewById(R.id.xingbo_service_arrgreement);
        verService.setOnClickListener(this);
        verControl = (TextView) findViewById(R.id.xingbo_control_regulations);
        verControl.setOnClickListener(this);
        findViewById(R.id.xingbo_version_check).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.xingbo_service_arrgreement:
                Intent userIntent = new Intent(this, UserProtocolAct.class);
                startActivity(userIntent);
                break;
            case R.id.xingbo_control_regulations:
                Intent anchorIntent = new Intent(this, AnchorProtocolAct.class);
                startActivity(anchorIntent);
                break;
            case R.id.xingbo_version_check:
                upAppDate();
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
                //  alert(msg + "-->" + "updateinfo");
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
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                Log.d("tag", "serverVersion-->" + serverVersion);
                //版本号不同提示用户更新
                if (!currentVersion.equals(serverVersion)) {
                    //wifi状态下直接下载,完成后提示用户安装
                    //String md5 = o.getCt_RomoteAppIsUpdateResult().get_ctUserAppUpdateInfo().getStrCtAppMd5();
                    // String filePath = APP_DOWNLOAD_PATH + "XingboTV_" + serverVersion + ".apk";
                    // String localMd5 = ToolMD5.getFileMD5(new File(filePath));
                    //boolean isExsit = ToolFile.isExsit(filePath);
                    //  boolean Md5File = (md5 == null ? "" : md5).equals(localMd5);
                    // if (isExsit) {
                    XingBoUtil.dialog(AboutWeb.this, "立即更新", "稍后更新", R.color.pink, R.color.c333333, "版本更新", "星播TV" + model.getD().getPackage_desc(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.px_dialog_cancel:
                                    dialog.dismiss();
                                    break;
                                case R.id.px_dialog_ok:
                                    String address = model.getD().getPackage_url();
                                    Intent mIntent = new Intent(AboutWeb.this, UpdateService.class);
                                    mIntent.putExtra("URL", address);
                                    mIntent.putExtra("NEWEST_VERCODE", serverVersion);
                                    Log.d("tag", "serverVersion-->2.0" + serverVersion);
                                    startService(mIntent);
                                    Message msg = handler.obtainMessage();
                                    msg.what = 1;
                                    handler.sendMessageDelayed(msg, 2000);
                                    //  installApk(new File(APP_DOWNLOAD_PATH, "XingboTV_" + serverVersion + ".apk"));
                                    break;
                            }

                        }
                    }).show();
                } else {
                    XingBoUtil.tip(AboutWeb.this, "当前已是最新版本", Gravity.CENTER);

                }
            }
        });
    }

    public void initJs() {
        if (Build.VERSION.SDK_INT < 17) {
            webView.addJavascriptInterface(new PXAndroidJsInterface(this), "PXAndroid");
        } else {
            webView.addJavascriptInterface(new PXAndroidJsSDK17Interface(this), "PXAndroid");
        }
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + WEB_CACHE_DIRNAME;
        //设置数据库缓存路径
        webView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);
    }

    @TargetApi(21)
    public void synCookies(Context context, String url) {
        PersistentCookieStore cookieStore = new PersistentCookieStore(context);//取cookie
        List<Cookie> cookies = cookieStore.getCookies();

        if (cookies.size() == 0) {
            removeCookie(this);
        } else {
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            for (Cookie c : cookies) {
                XingBoUtil.log(TAG, "domain=" + c.getDomain() + "\n Comment=" + c.getComment()
                        + "\n CommentURL=" + c.getCommentURL()
                        + "\n Name=" + c.getName() + "\n ExpiryDate=" + c.getExpiryDate() + "\n Value=" + c.getValue());
                cookieManager.setCookie(url, c.getName() + "=" + c.getValue());
            }
            //cookieManager.flush();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.setAcceptThirdPartyCookies(webView, true);
            }
            CookieSyncManager.getInstance().sync();
        }
    }

    private void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + WEB_CACHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        Log.i(TAG, "delete file path=" + file.getAbsolutePath());

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
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

    @Override
    public void finish() {
        clearWebViewCache();
        super.finish();
    }
}
