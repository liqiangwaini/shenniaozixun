package com.xingbo.live.emotion;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.xingbo.live.SystemApp;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.entity.msg.MsgTUser;
import com.xingbo.live.eventbus.UserHeaderClickedEvent;
import com.xingbo.live.view.ResizeImageSpan;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.KeyClickableSpan;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

public class EmotionManager {
    public final static boolean IS_SHOW_SENDER_LV_ICON=true;//显示消息发出者等级图标
    public static final Pattern PATTERN = Pattern.compile(Emotions.EMOTICON_REGEX);

    private static Map<String, SoftReference<Bitmap>> bitmapMap = new HashMap<>();
    public static Map<String, EmotionEntity> emotionMap = new HashMap<>();

    public static void registerEmotion(EmotionEntity emotionEntity) {
        emotionMap.put(emotionEntity.getCode(), emotionEntity);
    }

    /**
     * 检测字符串中表情个数
     */
    public static int checkEmotionCount(String msg){
        int ec=0;
        Matcher matcher = PATTERN.matcher(msg);
        while (matcher.find()) {
            ec++;
            msg=msg.replace(matcher.group(),"");
        }
        ec=msg.length()+ec;
        return ec;
    }
    public static CharSequence parseNick(Context context,MsgFUser sender){
        String senderNick=sender.getNick();
        SpannableStringBuilder msgHead=new SpannableStringBuilder(senderNick);
        int skip=senderNick.length()-1;
        if(IS_SHOW_SENDER_LV_ICON){//显示发送者等级图标
            try {
                int rLv = Integer.parseInt(sender.getRichlvl());
                msgHead.append(" richLvIcon");//12个字符
                msgHead.setSpan(new ResizeImageSpan(context, XingBoConfig.RICH_LV_ICONS[rLv],ImageSpan.ALIGN_BASELINE),skip+1,skip+12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                skip=skip+12;
            }catch (NumberFormatException e){

            }

            try {
                int vLv = Integer.parseInt(sender.getViplvl());
                if(vLv>0){
                    msgHead.append(" vipLvIcon");
                    msgHead.setSpan(new ImageSpan(context,XingBoConfig.VIP_LV_RES_IDS[vLv-1],ImageSpan.ALIGN_BOTTOM),skip+1,skip+10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    skip=skip+10;
                }

            }catch (NumberFormatException e){

            }
            try {
                int gLv = Integer.parseInt(sender.getGuardlvl());
                if(gLv>0){
                    msgHead.append(" guardLvIcon ");
                    msgHead.setSpan(new ImageSpan(context,XingBoConfig.GUARD_LV_RES_IDS[gLv-1],ImageSpan.ALIGN_BOTTOM),skip+1,skip+12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }catch (NumberFormatException e){

            }
        }
        return msgHead;
    }

    public static CharSequence parseNick(Context context,MsgTUser receiver){
        String senderNick=receiver.getNick();
        SpannableStringBuilder msgHead=new SpannableStringBuilder(senderNick);
        int skip=senderNick.length()-1;
        if(IS_SHOW_SENDER_LV_ICON){//显示发送者等级图标
            try {
                int rLv = Integer.parseInt(receiver.getRichlvl());
                msgHead.append(" richLvIcon");//12个字符
                msgHead.setSpan(new ResizeImageSpan(context,XingBoConfig.RICH_LV_ICONS[rLv],ImageSpan.ALIGN_BASELINE),skip+1,skip+12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                skip=skip+12;
            }catch (NumberFormatException e){

            }

            try {
                int vLv = Integer.parseInt(receiver.getViplvl());
                if(vLv>0){
                    msgHead.append(" vipLvIcon");
                    msgHead.setSpan(new ImageSpan(context,XingBoConfig.VIP_LV_RES_IDS[vLv-1],ImageSpan.ALIGN_BOTTOM),skip+1,skip+10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    skip=skip+10;
                }

            }catch (NumberFormatException e){

            }
            try {
                int gLv = Integer.parseInt(receiver.getGuardlvl());
                if(gLv>0){
                    msgHead.append(" guardLvIcon ");
                    msgHead.setSpan(new ImageSpan(context,XingBoConfig.GUARD_LV_RES_IDS[gLv-1],ImageSpan.ALIGN_BOTTOM),skip+1,skip+12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }catch (NumberFormatException e){

            }
        }
        return msgHead;
    }

    /**
     * 解析公聊信息
     */
    public static CharSequence parseCommonMsg(Context context,MsgFUser sender,String msgBody) {
        String senderNick=sender.getNick();
        SpannableStringBuilder msgHead=new SpannableStringBuilder(senderNick);
        KeyClickableSpan senderInfo=new KeyClickableSpan(sender.getId());
        senderInfo.setListener(new KeyClickableSpan.OnKeyClickableSpanClickListener() {
            @Override
            public void onClick(String extraKey) {
                EventBus.getDefault().post(new UserHeaderClickedEvent(extraKey));
                XingBoUtil.log("KeyClickableSpan", "@@@@@@@@--------------" + extraKey);
            }
        });
        msgHead.setSpan(senderInfo, 0, senderNick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int skip=senderNick.length()-1;
        if(IS_SHOW_SENDER_LV_ICON){//显示发送者等级图标
            try {
                int rLv = Integer.parseInt(sender.getRichlvl());
                msgHead.append(" richLvIcon");//12个字符
                msgHead.setSpan(new ResizeImageSpan(context,XingBoConfig.RICH_LV_ICONS[rLv],ImageSpan.ALIGN_BASELINE),skip+1,skip+12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                skip=skip+12;
            }catch (NumberFormatException e){

            }

            try {
                int vLv = Integer.parseInt(sender.getViplvl());
                if(vLv>0){
                    msgHead.append(" vipLvIcon");
                    msgHead.setSpan(new ImageSpan(context,XingBoConfig.VIP_LV_RES_IDS[vLv-1],ImageSpan.ALIGN_BOTTOM),skip+1,skip+10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    skip=skip+10;
                }

            }catch (NumberFormatException e){

            }
            try {
                int gLv = Integer.parseInt(sender.getGuardlvl());
                if(gLv>0){
                    msgHead.append(" guardLvIcon ");
                    msgHead.setSpan(new ImageSpan(context,XingBoConfig.GUARD_LV_RES_IDS[gLv-1],ImageSpan.ALIGN_BOTTOM),skip+1,skip+12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }catch (NumberFormatException e){

            }
        }
        msgHead.append("说：");
        msgHead.append(parseCharSequence(new SpannableStringBuilder(msgBody),0,msgBody.length()));
        return msgHead;
    }

    /**
     * 解析私聊信息
     */
    public static CharSequence parseCommonMsg(Context context,MsgFUser sender,MsgTUser receiver,String msgBody) {
        String senderNick=sender.getNick();
        SpannableStringBuilder msgHead=new SpannableStringBuilder(senderNick);
        KeyClickableSpan senderInfo=new KeyClickableSpan(sender.getId());
        senderInfo.setListener(new KeyClickableSpan.OnKeyClickableSpanClickListener() {
            @Override
            public void onClick(String extraKey) {
                EventBus.getDefault().post(new UserHeaderClickedEvent(extraKey));
                XingBoUtil.log("KeyClickableSpan", "1111@@@@@@@@--------------" + extraKey);
            }
        });
        msgHead.setSpan(senderInfo, 0,senderNick.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int skip=senderNick.length()-1;
        if(IS_SHOW_SENDER_LV_ICON){//显示发送者等级图标

            try {
                int rLv = Integer.parseInt(sender.getRichlvl());
                msgHead.append(" richLvIcon");//12个字符
                msgHead.setSpan(new ResizeImageSpan(context,XingBoConfig.RICH_LV_ICONS[rLv],ImageSpan.ALIGN_BASELINE),skip+1,skip+12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                skip=skip+12;
            }catch (NumberFormatException e){

            }

            try {
                int vLv = Integer.parseInt(sender.getViplvl());
                if(vLv>0){
                    msgHead.append(" vipLvIcon");
                    msgHead.setSpan(new ImageSpan(context,XingBoConfig.VIP_LV_RES_IDS[vLv-1],ImageSpan.ALIGN_BOTTOM),skip+1,skip+10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    skip=skip+10;
                }

            }catch (NumberFormatException e){

            }
            try {
                int gLv = Integer.parseInt(sender.getGuardlvl());
                if(gLv>0){
                   msgHead.append(" guardLvIcon ");
                   msgHead.setSpan(new ImageSpan(context,XingBoConfig.GUARD_LV_RES_IDS[gLv-1],ImageSpan.ALIGN_BOTTOM),skip+1,skip+12,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                   skip=skip+13;
                }
            }catch (NumberFormatException e){

            }
        }
        String receiverNick=receiver.getNick();
        msgHead.append("对").append(receiverNick).append("说：");
        skip=skip+1;
        KeyClickableSpan receiverInfo=new KeyClickableSpan(receiver.getId());
        receiverInfo.setListener(new KeyClickableSpan.OnKeyClickableSpanClickListener() {
            @Override
            public void onClick(String extraKey) {
                EventBus.getDefault().post(new UserHeaderClickedEvent(extraKey));
                XingBoUtil.log("KeyClickableSpan", "222222@@@@@@@@--------------" + extraKey);
            }
        });
        msgHead.setSpan(receiverInfo,skip,skip+receiverNick.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        msgHead.append(parseCharSequence(new SpannableStringBuilder(msgBody),0,msgBody.length()));
        return msgHead;
    }

    public static CharSequence parseCharSequence(SpannableStringBuilder text, int start, int end) {
        CharSequence charSequence = text.subSequence(start, end);
        Matcher matcher = PATTERN.matcher(charSequence);
        while (matcher.find()) {
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();
            String group = matcher.group();
            EmotionEntity emotionEntity = emotionMap.get(group);
            if (emotionEntity != null) {
                text.setSpan(getImageSpan(emotionEntity), start + matcherStart, start + matcherEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return text;
    }

    public static ImageSpan getImageSpan(EmotionEntity emotionEntity) {
        Bitmap bitmap = getBitmap(emotionEntity.getSource());
        return new ImageSpan(SystemApp.getInstance().getApplicationContext(), bitmap);
    }

    public static Bitmap getBitmap(String source) {
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
        int newWidth = XingBoUtil.dip2px(SystemApp.getInstance().getApplicationContext(), 26);

        int newHeight = XingBoUtil.dip2px(SystemApp.getInstance().getApplicationContext(),26);

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
