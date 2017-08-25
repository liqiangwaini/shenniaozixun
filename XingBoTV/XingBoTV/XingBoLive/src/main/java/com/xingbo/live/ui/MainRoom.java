package com.xingbo.live.ui;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.socialize.UMShareAPI;
import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.adapter.RoomMessageAdapter;
import com.xingbo.live.adapter.VisitorAdapter;
import com.xingbo.live.broadcast.InternetStateBroadcast;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.controller.DanmuControl;
import com.xingbo.live.controller.GiftPan;
import com.xingbo.live.controller.RoomController;
import com.xingbo.live.controller.PrivateMsgController;
import com.xingbo.live.controller.PrivateMsgDetaiController;
import com.xingbo.live.dialog.UserInfoDialog;
import com.xingbo.live.entity.Anchor;
import com.xingbo.live.entity.CloseLive;
import com.xingbo.live.entity.Danmu;
import com.xingbo.live.entity.GiftShow;
import com.xingbo.live.entity.Mute;
import com.xingbo.live.entity.msg.AnchorLvUpMsg;
import com.xingbo.live.entity.msg.CancelUserBagItemMsg;
import com.xingbo.live.entity.msg.GuardMsg;
import com.xingbo.live.entity.msg.LVMsg;
import com.xingbo.live.entity.msg.LuckyGiftRetAwardMsg;
import com.xingbo.live.entity.msg.PriMsgDetailMsg;
import com.xingbo.live.entity.RoomInfo;
import com.xingbo.live.entity.RoomMessage;
import com.xingbo.live.entity.model.MuteModle;
import com.xingbo.live.entity.msg.BaseMsg;
import com.xingbo.live.entity.msg.CommonMsg;
import com.xingbo.live.entity.msg.DanmuMsg;
import com.xingbo.live.entity.msg.GiftMsg;
import com.xingbo.live.entity.msg.JoinMsg;
import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.entity.msg.ScrollGiftMsg;
import com.xingbo.live.entity.msg.SystemMsg;
import com.xingbo.live.entity.msg.SystemTypeMsg;
import com.xingbo.live.entity.msg.OnlineMsg;
import com.xingbo.live.entity.msg.UserRichUpMsg;
import com.xingbo.live.eventbus.GiftItemSelectedEvent;
import com.xingbo.live.eventbus.LiveStopAndStartEvent;
import com.xingbo.live.eventbus.MFavoriteEvent;
import com.xingbo.live.eventbus.NotifycationEvent;
import com.xingbo.live.eventbus.OnInternetChangeEvent;
import com.xingbo.live.eventbus.PriMessageEvent;
import com.xingbo.live.eventbus.ReplyEvent;
import com.xingbo.live.eventbus.ScrollGiftEvent;
import com.xingbo.live.eventbus.UserInfoEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.popup.SharePop;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.DpOrSp2PxUtil;
import com.xingbo.live.util.FastBlur;
import com.xingbo.live.util.SoftInputUtils;
import com.xingbo.live.util.SoftKeyboardStateHelper;
import com.xingbo.live.view.GetMessageSocket;
import com.xingbo.live.view.InputBarWithBoard;
import com.xingbo.live.view.ScrollGift;
import com.xingbo.live.view.ShowGift;
import com.xingbobase.config.XingBo;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import master.flame.danmaku.controller.IDanmakuView;

