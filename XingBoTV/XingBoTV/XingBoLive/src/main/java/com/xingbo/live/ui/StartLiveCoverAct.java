package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.alibaba.livecloud.live.AlivcMediaRecorder;
import com.alibaba.livecloud.live.AlivcMediaRecorderFactory;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xingbo.live.R;
import com.xingbo.live.broadcast.InternetStateBroadcast;
import com.xingbo.live.controller.RoomController;
import com.xingbo.live.entity.LiveCover;
import com.xingbo.live.entity.RoomInfo;
import com.xingbo.live.entity.model.LiveCoverModel;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.entity.model.StartLiveModel;
import com.xingbo.live.eventbus.OnInternetChangeEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.popup.SharePop;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.view.widget.PicSelectorMenu;
import com.xingbobase.config.XingBo;
import com.xingbobase.eventbus.CropEvent;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.UploadFileResponseModel;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.XingBoUploadHandler;
import com.xingbobase.view.FrescoImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 开播封面
 */
public class StartLiveCoverAct extends BaseAct implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "StartLiveCoverAct";
    private static final int UPLOAD_FILE = 0;
    private static final int GET_START_LIVE_INFO = 1;

    private ImageView camera;
    private TextView address;
    private FrescoImageView changeCover;
    private EditText title;
    private TextView startLive;
    private TextView apply;
    private SurfaceView surfaceView;
    public AlivcMediaRecorder mMediaRecorder;

    private Surface mPreviewSurface;
    private Map<String, Object> mConfigure = new HashMap<String, Object>();
    private String uid;
    private String coverLogo;
    private View rootView;
    private RelativeLayout loadingBox;
    private ImageView loadingImage;
    private TextView uploadPro;
    private AnimationDrawable uploadAni;
    private ImageView locationIcon;
    private CheckBox shareWeichat;
    private CheckBox shareFricircle;
    private CheckBox shareQq;
    private CheckBox shareSina;
    private ImageView close;
    private LiveCover liveCoverModel;
    private RelativeLayout rlLocation;

    private boolean isFrontCamera = true;  //是否是前置摄像头
    private ImageView verticalLive;
    private ImageView horizontalLive;
    private TextView direction;
    private RelativeLayout rlDirection;
    private LinearLayout shareRg;
    private CheckBox shareQQSpace;

    @Override
    public void onCreate(Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        EventBus.getDefault().register(this);
        rootView = View.inflate(this, R.layout.start_live_cover, null);
        setContentView(rootView);
        SharedPreferences sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        uid = sp.getString(XingBo.PX_USER_LOGIN_UID, "");
        umApi = UMShareAPI.get(this);
        initView();
    }



    public void initView() {
        loadingBox = (RelativeLayout) rootView.findViewById(R.id.loading_box);
        loadingImage = (ImageView) findViewById(R.id.loading_header_view);
        uploadPro = (TextView) findViewById(R.id.header_upload_pro);
        uploadAni = (AnimationDrawable) loadingImage.getBackground();
        close = (ImageView) rootView.findViewById(R.id.start_live_cover_close);
        surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceview);
        camera = (ImageView) rootView.findViewById(R.id.start_live_cover_camera);
        rlLocation = (RelativeLayout) rootView.findViewById(R.id.start_live_cover_location_rl);
        locationIcon = (ImageView) rootView.findViewById(R.id.start_live_cover_location);
        address = (TextView) rootView.findViewById(R.id.start_live_cover_address);
        changeCover = (FrescoImageView) rootView.findViewById(R.id.start_live_cover_changecover);
        title = (EditText) rootView.findViewById(R.id.start_live_cover_title);
        startLive = (TextView) rootView.findViewById(R.id.start_live_cover_start);
        apply = (TextView) rootView.findViewById(R.id.start_live_cover_apply);
        //direction
        rlDirection = (RelativeLayout) rootView.findViewById(R.id.start_live_cover_live_directionRl);
        verticalLive = (ImageView) rootView.findViewById(R.id.start_live_cover_vertical_live);
        horizontalLive = (ImageView) rootView.findViewById(R.id.start_live_cover_horizontal_live);
        direction = (TextView) rootView.findViewById(R.id.start_live_cover_live_direction);
        //share
        shareRg = (LinearLayout) rootView.findViewById(R.id.live_cover_share);
        shareWeichat = (CheckBox) rootView.findViewById(R.id.live_cover_weichat);
        shareFricircle = (CheckBox) rootView.findViewById(R.id.live_cover_fri_circle);
        shareQq = (CheckBox) rootView.findViewById(R.id.live_cover_qq);
        shareSina = (CheckBox) rootView.findViewById(R.id.live_cover_sina);
        shareQQSpace = (CheckBox) rootView.findViewById(R.id.live_cover_qq_space);

        shareWeichat.setOnCheckedChangeListener(this);
        shareFricircle.setOnCheckedChangeListener(this);
        shareQq.setOnCheckedChangeListener(this);
        shareSina.setOnCheckedChangeListener(this);
        shareQQSpace.setOnCheckedChangeListener(this);
        rlDirection.setOnClickListener(this);
        rlLocation.setOnClickListener(this);
        close.setOnClickListener(this);
        camera.setOnClickListener(this);
        changeCover.setOnClickListener(this);
        locationIcon.setOnClickListener(this);
        address.setOnClickListener(this);
        startLive.setOnClickListener(this);
        apply.setOnClickListener(this);
        surfaceView.getHolder().addCallback(callback);
    }

    public void initCamera() {
        mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, AlivcMediaFormat.CAMERA_FACING_FRONT);
        mConfigure.put(AlivcMediaFormat.KEY_MAX_ZOOM_LEVEL, 3);
        mConfigure.put(AlivcMediaFormat.KEY_OUTPUT_RESOLUTION, AlivcMediaFormat.OUTPUT_RESOLUTION_720P);
        mConfigure.put(AlivcMediaFormat.KEY_MAX_VIDEO_BITRATE, 800000);
        mConfigure.put(AlivcMediaFormat.KEY_DISPLAY_ROTATION, AlivcMediaFormat.DISPLAY_ROTATION_0);
        mConfigure.put(AlivcMediaFormat.KEY_EXPOSURE_COMPENSATION, 20);//曝光度

        mMediaRecorder = AlivcMediaRecorderFactory.createMediaRecorder();
        mMediaRecorder.init(this);
        mMediaRecorder.prepare(mConfigure, mPreviewSurface);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        Log.e(TAG,"requestCode:"+requestCode);
