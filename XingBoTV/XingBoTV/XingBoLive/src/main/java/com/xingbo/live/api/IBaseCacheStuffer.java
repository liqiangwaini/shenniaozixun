package com.xingbo.live.api;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;

/**
 * Created by WuJinZhou on 2016/1/22.
 */
public class IBaseCacheStuffer extends BaseCacheStuffer.Proxy {
    public Map<String,WeakReference<BitmapDrawable>>drawables=new HashMap<String, WeakReference<BitmapDrawable>>();

    public IBaseCacheStuffer() {

    }
    @Override
    public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {

    }

    @Override
    public void releaseResource(BaseDanmaku danmaku) {

    }


}
