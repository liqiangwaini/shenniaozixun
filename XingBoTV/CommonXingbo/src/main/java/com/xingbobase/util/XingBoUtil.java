package com.xingbobase.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.xingbobase.R;
import com.xingbobase.config.XingBo;
import com.xingbobase.view.widget.XingBoAlert;
import com.xingbobase.view.widget.XingBoDialog;
import com.xingbobase.view.widget.XingBoDialogCheck;
import com.xingbobase.view.widget.XingBoLoadingDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PHPXiu工具类
 * <p/>
 * Created by WuJinZhou on 2015/8/8.
 */
public class XingBoUtil {
    public final static boolean DEBUG = true; //测试的时候 打开log 进行测试用
//        public final static boolean DEBUG = false; // 正式包log  关闭log false
    private final static SimpleDateFormat FORMAT_Y_M_D = new SimpleDateFormat("yyyy-MM-dd");

    //异步请求线程池对象
    protected static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * json解析器
     */
    public static Gson gson = new Gson();


    /**
     * 设计连接和读取超时时间
     */
    static {
        client.setTimeout(20000);
        client.setResponseTimeout(20000);
    }


    /**
     * 日志输出
     */
    public static void log(String TAG, String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }

    /**
     * 取得bitmap图片
     */
  /*  public static Bitmap bitmapFromUri(String uri) {
        return ImageLoader.getInstance().loadImageSync(uri);
    }*/

