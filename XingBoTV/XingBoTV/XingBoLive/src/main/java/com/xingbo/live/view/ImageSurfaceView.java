package com.xingbo.live.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;

/**
 *
 *  播放gif动画效果
 */
public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    AssetManager assetManager;
    //图片集
    private String[] PATH_KEYS=null;
    //运行状态
    public boolean mLoop=false;
    //获取画布
    private SurfaceHolder mSurfaceHolder=null;
    //图片索引
    private int mCount = 0;
    //时间间隔
    private long SPEED =300;
    private static Matrix matrix=new Matrix();

    private OnAnimSurfaceViewPlayListener listener;

    private String dir;
    public ImageSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public ImageSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);init(context);
    }

    public ImageSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);init(context);
    }

    @TargetApi(21)
    public ImageSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);init(context);
    }

    public void init(Context context){
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mLoop = true;//开始画图
        assetManager=context.getAssets();

    }

    public void play(String pathKey){
        mLoop=true;
        mCount=0;
        dir=pathKey;
        try {
            PATH_KEYS=assetManager.list(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(PATH_KEYS!=null && PATH_KEYS.length>0){
            SPEED=3000/PATH_KEYS.length;//三秒播完
            if(PATH_KEYS.length == 1){
                drawPic();
            }else{
                new Thread(this).start();
            }
        }else{
            listener.onAnimSurfaceViewEnd();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,    int height) {

    }

    //图像创建时
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    //视图销毁时
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mLoop = false;
    }

    //画图方法
    private void drawPic(){
        if(mCount >= PATH_KEYS.length){
            mLoop = false;
            if(listener!=null){
               listener.onAnimSurfaceViewEnd();
            }
            return;
        }
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if(canvas == null || mSurfaceHolder == null){
            return;
        }
        Bitmap bitmap  = null;
        try{
            String path = PATH_KEYS[mCount++];
            bitmap  = BitmapFactory.decodeStream(assetManager.open(dir+ File.separator+path));
            if(bitmap!=null){
                //画布宽和高
                int height = getHeight();
                int width  = getWidth();
                //生成合适的图像
                bitmap = getReduceBitmap(bitmap,width,height);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Style.FILL);
                //清屏
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                //画图
                canvas.drawBitmap(bitmap, matrix, paint);
            }
            //解锁显示
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }catch(Exception ex){
            Log.e("ImageSurfaceView",ex.getMessage());
            return;
        }finally{
            //资源回收
            if(bitmap!=null){
                bitmap.recycle();
            }
        }
    }

    //刷新图片
    public void run() {
        while(mLoop){
            try {
                Thread.sleep(SPEED);
            } catch (InterruptedException e) {
                Log.e("ImageSurfaceView_Thread",e.getMessage());
            }
            synchronized (mSurfaceHolder) {
                drawPic();
            }
        }
        PATH_KEYS = null;//消毁
    }

    //缩放图片
    private Bitmap getReduceBitmap(Bitmap bitmap ,int w,int h){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        Matrix matrix=new Matrix();
        float wScale=((float)w/width);
        float hScale=((float)h/height);
        matrix.postScale(wScale,hScale);
        return Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
    }

    public void setSurfaceViewPlayListener(OnAnimSurfaceViewPlayListener listener) {
        this.listener = listener;
    }

    public interface OnAnimSurfaceViewPlayListener {
        void onAnimSurfaceViewEnd();
    }

}