public class MainRoom extends BaseAct implements RoomController.MainRoomControllerListener, GetMessageSocket.OnMessageCallback,
        View.OnClickListener, VisitorAdapter.VisitorClickCallback, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener, AbsListView.OnScrollListener, PrivateMsgController.OnPriMessageItemClickListener, InputBarWithBoard.InputBarWithBoardListener,
        InputbarController.OnSendSuccessListener, PrivateMsgDetaiController.OnClickCallback, OnItemClickListener, View.OnTouchListener, GiftPan.OnGiftPanCallback,
        SoftKeyboardStateHelper.SoftKeyboardStateListener {
    private static final String TAG = "MainRoom";
    public static final String EXTRA_MAIN_ROOM_INFO = "extra_main_room_info";
    public static final String ANCHOR_LIVE_LOGO = "anchor_live_logo";
    private String uid;
    private int startAnchorExp;
    private Gson gson;
    public RoomInfo roomInfo;
    private Anchor anchor;
    private String socketUrl;//   ws://
    private boolean isScrolling = false;
    private String netName;  //网络状况
    private GetMessageSocket getMessageSocket;
    private OnlineMsg onlineMsg;
    private List<RoomMessage> msgList = new ArrayList<RoomMessage>();
    private GestureDetectorCompat detectorCompat;
    private VisitorAdapter visitorAdapter;
    private RoomMessageAdapter mAdapter;
    private UserInfoDialog userInfoDialog;
    private Rect outRect = new Rect();
    private ScrollGiftMsg scrollGiftMsg;
    //view
    public View rootView;
    private ListView messageListView;
    private FrescoImageView anchorAvator;
    private TextView anchorNick;
    private TextView anchorFansnum;
    private TextView anchorConcern;
    private RecyclerView recyclerView;
    //送礼
    private TextView coin;
    private IDanmakuView mDanmakuView;
    private DanmuControl danmuController;
    private ImageView cancel;
    private ShowGift showGift;
    private VideoView videoView;
    private FrescoImageView videoLoadingImage;
    private RelativeLayout anchorInfoRl;
    private ViewGroup.LayoutParams anchorInfoParams;
    private RelativeLayout upRl;
    private TextView messageCount;
    private RelativeLayout rlPriMsg;
    private View priMsgBlank;
    private RelativeLayout rlPriMsgDetail;
    private View priMsgDetailBlank;
    private LinearLayout llVideoview;
    private InputBarWithBoard inputBarWithBoard;
    private View blankInputbar;
    private RelativeLayout rlInutbar;
    private RelativeLayout rlBottom;
    private ImageView ivMessage;
    private ImageView ivShare;
    private ImageView ivPriMessage;
    private ImageView ivGroupchat;
    private ImageView ivGift;
    //controller
    private InputbarController inputbarController;
    public RoomController controller;
    private PrivateMsgController privateMsgController;
    private PrivateMsgDetaiController privateMsgDetaiController;

    private int currentPriCount;
    private AnimatorSet animatorSet;
    private boolean isContains = false;   //观众列表是否包含当前用户
    private MsgFUser msgFUser;
    private GiftPan giftPan;
    private ImageView ivClosePushLive;
    private ScrollGift scrollGift;
    private RelativeLayout slideRl;
    private SharedPreferences sp;
    private TextView slideISee;
    private String posterLogo;
    private CommonMsg commonMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.main_room, null);
        setRootView(rootView);
        setContentView(rootView);
        requestStart();
        EventBus.getDefault().register(this);
        roomInfo = (RoomInfo) getIntent().getSerializableExtra(EXTRA_MAIN_ROOM_INFO);
        posterLogo = getIntent().getStringExtra(ANCHOR_LIVE_LOGO);
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(XingBo.PX_USER_SETTING_IS_IN_MAINROOM, "1");
        editor.commit();
        initView();
        initVideoView();
        initData();
        getGreenMesg();
        getPriMsgCount();
        mDanmakuView.resume();
    }

    public void initView() {
        //新手引导
        slideRl = (RelativeLayout) rootView.findViewById(R.id.room_slide);
        slideISee = (TextView) rootView.findViewById(R.id.room_slide_i_see);
        videoLoadingImage = (FrescoImageView) rootView.findViewById(R.id.room_loading_image);
        ViewGroup.LayoutParams params = videoLoadingImage.getLayoutParams();
        params.height = (int) SystemApp.screenHeight;
        videoLoadingImage.setLayoutParams(params);
//        videoLoadingImage.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+posterLogo));
        FastBlur.show(videoLoadingImage, HttpConfig.FILE_SERVER + posterLogo, 30, this);
////        FastBlur.show(videoLoadingImage, HttpConfig.FILE_SERVER + roomInfo.getAnchor().getLivelogo(), 20.0f, this);
        rlInutbar = (RelativeLayout) rootView.findViewById(R.id.rl_inputbar);
        inputBarWithBoard = (InputBarWithBoard) rootView.findViewById(R.id.input_bar);
        blankInputbar = rootView.findViewById(R.id.blank_inputbar);
        llVideoview = (LinearLayout) rootView.findViewById(R.id.ll_videoview);
        upRl = (RelativeLayout) rootView.findViewById(R.id.rl_up_layout);
        //送礼记录
        showGift = (ShowGift) rootView.findViewById(R.id.showGift);
        scrollGift = (ScrollGift) rootView.findViewById(R.id.main_room_scroll_gift);
        //anchor info
        anchorInfoRl = (RelativeLayout) rootView.findViewById(R.id.main_room_anchor_info);
        anchorAvator = (FrescoImageView) rootView.findViewById(R.id.civ_anchor_avator);
        anchorNick = (TextView) rootView.findViewById(R.id.tv_anchor_nick);
        anchorFansnum = (TextView) rootView.findViewById(R.id.tv_anchor_fansnum);
        anchorConcern = (TextView) rootView.findViewById(R.id.tv_anchor_concern);
        coin = (TextView) rootView.findViewById(R.id.main_room_coin);
        cancel = (ImageView) rootView.findViewById(R.id.iv_main_room_cancel);
        //fans
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_fans);
        mDanmakuView = (IDanmakuView) rootView.findViewById(R.id.main_room_danmu);
        //message
        messageListView = (ListView) rootView.findViewById(R.id.room_listview);
        //bottom button
        rlBottom = (RelativeLayout) rootView.findViewById(R.id.rl_bottom);
        ivMessage = (ImageView) rootView.findViewById(R.id.iv_message);
        ivShare = (ImageView) rootView.findViewById(R.id.iv_share);
        ivPriMessage = (ImageView) rootView.findViewById(R.id.iv_pri_message);
        messageCount = (TextView) rootView.findViewById(R.id.iv_pri_message_count);
        ivGroupchat = (ImageView) rootView.findViewById(R.id.iv_groupchat);
        ivGift = (ImageView) rootView.findViewById(R.id.iv_gift);
        ivClosePushLive = (ImageView) rootView.findViewById(R.id.iv_close_psrh_live);
        videoView = (VideoView) rootView.findViewById(R.id.room_videoview);
        //pri_msg
        rlPriMsg = (RelativeLayout) rootView.findViewById(R.id.rl_pri_msg);
        rlPriMsgDetail = (RelativeLayout) rootView.findViewById(R.id.rl_pri_msg_detail);
        priMsgBlank = rootView.findViewById(R.id.pri_msg_blank);
        priMsgDetailBlank = rootView.findViewById(R.id.rl_pri_msg_detail);
    }

    public void initVideoView() {
        videoView.requestFocus();
        MediaController mc = new MediaController(this);
        mc.setVisibility(View.INVISIBLE);
        videoView.setMediaController(mc);
        videoView.setOnPreparedListener(this);
        videoView.setOnInfoListener(this);
        videoView.setOnErrorListener(this);
    }

    public void initData() {
        gson = new Gson();
        socketUrl = roomInfo.getNotify();
        anchor = roomInfo.getAnchor();
        screenWidth = (int) SystemApp.screenWidth;
        screenHeight = (int) SystemApp.screenHeight;
        if (roomInfo.getSuperadmin() == 0) {
            ivClosePushLive.setVisibility(View.INVISIBLE);
        }
        detectorCompat = new GestureDetectorCompat(MainRoom.this, new MyGestureListener());
        startAnchorExp = roomInfo.getAnchor().getAnchorexp();
        inputbarController = new InputbarController(this);
        inputbarController.setOnSendSuccess(this);
        controller = new RoomController();
        controller.setMainroomControllerCallback(this);
        danmuController = new DanmuControl(this);
        danmuController.setDanmakuView(mDanmakuView);
        netName = controller.checkNetState(this);
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        uid = sp.getString(XingBo.PX_USER_LOGIN_UID, "");
        if (anchor == null) {
            finish();
            return;
        }
        //新手引导
        boolean isFirst = sp.getBoolean(XingBo.PX_ROOM_SLIDE, true);
        if (isFirst) {
            slideRl.setVisibility(View.VISIBLE);
            sp.edit().putBoolean(XingBo.PX_ROOM_SLIDE, false);
        } else {
            slideRl.setVisibility(View.INVISIBLE);
        }
        anchorInfoParams = anchorInfoRl.getLayoutParams();
        //setdata
        if (anchor != null) {
            anchorAvator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + anchor.getAvatar()));
            anchorNick.setText(anchor.getNick());
            anchorFansnum.setText(anchor.getLiveonlines());
            if (roomInfo.isFollowed()) {
                anchorInfoParams.width = DpOrSp2PxUtil.dp2pxConvertInt(this, 115);
                anchorInfoRl.setLayoutParams(anchorInfoParams);
                anchorConcern.setVisibility(View.GONE);
            } else {
                anchorConcern.setVisibility(View.VISIBLE);
            }