//        Log.e(TAG,"permissions:"+permissions.length);
//        Log.e(TAG,"grantResults:"+grantResults.length);
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    private PicSelectorMenu picSelector;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_live_cover_live_directionRl:
                if (direction.getText().toString().trim().equals(getResources().getString(R.string.vertical_live))) {
                    direction.setText(getResources().getString(R.string.horitontal_Live));
                    verticalLive.setVisibility(View.INVISIBLE);
                    horizontalLive.setVisibility(View.VISIBLE);
                } else {
                    direction.setText(getResources().getString(R.string.vertical_live));
                    verticalLive.setVisibility(View.VISIBLE);
                    horizontalLive.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.start_live_cover_close:
                finish();
                break;
            case R.id.start_live_cover_camera:
                isFrontCamera = !isFrontCamera;
                mMediaRecorder.switchCamera();
                break;
            case R.id.start_live_cover_changecover:
                if (picSelector == null) {
                    picSelector = new PicSelectorMenu(StartLiveCoverAct.this, 0);
                }
                picSelector.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.start_live_cover_start:
                /*if (TextUtils.isEmpty(title.getText().toString())) {
                    alert("直播主题不能为空");
                    return;
                }*/
                if (TextUtils.isEmpty(posterLogo) && TextUtils.isEmpty(coverUrl)) {
                    alert("直播LOGO不能为空");
                    return;
                }
                if (platform != null) {
                    share(platform);
                } else {
                    startLive();
                }
                break;
            case R.id.start_live_cover_apply:
                PackageManager packageManager = getPackageManager();
                int permission = packageManager.checkPermission("android.permission.CAMERA", getPackageName());
                if (PackageManager.PERMISSION_GRANTED == permission) {
                    //有这个权限
                    Intent applyAnchorIntent = new Intent(this, ApplyTobeAnchorAct.class);
                    startActivity(applyAnchorIntent);
                } else {
                    //没有这个权限
                    alert("当前相机权限没有打开，请前往设置-页面进行设置");
                }
                break;
            case R.id.start_live_cover_location_rl:
            case R.id.start_live_cover_location:
            case R.id.start_live_cover_address:
                locationIcon.setImageResource((address.getVisibility() == View.VISIBLE) ? R.mipmap.live_cover_location_closed : R.mipmap.live_cover_location);
                address.setVisibility((address.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE);
                break;
            case R.id.live_cover_weichat:
                break;
            case R.id.live_cover_fri_circle:
                break;
            case R.id.live_cover_qq:
                break;
            default:
                break;
        }
    }

    private UMShareAPI umApi;

    public void reset() {
        shareWeichat.setEnabled(true);
        shareFricircle.setEnabled(true);
        shareQq.setEnabled(true);
        shareSina.setEnabled(true);
    }

    private SHARE_MEDIA platform;

    public void share(SHARE_MEDIA platform/*, int mediaType*/) {
       /* if (TextUtils.isEmpty(title.getText().toString().trim())) {
            alert("直播标题为空");
            return;
        }*/
        if (TextUtils.isEmpty(livename)) {
            alert("获取直播信息为空");
            return;
        }
        if (TextUtils.isEmpty(posterLogo) && TextUtils.isEmpty(coverUrl)) {
            alert("封面照为空");
            return;
        }
        if (roomInfo == null || roomInfo.getShare() == null) {
            return;
        }
        Config.dialog=loading;
        ShareAction shareAction = new ShareAction(this).setPlatform(platform)
                .withTitle(title.getText().toString().trim())
                .withText(roomInfo.getShare().getDesc())
                .withTargetUrl(roomInfo.getShare().getHref());
        if (platform != SHARE_MEDIA.SINA) {
            shareAction.withMedia(new UMImage(this, (TextUtils.isEmpty(coverUrl) ? HttpConfig.FILE_SERVER + posterLogo : HttpConfig.FILE_SERVER + coverUrl))); //  "http://dev.umeng.com/images/tab2_1.png"
        } else {
            shareAction.withMedia(new UMImage(this, HttpConfig.FILE_SERVER + roomInfo.getShare().getSite_logo()));
        }
        if (!umApi.isInstall(this, platform)) {
            if (platform == SHARE_MEDIA.QQ) {
                alert("QQ程序未安装");
                return;
            } else if (platform == SHARE_MEDIA.WEIXIN) {
                alert("微信程序未安装");
                return;
            } else if (platform == SHARE_MEDIA.SINA) {
                alert("微博未安装");
                return;
            }
        }
        shareAction.setCallback(umShareListener);
        shareAction.share();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA share_media) {
            CommonUtil.log(TAG, "分享成功");
            Toast.makeText(StartLiveCoverAct.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
            startLive();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            CommonUtil.log(TAG, "分享失败" + throwable.getMessage());
            Toast.makeText(StartLiveCoverAct.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            startLive();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(StartLiveCoverAct.this, platform + " 取消分享", Toast.LENGTH_SHORT).show();
            CommonUtil.log(TAG, "取消分享");
            startLive();
        }
    };

    @Subscribe
    public void uploadFile(CropEvent event) {
        if (event.getPath() == null) {
            return;
        }
        if (event.getSourceTagCode() == 0) {
            coverLogo = event.getPath();
            Message message = Message.obtain();
            message.what = UPLOAD_FILE;
            message.obj = coverLogo;
            handler.sendMessage(message);
        }
    }


    @Override
    public void handleMsg(Message message) {
        switch (message.what) {
            case UPLOAD_FILE:
                uploadCover((String) message.obj);
                break;
            case SharePop.RESET_BTN:
                reset();
                break;
            case GET_START_LIVE_INFO:
                if (liveCoverModel != null) {
                    String livemood = liveCoverModel.getLivemood();
                    if (!TextUtils.isEmpty(livemood)) {
                        title.setText(livemood);
                    }
                    livename = liveCoverModel.getLivename();  //直播房间号
                    posterLogo = liveCoverModel.getPosterlogo();  //封面图片
                    if (!TextUtils.isEmpty(posterLogo)) {
                        changeCover.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + posterLogo));
                    }
                }
                break;
        }
    }

    //开始直播
    public void startLive() {
        requestStart();
        //API_APP_GET_LIVE_URL
        String location = (address.getVisibility() == View.VISIBLE) ? address.getText().toString().trim() : "";
        RequestParam param = RequestParam.builder(this);
        param.put("posterlogo", (TextUtils.isEmpty(coverUrl) ? posterLogo : coverUrl));
        param.put("livemood", title.getText().toString());
        param.put("location", location);
        param.put("livestream", "alcloud");  //阿里云
        CommonUtil.request(this, HttpConfig.API_APP_GET_LIVE_URL, param, TAG, new XingBoResponseHandler<StartLiveModel>(this, StartLiveModel.class) {
            @Override

            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
//                mPreviewSurface = null;
//                mMediaRecorder.stopRecord();
//                mMediaRecorder.reset();
                String upUrl = model.getD().getUpUrl();
                finish();
                PackageManager packageManager = getPackageManager();
                int permission = packageManager.checkPermission("android.permission.CAMERA", getPackageName());
                if (PackageManager.PERMISSION_GRANTED == permission) {
                    //有这个权限
                    callback.surfaceDestroyed(surfaceView.getHolder());
                    Intent liveIntent = new Intent(StartLiveCoverAct.this, AnchorLiveAct.class);
                    liveIntent.putExtra(AnchorLiveAct.LIVE_UP_URL, upUrl);
                    liveIntent.putExtra(AnchorLiveAct.LIVE_IS_FRONT_CAMERA, isFrontCamera);
                    if (roomInfo == null) {
                        alert("获取房间信息失败");
                        return;
                    }
                    liveIntent.putExtra(AnchorLiveAct.LIVE_ANCHOR_INFO, roomInfo);
                    startActivity(liveIntent);
                } else {
                    //没有这个权限
                    alert("当前相机权限没有打开，请前往设置-页面进行设置");
                }
            }
        });
    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return super.checkPermission(permission, pid, uid);
    }

    private String livename;
    private String posterLogo;

    //获取直播封面信息
    public void liveCoverMsg() {
        RoomController.checkNet(this, this);
        requestStart();
        RequestParam param = RequestParam.builder(this);
        CommonUtil.request(this, HttpConfig.API_APP_GET_LIVE_COVER_MSG, param, TAG, new XingBoResponseHandler<LiveCoverModel>(this, LiveCoverModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                alert("获取封面信息失败");
            }

            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
                liveCoverModel = model.getD();
                handler.sendEmptyMessage(GET_START_LIVE_INFO);
            }
        });
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            holder.setKeepScreenOn(true);
            mPreviewSurface = holder.getSurface();
            mMediaRecorder.prepare(mConfigure, mPreviewSurface);
            if ((int) mConfigure.get(AlivcMediaFormat.KEY_CAMERA_FACING) == AlivcMediaFormat.CAMERA_FACING_FRONT) {
                mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
            }
            checkPermission();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mMediaRecorder.setPreviewSize(width, height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mPreviewSurface = null;
            mMediaRecorder.stopRecord();
            mMediaRecorder.reset();
        }
    };



