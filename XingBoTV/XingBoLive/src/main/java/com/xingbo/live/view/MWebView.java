package com.xingbo.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by WuJinZhou on 2016/2/18.
 */
public class MWebView extends WebView {
    public MWebView(Context context) {
        super(context);
        setWebChromeClient(new WebChromeClient());
    }

    public MWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

    }


}