//            EventBus.getDefault().register(new MFavoriteEvent(roomInfo.isFollowed(), roomInfo.getAnchor().getId()));
            coin.setText("经验值:" + roomInfo.getAnchor().getAnchorexp());
        }
        //gift pan
        giftPan = new GiftPan(this, rootView, roomInfo, controller);
        giftPan.setGiftPanCallback(this);
        //soft inputbar
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(rootView);
        softKeyboardStateHelper.addSoftKeyboardStateListener(this);
        //adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        visitorAdapter = new VisitorAdapter(this, fUserList);
        visitorAdapter.setVisitorsClickLietener(this);
        recyclerView.setAdapter(visitorAdapter);
        getMessageSocket = new GetMessageSocket(socketUrl);
        getMessageSocket.setOnSocketCallback(this);
        getMessageSocket.checkWebSocketState();
        mAdapter = new RoomMessageAdapter(this, msgList);
        messageListView.setAdapter(mAdapter);
        //click
        slideISee.setOnClickListener(this);
        cancel.setOnClickListener(this);
        anchorAvator.setOnClickListener(this);
        anchorConcern.setOnClickListener(this);
        coin.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivPriMessage.setOnClickListener(this);
        ivGroupchat.setOnClickListener(this);
        ivGift.setOnClickListener(this);
        ivClosePushLive.setOnClickListener(this);
        inputBarWithBoard.setInputBarWithBoardListener(this);
        blankInputbar.setOnClickListener(this);
        messageListView.setOnScrollListener(this);
        messageListView.setOnItemClickListener(this);
        messageListView.setOnTouchListener(this);
        //pri msg
        privateMsgController = new PrivateMsgController(this, rootView);
        privateMsgController.init();
        privateMsgController.setOnPriMessageItemClickListener(this);
        privateMsgDetaiController = new PrivateMsgDetaiController(this, rootView);
        privateMsgDetaiController.initView();
        privateMsgDetaiController.setBackClick(this);
        priMsgBlank.setOnClickListener(this);
        priMsgDetailBlank.setOnClickListener(this);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        onStartVideo();
    }

    //获取私信数量
    public void getPriMsgCount() {
        controller.getMessageCount(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detectorCompat.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.start();
        int videoWidth = videoView.getVideoWidth();
        int videoHeight = videoView.getVideoHeight();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) videoView.getLayoutParams();
        if (videoWidth > videoHeight) {
            params.topMargin = DpOrSp2PxUtil.dp2pxConvertInt(this, 120);
            params.width = (int) SystemApp.screenWidth;
            params.height = 3 * (int) SystemApp.screenWidth / 4;
            videoView.setLayoutParams(params);
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM, 4 / 3f);
        } else {
            params.height = (int) SystemApp.screenHeight;
            videoView.setLayoutParams(params);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (alert != null && !alert.isShowing()) {

//            alert("视频加载失败");
        }
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {  //开始缓存数据
            handler.sendEmptyMessageDelayed(XingBoConfig.VIDEO_CANCEL_IMAGE, 500);
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:  //滑动停止
                handler.sendEmptyMessageDelayed(XingBoConfig.REFRESH_SCROLL_STATE, 2000);
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
    }

    @Override
    public void setOnItemClickListener(String nick, String uid) {
        privateMsgDetaiController.setData(uid, nick);
        rlPriMsgDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSend(String msg) {
        inputbarController.onSend(msg, roomInfo.getAnchor().getId());
    }

    @Override
    public void onDanmuStateChange(boolean isChecked) {
        inputbarController.onDanmuStateChange(isChecked);
    }

    @Override
    public void onEmotionShow(int marginBottom) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageListView.getLayoutParams();
        params.bottomMargin = marginBottom;
        messageListView.setLayoutParams(params);
    }

    @Override
    public void sendMessageSuccess(int coin) {
        inputBarWithBoard.mInput.setText("");
       /* if (inputBarWithBoard.emotionContainer.getVisibility() != View.VISIBLE) {
            inputBarWithBoard.hideSoftInput(inputBarWithBoard.mInput);
        }*/
//        hideEmotionContainer();
        if(coin!=-1){
            giftPan.balanceInfo.setText(coin+"");
        }
    }

    public void hideInputbar() {
        rlInutbar.setVisibility(View.GONE);
        inputBarWithBoard.setVisibility(View.INVISIBLE);
        TranslateAnimation animationIn = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);
        anchorInfoRl.startAnimation(animationIn);
        recyclerView.startAnimation(animationIn);
        cancel.startAnimation(animationIn);
        coin.startAnimation(animationIn);
        animationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                anchorInfoRl.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                coin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void clickCallback() {
        rlPriMsgDetail.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RoomMessage roomMessage = msgList.get(position);
        switch (roomMessage.getType()) {
            case RoomMessage.COMMON_MSG:
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getCommonMsg().getData().getFuser().getId());
                userInfoDialog.show();
                break;
            case RoomMessage.SEND_GIFT:
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomMessage.getGiftMsg().getData().getFuser().getId());
                userInfoDialog.show();
                break;
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
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.room_listview) {
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
        }
        return false;
    }

    private boolean isSoftKeyboardOpened = false;

    @Override
    public void showRoomBottomButton() {
        rlBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
        isSoftKeyboardOpened = true;
        Log.e(TAG, "onSoftKeyboardOpened");
    }

    @Override
    public void onSoftKeyboardClosed() {
        Log.e(TAG, "onSoftKeyboardClosed");
        isSoftKeyboardOpened = false;
//        hideEmotionContainer();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG, "onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if(giftPan.giftPanRl.getVisibility()==View.VISIBLE){
                return false;
            }
            if (llVideoview.getVisibility() == View.VISIBLE) {
                if (velocityX > 3000) {
                    if (upRl.getVisibility() == View.VISIBLE) {
                        TranslateAnimation toRightAnimation = new TranslateAnimation(0, SystemApp.screenWidth, 0, 0);
                        toRightAnimation.setDuration(500);
                        upRl.setAnimation(toRightAnimation);
                        upRl.setVisibility(View.INVISIBLE);
                    }
                } else if (velocityX < -3000) {
                    if (upRl.getVisibility() != View.VISIBLE) {
                        TranslateAnimation toLeftAnimation = new TranslateAnimation(SystemApp.screenWidth, 0, 0, 0);
                        toLeftAnimation.setDuration(500);
                        upRl.setAnimation(toLeftAnimation);
                        upRl.setVisibility(View.VISIBLE);
                    }
                }
                if (Math.abs(velocityY) > 5000) {
//                    alert("上下滑动切换直播间功能暂未实现，敬请期待！");
                }
            }
            return true;
        }
    }

    private List<MsgFUser> fUserList = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();
        mDanmakuView.pause();
