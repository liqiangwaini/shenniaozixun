package com.xingbo.live.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.xingbo.live.R;
import com.xingbo.live.ui.HomeActivity;
import com.xingbo.live.util.ToolFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateService extends Service {
    private static final int NOTIF_ID = 9527;
    private static final int MSG_UPDATE = 0;
    private static final int MSG_FINISH = 1;
    private static final int MSG_ERROR = 2;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 20000;
    public static final String APP_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + "/xingbo/XingboDownload/App/";

    private NotificationManager manager;
    private Notification notif;
    private File saveFile;
    private String NEWEST_VERCODE;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE:
//                Log.d("download", "change progress");
                    int len = (Integer) msg.obj;
                    notif.contentView.setTextViewText(R.id.tvProgress, len + "%");
                    notif.contentView.setProgressBar(R.id.pbProgress, 100, len, false);
                    manager.notify(NOTIF_ID, notif);
                    break;
                case MSG_FINISH:
                    String name = saveFile.getName();
                    if (name.endsWith(".apktemp")) {
                        saveFile.renameTo(new File(saveFile.getParent() + "/" + name.substring(0, name.indexOf(".apktemp")) + ".apk"));
                    }
                    saveFile = new File(APP_DOWNLOAD_PATH, "XingboTV_" + NEWEST_VERCODE + ".apk");
                    notif.contentView.setTextViewText(R.id.tvAppName, "");
                    notif.contentView.setTextViewText(R.id.tvProgress,"下载完成");
                    notif.contentView.setProgressBar(R.id.pbProgress, 100, 100, false);
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setDataAndType(Uri.fromFile(saveFile),
                            "application/vnd.android.package-archive");
                    notif.contentIntent = PendingIntent.getActivity(UpdateService.this, 8880,
                            installIntent,
                            PendingIntent.FLAG_ONE_SHOT);
                    notif.flags |= Notification.FLAG_AUTO_CANCEL;
                    manager.notify(NOTIF_ID, notif);
                    Toast.makeText(UpdateService.this, "下载完成", Toast.LENGTH_SHORT).show();
                    //自动安装APK
                    installApk(saveFile);
                    stopSelf();
                    break;
                case MSG_ERROR:
                    String errorMsg = (String) msg.obj;
                    if (TextUtils.isEmpty(errorMsg)) {
                        errorMsg = "网络连接错误";
                    }
                    errorMsg += "," + "文件下载失败";
                    notif.contentView.setTextViewText(R.id.tvProgress, "文件下载失败");
                    notif.flags |= Notification.FLAG_AUTO_CANCEL;
                    manager.notify(NOTIF_ID, notif);
                    Toast.makeText(UpdateService.this, errorMsg, Toast.LENGTH_SHORT).show();
                    stopSelf();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    /**
     * 自动安装
     *
     * @param file
     */
    private void installApk(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent activityIntent = new Intent(this, HomeActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif = new Notification();
        notif.icon = R.mipmap.icon;
        notif.tickerText = getString(R.string.app_name);
        // 通知栏显示所用到的布局文件
        notif.contentView = new RemoteViews(getPackageName(), R.layout.notifi_layout);
        notif.flags |= Notification.FLAG_ONGOING_EVENT;
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.contentIntent = pIntent;
        manager.notify(NOTIF_ID, notif);
        String downLoadUrl = intent.getStringExtra("URL");
        NEWEST_VERCODE = intent.getStringExtra("NEWEST_VERCODE");
        //删除旧的安装包
        if(ToolFile.isExsit(APP_DOWNLOAD_PATH)){
            ToolFile.deleteFile(new File(APP_DOWNLOAD_PATH));
        }
        saveFile = new File(APP_DOWNLOAD_PATH, "XingboTV_" + NEWEST_VERCODE + ".apktemp");
        if (TextUtils.isEmpty(downLoadUrl)) {
            Message msg = handler.obtainMessage(MSG_ERROR, "路径不存在");
            handler.sendMessage(msg);
        } else {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                new DownLoadThread(downLoadUrl).start();
            } else {
                Message msg = handler.obtainMessage(MSG_ERROR, "SD卡不存在");
                handler.sendMessage(msg);
            }
        }

//        return super.onStartCommand(intent, flags, startId);
//        return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
        return Service.START_REDELIVER_INTENT;
    }

    class DownLoadThread extends Thread {
        private final int NOTIFICATION_STEP = 5;  //没隔百分之几更新任务栏一次
        int downloadPercent = 0;                //已下载的百分比
        long downloadedSize = 0;                //已下载文件大小
        int totalSize = 0;                      //下载文件总大小
        private String donwloadUrl = "";

        public DownLoadThread(String donwloadUrl) {
            this.donwloadUrl = donwloadUrl;
        }

        @Override
        public void run() {
            HttpURLConnection httpConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;

            try {
                File f = new File(APP_DOWNLOAD_PATH);
                if (!f.exists()) {
                    f.mkdirs();
                }
                if (saveFile != null && !saveFile.exists()) {
                    saveFile.createNewFile();
                }
                if (saveFile == null || !saveFile.exists()) {
                    throw new Exception("文件保存失败");
                }
                URL url = new URL(donwloadUrl);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
                httpConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpConnection.setReadTimeout(READ_TIMEOUT);
                totalSize = httpConnection.getContentLength();
//                Log.d("download", "updateTotalSize:" + totalSize);
                if (httpConnection.getResponseCode() == 404) {
                    throw new Exception("404NotFound");
                }
                is = httpConnection.getInputStream();
                fos = new FileOutputStream(saveFile, false);
                byte buffer[] = new byte[4096];
                int readsize = 0;
                while ((readsize = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readsize);
                    downloadedSize += readsize;
//                    Log.d("download", "已下载大小:" + downloadedSize + ",下载百分比:" + downloadPercent);
                    // 为了防止频繁的通知导致应用吃紧，百分比增加设定值才通知一次
                    if ((downloadPercent == 0) || (int) (downloadedSize * 100 / totalSize) - downloadPercent > NOTIFICATION_STEP) {
                        downloadPercent += NOTIFICATION_STEP;
                        Message msg = handler.obtainMessage(MSG_UPDATE, downloadPercent);
                        handler.sendMessage(msg);
                    }
                }
                handler.sendEmptyMessage(MSG_FINISH);
            } catch (MalformedURLException e) {
                handler.sendEmptyMessage(MSG_ERROR);
                e.printStackTrace();
            } catch (IOException e) {
                handler.sendEmptyMessage(MSG_ERROR);
                e.printStackTrace();
            } catch (Exception e) {
                String errorMsg = e.getMessage();
                Message msg = handler.obtainMessage(MSG_ERROR, errorMsg);
                handler.sendMessage(msg);
                e.printStackTrace();
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.run();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
