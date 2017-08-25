package com.xingbo.live.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xingbo_szd on 2016/7/25.
 */
public class DrawableUtils {
    /**
     * @param urlpath 根据url获取布局背景的对象
     */
    public static Drawable getDrawable(String urlpath) {
        Drawable d = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            d = Drawable.createFromStream(in, "image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

    private static Bitmap bitmap;

    public static Bitmap getBitMBitmap(final String urlpath) {
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
//            bitmap.compress(Bitmap.CompressFormat.PNG,100,in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