//        if (videoView != null) {
//            videoView.pause();
//        }
//        videoLoadingImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (videoView != null) {
            videoView.resume();
        }*/
        if (videoView != null) {
            videoView.setVideoPath(roomInfo.getLive());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (videoView != null) {
            videoView.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMessageSocket.destroy();
        socketUrl = null;
        handler.removeCallbacksAndMessages(null);
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
        if (videoView != null) {
            videoView.destroyDrawingCache();
        }
        EventBus.getDefault().unregister(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(XingBo.PX_USER_SETTING_IS_IN_MAINROOM, "0");
        editor.commit();
    }


    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case XingBoConfig.START_VIDEO:
                onStartVideo();
                break;
            case XingBoConfig.STOP_VIDEO:
                CommonUtil.log(TAG, "有停播放");
                //显示未开播
                onStopVideo();
                break;
            case XingBoConfig.VIDEO_CANCEL_IMAGE:
                requestFinish();
                upRl.setVisibility(View.VISIBLE);
//                videoLoadingImage.setVisibility(View.GONE);
                llVideoview.setVisibility(View.VISIBLE);
                break;
            case XingBoConfig.REFRESH_MESSAGE_LIST:
                mAdapter.setData(msgList);
                break;
            case XingBoConfig.REFRESH_SCROLL_LASTEST:
                mAdapter.setData(msgList);
                messageListView.setSelection(msgList.size() - 1);
                break;
            case XingBoConfig.REFRESH_ONLINES:
                anchorFansnum.setText(onlineMsg.getData().size() + "");
                break;
            case XingBoConfig.REFRESH_FANS_LIST:
                visitorAdapter.setData(fUserList);
                visitorAdapter.notifyDataSetChanged();
                break;
            case XingBoConfig.REFRESH_PRI_MSG_COUNT:
                PriMsgDetailMsg priMsgDetailMsg = (PriMsgDetailMsg) msg.obj;
                if (rlPriMsgDetail.getVisibility() == View.VISIBLE && privateMsgDetaiController.getSenderId().equals(priMsgDetailMsg.getData().getFuser().getId())) {
                    privateMsgDetaiController.setData(priMsgDetailMsg.getData().getFuser().getId(), priMsgDetailMsg.getData().getFuser().getNick());
                }
                if (rlPriMsg.getVisibility() == View.VISIBLE) {
                    privateMsgController.refreshPriMsgList(priMsgDetailMsg);
                }
                currentPriCount++;
                messageCount.setText("" + currentPriCount);
                if (messageCount.getVisibility() != View.VISIBLE) {
                    messageCount.setVisibility(View.VISIBLE);
                }
                break;
            case XingBoConfig.REFRESH_JOIN_ROOM:
                anchorFansnum.setText((Integer.parseInt(anchorFansnum.getText().toString()) + 1) + "");
                break;
            case XingBoConfig.REFRESH_LEAVE_ROOM:
                anchorFansnum.setText((Integer.parseInt(anchorFansnum.getText().toString()) - 1) + "");
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
                            if (onlineMsg.getData().get(i).getLogintype().equals("login") && !fUserList.contains(onlineMsg.getData().get(i)) && !onlineMsg.getData().get(i).getId().equals(anchor.getId())) {
                                fUserList.add(onlineMsg.getData().get(i));
                            } else if (onlineMsg.getData().get(i).equals("guest")) {   //游客不显示
                                continue;
                            }
                        }
                        handler.sendEmptyMessage(XingBoConfig.REFRESH_FANS_LIST);
                    }
                }.start();
                break;
            case XingBoConfig.CLEAR_SCROLL_GIFT:
