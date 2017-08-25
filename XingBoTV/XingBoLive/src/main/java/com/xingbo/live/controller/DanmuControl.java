package com.xingbo.live.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Spannable;

import com.xingbo.live.entity.Danmu;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.DpOrSp2PxUtil;
import com.xingbo.live.view.MyCacheStuffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.util.IOUtils;

/**
 * Created by feiyang on 16/3/2.
 */
public class DanmuControl implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String TAG = "DanmuControl";

    private float DANMU_TEXT_SIZE = 24f;
    private Context mContext;
    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;

    public DanmuControl(Context context) {
        this.mContext = context;
        setSize(context);
        initDanmuConfig();
    }

    private void setSize(Context context) {
        DANMU_TEXT_SIZE = DpOrSp2PxUtil.dp2px(context, 24);
    }

    private void initDanmuConfig() {
         HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 2);
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuContext = DanmakuContext.create();
        mDanmakuContext
                .setDanmakuStyle(IDisplayer.DANMAKU_STYLE_NONE)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(3f)
                .setScaleTextSize(1.2f)
                .setCacheStuffer(new MyCacheStuffer(mContext), mCacheStufferAdapter)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
    }

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            if (danmaku.text instanceof Spannable) {
                danmaku.text = "";
            }
        }
    };

    public void setDanmakuView(IDanmakuView danmakuView) {
        this.mDanmakuView = danmakuView;
        initDanmuView();
    }

    private void initDanmuView() {
        if (mDanmakuView != null) {
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    mDanmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                }

                @Override
                public void drawingFinished() {
                }
            });
        }

        mDanmakuView.prepare(new BaseDanmakuParser() {

            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        }, mDanmakuContext);
        mDanmakuView.enableDanmakuDrawingCache(true);
    }

    public void addDanmu(final Danmu danmu) {
        final BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        new Thread() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    String avator = HttpConfig.FILE_SERVER+danmu.avatar;//"http://g.hiphotos.baidu.com/image/h%3D200/sign=9b2f9371992397ddc9799f046983b216/dc54564e9258d1094dc90324d958ccbf6c814d7a.jpg";
                    URLConnection urlConnection = new URL(avator).openConnection();
                    inputStream = urlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if(bitmap==null){
                        return;
                    }
                    danmaku.tag = makeRoundCorner(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
                danmaku.text = danmu.nick + ":" + danmu.content;
                danmaku.padding = 0;
                danmaku.priority = 1;
                danmaku.isLive = true;
                danmaku.time = mDanmakuView.getCurrentTime() + 1000;
                danmaku.textSize = DANMU_TEXT_SIZE;
                danmaku.textColor = Color.RED;
                mDanmakuView.addDanmaku(danmaku);
            }
        }.start();
    }

    private static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
