package com.xingbobase.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.loopj.android.http.PersistentCookieStore;
import com.xingbobase.R;
import com.xingbobase.api.PXAndroidJsInterface;
import com.xingbobase.api.PXAndroidJsSDK17Interface;
import com.xingbobase.util.XingBoUtil;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * Created by WuJinZhou on 2016/2/17.
 */
public class BaseWebAct extends Activity {
    public final static String TAG="WebAct";
    public final static String WEB_CACHE_DIRNAME="web.cache";
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_web);
        webView=(WebView)findViewById(R.id.test_web_view);
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);// 设置webView可以与js交互
        initJs();
        synCookies(this, "http://xingbobase.xingbobase.com/test/cookieinfo?platform=android&delay=1&expire=100");
        //webView.loadUrl("http://phpxiu.phpxiu.com/test/cookieinfo?platform=android&delay=1&expire=100");
        webView.loadUrl("http://xingbobase.xingbobase.com/app.php");
        findViewById(R.id.answer_js).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url="http://xingbobase.xingbobase.com/test/cookieinfo?platform=android&delay=1&expire=100";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        XingBoUtil.log(TAG, "say Hello to JS");
                        webView.loadUrl("javascript:reload('"+url+"')");
                    }
                });

            }
        });
    }

    public void initJs(){
        if(Build.VERSION.SDK_INT< 17) {
            webView.addJavascriptInterface(new PXAndroidJsInterface(this),"PXAndroid");
        }else{
            webView.addJavascriptInterface(new PXAndroidJsSDK17Interface(this),"PXAndroid");
        }
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()+WEB_CACHE_DIRNAME;
        //设置数据库缓存路径
        webView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);
    }

    @TargetApi(21)
    public void synCookies(Context context, String url) {
        PersistentCookieStore cookieStore=new PersistentCookieStore(context);//取cookie
        List<Cookie> cookies=cookieStore.getCookies();

        if(cookies.size()==0){
            removeCookie(this);
        }else{
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            for(Cookie c:cookies){
                XingBoUtil.log(TAG, "domain=" + c.getDomain() + "\n Comment=" + c.getComment()
                        + "\n CommentURL=" + c.getCommentURL()
                        + "\n Name=" + c.getName() + "\n ExpiryDate=" + c.getExpiryDate() + "\n Value=" + c.getValue());
                cookieManager.setCookie(url, c.getName()+"="+c.getValue());
            }
            //cookieManager.flush();
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
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
    public void clearWebViewCache(){

        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath()+WEB_CACHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()+"/webviewCache");
        Log.e(TAG, "webviewCacheDir path="+webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
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
}