/*    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        mPreviewSurface = null;
//        mMediaRecorder.stopRecord();
//        mMediaRecorder.reset();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        liveCoverMsg();
        getLocationCity(StartLiveCoverAct.this);
        initCamera();
        getRoomInfo();
    }

    /*
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            switch (requestCode) {
                case RESULT_OK:
                    alert("RESULT_OK");
                    break;
                case RESULT_CANCELED:
                    alert("RESULT_CANCELED");
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }*/
    public void checkPermission() {
        PackageManager packageManager = getPackageManager();
        int permission = packageManager.checkPermission("android.permission.CAMERA", getPackageName());
        if (PackageManager.PERMISSION_GRANTED == permission) {
            //有这个权限

        } else {
            //没有这个权限
            alert("当前相机权限没有打开，请前往设置-页面进行设置");
        }
    }

    private String currentLocation;

    //定位当前城市
    public void getLocationCity(final Context context) {
        final String city = "";
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);    //高精度
        criteria.setAltitudeRequired(false);    //不要求海拔
        criteria.setBearingRequired(false);    //不要求方位
        criteria.setCostAllowed(false);    //不允许有话费
        criteria.setPowerRequirement(Criteria.POWER_LOW);    //低功耗
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("onLocationChanged", location.getProvider());
                double latitude = 0;
                double longitude = 0;
                if (location != null) {
                    latitude = location.getLatitude(); // 经度
                    longitude = location.getLongitude(); // 纬度
                }