//                scrollGiftText.setVisibility(View.INVISIBLE);
//                scrollGiftBg.setVisibility(View.INVISIBLE);
                break;
            case XingBoConfig.REFRESH_SCROLL_STATE:
                isScrolling = false;
                break;
            case XingBoConfig.REFRESH_ANCHOR_EXP:
                coin.setText("经验值:" + ((LVMsg) (msg.obj)).getData().getLvl().getCurexp());
                break;
        }
    }

    @Subscribe
    public void scrollGiftShowEnd(ScrollGiftEvent scrollGiftEvent) {
        handler.sendEmptyMessage(XingBoConfig.CLEAR_SCROLL_GIFT);
    }

    @Subscribe
    public void notifycationEvent(NotifycationEvent notifycationEvent) {
        XingBoUtil.alert(this, "不能同时进入两个直播房间").show();
    }

    /*public void show(View view, int textWidth) {
        Animator animatorX = ObjectAnimator.ofFloat(view, "translationX", screenWidth, -textWidth);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX);
        animatorSet.setDuration(15000);
        animatorSet.start();

    }
*/
    //关注
    public void concern() {
        controller.favoriteUser(this, roomInfo.isFollowed(), roomInfo.getAnchor().getId());
    }

    @Subscribe
    public void updateConcern(MFavoriteEvent favoriteEvent) {
        if (favoriteEvent.getUid().equals(anchor.getId())) {
//            anchorConcern.setVisibility(favoriteEvent.isFavorite() ? View.GONE : View.VISIBLE);
            if (favoriteEvent.isFavorite()) {
                anchorInfoParams.width = DpOrSp2PxUtil.dp2pxConvertInt(this, 115);
                anchorInfoRl.setLayoutParams(anchorInfoParams);
                anchorConcern.setVisibility(View.GONE);
            } else {
                anchorInfoParams.width = DpOrSp2PxUtil.dp2pxConvertInt(this, 165);
                anchorInfoRl.setLayoutParams(anchorInfoParams);
                anchorConcern.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.room_slide_i_see:
                slideRl.setVisibility(View.INVISIBLE);
                sp.edit().putBoolean(XingBo.PX_ROOM_SLIDE, false).commit();
                break;
            case R.id.civ_anchor_avator:  //点击主播头像，显示主播信息
                if (userInfoDialog == null) {
                    userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                }
                userInfoDialog.setData(false, roomInfo.getAnchor().getId(), roomInfo.getAnchor().getId());
                userInfoDialog.show();
                break;
            case R.id.tv_anchor_concern:  //关注
                concern();
                break;
            case R.id.main_room_coin:  //守护页面
                Intent guardIntent = new Intent(this, GuardAct.class);
                guardIntent.putExtra(GuardAct.ANCHOR_GUARD_ID, anchor.getId());
                guardIntent.putExtra(GuardAct.ANCHOR_GUARD_NICK, anchor.getNick());
                guardIntent.putExtra(GuardAct.USER_COIN, roomInfo.getCoin());
                guardIntent.putExtra(GuardAct.ANCHOR_GUARD_AVATAR, roomInfo.getAnchor().getAvatar());
                startActivity(guardIntent);
                break;
            case R.id.iv_main_room_cancel:
                finish();
                break;
            case R.id.iv_message:
                rlBottom.setVisibility(View.INVISIBLE);
                showInputbar();
                break;
            case R.id.iv_share:
                SharePop popShare = new SharePop(this, UMShareAPI.get(this));
                popShare.setShareContent(roomInfo.getShare());
                popShare.setFocusable(true);
                popShare.setBackgroundDrawable(new BitmapDrawable());
                popShare.setAnimationStyle(R.style.style_popup);
                popShare.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.iv_pri_message:  //私信
                rlPriMsg.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_groupchat:
                break;
            case R.id.iv_gift:
                rlBottom.setVisibility(View.INVISIBLE);
                giftPan.showGiftPan();
                break;
            case R.id.iv_close_psrh_live:  //禁播
                XingBoUtil.dialog(this, "提示", "确定禁播该主播吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        controller.closePushLive(MainRoom.this, anchor.getId());
                    }
                });
                break;
            case R.id.pri_msg_blank:
                controller.getMessageCount(this);
                rlPriMsg.setVisibility(View.INVISIBLE);
                break;
            case R.id.rl_pri_msg_detail:
                controller.getMessageCount(this);
                rlPriMsgDetail.setVisibility(View.INVISIBLE);
                if (rlPriMsg.getVisibility() == View.VISIBLE) {
                    rlPriMsg.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.blank_inputbar:
                hideEmotionContainer();
                break;
        }
    }

    public void hideEmotionContainer() {
        if (isSoftKeyboardOpened) {
            SoftInputUtils.hideInput(this, inputBarWithBoard.mInput);
            isSoftKeyboardOpened=false;
        }
        if (rlBottom.getVisibility() != View.VISIBLE) {
            rlBottom.setVisibility(View.VISIBLE);
        }
        hideInputbar();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageListView.getLayoutParams();
        params.bottomMargin = DpOrSp2PxUtil.dp2pxConvertInt(this, 10);
        messageListView.setLayoutParams(params);
        inputBarWithBoard.hideEmotionContainer();
    }

    public void showInputbar() {
        TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);
        anchorInfoRl.startAnimation(animation);
        recyclerView.startAnimation(animation);
        cancel.startAnimation(animation);
        coin.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                anchorInfoRl.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                coin.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rlInutbar.setVisibility(View.VISIBLE);
        inputBarWithBoard.reset();
        inputBarWithBoard.setVisibility(View.VISIBLE);
        inputBarWithBoard.showSoftInput(inputBarWithBoard.mInput);
    }

    /**
     * 有礼物项被先中
     */
    @Subscribe
    public void GiftSelected(GiftItemSelectedEvent event) {
        giftPan.GiftSelected(event);
    }

    public void checkNetState() {
        if (netName != null && netName.equals(InternetStateBroadcast.NET_NO)) {
            alert("当前网络已断开");
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        com.umeng.socialize.utils.Log.d("result", "onActivityResult");
    }

    /**
     * 网络变化
     */
    @Subscribe
    public void onInternetChange(OnInternetChangeEvent event) {
        netName = event.getNetName();
        if (netName != null && netName.equals(InternetStateBroadcast.NET_MOBILE)) {
            if (videoView != null && videoView.isPlaying()) {
                dialog("停止观看", "继续观看", R.color.orange_FC563C, R.color.first_text_424242, "系统提示", "当前非wifi网络，继续观看吗", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoView.stopPlayback();
                        videoView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
        if (netName != null && netName.equals(InternetStateBroadcast.NET_NO)) {
            alert("网络已断开");
            if (videoView != null && videoView.isPlaying()) {
                videoView.stopPlayback();
            }
        }
        if (netName != null && netName.equals(InternetStateBroadcast.NET_WIFI)) {
            if (videoView != null && !videoView.isPlaying()) {
                handler.sendEmptyMessage(XingBoConfig.START_VIDEO);
            }
        }
    }

    @Subscribe
    public void showUserInfo(UserInfoEvent userInfoEvent) {
        if (userInfoDialog != null) {
            userInfoDialog.showUserInfoCard(userInfoEvent);
        }
    }

    @Override
    public void onItemClick(int position, String userId) {
        if (userInfoDialog == null) {
            userInfoDialog = new UserInfoDialog(this, R.style.dialog);
        }
        if (uid.equals(userId)) {
            userInfoDialog.setData(true, roomInfo.getAnchor().getId(), userId);
        } else {
            userInfoDialog.setData(false, roomInfo.getAnchor().getId(), userId);
        }
        userInfoDialog.show();
    }

    public static float screenWidth = 0;//屏幕宽px
    public static float screenHeight = 0;//屏幕高px

    @Override
    public void onError(int tag, String msg) {
        alert(msg);
    }

    @Override
    public void onSuccess(int tag, Object modelD) {
        switch (tag) {
            case RoomController.SEND_GIFT:
//                Toast.makeText(MainRoom.this, "送礼成功", Toast.LENGTH_SHORT).show();
                giftPan.sendGiftSuccess(modelD);
                break;
            case RoomController.GET_MESSAGE_COUNT:
                if (modelD instanceof String) {
                    messageCount.setText((String) modelD);
                    currentPriCount = Integer.parseInt((String) modelD);
                    if (Integer.parseInt((String) modelD) > 0) {
                        messageCount.setVisibility(View.VISIBLE);
                    } else {
                        messageCount.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case RoomController.CLICK_FAVORATE:  //关注成功
                if (modelD instanceof MFavoriteEvent) {
                    EventBus.getDefault().post((MFavoriteEvent) modelD);
                }
                break;
            case RoomController.CLOSE_PUSH_LIVE:  //禁播
                if (modelD instanceof CloseLive) {
                    finish();
                    Intent stopIntent = new Intent(this, LiveFinishedAct.class);
                    stopIntent.putExtra(LiveFinishedAct.LIVE_ROOM_IS_FOLLOWED, anchorConcern.getVisibility() == View.VISIBLE ? false : true);
                    stopIntent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ONLINES, ((CloseLive) modelD).getWatch_num() + "");
                    stopIntent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ID, roomInfo.getAnchor().getId());
                    stopIntent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, posterLogo);
                    startActivity(stopIntent);
                }
                break;
        }
    }

    @Subscribe
    public void showMute(MuteModle muteModle) {
        Mute mute = muteModle.getD();
        msgList.add(new RoomMessage(1, (mute.getType() == 0) ? (mute.getNickname() + "被管理员本场禁言") : (mute.getNickname() + "被管理员永久禁言")));
        handler.sendEmptyMessage(XingBoConfig.REFRESH_FANS_LIST);
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
                commonMsg = gson.fromJson(msg, CommonMsg.class);
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
                } else if (joinMsg.getData().getFuser().getLogintype().equals("login") && !joinMsg.getData().getFuser().getId().equals(anchor.getId())) {
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
                    }
                }
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
            case BaseMsg.BROAD_CAST_MSG:  //广播
                DanmuMsg danmuMsg = gson.fromJson(msg, DanmuMsg.class);
                danmuController.addDanmu(new Danmu(Integer.parseInt("1"), danmuMsg.getData().getFuser().getAvatar(), danmuMsg.getData().getFuser().getNick(), danmuMsg.getData().getWord()));
                break;
            case BaseMsg.SYSTEM_MSG_START_LIVE:  //开播
                EventBus.getDefault().post(new LiveStopAndStartEvent(LiveStopAndStartEvent.VIDEO_PLAY));
                break;
            case BaseMsg.SYSTEM_MSG_STOP_LIVE:
                EventBus.getDefault().post(new LiveStopAndStartEvent(LiveStopAndStartEvent.VIDEO_STOP));
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
            case BaseMsg.TOU_TIAO_GIFT:  //头条
                scrollGiftMsg = gson.fromJson(msg, ScrollGiftMsg.class);
                /*if(scrollGiftBg.getVisibility()!=View.VISIBLE){
                    scrollGiftBg.setVisibility(View.VISIBLE);
                }*/
                scrollGift.add(scrollGiftMsg);
                break;
            case BaseMsg.SYSTEM_PUBPRI_MSG:  //幸运礼物返奖信息
                LuckyGiftRetAwardMsg luckyGiftRetAwardMsg = gson.fromJson(msg, LuckyGiftRetAwardMsg.class);
                if (!luckyGiftRetAwardMsg.getData().getMsg().contains("{")) {
                    msgList.add(new RoomMessage(RoomMessage.LUCKY_RETURN_AWARD, luckyGiftRetAwardMsg));
                    if (!isScrolling) {
                        handler.sendEmptyMessage(XingBoConfig.REFRESH_SCROLL_LASTEST);
                    } else {
                        handler.sendEmptyMessage(XingBoConfig.REFRESH_MESSAGE_LIST);
                    }
                }

                break;
            case BaseMsg.CANCEL_USER_BAG_ITEM:  //消耗背包礼物
                CancelUserBagItemMsg cancelUserBagItemMsg=gson.fromJson(msg,CancelUserBagItemMsg.class);
                giftPan.setBags(cancelUserBagItemMsg.getBody());
                break;
            default:
                break;
        }
    }

    @Subscribe   //回复
    public void reply(ReplyEvent replyEvent) {
        rlBottom.setVisibility(View.INVISIBLE);
        showInputbar();
        inputBarWithBoard.mInput.setText("@" + replyEvent.getNick() + " ");
        inputBarWithBoard.mInput.setSelection(("@" + replyEvent.getNick() + " ").length());
       /* inputBarWithBoard.mInput.requestFocus();
        inputBarWithBoard.mInput.setFocusable(true);
        showInputbar();
        SoftInputUtils.showInput(this, rootView);*/
    }

    @Subscribe  //私信
    public void privateChat(PriMessageEvent priMessageEvent) {
        privateMsgDetaiController.setData(priMessageEvent.getId(), priMessageEvent.getNick());
        rlPriMsgDetail.setVisibility(View.VISIBLE);
    }

    /**
     * 直播开播
     */
    private void onStartVideo() {
        if (netName != null && netName.equals(InternetStateBroadcast.NET_MOBILE)) {
            dialog("观看", "取消", R.color.orange_FC563C, R.color.first_text_424242, "开播提示", "当前非wifi网络，接收观看吗", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.setVideoPath(roomInfo.getLive());  //roomInfo.getLive()
                }
            });
        } else if (netName != null && netName.equals(InternetStateBroadcast.NET_WIFI)) {
            videoView.setVideoPath(roomInfo.getLive()); //"rtmp://live.hkstv.hk.lxdns.com/live/hks"
            videoView.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        llVideoview.setVisibility(View.INVISIBLE);
        if (videoView != null) {
            videoView.stopPlayback();//停止播放器
        }
    }

    /**
     * 直播停播
     */
    private void onStopVideo() {
        //显示主播海报及其它信息
        videoView.stopPlayback();
        finish();
        Intent stopIntent = new Intent(this, LiveFinishedAct.class);
        stopIntent.putExtra(LiveFinishedAct.LIVE_ROOM_IS_FOLLOWED, anchorConcern.getVisibility() == View.VISIBLE ? false : true);
        stopIntent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ONLINES, fUserList.size() + "");
        stopIntent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ID, roomInfo.getAnchor().getId());
        stopIntent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, posterLogo);
        startActivity(stopIntent);
    }

    /**
     * 开播或停播事件通知
     */
    @Subscribe
    public void startOrStopVideo(LiveStopAndStartEvent event) {
        switch (event.getState()) {
            case LiveStopAndStartEvent.VIDEO_PLAY:
                handler.sendEmptyMessage(XingBoConfig.START_VIDEO);
                break;
            case LiveStopAndStartEvent.VIDEO_STOP:
                handler.sendEmptyMessage(XingBoConfig.STOP_VIDEO);
                break;
            default:
                break;
        }
    }
}
