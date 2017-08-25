package com.xingbo.live.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.alibaba.livecloud.live.AlivcMediaRecorder;
import com.alibaba.livecloud.live.AlivcMediaRecorderFactory;
import com.alibaba.livecloud.live.AlivcStatusCode;
import com.alibaba.livecloud.live.OnLiveRecordErrorListener;
import com.alibaba.livecloud.live.OnNetworkStatusListener;
import com.alibaba.livecloud.live.OnRecordStatusListener;
import com.duanqu.qupai.android.camera.CaptureRequest;
import com.google.gson.Gson;
import com.umeng.socialize.UMShareAPI;
import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.adapter.RoomMessageAdapter;
import com.xingbo.live.adapter.VisitorAdapter;
import com.xingbo.live.broadcast.InternetStateBroadcast;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.controller.AdminControl;
import com.xingbo.live.controller.DanmuControl;
import com.xingbo.live.controller.RoomController;
import com.xingbo.live.controller.PrivateMsgController;
import com.xingbo.live.controller.PrivateMsgDetaiController;
import com.xingbo.live.dialog.ManageAdminDialog;
import com.xingbo.live.dialog.UserInfoDialog;
import com.xingbo.live.entity.CloseLive;
import com.xingbo.live.entity.Danmu;
import com.xingbo.live.entity.GiftShow;
import com.xingbo.live.entity.RoomInfo;
import com.xingbo.live.entity.RoomMessage;
import com.xingbo.live.entity.msg.AnchorLvUpMsg;
import com.xingbo.live.entity.msg.BaseMsg;
import com.xingbo.live.entity.msg.CommonMsg;
import com.xingbo.live.entity.msg.DanmuMsg;
import com.xingbo.live.entity.msg.GiftMsg;
import com.xingbo.live.entity.msg.GuardMsg;
import com.xingbo.live.entity.msg.JoinMsg;
import com.xingbo.live.entity.msg.LVMsg;
import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.entity.msg.OnlineMsg;
import com.xingbo.live.entity.msg.PriMsgDetailMsg;
import com.xingbo.live.entity.msg.ScrollGiftMsg;
import com.xingbo.live.entity.msg.SystemMsg;
import com.xingbo.live.entity.msg.SystemTypeMsg;
import com.xingbo.live.entity.msg.UserRichUpMsg;
import com.xingbo.live.eventbus.ManagerEmptyEvent;
import com.xingbo.live.eventbus.ManagerEvent;
import com.xingbo.live.eventbus.NotifycationEvent;
import com.xingbo.live.eventbus.OnInternetChangeEvent;
import com.xingbo.live.eventbus.PriMessageEvent;
import com.xingbo.live.eventbus.UserInfoEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.popup.SharePop;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.view.GetMessageSocket;
import com.xingbo.live.view.ScrollGift;
import com.xingbo.live.view.ShowGift;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by xingbo_szd on 2016/8/16.
 */
