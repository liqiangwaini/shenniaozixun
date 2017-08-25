package com.xingbo.live.ui;


import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.PhotosAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.UserPhotos;
import com.xingbo.live.entity.UserPhotosPage;
import com.xingbo.live.entity.model.UserPhotosModel;
import com.xingbo.live.eventbus.PhotoDeleteEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.view.widget.PicSelectorMoreMenu;
import com.xingbobase.extra.cropzoom.GOTOConstants;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshStickyHeaderGridView;
import com.xingbobase.extra.sticky.StickyGridHeadersGridView;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.UploadFileResponseModel;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.XingBoUploadHandler;
import com.xingbobase.view.FrescoImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.vov.vitamio.utils.Log;

/**
 * 我的相册  页面
 * Date: 2016/7/29
 */
public class UserPhotosAct extends BaseAct implements View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    public final static String TAG = "UserPhotosAct";
    public final static String EXTRA_USER_ID = "extra_user_id";
    public final static String EXTRA_USER_POP_ID = "extra_user_id";
    public final static String SOURCE_TAG_CODE_KEY = "source_tag_code_key";//发起编辑源代码索引
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.png";
    public static final int REQUEST_CODE_PICK_GALLERY = 0x1;//
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;//
    public static final int REQUEST_CODE_DELETE = 0x3;
    public final static int PHOTOS_UPLOAD_CODE = 0x510;
    public final static int PHOTOS_REFRESH_DATA = 0x511;

    private File mFileTemp;
    private Uri mFileTempUri = null;
    private int sourceTagCode = 0;
    public static String uid;
    public String popUserId;
    //用来存储 所选图片路径的集合
    private ArrayList<String> mSelectPath;
    private ArrayList<String> mServerPath = new ArrayList<>();
    private PhotosAdapter mPhotosAdapter;
    private PullToRefreshStickyHeaderGridView pullToRefreshStickyHeaderGridView;
    private TextView upLoadPhotos;
    private ImageButton imageViewBack;
    private List<UserPhotos> userPhotosList = new ArrayList<>();

    private boolean isSelf = false;
    private TextView errMsg;
    private Button errBtn;
    private RelativeLayout emptyViewBox;
    private StickyGridHeadersGridView stickyGridHeadersGridView;
    private ArrayList<UserPhotos> currentPhotos = new ArrayList<UserPhotos>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_photos_view);
        uid = getIntent().getStringExtra(EXTRA_USER_ID);
        EventBus.getDefault().register(this);
        initView();
        viewImageShow();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        sourceTagCode = intent.getIntExtra(SOURCE_TAG_CODE_KEY, -1);
        popUserId = intent.getStringExtra(EXTRA_USER_POP_ID);
        String action = intent.getStringExtra("ACTION");
        if (null != action) {
            if (action.equalsIgnoreCase(GOTOConstants.IntentExtras.ACTION_CAMERA)) {
                getIntent().removeExtra("ACTION");
                takePic();
            }
            if (action.equalsIgnoreCase(GOTOConstants.IntentExtras.ACTION_GALLERY)) {
                getIntent().removeExtra("ACTION");
                pickImage();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void photoDelete(PhotoDeleteEvent photoDeleteEvent) {
        currentPage = 1;
        viewImageShow();
    }

    /**
     * 初始化操作
     */
    private void initView() {
        pullToRefreshStickyHeaderGridView = (PullToRefreshStickyHeaderGridView) findViewById(R.id.gridview);
        stickyGridHeadersGridView = pullToRefreshStickyHeaderGridView.getRefreshableView();
        pullToRefreshStickyHeaderGridView.setOnRefreshListener(this);
        upLoadPhotos = (TextView) findViewById(R.id.user_photos_upload);
        imageViewBack = (ImageButton) findViewById(R.id.user_photos_top_back);
        imageViewBack.setOnClickListener(this);
        upLoadPhotos.setOnClickListener(this);
        mPhotosAdapter = new PhotosAdapter(UserPhotosAct.this, userPhotosList, R.layout.header, R.layout.item);
        stickyGridHeadersGridView.setAdapter(mPhotosAdapter);
        stickyGridHeadersGridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_photos_top_back:
                onBackPressed();
                break;
            case R.id.user_photos_upload:
                new PicSelectorMoreMenu(this, PHOTOS_UPLOAD_CODE).showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("restoreState", true);
    }

    /**
     * 带照相机的图片选择
     */
    private void takePic() {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, TEMP_PHOTO_FILE_NAME);

        try {
            mFileTempUri = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePicIntent.putExtra("return-data", true);
            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFileTempUri);
            startActivityForResult(takePicIntent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Can't take picture", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * pickImage
     * 获取所选图片的路径
     */
    private void pickImage() {
        int maxNum = 9;
        MultiImageSelector selector = MultiImageSelector.create(UserPhotosAct.this);
        selector.showCamera(false);
        selector.count(maxNum);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(UserPhotosAct.this, REQUEST_CODE_PICK_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mServerPath.clear();
        switch (requestCode) {
            case REQUEST_CODE_PICK_GALLERY:
                if (resultCode == RESULT_OK) {
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    for (int i = 0; i < mSelectPath.size(); i++) {
                        CommonUtil.uploadFile(this,mSelectPath.get(i), new XingBoUploadHandler<UploadFileResponseModel>(UploadFileResponseModel.class) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                //图片不存在
                                Log.e("tag", msg);
                                alert(msg);
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                Log.e("上传", response);
                                String sPath = model.getUrl().toString();
                                Log.d(TAG, "服务器返回的照片路径:" + sPath);
                                if (sPath != null && !"".equals(sPath)) {
                                    mServerPath.add(sPath);
                                    if (mSelectPath.size() == mServerPath.size()) {
                                        addImageToSever();
                                    }
                                }
                            }

                            @Override
                            public void phpXiuProgress(long bytesWritten, long totalSize) {
                                //上传的进度
//                                alert(((int) (bytesWritten / totalSize) * 100) + "%");
                            }
                        });
                    }
                }
                break;
            case REQUEST_CODE_TAKE_PICTURE://照相机拍照获取图片
                if (resultCode == RESULT_OK) {
                    try {
                        if (mFileTempUri == null) {
                            //CommonUtil.centerTip(this,"获取照片信息失败!").show();
                            return;
                        }
                        String path = getAbsoluteImagePath(mFileTempUri);
                        CommonUtil.uploadFile(this,path, new XingBoUploadHandler<UploadFileResponseModel>(UploadFileResponseModel.class) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                Log.e("tag", msg);
                                alert(msg);
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                Log.e("上传", response);
                                String sPath = model.getUrl().toString();
                                if (sPath != null && !"".equals(sPath)) {
                                    mServerPath.add(sPath);
                                    addImageToSever();
                                }
                            }

                            @Override
                            public void phpXiuProgress(long bytesWritten, long totalSize) {
                            }
                        });
                        // Toast.makeText(this, "--->" + path, Toast.LENGTH_SHORT).show();
                    } catch (OutOfMemoryError error) {
                        //CommUtil.showSysTip(this,"图片加载异常，是否自动调后重试？",true,handler,false);
                    }
                }
                break;

        }
    }


    /**
     * //根据 uri 获取文件的绝对路径
     *
     * @param uri
     * @return
     */
    protected String getAbsoluteImagePath(Uri uri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,
                proj,                 // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null);                 // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * 上传照片到服务器 并获得从服务器返回的照片地址
     */
    public void addImageToSever() {
        // 	purls：照片地址，多个可用半角逗号","隔开，例如：20160706/14677971034700.jpg,20160706/14677971024388.jpg
        Log.d("id", "添加照片");
        StringBuilder purls = new StringBuilder();
        for (int i = 0; i < mServerPath.size(); i++) {
            if (i == mServerPath.size() - 1) {
                purls.append(mServerPath.get(i));
            } else {
                purls.append(mServerPath.get(i));
                purls.append(",");
            }
        }
        RequestParam param = RequestParam.builder(this);
        param.put("uid", uid);
        param.put("purls", purls.toString());
        CommonUtil.request(this, HttpConfig.API_USER_GET_ADDPHOTOS, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                alert(model.getM());
                Log.e("添加", response);
                Log.d(TAG, model.getM());
                currentPage = 1;
                viewImageShow();
            }
        });
    }

    private int currentPage = 1;
    private int totalPhotos=0;
    /**
     * 相册列表显示
     */
    private void viewImageShow() {
        Log.d(TAG, "相册列表显示");
        RequestParam param = RequestParam.builder(this);
//        param.put("uid",id);
        param.put("page", currentPage + "");
        param.put("pagesize", 5 + "");
        CommonUtil.request(this, HttpConfig.API_USER_GET_PHOTOS, param, TAG, new XingBoResponseHandler<UserPhotosModel>(this, UserPhotosModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (pullToRefreshStickyHeaderGridView != null) {
                    pullToRefreshStickyHeaderGridView.onRefreshComplete();
                }
                if (emptyViewBox == null) {
                    showErrView();
                }
                if (emptyViewBox.getVisibility() == View.GONE) {
                    emptyViewBox.setVisibility(View.VISIBLE);
                    errMsg.setText("获取数据失败，请检测网络连接" + msg);
                } else {
                    alert(msg);
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                if (pullToRefreshStickyHeaderGridView != null) {
                    pullToRefreshStickyHeaderGridView.onRefreshComplete();
                }
                UserPhotosPage d = model.getD();
                if (currentPage == 1) {
                    userPhotosList.clear();
                }
                userPhotosList.addAll(d.getItems());
                totalPhotos = d.getTotal();
                if (currentPage%5==0&&currentPage >= d.getPagetotal()) {
                    currentPage = -1;
                } else {
                    currentPage+=5;
                }
                Log.e("列表", response);
                for (int i = 0; i < userPhotosList.size(); i++) {
                    if ("".equals(userPhotosList.get(i).getUrl()) || userPhotosList.get(i).getUrl() == null) {
                        userPhotosList.remove(i);
                    }
                }
                if (userPhotosList.size() == 0) {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    if (isSelf) {
                        errMsg.setText("你还没有上传任何照片");
                    } else {
                        errMsg.setText("你还没有上传任何照片");
                    }
                } else {
                    if (emptyViewBox != null && emptyViewBox.getVisibility() == View.VISIBLE) {
                        emptyViewBox.setVisibility(View.GONE);
                    }
                }
                handler.sendEmptyMessage(PHOTOS_REFRESH_DATA);
            }
        });
    }

    public void arragePhotos(List<UserPhotos> photoses,String time){
        currentPhotos.clear();
        for(int i=0;i<photoses.size();i++){
            if(photoses.get(i).getUptime().equals(time)){
                currentPhotos.add(photoses.get(i));
            }
        }
    }

    //点击编辑进行删除
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (userPhotosList != null) {
            arragePhotos(userPhotosList,userPhotosList.get(position).getUptime());
            int curentPosition=0;
            for(int i=0;i<currentPhotos.size();i++){
                if(currentPhotos.get(i).getId().equals(userPhotosList.get(position).getId())){
                    curentPosition=i;
                    break;
                }
            }
            Intent photoDelete = new Intent(this, PhotosDeleteAct.class);
            photoDelete.putExtra(PhotosDeleteAct.IMAGE_URL, userPhotosList.get(position).getUrl());
            photoDelete.putExtra(PhotosDeleteAct.IMAGE_POSITION, (curentPosition+1)+"");
            photoDelete.putExtra(PhotosDeleteAct.IMAGE_UPTIME, userPhotosList.get(position).getUptime());
            photoDelete.putExtra(PhotosDeleteAct.IMAGE_PHOTOS,currentPhotos);
            startActivity(photoDelete);
        }
    }

    public void showErrView() {
        ViewStub stub = (ViewStub) findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.common_empty_view_icon));
        errBtn = (Button) findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) findViewById(R.id.empty_view_box);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        currentPage = 1;
        viewImageShow();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (currentPage == -1) {
            if (pullToRefreshStickyHeaderGridView != null) {
                pullToRefreshStickyHeaderGridView.onRefreshComplete();
            }
            Toast.makeText(this, "数据已加载完毕", Toast.LENGTH_SHORT).show();
            return;
        }
        viewImageShow();
    }

    @Override
    public void handleMsg(Message message) {
        if(message.what==PHOTOS_REFRESH_DATA){
            mPhotosAdapter.upDataAdapter(userPhotosList);
            mPhotosAdapter.notifyDataSetChanged();
        }
    }
}