//                String latLongString = "纬度:" + latitude + "\n经度:" + longitude;
                Geocoder geocoder = new Geocoder(context);
                try {
                    List<Address> addList = geocoder.getFromLocation(latitude, longitude, 1);  //37.73, 112.57  太原市
                    if (addList != null && addList.size() > 0) {
                        for (int i = 0; i < addList.size(); i++) {
//                            Address ad = addList.get(i);
//                            latLongString += "\n";
//                            latLongString += ad.getCountryName() + ";" + ad.getLocality();
                            currentLocation = addList.get(i).getLocality();
                            address.setText(addList.get(i).getLocality());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.e("onStatusChanged", s);
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.e("onProviderEnabled", s);
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.e("onProviderDisabled", s);
            }
        });
    }

    private RoomInfo roomInfo;

    //请求进入直播房间，准备跳转到房间
    public void getRoomInfo() {
        RequestParam param = RequestParam.builder(this);
        param.put("rid", uid);
        param.put("device", "android");
        CommonUtil.request(this, HttpConfig.API_ENTER_ROOM, param, TAG,
                new XingBoResponseHandler<RoomModel>(this, RoomModel.class) {
                    @Override
                    public void phpXiuErr(int errCode, String msg) {
                        alert(msg);
                    }

                    @Override
                    public void phpXiuSuccess(String response) {
                        roomInfo = model.getD();
                    }
                });
    }


    /**
     * 上传封面
     */
    public void uploadCover(final String path) {
        requestStart();
        if (loadingBox.getVisibility() == View.GONE) {
            loadingBox.setVisibility(View.VISIBLE);
            uploadAni.start();
        }
        CommonUtil.uploadFile(this, path, new XingBoUploadHandler<UploadFileResponseModel>(UploadFileResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                alert("上传图片失败");
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                    uploadAni.stop();
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                    uploadAni.stop();
                }
                coverUrl = model.getUrl();
                changeCover.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + coverUrl));
            }

            @Override
            public void phpXiuProgress(long bytesWritten, long totalSize) {
                uploadPro.setText(((int) (bytesWritten / totalSize) * 100) + "%");
            }
        });
    }

    private String coverUrl = "";

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe  //网络状态变化
    public void netChanged(OnInternetChangeEvent onInternetChangeEvent) {
        switch (onInternetChangeEvent.getNetName()) {
            case InternetStateBroadcast.NET_MOBILE:
                Toast.makeText(this, "当前网络已切换为手机移动网络", Toast.LENGTH_SHORT).show();
                break;
            case InternetStateBroadcast.NET_NO:
                Toast.makeText(this, "当前网络已断开", Toast.LENGTH_SHORT).show();
                break;
            case InternetStateBroadcast.NET_WIFI:
                Toast.makeText(this, "已链接wifi", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private int preCheckId = -1;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.live_cover_weichat:
                if (isChecked) {
                    platform = SHARE_MEDIA.WEIXIN;
                    shareFricircle.setOnCheckedChangeListener(null);
                    shareQq.setOnCheckedChangeListener(null);
                    shareQQSpace.setOnCheckedChangeListener(null);
                    shareSina.setOnCheckedChangeListener(null);
                    shareFricircle.setChecked(false);
                    shareQq.setChecked(false);
                    shareQQSpace.setChecked(false);
                    shareSina.setChecked(false);
                    shareFricircle.setOnCheckedChangeListener(this);
                    shareQq.setOnCheckedChangeListener(this);
                    shareQQSpace.setOnCheckedChangeListener(this);
                    shareSina.setOnCheckedChangeListener(this);
                } else {
                    platform = null;
                }
                break;
            case R.id.live_cover_fri_circle:
                if (isChecked) {
                    platform = SHARE_MEDIA.WEIXIN_CIRCLE;
                    shareWeichat.setOnCheckedChangeListener(null);
                    shareQq.setOnCheckedChangeListener(null);
                    shareQQSpace.setOnCheckedChangeListener(null);
                    shareSina.setOnCheckedChangeListener(null);
                    shareWeichat.setChecked(false);
                    shareQq.setChecked(false);
                    shareQQSpace.setChecked(false);
                    shareSina.setChecked(false);
                    shareWeichat.setOnCheckedChangeListener(this);
                    shareQq.setOnCheckedChangeListener(this);
                    shareQQSpace.setOnCheckedChangeListener(this);
                    shareSina.setOnCheckedChangeListener(this);
                } else {
                    platform = null;
                }
                break;
            case R.id.live_cover_qq:
                if (isChecked) {
                    platform = SHARE_MEDIA.QQ;
                    shareFricircle.setOnCheckedChangeListener(null);
                    shareWeichat.setOnCheckedChangeListener(null);
                    shareQQSpace.setOnCheckedChangeListener(null);
                    shareSina.setOnCheckedChangeListener(null);
                    shareFricircle.setChecked(false);
                    shareWeichat.setChecked(false);
                    shareQQSpace.setChecked(false);
                    shareSina.setChecked(false);
                    shareFricircle.setOnCheckedChangeListener(this);
                    shareWeichat.setOnCheckedChangeListener(this);
                    shareQQSpace.setOnCheckedChangeListener(this);
                    shareSina.setOnCheckedChangeListener(this);
                } else {
                    platform = null;
                }
                break;
            case R.id.live_cover_qq_space:
                if (isChecked) {
                    platform = SHARE_MEDIA.QZONE;
                    shareFricircle.setOnCheckedChangeListener(null);
                    shareQq.setOnCheckedChangeListener(null);
                    shareWeichat.setOnCheckedChangeListener(null);
                    shareSina.setOnCheckedChangeListener(null);
                    shareFricircle.setChecked(false);
                    shareQq.setChecked(false);
                    shareWeichat.setChecked(false);
                    shareSina.setChecked(false);
                    shareFricircle.setOnCheckedChangeListener(this);
                    shareQq.setOnCheckedChangeListener(this);
                    shareWeichat.setOnCheckedChangeListener(this);
                    shareSina.setOnCheckedChangeListener(this);
                } else {
                    platform = null;
                }
                break;
            case R.id.live_cover_sina:
                if (isChecked) {
                    platform = SHARE_MEDIA.SINA;
                    shareFricircle.setOnCheckedChangeListener(null);
                    shareQq.setOnCheckedChangeListener(null);
                    shareQQSpace.setOnCheckedChangeListener(null);
                    shareWeichat.setOnCheckedChangeListener(null);
                    shareFricircle.setChecked(false);
                    shareQq.setChecked(false);
                    shareQQSpace.setChecked(false);
                    shareWeichat.setChecked(false);
                    shareFricircle.setOnCheckedChangeListener(this);
                    shareQq.setOnCheckedChangeListener(this);
                    shareQQSpace.setOnCheckedChangeListener(this);
                    shareWeichat.setOnCheckedChangeListener(this);
                } else {
                    platform = null;
                }
                break;

        }
    }
}