public class AnchorLiveAct extends BaseAct implements GetMessageSocket.OnMessageCallback, View.OnClickListener, PrivateMsgController.OnPriMessageItemClickListener,
        PrivateMsgDetaiController.OnClickCallback, VisitorAdapter.VisitorClickCallback, RoomController.MainRoomControllerListener, OnLiveRecordErrorListener,
        OnNetworkStatusListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private static final String TAG = "AnchorLiveAct";
    public static final String LIVE_UP_URL = "live_up_url";
    public static final String LIVE_ANCHOR_INFO = "live_anchor_info";
    public static final String LIVE_IS_FRONT_CAMERA = "live_is_front_camera";

    private AlivcMediaRecorder mMediaRecorder;
    private Map<String, Object> mConfigure = new HashMap<>();
    private List<MsgFUser> fUserList = new ArrayList<>();
    private List<RoomMessage> msgList = new ArrayList<RoomMessage>();
    private List<MsgFUser> adminList = new ArrayList<>();
    private boolean isBeauty = true;
    private boolean isFlash = false;
    private String upUrl;
    private String uid;
    private RoomInfo roomInfo;
    private GetMessageSocket getMessageSocket;
    private Gson gson;
    private int startAnchorExp;
    private VisitorAdapter visitorAdapter;
    private RoomMessageAdapter mAdapter;
    private int currentPriCount;
    private Timer timer;
    private OnlineMsg onlineMsg;
    private boolean isContains;
    private MsgFUser msgFUser;
    private ScrollGiftMsg scrollGiftMsg;
    private ScrollGift scrollGift;
    private TranslateAnimation animation;
    private boolean isScrolling = false;
    //controller
    private PrivateMsgDetaiController privateMsgDetaiController;
    private RoomController controller;
    private DanmuControl danmuController;
    //view
    private SurfaceView surfaceView;
    private ListView messageListView;
    private RecyclerView recyclerView;
    private FrescoImageView avator;
    private TextView coin;
    private ImageView cancel;
    private ImageView more;
    private ImageView share;
    private ImageView priMsg;
    private RelativeLayout includeMore;
    private TextView switchCamera;
    private RelativeLayout beauty;
    private RelativeLayout flash;
    private TextView admin;
    private View moreBlank;
    private View rootView;
    private RelativeLayout priMsgRl;
    private PrivateMsgController privateMsgController;
    private ImageView beautyIcon;
    private ImageView flashIcon;
    private ShowGift showGift;
    private DanmakuView mDanmakuView;
    private RelativeLayout priMsgDetail;
    private TextView nick;
    private TextView fanNum;
    private RelativeLayout rlMore;
    private View priMsgDetailBlank;
    private View priMsgBlank;
    private FrameLayout priMsgDetailBack;
    private TextView priCount;
    private RelativeLayout includeCloseLive;
    private TextView visitorsClose;
    private TextView profitsClose;
    private TextView backClode;
    private RelativeLayout liveAnchorInfo;
    private SpannableStringBuilder spannableStringBuilder;
    private BeatTask beatTask;
    private AdminControl adminControl;
    private ManageAdminDialog manageAdminDialog;
    private View blackBack;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.anchor_live, null);
        setRootView(rootView);
        setContentView(rootView);
        EventBus.getDefault().register(this);
        upUrl = getIntent().getStringExtra(LIVE_UP_URL);
        roomInfo = (RoomInfo) getIntent().getSerializableExtra(LIVE_ANCHOR_INFO);
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(XingBo.PX_USER_SETTING_IS_ANCHORLIVE, "1");
        editor.commit();
        requestStart();
        initView();
        initData();
        initClick();
        initCamera();
        initSocket();
        getGreenMesg();
        controller.getMessageCount(this);
    }

    public void initClick() {
        //pri
        priMsgDetailBack.setOnClickListener(this);
        priMsgDetailBlank.setOnClickListener(this);
        priMsgBlank.setOnClickListener(this);
        avator.setOnClickListener(this);
        more.setOnClickListener(this);
        share.setOnClickListener(this);
        priMsg.setOnClickListener(this);
        switchCamera.setOnClickListener(this);
        beauty.setOnClickListener(this);
        flash.setOnClickListener(this);
        admin.setOnClickListener(this);
        moreBlank.setOnClickListener(this);
        cancel.setOnClickListener(this);
        coin.setOnClickListener(this);
        visitorAdapter.setVisitorsClickLietener(this);
        messageListView.setOnScrollListener(this);
        messageListView.setOnItemClickListener(this);
        //pri
        privateMsgController.setOnPriMessageItemClickListener(this);
        privateMsgDetaiController.setBackClick(this);
        //close
        backClode.setOnClickListener(this);
    }

    public void initData() {
        //controller
        controller = new RoomController();
        controller.setMainroomControllerCallback(this);
        privateMsgController = new PrivateMsgController(this, rootView);
        privateMsgController.init();
        privateMsgDetaiController = new PrivateMsgDetaiController(this, rootView);
        privateMsgDetaiController.initView();
        //message listview
        messageListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (!isScrolling) {
                            isScrolling = true;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        handler.sendEmptyMessageDelayed(XingBoConfig.REFRESH_SCROLL_STATE, 2000);
                        break;
                }
                return false;
            }
        });

        timer = new Timer();
        beatTask = new BeatTask();
        timer.schedule(beatTask, 0, 1000 * 60);
        SharedPreferences sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        uid = sp.getString(XingBo.PX_USER_LOGIN_UID, "");
        gson = new Gson();
        startAnchorExp = roomInfo.getAnchor().getAnchorexp();
        coin.setText("经验值:" + roomInfo.getAnchor().getAnchorexp());
        if (roomInfo != null && roomInfo.getAnchor() != null && roomInfo.getAnchor().getAvatar() != null) {
            avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + roomInfo.getAnchor().getAvatar()));
        }
        nick.setText(roomInfo.getAnchor().getNick());
        fanNum.setText(roomInfo.getAnchor().getLiveonlines());
        visitorAdapter = new VisitorAdapter(this, fUserList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(visitorAdapter);
        mAdapter = new RoomMessageAdapter(this, msgList);
        messageListView.setAdapter(mAdapter);
        danmuController = new DanmuControl(this);
        danmuController.setDanmakuView(mDanmakuView);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:  //滑动停止
                handler.sendEmptyMessageDelayed(XingBoConfig.REFRESH_SCROLL_STATE, 2000);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        com.umeng.socialize.utils.Log.d("result", "onActivityResult");
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
    }

    class BeatTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    heartBeat();
                }
            });
        }
    }

    public void initView() {
        surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceview_live);
        surfaceView.setDrawingCacheEnabled(true);
        blackBack = rootView.findViewById(R.id.live_anchor_black_back);
        //info
        liveAnchorInfo = (RelativeLayout) rootView.findViewById(R.id.live_anchor_info);
        avator = (FrescoImageView) rootView.findViewById(R.id.live_anchor_avator);
        nick = (TextView) rootView.findViewById(R.id.live_anchor_nick);
        fanNum = (TextView) rootView.findViewById(R.id.live_anchor_fannum);
        coin = (TextView) rootView.findViewById(R.id.live_anchor_coin);
        cancel = (ImageView) rootView.findViewById(R.id.live_anchor_cancel);
        more = (ImageView) rootView.findViewById(R.id.live_anchor_more);
        share = (ImageView) rootView.findViewById(R.id.live_anchor_share);
        priCount = (TextView) rootView.findViewById(R.id.live_anchor_pri_count);
        priMsg = (ImageView) rootView.findViewById(R.id.live_anchor_pri);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.live_anchor_visitors);
        messageListView = (ListView) rootView.findViewById(R.id.live_anchor_message_list);
        showGift = (ShowGift) rootView.findViewById(R.id.live_anchor_gift);
        mDanmakuView = (DanmakuView) rootView.findViewById(R.id.live_anchor_danmu);
        //scroll gift
        scrollGift = (ScrollGift) rootView.findViewById(R.id.live_anchor_scroll_gift);
        //pri
        priMsgRl = (RelativeLayout) rootView.findViewById(R.id.live_anchor_pri_message_list);
        priMsgDetail = (RelativeLayout) rootView.findViewById(R.id.live_anchor_pri_message_detail);
        priMsgDetailBack = (FrameLayout) rootView.findViewById(R.id.pri_msg_detail_back);
        priMsgBlank = rootView.findViewById(R.id.blank_pri_msg_live_anchor);
        priMsgDetailBlank = rootView.findViewById(R.id.blank_pri_msg_detail_live_anchor);
        //more
        rlMore = (RelativeLayout) rootView.findViewById(R.id.ll_live_anchor_more);
        moreBlank = findViewById(R.id.live_anchor_more_blank);
        includeMore = (RelativeLayout) rootView.findViewById(R.id.include_live_anchor_more);
        switchCamera = (TextView) rootView.findViewById(R.id.live_anchor_switch_camera);
        beauty = (RelativeLayout) rootView.findViewById(R.id.live_anchor_beauty);
        flash = (RelativeLayout) rootView.findViewById(R.id.live_anchor_flash);
        beautyIcon = (ImageView) rootView.findViewById(R.id.live_anchor_beauty_icon);
        flashIcon = (ImageView) rootView.findViewById(R.id.live_anchor_flash_icon);
        admin = (TextView) rootView.findViewById(R.id.live_anchor_admin);
        //close live
        includeCloseLive = (RelativeLayout) rootView.findViewById(R.id.include_close_live);
        visitorsClose = (TextView) rootView.findViewById(R.id.close_live_visitors);
        profitsClose = (TextView) rootView.findViewById(R.id.close_live_profits);
        backClode = (TextView) rootView.findViewById(R.id.close_live_back);
    }

    public void initSocket() {
        getMessageSocket = new GetMessageSocket(roomInfo.getNotify());
        getMessageSocket.setOnSocketCallback(this);
        getMessageSocket.checkWebSocketState();
    }

    public void initCamera() {
        surfaceView.getHolder().addCallback(_CameraSurfaceCallback);
        mMediaRecorder = AlivcMediaRecorderFactory.createMediaRecorder();
        mMediaRecorder.init(this);
        mMediaRecorder.setOnRecordStatusListener(mRecordStatusListener);
        mMediaRecorder.setOnRecordErrorListener(this);
        mMediaRecorder.setOnNetworkStatusListener(this);
        if (getIntent().getBooleanExtra(LIVE_IS_FRONT_CAMERA, true)) {
            mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, AlivcMediaFormat.CAMERA_FACING_FRONT);
        } else {
            mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, AlivcMediaFormat.CAMERA_FACING_BACK);
        }
        mConfigure.put(AlivcMediaFormat.KEY_MAX_ZOOM_LEVEL, 3);
        if (SystemApp.screenWidth < 1080) {
            mConfigure.put(AlivcMediaFormat.KEY_OUTPUT_RESOLUTION, AlivcMediaFormat.OUTPUT_RESOLUTION_720P);
        } else {
            mConfigure.put(AlivcMediaFormat.KEY_OUTPUT_RESOLUTION, AlivcMediaFormat.OUTPUT_RESOLUTION_1080P);
        }
        mConfigure.put(AlivcMediaFormat.KEY_MAX_VIDEO_BITRATE, 800000);
        mConfigure.put(AlivcMediaFormat.KEY_DISPLAY_ROTATION, AlivcMediaFormat.DISPLAY_ROTATION_0);
        mConfigure.put(AlivcMediaFormat.KEY_EXPOSURE_COMPENSATION, 20);//曝光度
    }

    public void getGreenMesg() {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < roomInfo.getGreen_msg_list().size(); i++) {
                    msgList.add(new RoomMessage(RoomMessage.GRREN_MESSAGE, roomInfo.getGreen_msg_list().get(i)));
                }
                handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
            }
        }.start();
    }

    private Surface mPreviewSurface;

    private final SurfaceHolder.Callback _CameraSurfaceCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            requestFinish();
            holder.setKeepScreenOn(true);
            if (holder != null && holder.getSurface() != null) {
                mPreviewSurface = holder.getSurface();
                mMediaRecorder.prepare(mConfigure, mPreviewSurface);
                mMediaRecorder.setZoom(9 / 16, new CaptureRequest.OnCaptureRequestResultListener() {
                    @Override
                    public void onCaptureResult(CaptureRequest captureRequest) {
                        Log.e(TAG, "onCaptureResult");
                    }
                });
            }
            if ((int) mConfigure.get(AlivcMediaFormat.KEY_CAMERA_FACING) == AlivcMediaFormat.CAMERA_FACING_FRONT) {
                mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
            }
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

    private OnRecordStatusListener mRecordStatusListener = new OnRecordStatusListener() {
        @Override
        public void onDeviceAttach() {
        }

        @Override
        public void onDeviceAttachFailed(int facing) {
        }

        @Override
        public void onSessionAttach() {
            if (!TextUtils.isEmpty(upUrl)) {
                mMediaRecorder.startRecord(upUrl);
            }
            mMediaRecorder.focusing(0.5f, 0.5f);
        }

        @Override
        public void onSessionDetach() {
        }

        @Override
        public void onDeviceDetach() {
        }

        @Override
        public void onIllegalOutputResolution() {
            Log.d(TAG, "selected illegal output resolution");
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        /*if (mPreviewSurface != null) {
            mMediaRecorder.prepare(mConfigure, mPreviewSurface);
            Log.e(TAG, upUrl);
            mMediaRecorder.startRecord(upUrl);
        }*/

        mDanmakuView.resume();
        if (visitorAdapter == null) {
            visitorAdapter = new VisitorAdapter(this, fUserList);
        }
        recyclerView.setAdapter(visitorAdapter);
    }

    @Override
    protected void onPause() {
        mMediaRecorder.stopRecord();
        //如果要调用stopRecord和reset()方法，则stopRecord（）必须在reset之前调用，否则将会抛出IllegalStateException
        mMediaRecorder.reset();
        mDanmakuView.pause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RecordLoggerManager.closeLoggerFile();
        EventBus.getDefault().unregister(this);
        mMediaRecorder.release();
        if (mDanmakuView != null) {
            mDanmakuView.release();
            mDanmakuView = null;
        }
        timer.cancel();
        getMessageSocket = null;
        handler.removeCallbacksAndMessages(null);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(XingBo.PX_USER_SETTING_IS_ANCHORLIVE, "0");
        editor.commit();
    }

    @Override
    public void handleMsg(Message message) {
        switch (message.what) {
            case XingBoConfig.REFRESH_MESSAGE_LIST:
                mAdapter.setData(msgList);
                break;
            case XingBoConfig.REFRESH_SCROLL_LASTEST:
                mAdapter.setData(msgList);
                messageListView.setSelection(msgList.size() - 1);
                break;
            case XingBoConfig.REFRESH_ONLINES:
                fanNum.setText(onlineMsg.getData().size() + "人");
                //admin
                if (adminControl == null) {
                    adminControl = new AdminControl(onlineMsg.getData(), uid);
                }
                break;
            case XingBoConfig.REFRESH_FANS_LIST:
                visitorAdapter.setData(fUserList);
                visitorAdapter.notifyDataSetChanged();
//                recyclerView.scrollToPosition(0);
                break;
            case XingBoConfig.REFRESH_PRI_MSG_COUNT:
                PriMsgDetailMsg priMsgDetailMsg = (PriMsgDetailMsg) message.obj;
                if (priMsgDetail.getVisibility() == View.VISIBLE && privateMsgDetaiController.getSenderId().equals(priMsgDetailMsg.getData().getFuser().getId())) {
                    privateMsgDetaiController.setData(priMsgDetailMsg.getData().getFuser().getId(), priMsgDetailMsg.getData().getFuser().getNick());
                }
                if (priMsgRl.getVisibility() == View.VISIBLE) {
                    privateMsgController.refreshPriMsgList(priMsgDetailMsg);
                }
                currentPriCount++;
                priCount.setText("" + currentPriCount);
                if (priCount.getVisibility() != View.VISIBLE) {
                    priCount.setVisibility(View.VISIBLE);
                }
                break;
            case XingBoConfig.REFRESH_JOIN_ROOM:
                fanNum.setText((Integer.parseInt(fanNum.getText().toString().split("人")[0]) + 1) + "人");
                break;
            case XingBoConfig.REFRESH_LEAVE_ROOM:
                fanNum.setText((Integer.parseInt(fanNum.getText().toString().split("人")[0]) - 1) + "人");
                break;
            case XingBoConfig.CLEAR_JOINS:
                fUserList.clear();
                Log.e(TAG, "在线用户数：" + onlineMsg.getData().size());
                handler.sendEmptyMessage(XingBoConfig.REFRESH_ONLINES);
                fUserList.clear();
                new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i < onlineMsg.getData().size(); i++) {
                            if (onlineMsg.getData().get(i).getId().equals(uid)) {
                                continue;
                            }
                            if (onlineMsg.getData().get(i).getLogintype().equals("login") && !fUserList.contains(onlineMsg.getData().get(i))) {
                                fUserList.add(onlineMsg.getData().get(i));
                            } else if (onlineMsg.getData().get(i).equals("guest")) {   //游客不显示
                                continue;
                            }
                        }
                        handler.sendEmptyMessage(XingBoConfig.REFRESH_FANS_LIST);
                    }
                }.start();
                break;
            case XingBoConfig.REFRESH_SCROLL_STATE:
                isScrolling = false;
                break;
            case XingBoConfig.ON_FAILER:
                close();
                break;
            case XingBoConfig.REFRESH_ANCHOR_EXP:
                coin.setText("经验值:" + ((LVMsg) (message.obj)).getData().getLvl().getCurexp());
                break;
            default:
                break;
        }
    }

    public void show(View view, int textWidth) {
        Animator animatorX = ObjectAnimator.ofFloat(view, "translationX", SystemApp.screenWidth, -textWidth);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX);
        animatorSet.setDuration(15000);
        animatorSet.start();
        handler.sendEmptyMessageDelayed(XingBoConfig.CLEAR_SCROLL_GIFT, 15000);
    }


    @Override
    public void getSocketMessage(String msg) {
        BaseMsg baseMsg = gson.fromJson(msg, BaseMsg.class);
        Log.e(TAG, msg);
        switch (baseMsg.getType()) {
            case BaseMsg.SYSTEM_ONLINE_MSG:  //通知初始化在线列表
                onlineMsg = gson.fromJson(msg, OnlineMsg.class);
                handler.sendEmptyMessage(XingBoConfig.CLEAR_JOINS);
                break;
            case BaseMsg.COMMON_MSG:  //普通消息
                CommonMsg commonMsg = gson.fromJson(msg, CommonMsg.class);
                msgList.add(new RoomMessage(RoomMessage.COMMON_MSG, commonMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.PRIVATE_MSG:  //私聊
                Message message = Message.obtain();
                message.obj = gson.fromJson(msg, PriMsgDetailMsg.class);
                message.what = XingBoConfig.REFRESH_PRI_MSG_COUNT;
                handler.sendMessage(message);
                break;
            case BaseMsg.COMMON_MSG_GIFT:  //送礼  皇家浪子：送了1朵玫瑰
                GiftMsg giftMsg = gson.fromJson(msg, GiftMsg.class);
                showGift.sendGift(new GiftShow(giftMsg.getData(), System.currentTimeMillis()));
                msgList.add(new RoomMessage(RoomMessage.SEND_GIFT, giftMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.SYSTEM_JOIN_MSG:  //进入房间
                isContains = false;
                JoinMsg joinMsg = gson.fromJson(msg, JoinMsg.class);
                for (int i = 0; i < onlineMsg.getData().size(); i++) {
                    if (onlineMsg.getData().get(i).getId().equals(joinMsg.getData().getFuser().getId())) {
                        isContains = true;
                        break;
                    }
                }
                if (!isContains) {
                    onlineMsg.getData().add(joinMsg.getData().getFuser());
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_JOIN_ROOM);
                }
                if (joinMsg.getData().getFuser().getLogintype().equals("guest")) {//游客
                } else if (joinMsg.getData().getFuser().getLogintype().equals("login")) {
                    if (!isContains) {
                        fUserList.add(joinMsg.getData().getFuser());
                        handler.sendEmptyMessage(XingBoConfig.REFRESH_FANS_LIST);
                        msgList.add(new RoomMessage(RoomMessage.JOIN_ROOM, joinMsg));
                        if (!isScrolling) {
                            handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                        } else {
                            handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                        }
                    }
                }
                //admin
                if (adminControl == null) {
                    adminControl = new AdminControl(onlineMsg.getData(), uid);
                }
                adminControl.addAdmin(joinMsg.getData().getFuser(), uid);
                break;
            case BaseMsg.SYSTEM_LEAVE_MSG:  //离开直播间
                isContains = false;
                JoinMsg leaveMsg = gson.fromJson(msg, JoinMsg.class);
                for (int i = 0; i < onlineMsg.getData().size(); i++) {
                    if (onlineMsg.getData().get(i).getId().equals(leaveMsg.getData().getFuser().getId())) {
                        isContains = true;
                        msgFUser = onlineMsg.getData().get(i);
                        break;
                    }
                }
                if (isContains) {
                    boolean flag = onlineMsg.getData().remove(msgFUser);
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_LEAVE_ROOM);
                }
                if (leaveMsg.getData().getFuser().getLogintype().equals("guest")) {  //游客
                } else if (leaveMsg.getData().getFuser().getLogintype().equals("login")) {
                    if (isContains) {
                        boolean flag = fUserList.remove(msgFUser);
                        handler.sendEmptyMessage(XingBoConfig.REFRESH_FANS_LIST);
                      /*  msgList.add(new RoomMessage(RoomMessage.LEAVE_ROOM, leaveMsg));
                        if (!isScrolling) {
                            handler.sendEmptyMessage(REFRESH_SCROLL_LASTEST);
                        } else {
                            handler.sendEmptyMessage(REFRESH_MESSAGE_LIST);
                        }*/
                    }
                }
                //admin
                if (adminControl == null) {
                    adminControl = new AdminControl(onlineMsg.getData(), uid);
                }
                adminControl.removeAdmin(leaveMsg.getData().getFuser());
                break;
            case BaseMsg.SYSTEM_MSG_ADD_MUTE:  //禁言
                SystemTypeMsg muteMsg = gson.fromJson(msg, SystemTypeMsg.class);
                msgList.add(new RoomMessage(RoomMessage.ADD_MUTE, muteMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.SYSTEM_MSG_CANCEL_MUTE:  //取消禁言
                SystemTypeMsg cancelMuteMsg = gson.fromJson(msg, SystemTypeMsg.class);
                msgList.add(new RoomMessage(RoomMessage.CANCEL_MUTE, cancelMuteMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.SYSTEM_MSG_FOR_ADMIN:  //设置为管理员
                SystemTypeMsg addAdminMsg = gson.fromJson(msg, SystemTypeMsg.class);
                msgList.add(new RoomMessage(RoomMessage.ADD_ADMIN, addAdminMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.SYSTEM_MSG_CANCEL_ADMIN:  //取消管理员
                SystemTypeMsg cancelAdminMsg = gson.fromJson(msg, SystemTypeMsg.class);
                msgList.add(new RoomMessage(RoomMessage.CENCEL_ADMIN, cancelAdminMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.SYSTEM_MSG_MOVE_USER:
                SystemTypeMsg kickMsg = gson.fromJson(msg, SystemTypeMsg.class);
                msgList.add(new RoomMessage(RoomMessage.KICK_OUT, kickMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }

                break;
            case BaseMsg.FLY_WORD_MSG:  //弹幕
                DanmuMsg danmuMsg = gson.fromJson(msg, DanmuMsg.class);
                danmuController.addDanmu(new Danmu(Integer.parseInt("1"), danmuMsg.getData().getFuser().getAvatar(), danmuMsg.getData().getFuser().getNick(), danmuMsg.getData().getWord()));
                break;
            case BaseMsg.SYSTEM_MSG_ANCHOR_LVL: //主播经验值变化通知
                LVMsg lvMsg = gson.fromJson(msg, LVMsg.class);
                Message expMsg = Message.obtain();
                expMsg.obj = lvMsg;
                expMsg.what = XingBoConfig.REFRESH_ANCHOR_EXP;
                handler.sendMessage(expMsg);
                break;
            case BaseMsg.M_SYSTEM_NOTICE:  //系统公告
                SystemMsg systemMsg = gson.fromJson(msg, SystemMsg.class);
                msgList.add(new RoomMessage(RoomMessage.SYSTEM_MESSAGE, systemMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.SYSTEM_MSG_GUARD:  //开通守护
                GuardMsg guardMsg = gson.fromJson(msg, GuardMsg.class);
                msgList.add(new RoomMessage(RoomMessage.OPEN_GUARD, guardMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.ANCHOR_LV_UP_MSG:  //主播升级通知
                AnchorLvUpMsg anchorLvUpMsg = gson.fromJson(msg, AnchorLvUpMsg.class);
                msgList.add(new RoomMessage(RoomMessage.ANCHOR_LVL_UP, anchorLvUpMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.USER_LV_UP_MSG:  //用户升级通知
                UserRichUpMsg userRichUpMsg = gson.fromJson(msg, UserRichUpMsg.class);
                msgList.add(new RoomMessage(RoomMessage.USER_LVL_UP, userRichUpMsg));
                if (!isScrolling) {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                } else {
                    handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                }
                break;
            case BaseMsg.SCROLL_GIFT_MSG: //跑道
                scrollGiftMsg = gson.fromJson(msg, ScrollGiftMsg.class);
                scrollGift.add(scrollGiftMsg);
                break;
        }
    }

    @Subscribe
    public void addAndDeleteManager(ManagerEvent managerEvent) {
        //admin
        if (adminControl == null) {
            adminControl = new AdminControl(onlineMsg.getData(), uid);
        }
        if (managerEvent.isAdd()) {
            adminControl.addAdminById(managerEvent.getId(), uid);
        } else {
            adminControl.deleteAdminById(managerEvent.getId(), uid);
        }
    }

    @Subscribe    //管理员列表为空
    public void cancelManagerDialog(ManagerEmptyEvent managerEmptyEvent) {
        manageAdminDialog.dismiss();
        manageAdminDialog = null;
        Toast.makeText(this, "暂无管理员记录", Toast.LENGTH_SHORT).show();
    }

    //用户信息卡片
    @Subscribe
    public void showUserInfo(UserInfoEvent userInfoEvent) {
        if (userInfoDialog != null) {
            userInfoDialog.showUserInfoCard(userInfoEvent);
        }
    }
   /* @Subscribe   //回复
    public void reply(ReplyEvent replyEvent) {
        inputBarWithBoard.mInput.setText("@" + replyEvent.getNick() + " ");
        inputBarWithBoard.mInput.requestFocus();
        inputBarWithBoard.mInput.setFocusable(true);
        showInputbar();
        SoftInputUtils.showInput(this, rootView);
    }*/

    @Subscribe  //私信
    public void privateChat(PriMessageEvent priMessageEvent) {
        privateMsgDetaiController.setData(priMessageEvent.getId(), priMessageEvent.getNick());
        showPriRlDetail();
    }

    @Subscribe//通知
    public void notifycationEvent(NotifycationEvent notifycationEvent) {
        XingBoUtil.alert(this, "您正在开播，暂时无法进入其他主播房间").show();
    }

    public void showPriRl() {
        priMsgRl.startAnimation(AnimationUtils.loadAnimation(this, R.anim.popup_in));
        priMsgRl.setVisibility(View.VISIBLE);
    }

    public void hidePriRl() {
        priMsgRl.startAnimation(AnimationUtils.loadAnimation(this, R.anim.popup_out));
        priMsgRl.setVisibility(View.INVISIBLE);
    }

    public void showPriRlDetail() {
        priMsgDetail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.popup_in));
        priMsgDetail.setVisibility(View.VISIBLE);
    }

    public void hidePriRlDetail() {
        priMsgDetail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.popup_out));
        priMsgDetail.setVisibility(View.INVISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_anchor_avator:
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(true, roomInfo.getAnchor().getId(), uid);
                userInfoDialog.show();
                break;
            case R.id.live_anchor_share:
                SharePop popShare = new SharePop(AnchorLiveAct.this, UMShareAPI.get(this));
                popShare.setShareContent(roomInfo.getShare());
                popShare.setFocusable(true);
                popShare.setBackgroundDrawable(new BitmapDrawable());
                popShare.setAnimationStyle(R.style.style_popup);
                popShare.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.live_anchor_pri:
                priMsgRl.startAnimation(AnimationUtils.loadAnimation(this, R.anim.popup_in));
                priMsgRl.setVisibility(View.VISIBLE);
                break;
            case R.id.live_anchor_more:
                rlMore.setVisibility(View.VISIBLE);
                includeMore.startAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.gift_selector_show));
                break;
            case R.id.live_anchor_switch_camera:
                mMediaRecorder.switchCamera();
                break;
            case R.id.live_anchor_beauty:
                isBeauty = !isBeauty;
                if (isBeauty) {
                    beautyIcon.setImageResource(R.mipmap.live_anchor_beauty);
                    mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
                } else {
                    beautyIcon.setImageResource(R.mipmap.live_anchor_beauty_close);
                    mMediaRecorder.removeFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
                }
                break;
            case R.id.live_anchor_flash:
                isFlash = !isFlash;
                if (isFlash) {
                    flashIcon.setImageResource(R.mipmap.live_anchor_flash);
                    mMediaRecorder.addFlag(AlivcMediaFormat.FALG_FALSH_MODE_ON);
                } else {
                    flashIcon.setImageResource(R.mipmap.live_anchor_flash_close);
                    mMediaRecorder.removeFlag(AlivcMediaFormat.FALG_FALSH_MODE_ON);
                }
                break;
            case R.id.live_anchor_admin:
                rlMore.setVisibility(View.INVISIBLE);
                includeMore.setAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.gift_selector_hide));
                //admin
                if (adminControl == null) {
                    adminControl = new AdminControl(onlineMsg.getData(), uid);
                }
                if (adminControl.adminList.size() == 0) {
                    alert("暂无管理员信息");
                    return;
                }
                if (manageAdminDialog == null) {
                    manageAdminDialog = new ManageAdminDialog(this, R.style.dialog);
                }
                manageAdminDialog.setData(adminControl.adminList);
                manageAdminDialog.show();
                break;
            case R.id.live_anchor_more_blank:
                rlMore.setVisibility(View.INVISIBLE);
                includeMore.setAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.gift_selector_hide));
                break;
            case R.id.live_anchor_cancel:
                close();
                break;
            case R.id.live_anchor_coin:
                Intent guardIntent = new Intent(this, GuardAct.class);
                guardIntent.putExtra(GuardAct.ANCHOR_GUARD_AVATAR, roomInfo.getAnchor().getAvatar());
                guardIntent.putExtra(GuardAct.USER_CONTRIBUTIONS_ID, uid);
                guardIntent.putExtra(GuardAct.ANCHOR_GUARD_ID, roomInfo.getAnchor().getId());
                guardIntent.putExtra(GuardAct.ANCHOR_GUARD_NICK, roomInfo.getAnchor().getNick());
                guardIntent.putExtra(GuardAct.USER_COIN, roomInfo.getCoin());
                startActivity(guardIntent);
                break;
            case R.id.pri_msg_detail_back:
                hidePriRlDetail();
                break;
            case R.id.blank_pri_msg_live_anchor:
                controller.getMessageCount(this);
                hidePriRl();
                break;
            case R.id.blank_pri_msg_detail_live_anchor:
                controller.getMessageCount(this);
                hidePriRlDetail();
                if (priMsgRl.getVisibility() == View.VISIBLE) {
                    priMsgRl.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.close_live_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RoomMessage roomMessage = msgList.get(position);
        if (roomMessage == null) {
            return;
        }
        switch (roomMessage.getType()) {
            case RoomMessage.COMMON_MSG:
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                if (roomInfo != null && roomInfo.getAnchor() != null && roomInfo.getAnchor().getId() != null) {
                    userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getCommonMsg().getData().getFuser().getId());
                    userInfoDialog.show();
                }
                break;
            case RoomMessage.SEND_GIFT:
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getGiftMsg().getData().getFuser().getId());
                userInfoDialog.show();
                break;
            case RoomMessage.JOIN_ROOM:
            case RoomMessage.LEAVE_ROOM:
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getJoinMsg().getData().getFuser().getId());
                userInfoDialog.show();
                break;
            case RoomMessage.ADD_MUTE:  //被管理员禁言
            case RoomMessage.CANCEL_MUTE:   //取消禁言
            case RoomMessage.ADD_ADMIN:  //设置为管理员
            case RoomMessage.CENCEL_ADMIN:  //取消管理员
            case RoomMessage.KICK_OUT:  //被踢出房间
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getMuteMsg().getData().getTuser().getId());
                userInfoDialog.show();
                break;
            case RoomMessage.USER_LVL_UP:  //用户升级:
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getUserRichUpMsg().getData().getFuser().getId());
                userInfoDialog.show();
                break;
            case RoomMessage.ANCHOR_LVL_UP:  //主播升级
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getAnchorLvUpMsg().getData().getFuser().getId());
                userInfoDialog.show();
                break;
            case RoomMessage.OPEN_GUARD:
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getGuardMsg().getData().getFuser().getId());
                userInfoDialog.show();
                break;
        }
    }

    private boolean isClosed = false;  //是否已经跳转到直播结束页面

    //退出当前页面
    public void close() {
        isClosed = true;
        controller.getCloseLiveInfo(this);
        if (priMsgDetail.getVisibility() == View.VISIBLE) {
            priMsgDetail.setVisibility(View.INVISIBLE);
        }
        if (priMsgRl.getVisibility() == View.VISIBLE) {
            priMsgRl.setVisibility(View.INVISIBLE);
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        includeCloseLive.setVisibility(View.VISIBLE);
        showGift.setVisibility(View.INVISIBLE);
        mDanmakuView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        coin.setVisibility(View.INVISIBLE);
        messageListView.setVisibility(View.INVISIBLE);
        liveAnchorInfo.setVisibility(View.INVISIBLE);
        more.setVisibility(View.INVISIBLE);
        priCount.setVisibility(View.INVISIBLE);
        priMsg.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        mPreviewSurface = null;
        mMediaRecorder.stopRecord();
        mMediaRecorder.reset();
        getMessageSocket.destroy();
        timer.cancel();
        beatTask.cancel();
        beatTask = null;
        blackBack.setAlpha(0.8f);
        _CameraSurfaceCallback.surfaceDestroyed(surfaceView.getHolder());
    }

    @Override
    public void setOnItemClickListener(String nick, String uid) {
        privateMsgDetaiController.setData(uid, nick);
        showPriRlDetail();
    }

    @Override
    public void clickCallback() {
        hidePriRlDetail();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (rlMore.getVisibility() == View.VISIBLE) {
                rlMore.setVisibility(View.INVISIBLE);
            }
            if (priMsgRl.getVisibility() == View.VISIBLE) {
                hidePriRl();
            }
            if (priMsgDetail.getVisibility() == View.VISIBLE) {
                hidePriRlDetail();
            }
            if (!isClosed) {
                XingBoUtil.dialog(this, "提示", "确定关闭直播吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        close();
                    }
                }).show();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe  //网络状态变化
    public void netChanged(OnInternetChangeEvent onInternetChangeEvent) {
        switch (onInternetChangeEvent.getNetName()) {
            case InternetStateBroadcast.NET_MOBILE:
            case InternetStateBroadcast.NET_NO:
                if (!isClosed) {
                    dialog("不再直播", "继续直播", R.color.orange_FC563C, R.color.first_text_424242, "开播提示", "当前使用网络状态为移动网络，确认要继续直播吗？", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            close();
                        }
                    });
                }
//                alert("当前网络状态为手机移动网络！");
                break;
            case InternetStateBroadcast.NET_WIFI:
                Toast.makeText(this, "已链接wifi", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private UserInfoDialog userInfoDialog;


    @Override
    public void onItemClick(int position, String uid) {
        if (userInfoDialog == null) {
            userInfoDialog = new UserInfoDialog(this, R.style.dialog);
        }
        userInfoDialog.setData(false, roomInfo.getAnchor().getId(), uid);
        userInfoDialog.show();
    }

    @Override
    public void onError(int tag, String msg) {
        switch (tag) {
            case RoomController.GET_MESSAGE_COUNT:
                alert(msg);
                break;
            case RoomController.GET_CLOSE_LIVE_INFO:
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(int tag, Object modelD) {
        switch (tag) {
            case RoomController.GET_MESSAGE_COUNT:
                if (modelD instanceof String) {
                    currentPriCount = Integer.parseInt((String) modelD);
                    if (Integer.parseInt((String) modelD) > 0) {
                        priCount.setVisibility(View.VISIBLE);
                    } else {
                        priCount.setVisibility(View.INVISIBLE);
                    }
                    priCount.setText((String) modelD);
                }
                break;
            case RoomController.GET_CLOSE_LIVE_INFO:
                if (modelD instanceof CloseLive) {
                    showProfit();
                    showVisitors(((CloseLive) modelD).getWatch_num());
                }
                break;
            default:
                break;
        }
    }

    public void showProfit() {
        String text1 = "收获";
        String text2 = (Integer.parseInt(coin.getText().toString().trim().split(":")[1]) - startAnchorExp) + "";
        String text3 = "星币";
        spannableStringBuilder = new SpannableStringBuilder(text1 + text2 + text3);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), text1.length(), text1.length() + text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text1.length() + text2.length(), (text1 + text2 + text3).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        profitsClose.setText(spannableStringBuilder);
        text1 = null;
        text2 = null;
        text3 = null;
    }

    public void showVisitors(int num) {
        String text1 = num + "";
        String text2 = "人观看";
        spannableStringBuilder = new SpannableStringBuilder(text1 + text2);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), text1.length(), text1.length() + text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        visitorsClose.setText(spannableStringBuilder);
        text1 = null;
        text2 = null;
    }

    @Override
    public void onError(int errorCode) {
        switch (errorCode) {
            case AlivcStatusCode.ERROR_AUTH_FAILED:
                Toast.makeText(this, "ERROR_AUTH_FAILED", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.ERROR_SERVER_CLOSED_CONNECTION:
                Toast.makeText(this, "服务连接失败，请重新开播", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.ERORR_OUT_OF_MEMORY:
                Toast.makeText(this, "内存溢出", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.ERROR_CONNECTION_TIMEOUT:
                Toast.makeText(this, "连接超时", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.ERROR_BROKEN_PIPE:
                Toast.makeText(this, "ERROR_BROKEN_PIPE", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.ERROR_ILLEGAL_ARGUMENT:
                Toast.makeText(this, "非法操作", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.ERROR_IO:
//                Toast.makeText(this, "ERROR_IO", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.ERROR_NETWORK_UNREACHABLE:
                Toast.makeText(this, "网络无法连接", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.ERROR_OPERATION_NOT_PERMITTED:
                Toast.makeText(this, "ERROR_OPERATION_NOT_PERMITTED", Toast.LENGTH_SHORT).show();
                break;
            default:
//                Toast.makeText(this, "Live stream connection error-->" + errorCode, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    //心跳包
    public void heartBeat() {
        //updatePublishHeartbeat
        RequestParam params = RequestParam.builder(this);
        CommonUtil.request(this, HttpConfig.API_APP_BEAT_HEART, params, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (!isClosed) {
                    alert("当前网络状态极差，已无法正常直播。如需继续直播，可重新开播哦~");
                    handler.sendEmptyMessage(XingBoConfig.ON_FAILER);
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
            }
        });
    }

    @Override
    public void onNetworkBusy() {
        if (!isClosed) {
            dialog("不再直播", "继续直播", R.color.orange_FC563C, R.color.first_text_424242, "开播提示", "当前网络状态极差，已无法正常流畅直播，确认要继续直播吗？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close();
                }
            });
        }
    }

    @Override
    public void onNetworkFree() {
    }

    @Override
    public void onConnectionStatusChange(int status) {
        switch (status) {
            case AlivcStatusCode.STATUS_CONNECTION_START:
//                Toast.makeText(this, "Start live stream connection!", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.STATUS_CONNECTION_ESTABLISHED:
//                Toast.makeText(this, "Live stream connection is established!", Toast.LENGTH_SHORT).show();
                break;
            case AlivcStatusCode.STATUS_CONNECTION_CLOSED:
//                Toast.makeText(this, "Live stream connection is closed!", Toast.LENGTH_SHORT).show();
                mMediaRecorder.stopRecord();
                break;
        }
    }

    @Override
    public boolean onNetworkReconnect() {
        return true;
    }
}
