package com.xingbo.live.api;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;


import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;

/**
 *
 * 绘制背景(自定义弹幕样式)
 *
 * Created by WuJinZhou on 2016/1/22.
 */
public class BackgroundCacheStuffer extends SpannedCacheStuffer {
    // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
    final Paint paint = new Paint();

    @Override
    public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
        danmaku.padding = 10;  // 在背景绘制模式下增加padding
        super.measure(danmaku, paint, fromWorkerThread);
    }

    @Override
    public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
        paint.setColor(Color.parseColor("#71FF582B"));//橙色透明背景
        RectF rectF=new RectF(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2);
        canvas.drawRect(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2, paint);
        //canvas.drawRoundRect(rectF, 4, 4, paint);
    }

    @Override
    public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
        // 禁用描边绘制
    }
}