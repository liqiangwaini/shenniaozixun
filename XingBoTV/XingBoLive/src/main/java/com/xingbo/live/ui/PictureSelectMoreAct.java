package com.xingbo.live.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.xingbo.live.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/2
 */
public class PictureSelectMoreAct extends BaseAct {
    private final  static String TAG="PictureSelectMoreAct";
    public final static String SOURCE_TAG_CODE_KEY="source_tag_code_key";//发起编辑源代码索引
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.png";
    public static final int REQUEST_CODE_PICK_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

    private GridView gridView;
    private Button  cancel;
    private Button choose;
    private Button back;
    private Button upload;
//    private ChoosePicAdapter mAdapter;
//    int sourseTagCode=0;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.user_photos_seletor);
//        initView();
//    }
//
//    private void initView() {
//        gridView = (GridView) findViewById(R.id.gridview);
//        upload = (Button) findViewById(R.id.upload_btn);
//        back = (Button) findViewById(R.id.back_btn);
//        cancel = (Button) findViewById(R.id.cancel_btn);
//        choose = (Button) findViewById(R.id.choose_btn);
//        getThumbnail();
//        mAdapter = new ChoosePicAdapter(PictureSelectMoreAct.this,list);
//        mAdapter.setOnPicsClicked(this);
//        gridView.setOnItemClickListener(this);
//        upload.setOnClickListener(this);
//        back.setOnClickListener(this);
//        choose.setOnClickListener(this);
//        cancel.setOnClickListener(this);
//        gridView.setAdapter(mAdapter);
//    }
//
//    /**
//     * 得到缩略图，这里主要得到的是图片的ID值
//     */
//    private void getThumbnail() {
//        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
//                MediaStore.Images.Thumbnails.DATA};
//        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(getContentResolver(), MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
//                MediaStore.Images.Thumbnails.MINI_KIND, projection);
//        getThumbnailColumnData(cursor);
//        cursor.close();
//    }
//    List<ChoosePic> list=new ArrayList<>();
//    // 缩略图列表
//    HashMap<String, String> thumbnailList = new HashMap<String, String>();
//
//    /**
//     * 从数据库中得到缩略图 * @param cur
//     */
//    private void getThumbnailColumnData(Cursor cur) {
//        if (cur.moveToFirst()) {
//            int image_id;
//            String image_path;
//            int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
//            int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
//            do {
//                image_id = cur.getInt(image_idColumn);
//                image_path = cur.getString(dataColumn);
//                thumbnailList.put("" + image_id, image_path);
//                Log.d("image",image_path);
//                list.add(new ChoosePic(image_path));
//            } while (cur.moveToNext());
//        }
//    }
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
////
//    }
//
//    private int picNumber=0;
//
//    @Override
//    public void setOnPicClick(int position) {
//        if(!list.get(position).isChecked()){
//            if(picNumber==9){
//                Toast.makeText(this, "最多一次上传9张图片哦", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            picNumber++;
//        }else{
//            picNumber--;
//        }
//        list.get(position).setIsChecked(!list.get(position).isChecked());
//        mAdapter.setList(list);
//        mAdapter.notifyDataSetChanged();
//        choose.setText("已选择（" + picNumber + "/9)");
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.choose_btn:
//                break;
//            case R.id.cancel_btn:
//            case R.id.back_btn:
//                finish();
//                break;
//            case R.id.upload_btn:
//                //模拟上传图片网络请求
//                for (int i = 0; i < 9; i++) {
//                    String url = list.get(i).getUrl();
//                }
//
//                break;
//        }
//    }
}
