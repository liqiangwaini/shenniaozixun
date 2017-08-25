package com.xingbo.live.emotion;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.xingbo.live.SystemApp;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WuJinZhou on 2016/1/22.
 */
public class EmotionWithSizeManager {

    public static final Pattern PATTERN = Pattern.compile(Emotions.EMOTICON_REGEX);

    private static Map<String, SoftReference<Bitmap>> bitmapMap = new HashMap<>();
    private static Map<String, EmotionEntity> emotionMap = new HashMap<>();

    public static void registerEmotion(EmotionEntity emotionEntity) {
        emotionMap.put(emotionEntity.getCode(), emotionEntity);
    }

    public static CharSequence parseCharSequence(SpannableStringBuilder text, int start, int end,float textSize) {
        CharSequence charSequence = text.subSequence(start, end);
        Matcher matcher = PATTERN.matcher(charSequence);
        while (matcher.find()) {
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            String group = matcher.group();
            EmotionEntity emotionEntity = emotionMap.get(group);
            if (emotionEntity != null) {
                text.setSpan(getImageSpan(emotionEntity,textSize), start + matcherStart, start + matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return text;
    }

    public static ImageSpan getImageSpan(EmotionEntity emotionEntity,float textSize) {
        Bitmap bitmap = getBitmap(emotionEntity.getSource(),textSize);
        return new ImageSpan(SystemApp.getInstance().getApplicationContext(), bitmap);
    }

    public static Bitmap getBitmap(String source,float textSize) {
        SoftReference<Bitmap> bitmapSoftReference = bitmapMap.get(source);
        if (bitmapSoftReference != null && bitmapSoftReference.get() != null) {
            return bitmapSoftReference.get();
        }
        AssetManager mngr = SystemApp.getInstance().getApplicationContext().getAssets();
        InputStream in = null;
        try {
            in = mngr.open(source);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap temp = BitmapFactory.decodeStream(in, null, null);
        int width = temp.getWidth();
        int height = temp.getHeight();
        float newWidth = textSize*1.5f;

        float newHeight = textSize*1.5f;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        Bitmap resizedBitmap = Bitmap.createBitmap(temp, 0, 0, width,
                height, matrix, true);
        bitmapSoftReference = new SoftReference<Bitmap>(resizedBitmap);
        bitmapMap.put(source, bitmapSoftReference);
        return resizedBitmap;
    }
}

