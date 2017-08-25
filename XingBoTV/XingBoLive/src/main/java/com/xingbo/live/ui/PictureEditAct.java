package com.xingbo.live.ui;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbobase.eventbus.CropEvent;
import com.xingbobase.extra.cropzoom.GOTOConstants;
import com.xingbobase.extra.cropzoom.imagecropzoom.view.ImageCropView;
import com.xingbobase.util.XingBoUtil;

import java.io.File;
import java.io.FileOutputStream;

import de.greenrobot.event.EventBus;

/**
 * Created By
 */
public class PictureEditAct extends BaseAct {
    public final static String TAG = "PictureEditAct";
    public final static String SOURCE_TAG_CODE_KEY="source_tag_code_key";//发起编辑源代码索引
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.png";
    public static final int REQUEST_CODE_PICK_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    private ImageCropView imageCropView;
    private File mFileTemp;
    private Uri mFileTempUri=null;
    private int sourceTagCode=0;
    private Button crop;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sourceTagCode=getIntent().getIntExtra(SOURCE_TAG_CODE_KEY,-1);
        if(sourceTagCode==-1){
            finish();
        }
        setContentView(R.layout.picture_edit);
        imageCropView = (ImageCropView) findViewById(R.id.image);
        imageCropView.setAspectRatio(1,1);

        crop=(Button)findViewById(R.id.crop_btn);
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crop.setEnabled(false);
                if(!imageCropView.isChangingScale()) {
                    Bitmap b=null;
                    try {
                        b = imageCropView.getCroppedImage(0);
                    } catch (OutOfMemoryError e) {
                        return;
                    }
                    if(b!=null) {
                        crop.setEnabled(false);
                        bitmapConvertToFile(b);
                    }else{
                        crop.setEnabled(true);
                        //CommonUtil.centerTip(CropActivity.this,"剪切失败").show();
                    }
                }
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (savedInstanceState == null || !savedInstanceState.getBoolean("restoreState")) {

            String action = getIntent().getStringExtra("ACTION");
            if (null != action) {
                if (action.equalsIgnoreCase(GOTOConstants.IntentExtras.ACTION_CAMERA)){
                    getIntent().removeExtra("ACTION");
                    takePic();
                }
                if (action.equalsIgnoreCase(GOTOConstants.IntentExtras.ACTION_GALLERY)){
                    getIntent().removeExtra("ACTION");
                    pickImage();
                }

            }
        }
    }
    private void takePic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, TEMP_PHOTO_FILE_NAME);
        try {
            mFileTempUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra("return-data", true);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,mFileTempUri);
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Can't take picture", e);
            Toast.makeText(this, "Can't take picture", Toast.LENGTH_LONG).show();
        }
    }
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            startActivityForResult(intent, REQUEST_CODE_PICK_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No image source available", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("restoreState", true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if(resultCode==RESULT_CANCELED){
            finish();
        }
        // Uri uri = result.getData();//部份手机返回为空
        if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                try {
                    if(mFileTempUri==null){
                        //CommonUtil.centerTip(this,"获取照片信息失败!").show();
                        return;
                    }
                    imageCropView.setImageFileUri(XingBoUtil.getPath(this, mFileTempUri), 0);
                }catch (OutOfMemoryError error){
                    //CommUtil.showSysTip(this,"图片加载异常，是否自动调后重试？",true,handler,false);
                }
            } else if (resultCode == RESULT_CANCELED) {
                return;
            } else {
                return;
            }
        } else if (requestCode == REQUEST_CODE_PICK_GALLERY) {
            if (resultCode == RESULT_CANCELED) {
                return;
            } else if (resultCode == RESULT_OK) {
                mFileTempUri = result.getData();
                try {
                    imageCropView.setImageFileUri(XingBoUtil.getPath(this, mFileTempUri),0);
                } catch (Exception e) {
                    //CommUtil.showSysTip(this,"图片加载异常，是否自动调后重试？",true,handler,false);
                    return;
                }
            } else {
                return;
            }

        }
    }

    private boolean isPossibleCrop(int widthRatio, int heightRatio){
        int bitmapWidth = imageCropView.getViewBitmap().getWidth();
        int bitmapHeight = imageCropView.getViewBitmap().getHeight();
        if(bitmapWidth < widthRatio && bitmapHeight < heightRatio){
            return false;
        } else {
            return true;
        }
    }
    private void createTempFile() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
        }
    }
    public void bitmapConvertToFile(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            createTempFile();
            fileOutputStream = new FileOutputStream(mFileTemp);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            MediaScannerConnection.scanFile(this, new String[]{mFileTemp.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {

                }

                @Override
                public void onScanCompleted(final String path, Uri uri) {
                    //本地保存完成,通知发起界面接收
                    EventBus.getDefault().postSticky(new CropEvent(sourceTagCode, path));
                    finish();
                }
            });
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
