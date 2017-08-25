package com.xingbo.live.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import com.xingbobase.util.XingBoUtil;

/**
 * Created by WuJinZhou on 2016/1/30.
 */
public class ResizeImageSpan extends ImageSpan {
    private int size;
    public ResizeImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
        size= XingBoUtil.sp2px(context, 14);
    }

    @Override
    public Drawable getDrawable() {
        Drawable drawable=super.getDrawable();
        drawable.setBounds(0,0,size*2,size);
        return drawable;
    }
}
