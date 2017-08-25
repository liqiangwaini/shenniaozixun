package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MainUser;
import com.xingbo.live.entity.User;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.entity.model.UserHomeModel;
import com.xingbo.live.eventbus.MFavoriteEvent;
import com.xingbo.live.eventbus.UpdatePhotosEvent;
import com.xingbo.live.fragment.HomepagePersonalFragment;
import com.xingbo.live.fragment.HomepagePhotosFragment;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.IFrescoImageView;

import de.greenrobot.event.EventBus;
import io.vov.vitamio.utils.Log;


/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/8
 * <p/>
 * 某用户主页布局
 */
public class UserHomepageAct extends BaseAct implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private final static String TAG = "UserHomepageAct";

    public final static String EXTRA_USER_ID = "extra_user_id";
    public final static String EXTRA_ANCHOR_ID = "extra_anchor_id";
    public final static  String EXTRA_USER_ISINLIVE="extra_user_isinlive";
    private String uid;
    private ImageButton imageTopBack;
    private ImageView imageIsLive;
    //头布局  头像 昵称 id 性别 等级标 关注数量， 粉丝数量
    private IFrescoImageView avatar;
    private IFrescoImageView starLevel;
    private TextView user_id;
    private TextView user_nick;
    private ImageView user_sex;
    private ImageView user_level;
    private ImageView user_isLive;
    private TextView user_personal_sign;
    private TextView favorite_num;
    private TextView fans_num;
    //radiobutton进行主页和相册的切换
    private RadioButton radioHomePage;
    private RadioButton radioPhotos;
    private RadioGroup radioGroup;
    private HomepagePersonalFragment personalFragment;
    private HomepagePhotosFragment photoFragment;
    //底层的关注和私信的跳转
    private ImageView imageFavorite;
    private TextView textFavorite;
    private String otherId;


    private boolean isNeedInit = true;//是否需要初始化
    private int mOpera = XingBo.REQUEST_OPERA_INIT;//请求初始化操作

    public MainUser mUser = null;
    private int currentFavoriteNum = 0;
    private String anchorId;
    private SharedPreferences sp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_personal_homepage);
        uid = getIntent().getStringExtra(EXTRA_USER_ID);
        anchorId = getIntent().getStringExtra(EXTRA_ANCHOR_ID);
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        Log.d("tag", "userHomepageAct-->" + uid + "()");
        initView();

    }


    private void initView() {
        /**
         * 顶部数据显示
         */
        imageTopBack = (ImageButton) findViewById(R.id.user_homepage_top_back);
        imageTopBack.setOnClickListener(this);
        imageIsLive = (ImageView) findViewById(R.id.user_homepage_state_isLive);

        imageIsLive.setOnClickListener(this);
        findViewById(R.id.ll_concern).setOnClickListener(this);
        findViewById(R.id.ll_fans).setOnClickListener(this);

        /**
         * 头布局  头像 昵称 id 性别 等级标 关注数量， 粉丝数量
         */
        avatar = (IFrescoImageView) findViewById(R.id.user_header);
        avatar.setOnClickListener(this);
        starLevel= (IFrescoImageView) findViewById(R.id.start_level_icon);
        user_id = (TextView) findViewById(R.id.user_id);
        user_nick = (TextView) findViewById(R.id.user_neck);
        user_sex = (ImageView) findViewById(R.id.user_sex_icon);
        user_level = (ImageView) findViewById(R.id.user_leve_icon);
        user_personal_sign = (TextView) findViewById(R.id.user_personal_sign);
        favorite_num = (TextView) findViewById(R.id.favorite_num);
        fans_num = (TextView) findViewById(R.id.fans_num);
        user_isLive = (ImageView) findViewById(R.id.user_homepage_state_isLive);
        //底部的text初始化
        imageFavorite = (ImageView) findViewById(R.id.user_homepage_favorite_image);
        textFavorite = (TextView) findViewById(R.id.user_homepage_favorite_text);
        findViewById(R.id.user_homepage_favorite).setOnClickListener(this);
        findViewById(R.id.user_homepage_message).setOnClickListener(this);

        requestBaseInfo(XingBo.REQUEST_OPERA_INIT);
        findViewById(R.id.favorite_linker).setOnClickListener(this);
        findViewById(R.id.fans_linker).setOnClickListener(this);
        /**
         * 主页相册的切换
         */

        radioGroup = (RadioGroup) findViewById(R.id.homepage_radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        radioHomePage = (RadioButton) findViewById(R.id.user_homepage_radiobutton_home);
        radioPhotos = (RadioButton) findViewById(R.id.user_homepage_radiobutton_photo);
        selectFragment(0);
    }


    private void selectFragment(int i) {
        FragmentManager fm = getSupportFragmentManager();//获得fragment管理器
        FragmentTransaction ft = fm.beginTransaction();//开启一个事物
        switch (i) {
            case 0:
                if (personalFragment == null) {
                    personalFragment = new HomepagePersonalFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(HomepagePersonalFragment.EXTRA_USER_ID, uid);
                    personalFragment.setArguments(bundle);
                }
                ft.replace(R.id.homepage_fragmentchange, personalFragment);

                break;
            case 1:
                if (photoFragment == null) {
                    photoFragment = new HomepagePhotosFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(HomepagePhotosFragment.EXTRA_USER_ID, uid);
                    photoFragment.setArguments(bundle1);
                }
                ft.replace(R.id.homepage_fragmentchange, photoFragment);
                break;
        }

        ft.commit();
    }

    /**
     * 头布局用户详情数据请求操作
     */
    private void requestBaseInfo(int opera) {
        mOpera = opera;
        RequestParam param = RequestParam.builder(this);
        param.put("uid", uid);
        CommonUtil.request(this, HttpConfig.API_USER_GET_USER_INFO, param, TAG, new XingBoResponseHandler<UserHomeModel>(UserHomeModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.d(TAG, "用户中心初始化结果" + response);
                mUser = model.getD();
                otherId = mUser.getId();
                if (mUser == null) {
                    alert("初始化失败");
                    return;
                }
                avatar.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + mUser.getAvatar()));
                user_nick.setText(mUser.getNick());
                user_id.setText("星播号：" + mUser.getId());
                if (mUser.getSex().equals("女")) {
                    user_sex.setImageResource(R.mipmap.female);
                } else if (mUser.getSex().equals("男")) {
                    user_sex.setImageResource(R.mipmap.male);
                }
                //设置用户财富等级
                int rlv = 0;
                try {
                    rlv = Integer.parseInt(mUser.getRichlvl());
                } catch (NumberFormatException e) {
                    rlv = 0;
                }
                if (rlv < 34) {
                    user_level.setImageResource(XingBoConfig.RICH_LV_ICONS[rlv]);
                } else {
                    user_level.setImageResource(XingBoConfig.RICH_LV_ICONS[33]);
                }
                //设置用户等级
                int slv = 0;
                try {
                    slv = Integer.parseInt(mUser.getAnchorlvl());
                } catch (NumberFormatException e) {
                    slv = 0;
                }
                if (slv < 41) {
                    starLevel.setImageResource(XingBoConfig.STAR_LV_ICONS[slv]);
                } else {
                    starLevel.setImageResource(XingBoConfig.STAR_LV_ICONS[40]);
                }

                if (mUser.isFollowed()) {
                    textFavorite.setText("已关注");
                } else {
                    textFavorite.setText("关注");
                }
                //个性签名
                if (!TextUtils.isEmpty(mUser.getIntro())) {
                    user_personal_sign.setText(mUser.getIntro());
                }

                if (sp.getString(XingBo.PX_USER_SETTING_IS_IN_MAINROOM, "").equals("1")){
                    imageIsLive.setVisibility(View.GONE);
                }else {
                    if (mUser.getLivestatus().equals("1")) {
                        user_isLive.setVisibility(View.VISIBLE);
                    } else {
                        user_isLive.setVisibility(View.INVISIBLE);
                    }
                }


                favorite_num.setText(mUser.getFollowings());
                fans_num.setText(mUser.getFollowers());
                isNeedInit = false;
            }
        });
    }

    public String getUid() {
        return otherId;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_homepage_top_back:
                onBackPressed();
                break;
            case R.id.user_header:
                if(!TextUtils.isEmpty(mUser.getAvatar())){
                    Intent intent  = new Intent(UserHomepageAct.this,UserAvatarPreviewAct.class);
                    intent.putExtra(UserAvatarPreviewAct.USER_AVATAR_URL,mUser.getAvatar());
                    startActivity(intent);
                }
                break;
            case R.id.user_homepage_state_isLive:
                //判断是否处于直播状态  若是应该可以点击进去其直播间 1 开播
                if (mUser.getLivestatus().equals("1")) {
                    //请求进入直播房间，准备跳转到房间
                    RequestParam param = RequestParam.builder(this);
                    param.put("device", "android");
                    param.put("rid", uid);
                    CommonUtil.request(this, HttpConfig.API_ENTER_ROOM, param, TAG,
                            new XingBoResponseHandler<RoomModel>(this, RoomModel.class, this) {
                                @Override
                                public void phpXiuErr(int errCode, String msg) {
                                    alert("网络不给力,请检查网络状态");
                                }

                                @Override
                                public void phpXiuSuccess(String response) {
                                    XingBoUtil.log(TAG, "请求进入直播房间结果：" + response);
                                    if (model.getD().getAnchor().getLivestatus().equals("0")) {
                                        Intent intent = new Intent(UserHomepageAct.this, LiveFinishedAct.class);
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, HttpConfig.FILE_SERVER + model.getD().getShare().getLogo());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ID, model.getD().getAnchor().getId());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ONLINES, model.getD().getAnchor().getLiveonlines());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_IS_FOLLOWED, model.getD().isFollowed());
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(UserHomepageAct.this, MainRoom.class);
                                        intent.putExtra(MainRoom.EXTRA_MAIN_ROOM_INFO, model.getD());
                                        intent.putExtra(MainRoom.ANCHOR_LIVE_LOGO, HttpConfig.FILE_SERVER + model.getD().getShare().getLogo());
                                        startActivity(intent);
                                    }
                                }
                            });
                }
                break;
            case R.id.ll_concern:
                CommonUtil.log(TAG, "我的关注");

                if (isNeedInit) {
                    alert("正在初始化,请稍后...");
//                    Toast.makeText(UserHomepageAct.this, "我的关注", Toast.LENGTH_SHORT).show();
                } else {
                    Intent favorite = new Intent(this, UserFavoriteAct.class);
                    favorite.putExtra(UserFavoriteAct.EXTRA_USER_ID, mUser.getId());
                    favorite.putExtra(UserFavoriteAct.EXTRA_IS_SELF, false);
                    startActivity(favorite);
                }

                break;
            case R.id.ll_fans:
                if (isNeedInit) {
                    alert("正在初始化，请稍后...");
                } else {
                    Intent fans = new Intent(this, UserFansAct.class);
                    fans.putExtra(UserFansAct.EXTRA_USER_ID, mUser.getId());
                    fans.putExtra(UserFavoriteAct.EXTRA_IS_SELF, false);
                    startActivity(fans);
                }
                break;
            case R.id.fans_linker://粉丝列表
                if (isNeedInit) {
                    alert("正在初始化，请稍后...");
                } else {
                    Intent fans = new Intent(this, UserFansAct.class);
                    fans.putExtra(UserFansAct.EXTRA_USER_ID, mUser.getId());
                    fans.putExtra(UserFavoriteAct.EXTRA_IS_SELF, false);
                    startActivity(fans);
                }
                break;
            case R.id.favorite_linker://关注列表
                CommonUtil.log(TAG, "我的关注");
                Intent favorite = new Intent(this, UserFavoriteAct.class);
                if (isNeedInit) {
                    alert("正在初始化,请稍后...");
//                    Toast.makeText(UserHomepageAct.this, "我的关注", Toast.LENGTH_SHORT).show();
                } else {
                    favorite.putExtra(UserFavoriteAct.EXTRA_USER_ID, mUser.getId());
                    favorite.putExtra(UserFavoriteAct.EXTRA_IS_SELF, false);
                    startActivity(favorite);
                }
                break;
            case R.id.user_homepage_favorite://关注状态的变化
                if (mUser.isFollowed()) {
                    RequestParam param = RequestParam.builder(this);
                    param.put("uid", uid);
                    CommonUtil.request(this, HttpConfig.API_APP_CANCEL_FOLLOW, param, TAG, new XingBoResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
                        @Override
                        public void phpXiuErr(int errCode, String msg) {
                        }

                        @Override
                        public void phpXiuSuccess(String response) {
                            textFavorite.setText("关注");
                            if (uid.equals(anchorId)) {
                                EventBus.getDefault().post(new MFavoriteEvent(false, anchorId));
                            }
                            requestBaseInfo(mOpera);
                        }
                    });
                }
                if (!mUser.isFollowed()) {
                    RequestParam param = RequestParam.builder(this);
                    param.put("uid", uid);
                    CommonUtil.request(this, HttpConfig.API_APP_ADD_FOLLOW, param, TAG, new XingBoResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
                        @Override
                        public void phpXiuErr(int errCode, String msg) {

                        }

                        @Override
                        public void phpXiuSuccess(String response) {
                            textFavorite.setText("已关注");
                            if (uid.equals(anchorId)) {
                                EventBus.getDefault().post(new MFavoriteEvent(true, anchorId));
                            }
                            requestBaseInfo(mOpera);
                        }
                    });

                }
                break;
            case R.id.user_homepage_message://发送私信
                Intent message = new Intent(this, UserMsgPriDetailAct.class);
                message.putExtra(UserMsgPriDetailAct.EXTRA_USER_ID, uid);
                startActivity(message);
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.user_homepage_radiobutton_home://主页的fragment
                selectFragment(0);
                break;
            case R.id.user_homepage_radiobutton_photo://相册的fragment
                selectFragment(1);
                EventBus.getDefault().post(new UpdatePhotosEvent(uid));
                break;
        }
    }
}
