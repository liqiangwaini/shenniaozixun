package com.xingbo.live.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;

import com.xingbo.live.R;

/**
 * Created by xingbo_szd on 2016/7/20.
 */
public class HtmlUtils {

    public static void loadAssetDrawable(Context context,String html){
        /*String html = "<img src='" + R.drawable.circle + "'/>";
        Html.ImageGetter imgGetter = new Html.ImageGetter() {

            @Override
            public Drawable getDrawable(String source) {
                // TODO Auto-generated method stub
                int id = Integer.parseInt(source);
                Drawable d = context.getResources().getDrawable(id);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        };
        CharSequence charSequence = Html.fromHtml(html, imgGetter, null);*/
    }
}
