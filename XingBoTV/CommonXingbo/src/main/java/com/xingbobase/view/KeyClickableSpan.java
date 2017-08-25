package com.xingbobase.view;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by WuJinZhou on 2016/1/30.
 */
public class KeyClickableSpan extends ClickableSpan {
    public String extraKey;
    private OnKeyClickableSpanClickListener listener;
    public KeyClickableSpan() {
        super();
    }
    public KeyClickableSpan(String extraKey) {
        super();
        this.extraKey=extraKey;

    }

    @Override
    public void onClick(View widget) {
        if(listener!=null){
           listener.onClick(extraKey);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.parseColor("#ffFF582B"));
        ds.setUnderlineText(false);
    }

    @Override
    public CharacterStyle getUnderlying() {
        return super.getUnderlying();
    }

    public void setListener(OnKeyClickableSpanClickListener listener) {
        this.listener = listener;
    }

    public interface OnKeyClickableSpanClickListener{
        void onClick(String extraKey);
    }
}