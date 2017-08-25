package com.xingbo.live.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;

import com.xingbo.live.R;
import com.xingbo.live.http.HttpConfig;

/**
 * Created by xingbo_szd on 2016/8/19.
 */
public class TextShowUtil {
    public static SpannableStringBuilder showScrollGift(Context context, String time, String fuser, String tuser, String gift, Drawable drawable) {
        String text = "  送给  ";
        String image="image";
        drawable.setBounds(0,0,DpOrSp2PxUtil.dp2pxConvertInt(context, 14),DpOrSp2PxUtil.dp2pxConvertInt(context,14));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(time + fuser + text + tuser + gift+image);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)), 0, time.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.scroll_gift_nick_color)), time.length(), time.length() + fuser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)), time.length() + fuser.length(), time.length() + fuser.length() + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.scroll_gift_nick_color)), time.length() + fuser.length() + text.length(), time.length() + fuser.length() + text.length() + tuser.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)), time.length() + fuser.length() + text.length() + tuser.length(), time.length() + fuser.length() + text.length() + tuser.length() + gift.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ImageSpan(drawable),time.length() + fuser.length() + text.length() + tuser.length() + gift.length(),time.length() + fuser.length() + text.length() + tuser.length() + gift.length()+image.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    //连送 礼物
    public static SpannableStringBuilder showContinueGiftNumber(Context context, String numberText) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(numberText);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main_room_yellow_color)), 0, numberText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(DpOrSp2PxUtil.sp2pxConvertInt(context, 17)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(DpOrSp2PxUtil.sp2pxConvertInt(context, 30)), 4, numberText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 4, numberText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    //普通 礼物
    public static SpannableStringBuilder showNormalGiftNumber(Context context, String numberText) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(numberText);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(DpOrSp2PxUtil.sp2pxConvertInt(context, 17)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(DpOrSp2PxUtil.sp2pxConvertInt(context, 30)), 1, numberText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 1, numberText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }
}
