package com.xingbo.live.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xingbo_szd on 2016/8/19.
 */
public class TimeUtils {
    public static String getHourAndMins(long time){
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String text=sdf.format(new Date(time*1000));
        return text;
    }

    public static long convertTime2Int(String time){ //2016-09-07 16:29
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            long l=sdf.parse(time).getTime();
            return l;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static long lastClickTime = 0;
    private static long DIFF = 1000;
    private static int lastButtonId = -1;

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            Log.v("isFastDoubleClick", "短时间内按钮多次触发");
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }




}
