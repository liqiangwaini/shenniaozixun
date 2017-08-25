package com.xingbo.live.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;


/**
 * 文件工具类
 */
public class ToolFile {

    private static final String TAG = ToolFile.class.getSimpleName();

    /**
     * 检查是否已挂载SD卡镜像（是否存在SD卡）
     *
     * @return
     */
    public static boolean isMountedSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            Log.w(TAG, "SDCARD is not MOUNTED !");
            return false;
        }
    }

    /**
     * 获取SD卡剩余容量（单位Byte）
     *
     * @return
     */
    public static long gainSDFreeSize() {
        if (isMountedSDCard()) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 空闲的数据块的数量
            long freeBlocks = sf.getAvailableBlocks();

            // 返回SD卡空闲大小
            return freeBlocks * blockSize; // 单位Byte
        } else {
            return 0;
        }
    }

    /**
     * 获取SD卡总容量（单位Byte）
     *
     * @return
     */
    public static long gainSDAllSize() {
        if (isMountedSDCard()) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 获取所有数据块数
            long allBlocks = sf.getBlockCount();
            // 返回SD卡大小（Byte）
            return allBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 获取可用的SD卡路径（若SD卡不没有挂载则返回""）
     *
     * @return
     */
    public static String gainSDCardPath() {
        if (isMountedSDCard()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (!sdcardDir.canWrite()) {
                Log.w(TAG, "SDCARD can not write !");
            }
            return sdcardDir.getPath();
        }
        return "";
    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param filePath 文件路径
     */
    public static String readFileByLines(String filePath) throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), System.getProperty("file.encoding")));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();

    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param filePath 文件路径
     * @param encoding 写文件编码
     */
    public static String readFileByLines(String filePath, String encoding) throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();
    }


    /**
     * 保存内容
     *
     * @param filePath 文件路径
     * @param content  保存的内容
     * @throws IOException
     */
    public static void saveToFile(String filePath, String content) throws IOException {
        saveToFile(filePath, content, System.getProperty("file.encoding"));
    }

    /**
     * 指定编码保存内容
     *
     * @param filePath 文件路径
     * @param content  保存的内容
     * @param encoding 写文件编码
     * @throws IOException
     */
    public static void saveToFile(String filePath, String content, String encoding) throws IOException {
        BufferedWriter writer = null;
        File file = new File(filePath);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), encoding));
            writer.write(content);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 追加文本
     *
     * @param content 需要追加的内容
     * @param file    待追加文件源
     * @throws IOException
     */
    public static void appendToFile(String content, File file) throws IOException {
        appendToFile(content, file, System.getProperty("file.encoding"));
    }

    /**
     * 追加文本
     *
     * @param content  需要追加的内容
     * @param file     待追加文件源
     * @param encoding 文件编码
     * @throws IOException
     */
    public static void appendToFile(String content, File file, String encoding) throws IOException {
        BufferedWriter writer = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), encoding));
            writer.write(content);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     * @throws Exception
     */
    public static Boolean isExsit(String filePath) {
        Boolean flag = false;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println("判断文件失败-->" + e.getMessage());
        }

        return flag;
    }

    /**
     * 快速读取程序应用包下的文件内容
     *
     * @param context  上下文
     * @param filename 文件名称
     * @return 文件内容
     * @throws IOException
     */
    public static String read(Context context, String filename) throws IOException {
        FileInputStream inStream = context.openFileInput(filename);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }

    /**
     * 读取指定目录文件的文件内容
     *
     * @param fileName 文件名称
     * @return 文件内容
     * @throws Exception
     */
    public static String read(String fileName) throws IOException {
        FileInputStream inStream = new FileInputStream(fileName);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }

    /***
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param fileName 文件名称
     * @param encoding 文件编码
     * @return 字符串内容
     * @throws IOException
     */
    public static String read(String fileName, String encoding)
            throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), encoding));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();
    }

    /**
     * 读取raw目录的文件内容
     *
     * @param context   内容上下文
     * @param rawFileId raw文件名id
     * @return
     */
    public static String readRawValue(Context context, int rawFileId) {
        String result = "";
        try {
            InputStream is = context.getResources().openRawResource(rawFileId);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context  内容上下文
     * @param fileName 文件名称，包含扩展名
     * @return
     */
    public static String readAssetsValue(Context context, String fileName) {
        String result = "";
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context  内容上下文
     * @param fileName 文件名称，包含扩展名
     * @return
     */
    public static List<String> readAssetsListValue(Context context, String fileName) {
        List<String> list = new ArrayList<String>();
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取SharedPreferences文件内容
     *
     * @param context       上下文
     * @param fileNameNoExt 文件名称（不用带后缀名）
     * @return
     */
    public static Map<String, ?> readShrePerface(Context context, String fileNameNoExt) {
        SharedPreferences preferences = context.getSharedPreferences(
                fileNameNoExt, Context.MODE_PRIVATE);
        return preferences.getAll();
    }

    /**
     * 写入SharedPreferences文件内容
     *
     * @param context       上下文
     * @param fileNameNoExt 文件名称（不用带后缀名）
     * @param values        需要写入的数据Map(String,Boolean,Float,Long,Integer)
     * @return
     */
    public static void writeShrePerface(Context context, String fileNameNoExt, Map<String, ?> values) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(fileNameNoExt, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            for (Iterator iterator = values.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, ?> entry = (Map.Entry<String, ?>) iterator.next();
                if (entry.getValue() instanceof String) {
                    editor.putString(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    editor.putBoolean(entry.getKey(), (Boolean) entry.getValue());
                } else if (entry.getValue() instanceof Float) {
                    editor.putFloat(entry.getKey(), (Float) entry.getValue());
                } else if (entry.getValue() instanceof Long) {
                    editor.putLong(entry.getKey(), (Long) entry.getValue());
                } else if (entry.getValue() instanceof Integer) {
                    editor.putInt(entry.getKey(), (Integer) entry.getValue());
                }
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @param content  文件内容
     */
    public static void write(Context context, String fileName, String content) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            outStream.write(content.getBytes());
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @param content  文件内容
     */
    public static void write(Context context, String fileName, byte[] content) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            outStream.write(content);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @param modeType 文件写入模式（Context.MODE_PRIVATE、Context.MODE_APPEND、Context.
     *                 MODE_WORLD_READABLE、Context.MODE_WORLD_WRITEABLE）
     * @param content  文件内容
     */
    public static void write(Context context, String fileName, byte[] content,
                             int modeType) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                    modeType);
            outStream.write(content);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定编码将内容写入目标文件
     *
     * @param target   目标文件
     * @param content  文件内容
     * @param encoding 写入文件编码
     * @throws Exception
     */
    public static void write(File target, String content, String encoding)
            throws IOException {
        BufferedWriter writer = null;
        try {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(target, false), encoding));
            writer.write(content);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

//    /**
//     * 指定目录写入文件内容
//     *
//     * @param filePath 文件路径+文件名
//     * @param content  文件内容
//     * @throws IOException
//     */
//    public static void write(String filePath, byte[] content, AppInterface appInterface)
//            throws IOException {
//        FileOutputStream fos = null;
//
//        try {
//            File file = new File(filePath);
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            fos = new FileOutputStream(file);
//            fos.write(content);
//            fos.flush();
//            appInterface.doSomething();
//        } finally {
//            if (fos != null) {
//                fos.close();
//            }
//        }
//    }

    /**
     * 写入文件
     *
     * @throws IOException
     */
    public static File write(InputStream inputStream, String filePath) throws IOException {
        OutputStream outputStream = null;
        // 在指定目录创建一个空文件并获取文件对象
        File mFile = new File(filePath);
        if (!mFile.getParentFile().exists())
            mFile.getParentFile().mkdirs();
        try {
            outputStream = new FileOutputStream(mFile);
            byte buffer[] = new byte[4 * 1024];
            int lenght = 0;
            while ((lenght = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, lenght);
            }
            outputStream.flush();
            return mFile;
        } catch (IOException e) {
            Log.e(TAG, "写入文件失败，原因：" + e.getMessage());
            throw e;
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 图片下载完成回调接口
     */
    public interface IsFinishSaveImg {
        void isFinish();

        void isError();
    }

    /**
     * 指定目录写入文件内容
     */
    public static void saveAsJPEG(Bitmap bitmap, String filePath, IsFinishSaveImg isFinishSaveImg)
            throws IOException {
        if (bitmap == null) {
//            ToolToast.showShort("保存失败");
//            Toast.makeText(ToolFile.this, "保存失败", Toast.LENGTH_SHORT).show();

            return;
        }
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            isFinishSaveImg.isFinish();
        } catch (IOException e) {
            isFinishSaveImg.isError();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 指定目录写入文件内容
     *
     * @param filePath 文件路径+文件名
     * @throws IOException
     */
    public static void saveAsPNG(Bitmap bitmap, String filePath)
            throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 序列化对象
     *
     * @param rsls     需要序列化的对象
     * @param filename 文件名
     */
    public static synchronized <T> void serializeObject(T rsls, String filename) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(rsls);
            byte[] data = baos.toByteArray();
            OutputStream os = new FileOutputStream(new File(filename));
            os.write(data);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定目录写入文件内容
     *
     * @param filePath 文件路径+文件名
     * @throws IOException
     */
    public static boolean saveBitmap(Bitmap bitmap, String filePath){
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 反序列化对象
     *
     * @param filename 文件名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T deserializeObject(String filename) {
        T obj = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (fis.available() > 0) {
                obj = (T) ois.readObject();
            }
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获取图片的存储目录，在有sd卡的情况下为 “/sdcard/apps_images/本应用包名/cach/images/”
     * 没有sd的情况下为“/data/data/本应用包名/cach/images/”
     *
     * @param context 上下文
     * @return 本地图片存储目录
     */
    private static String getPath(Context context) {
        String path = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String packageName = context.getPackageName() + "/cach/images/";
        if (hasSDCard) {
            path = "/sdcard/apps_images/" + packageName;
        } else {
            path = "/data/data/" + packageName;
        }
        File file = new File(path);
        boolean isExist = file.exists();
        if (!isExist) {

            file.mkdirs();

        }
        return file.getPath();
    }

//    /**
//     * 网络可用状态下，下载图片并保存在本地
//     *
//     * @param strUrl  图片网址
//     * @param file    本地保存的图片文件
//     * @param context 上下文
//     * @return 图片
//     */
//    private static Bitmap getNetBitmap(String strUrl, File file, Context context) {
//        Log.e(TAG, "getBitmap from net");
//        Bitmap bitmap = null;
//        if (ToolNetwork.isMobileConnected(context)) {
//            try {
//                URL url = new URL(strUrl);
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setDoInput(true);
//                con.connect();
//                InputStream in = con.getInputStream();
//                bitmap = BitmapFactory.decodeStream(in);
//                FileOutputStream out = new FileOutputStream(file.getPath());
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                out.flush();
//                out.close();
//                in.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//
//            }
//        }
//        return bitmap;
//    }

    //将SD卡文件删除
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
            // 如果它是一个目录
            else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    /**
     * 删除指定目录下,文件名以xxx结尾的文件
     *
     * @param file 文件/目录
     * @param flag 以flag结尾
     */
    public static void deleteFile(File file, String flag) {
        if (file.exists()) {
            if (file.isFile() && file.getName().endsWith(flag)) {
                file.delete();
            }
            // 如果它是一个目录
            else if (file.isDirectory()) {
                // 声明目录下所有的文件 files[];
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            if (file.getName().endsWith(flag)) {
                file.delete();
            }
        }
    }

    /**
     * 判断文件是否是指定格式的文件
     *
     * @param filePath  文件路径
     * @param assignStr 文件后缀名
     * @return
     */
    public static boolean isAssignFile(String filePath, String assignStr) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(assignStr))
            return false;
        String a = assignStr.toLowerCase();
        String aa = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase();
        return assignStr.toLowerCase().equals(filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase());
    }

    /**
     * 获取文件大小(单位:M)
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        long mbSize = 0;
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            mbSize = f.length() / 1024 / 1024;
        } else {
            mbSize = -1;
        }
        return mbSize;
    }

    private static int scale(String targetPath) {
        long size = getFileSize(targetPath);
        if (size >= 1) {
            return 60;
        } else {
            return 40;
        }
    }

    public static String compressImage(String filePath, String targetPath) {
        Bitmap bm = getSmallBitmap(filePath);
        //旋转照片角度，省略
        /*int degree = readPictureDegree(filePath);
        if(degree!=0){
            bm=rotateBitmap(bm,degree);
        }*/
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, scale(filePath), out);
        } catch (Exception e) {
        }
        return outputFile.getPath();
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 计算图片缩放值
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