    /**
     * 显示进度动画
     *
     * @param context :Context 传入当前调用该方法的activity实例
     */
    public static XingBoLoadingDialog showDoing(Context context, String tag, DialogInterface.OnCancelListener cancelListener) {
        AnimationDrawable aniDraw;
        XingBoLoadingDialog loading = new XingBoLoadingDialog(context, R.style.loading);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.xingbo_loading_dialog, null);
        ImageView loadView = (ImageView) view.findViewById(R.id.loadingImageView);
        aniDraw = (AnimationDrawable) loadView.getBackground();
        aniDraw.start();
        loading.addContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        loading.setTag(tag);
        loading.setCancelable(true);// 点击返回键关闭
        loading.setCanceledOnTouchOutside(false);// 点击外部不关闭
        loading.setOnCancelListener(cancelListener);
        return loading;
    }

    /**
     * 提示框
     */
    public static Toast tip(Context context, String msg, int gravity) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.drawable.tip_bg);
        toast.setGravity(gravity, 0, 0);
        toast.show();
        return toast;
    }


    /**
     * 提示警告弹框
     *
     * @param msg 提示消息
     */
    public static XingBoAlert alert(Context context, String msg) {
        return XingBoAlert.builder(context, R.style.PXAlert).setView(R.layout.xingbo_alert).setMsg(msg);
    }

    /**
     * 提示警告弹框,带监听
     *
     * @param msg 提示消息
     */
    public static XingBoAlert alert(Context context, String msg, View.OnClickListener listener) {
        return XingBoAlert.builder(context, R.style.PXAlert).setView(R.layout.xingbo_alert).setMsg(msg).addOnclickListener(listener);
    }

    /**
     * 默认只有[确定]和[取消]的交互对话框
     */
    public static XingBoDialog dialog(Context context, String title, String description, View.OnClickListener listener) {
        return XingBoDialog.builder(context, R.style.PXDialog)
                .isCanceledOnTouchOutside(false)
                .setView(R.layout.xingbo_dialog)
                .setTitle(title)
                .setDescription(description)
                .addListener(listener);
    }

    /**
     * 可设定[确定]和[取消]内容及颜色的交互对话框
     */
    public static XingBoDialog dialog(Context context, String ok, String cancel, int okColorId, int cancelColorId, String title, String description, View.OnClickListener listener) {
        return XingBoDialog.builder(context, R.style.PXDialog)
                .isCanceledOnTouchOutside(false)
                .setView(R.layout.xingbo_dialog)
                .setOKText(ok)
                .setCancelText(cancel)
                .okBtnTxtColor(okColorId)
                .cancelBtnTxtColor(cancelColorId)
                .addListener(listener)
                .setTitle(title)
                .setDescription(description);
    }
    /**
     * 可设定[确定]和[取消]内容及颜色的交互对话框  cancel按钮也有点击事件监听
     */

    public  static XingBoDialogCheck dialogCheck(Context context, String ok, String cancel, int okColorId, int cancelColorId, String title, String description, View.OnClickListener listener){
        return XingBoDialogCheck.builder(context, R.style.PXDialog)
                .isCanceledOnTouchOutside(false)
                .setView(R.layout.xingbo_dialog)
                .setOKText(ok)
                .setCancelText(cancel)
                .okBtnTxtColor(okColorId)
                .cancelBtnTxtColor(cancelColorId)
                .addListener(listener)
                .addCannelListener(listener)
                .setTitle(title)
                .setDescription(description);
    }


    /**
     * 取消某个已标志的请求
     */
    public static void cancelRequest(String tag) {
        client.cancelRequestsByTAG(tag, true);
    }


    /**
     * 手机号码规范验证
     */
    public static boolean isMobileNo(String phoneNum) {
        String telRegex = "[1][358]\\d{9}";
        Pattern p = Pattern
                .compile(telRegex);
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }

    /**
     * MD5加密码处理
     */
    public static String Str2MD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    /**
     * 取得当前时间年月日格式的日期字串
     */
    public static String getCurrentDateYMD() {
        return dateFormatYMD(new Date());
    }


    /**
     * 取得某日期年月日格式的日期字串
     */
    public static String dateFormatYMD(Date date) {
        return FORMAT_Y_M_D.format(date);
    }


    /**************************************************尺寸换算及取值**************************************/

    /**
     * 获得屏幕px单位高度
     */
    public static final float getHeightInPx(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    /**
     * 获得屏幕px单位宽度
     */
    public static final float getWidthInPx(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    /**
     * 获得屏幕dp单位高度
     */
    public static final int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int heightInDp = px2dip(context, height);
        return heightInDp;
    }

    /**
     * 获得屏幕dp单位宽度
     */
    public static final int getWidthInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        int widthInDp = px2dip(context, height);
        return widthInDp;
    }

    /**
     * dp转px值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转sp值
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px值
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * dp转px值
     */
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    /**
     * sp转px值
     */
    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * Gets the resolution,
     *
     * @return a pair to return the width and height
     */
    public static Pair<Integer, Integer> getResolution(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getRealResolution(ctx);
        } else {
            return getRealResolutionOnOldDevice(ctx);
        }
    }

    /**
     * Gets resolution on old devices.
     * Tries the reflection to get the real resolution first.
     * Fall back to getDisplayMetrics if the above method failed.
     */
    private static Pair<Integer, Integer> getRealResolutionOnOldDevice(Context ctx) {
        try {
            WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Method mGetRawWidth = Display.class.getMethod("getRawWidth");
            Method mGetRawHeight = Display.class.getMethod("getRawHeight");
            Integer realWidth = (Integer) mGetRawWidth.invoke(display);
            Integer realHeight = (Integer) mGetRawHeight.invoke(display);
            return new Pair<Integer, Integer>(realWidth, realHeight);
        } catch (Exception e) {
            DisplayMetrics disp = ctx.getResources().getDisplayMetrics();
            return new Pair<Integer, Integer>(disp.widthPixels, disp.heightPixels);
        }
    }

    /**
     * Gets real resolution via the new getRealMetrics API.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Pair<Integer, Integer> getRealResolution(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        return new Pair<Integer, Integer>(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * 获取手机状态栏高度,默认为38px
     */
    public static int getStateBarHeight(Activity activity) {
        int h = 0;
        try {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            if (frame.top != 0) {
                h = frame.top;
            } else {
                Class c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                int y = activity.getResources().getDimensionPixelSize(x);
                h = y;
            }
            if (h == 0) {
                h = 38;
            }
            return h;
        } catch (Exception e) {
            e.getMessage();
        }
        return 38;
    }


    /************************************************
     * 路径获取
     ******************************************************/

    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /****************************************************************
     * 图片高斯模糊
     *************************************************************/

    public static Bitmap doBlur(Bitmap sentBitmap, int radius,
                                boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    /**************************************检测SD卡信息**********************************************************************************************/
    /**
     * 检测SD卡是否存在
     */
    public static boolean hasSD() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测SD卡空余空间是否足够
     */
    public static boolean enoughSD(int size) {
        if (getSDFreeSize() < size) {
            return false;
        }
        return true;
    }

    /**
     * 检测SD卡空余空间(MB)
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }


    /*****************************************************************
     * 缓存文件大小计算
     ************************************************************************/

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }


    /*****************************************文件、缓存、清理、删除******************************************************/

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据
     */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 清除各功能界面信息缓存
     */
    public static void clearCache(Context context, String[] files) {
        for (int i = 0; i < files.length; i++) {
            cleanCustomCache(context.getCacheDir() + File.separator + files[i]);
        }
    }


    /*******************************************************
     * 星座计算
     ******************************************************************************************/

    public enum Constellation {
        Capricorn(1, "摩羯座"), Aquarius(2, "水瓶座"), Pisces(3, "双鱼座"), Aries(4,
                "白羊座"), Taurus(5, "金牛座"), Gemini(6, "双子座"), Cancer(7, "巨蟹座"), Leo(
                8, "狮子座"), Virgo(9, "处女座"), Libra(10, "天秤座"), Scorpio(11, "天蝎座"), Sagittarius(
                12, "射手座");

        private Constellation(int code, String chineseName) {
            this.code = code;
            this.chineseName = chineseName;
        }

        private int code;
        private String chineseName;

        public int getCode() {
            return this.code;
        }

        public String getChineseName() {
            return this.chineseName;
        }
    }

    public static final Constellation[] constellationArr = {
            Constellation.Aquarius, Constellation.Pisces, Constellation.Aries,
            Constellation.Taurus, Constellation.Gemini, Constellation.Cancer,
            Constellation.Leo, Constellation.Virgo, Constellation.Libra,
            Constellation.Scorpio, Constellation.Sagittarius,
            Constellation.Capricorn
    };

    public static final int[] constellationEdgeDay = {21, 20, 21, 21, 22, 22,
            23, 24, 24, 24, 23, 22};

    public static String calculateConstellation(String birthday) {
        if (birthday == null || birthday.trim().length() == 0)
            throw new IllegalArgumentException("the birthday can not be null");
        String[] birthdayElements = birthday.split("-");
        if (birthdayElements.length != 3)
            throw new IllegalArgumentException(
                    "the birthday form is not invalid");
        int month = Integer.parseInt(birthdayElements[1]);
        int day = Integer.parseInt(birthdayElements[2]);
        if (month == 0 || day == 0 || month > 12)
            return "";
        month = day < constellationEdgeDay[month - 1] ? month - 1 : month;
        return month > 0 ? constellationArr[month - 1].getChineseName() : constellationArr[11].getChineseName();
    }


}
