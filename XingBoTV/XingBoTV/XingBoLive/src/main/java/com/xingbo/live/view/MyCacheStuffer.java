package com.xingbo.live.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;

import com.xingbo.live.util.DpOrSp2PxUtil;

import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;


//弹幕
public class MyCacheStuffer extends BaseCacheStuffer {
    private int AVATAR_DIAMETER;
    private int AVATAR_PADDING;
    private int TEXT_LEFT_PADDING;
    private int TEXT_RIGHT_PADDING;
    private int TEXT_SIZE;
    private int TEXT_TOP_PADDING;

    private static final int NICK_COLOR = 0xfffd4790;
    private static final int NICK_SHADOW_COLOR = 0x010101;
    private static final int TEXT_COLOR = 0xffeeeeee;
    private int LIGHT_GREY_COLOR = 0x66000000;
    private int DANMU_RADIUS;

    private Context context;

    public MyCacheStuffer(Context context) {
        this.context = context;
        AVATAR_DIAMETER = DpOrSp2PxUtil.dp2pxConvertInt(context, 33);
        AVATAR_PADDING = DpOrSp2PxUtil.dp2pxConvertInt(context, 1);
        TEXT_LEFT_PADDING = DpOrSp2PxUtil.dp2pxConvertInt(context, 2);
        TEXT_RIGHT_PADDING = DpOrSp2PxUtil.dp2pxConvertInt(context, 8);
        TEXT_SIZE = DpOrSp2PxUtil.dp2pxConvertInt(context, 13);
        DANMU_RADIUS = DpOrSp2PxUtil.dp2pxConvertInt(context, 8);
        TEXT_TOP_PADDING = DpOrSp2PxUtil.dp2pxConvertInt(context, 14);
    }

    @Override
    public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
        paint.setTextSize(TEXT_SIZE);
        float w = 0;
        for (String tempStr : danmaku.text.toString().split(":")) {
            if (tempStr.length() > 0) {
                w = Math.max(paint.measureText(tempStr), w);
            }
        }
        danmaku.paintWidth = w + AVATAR_DIAMETER + AVATAR_PADDING * 2 + TEXT_LEFT_PADDING + TEXT_RIGHT_PADDING;
        danmaku.paintHeight = AVATAR_DIAMETER + AVATAR_PADDING * 2;
    }

    @Override
    public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
    }

    @Override
    public void drawText(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, TextPaint paint, boolean fromWorkerThread) {
        String texts = danmaku.text.toString();
        String[] text = texts.split(":");
        paint.setColor(Color.WHITE);
        if (danmaku.tag != null) {
            canvas.drawCircle(AVATAR_DIAMETER / 2 + AVATAR_PADDING, AVATAR_DIAMETER / 2 + AVATAR_PADDING, AVATAR_DIAMETER / 2 + AVATAR_PADDING, paint);
            canvas.drawBitmap((Bitmap) danmaku.tag, null, new RectF(left + AVATAR_PADDING, AVATAR_PADDING, left + AVATAR_PADDING + AVATAR_DIAMETER, AVATAR_DIAMETER + AVATAR_PADDING), paint);
        }
        paint.setTextSize(TEXT_SIZE);
        paint.setColor(NICK_COLOR);
        paint.setShadowLayer(1, 1, 4, NICK_SHADOW_COLOR);
        canvas.drawText(text[0], left + AVATAR_DIAMETER + AVATAR_PADDING * 2 + TEXT_LEFT_PADDING, top - TEXT_TOP_PADDING + AVATAR_PADDING, paint);
        paint.setColor(TEXT_COLOR);
        paint.clearShadowLayer();
        canvas.drawText(text[1], left + AVATAR_DIAMETER + AVATAR_PADDING * 2 + TEXT_LEFT_PADDING, top - TEXT_TOP_PADDING + AVATAR_PADDING + AVATAR_DIAMETER / 2, paint);
    }

    @Override
    public void clearCaches() {
    }

    @Override
    public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
        String[] text = danmaku.text.toString().split(":");
        Paint paint = new Paint();
        paint.setTextSize(TEXT_SIZE);
        float textWidth = paint.measureText(text[1]);
        paint.setColor(LIGHT_GREY_COLOR);
        paint.setAntiAlias(true);
        float danmuBgWidth = AVATAR_DIAMETER / 2 + AVATAR_PADDING + TEXT_LEFT_PADDING + textWidth + TEXT_RIGHT_PADDING;
        canvas.drawRoundRect(new RectF(AVATAR_DIAMETER / 2 + AVATAR_PADDING, top + AVATAR_DIAMETER / 2 + AVATAR_PADDING, AVATAR_DIAMETER / 2 + AVATAR_PADDING + danmuBgWidth, top + AVATAR_DIAMETER + AVATAR_PADDING), DANMU_RADIUS, DANMU_RADIUS, paint);
    }
}
