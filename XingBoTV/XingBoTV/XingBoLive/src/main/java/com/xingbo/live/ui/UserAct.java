package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.BaseUser;
import com.xingbo.live.entity.model.MainUserModel;
import com.xingbo.live.entity.model.UnreadCountModel;
import com.xingbo.live.eventbus.ConcernBtnClickEvent;
import com.xingbo.live.eventbus.LoginOutEvent;
import com.xingbo.live.eventbus.PriMsgEvent;
import com.xingbo.live.eventbus.UpdateBalanceEvent;
import com.xingbo.live.eventbus.UserEditEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.IFrescoImageView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.vov.vitamio.utils.Log;


public class UserAct extends BaseAct implements View.OnClickListener {

    private final static String TAG = "UserAct";
    private final static int USER_EDIT_REQUESTCODE = 123;
    private boolean isNeedInit = true;//是否需要初始化
    public BaseUser mUser = null;
    private long currentCoin = 0;
    private int currentFavoriteNum = 0;
    private RelativeLayout userInfoAccount, userInfoIncome, userInfoFansTop, userInfoMessage, userInfoCamera, userInfoSetting;
    private ImageButton userBack, userEdit;//返回按钮 和编辑按钮
    private IFrescoImageView header;
    private IFrescoImageView startLevel;
    private RelativeLayout header2;
    private TextView userId;
    private TextView nick;
    private ImageView sex;
    private ImageView user_level;
    private TextView coin, user_income_num;
    private TextView favorite_num, fans_num;
    private TextView unreadcount_num;
    private int mOpera = XingBo.REQUEST_OPERA_INIT;//请求初始化操作
    private String uid;
    private String userUrl;
    private SharedPreferences sp;
    private RelativeLayout loadingBox;
    private ScrollView userInfoScrollView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemApp.getInstance().addActivity(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_user);
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        uid = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID, "");
        if (sp.getString(XingBo.PX_USER_LOGIN_UID, "0").equals("0")) { //往登录界面
            Intent intent = new Intent(this, LoginAct.class);
            startActivity(intent);
            finish();
        }
        loadingBox = (RelativeLayout) findViewById(R.id.loading_view_box);
        userInfoScrollView = (ScrollView) findViewById(R.id.scroll_viwe_user_info);
        initView();
        unReadCount();
    }


    @Override
    protected void onResume() {
        super.onResume();
        unReadCount();
    }

    @Subscribe
    public void setUnreadcount(PriMsgEvent priMsgEvent) {
        unReadCount();
    }

    @Subscribe
    public void userAvatarUpdate(UserEditEvent editEvent) {
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        userUrl = sp.getString(XingBo.PX_USER_LOGIN_AVATAR, "1");
        header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + userUrl));
    }

    @Subscribe
    public void loginOut(LoginOutEvent loginOutEvent) {
        finish();
    }

    @Subscribe
    public void concernBtn(ConcernBtnClickEvent concernBtnClickEvent) {
        request(XingBo.REQUEST_OPERA_INIT);
    }


    private void unReadCount() {
        RequestParam param = RequestParam.builder(this);
        param.put("uid", uid);
        CommonUtil.request(this, HttpConfig.API_USER_GET_UNREAD_COUNT, param, TAG, new XingBoResponseHandler<UnreadCountModel>(UnreadCountModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {

            }

            @Override
            public void phpXiuSuccess(String response) {
                String unreadCount = model.getD();
                int unread = Integer.parseInt(unreadCount);
                if (unread == 0) {
                    unreadcount_num.setVisibility(View.INVISIBLE);
                } else {
                    unreadcount_num.setVisibility(View.VISIBLE);
                    unreadcount_num.setText(unreadCount);
                }
            }
        });


    }

    private void initView() {
        /**
         *  我的账户,收益，粉丝贡献榜。消息列表，相册，设置
         */
        userInfoAccount = (RelativeLayout) findViewById(R.id.user_account);
        userInfoAccount.setOnClickListener(this);
        userInfoIncome = (RelativeLayout) findViewById(R.id.user_income);
        userInfoIncome.setOnClickListener(this);
        userInfoFansTop = (RelativeLayout) findViewById(R.id.user_fans_top);
        userInfoFansTop.setOnClickListener(this);
        userInfoMessage = (RelativeLayout) findViewById(R.id.user_message);
        userInfoMessage.setOnClickListener(this);
        userInfoCamera = (RelativeLayout) findViewById(R.id.user_camera);
        userInfoCamera.setOnClickListener(this);
        userInfoSetting = (RelativeLayout) findViewById(R.id.user_setting);
        userInfoSetting.setOnClickListener(this);
        startLevel = (IFrescoImageView) findViewById(R.id.start_level_icon);
        findViewById(R.id.ll_concern).setOnClickListener(this);
        findViewById(R.id.ll_fans).setOnClickListener(this);

        /**
         * 顶部 返回按钮  个人信息编辑
         */
        userBack = (ImageButton) findViewById(R.id.user_back_btn);
        userBack.setOnClickListener(this);
        userEdit = (ImageButton) findViewById(R.id.user_edit_btn);
        userEdit.setOnClickListener(this);

        /**
         * 头布局  头像 昵称 id 性别 等级标 关注数量， 粉丝数量
         */
        header = (IFrescoImageView) findViewById(R.id.user_header);
        header.setOnClickListener(this);
        header2 = (RelativeLayout) findViewById(R.id.user_home_header);
        header2.findViewById(R.id.fans_linker).setOnClickListener(this);
        header2.findViewById(R.id.favorite_linker).setOnClickListener(this);
        nick = (TextView) findViewById(R.id.user_neck);
        userId = (TextView) findViewById(R.id.user_id);
        sex = (ImageView) findViewById(R.id.user_sex_icon);
        user_level = (ImageView) findViewById(R.id.user_leve_icon);
        favorite_num = (TextView) findViewById(R.id.favorite_num);//粉丝
        favorite_num.setOnClickListener(this);
        fans_num = (TextView) findViewById(R.id.fans_num);//关注
        fans_num.setOnClickListener(this);
        coin = (TextView) findViewById(R.id.user_account_num);
        user_income_num = (TextView) findViewById(R.id.user_income_num);
        unreadcount_num = (TextView) findViewById(R.id.user_msg_unreadcount);
        if (isNeedInit) {
            request(XingBo.REQUEST_OPERA_INIT);
            unReadCount();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_header:
                Intent intent = new Intent(this, UserAvatarChangeAct.class);
                if (!TextUtils.isEmpty(mUser.getAvatar())) {
                    intent.putExtra(UserAvatarChangeAct.USER_AVATAR_URL, mUser.getAvatar());

                } else {
                    intent.putExtra(UserAvatarChangeAct.USER_AVATAR_URL, "res:///" + R.mipmap.avatar_placeholder);
                }
                startActivity(intent);
                break;
            case R.id.ll_concern:
                CommonUtil.log(TAG, "我的关注");
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                    //Toast.makeText(UserAct.this, "我的关注", Toast.LENGTH_SHORT).show();
                } else {
                    Intent favorite = new Intent(this, UserFavoriteAct.class);
                    favorite.putExtra(UserFavoriteAct.EXTRA_USER_ID, mUser.getId());
                    favorite.putExtra(UserFavoriteAct.EXTRA_IS_SELF, true);
                    startActivity(favorite);
                }
                break;
            case R.id.ll_fans:
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                } else {
                    Intent fans = new Intent(this, UserFansAct.class);
                    fans.putExtra(UserFansAct.EXTRA_USER_ID, mUser.getId());
                    fans.putExtra(UserFansAct.EXTRA_IS_SELF, true);
                    startActivity(fans);
                }
                break;
            case R.id.favorite_linker://我的关注
                CommonUtil.log(TAG, "我的关注");
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                    //Toast.makeText(UserAct.this, "我的关注", Toast.LENGTH_SHORT).show();
                } else {
                    Intent favorite = new Intent(this, UserFavoriteAct.class);
                    favorite.putExtra(UserFavoriteAct.EXTRA_USER_ID, mUser.getId());
                    favorite.putExtra(UserFavoriteAct.EXTRA_IS_SELF, true);
                    startActivity(favorite);
                }
                break;

            case R.id.fans_linker://我的粉丝
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                } else {
                    Intent fans = new Intent(this, UserFansAct.class);
                    fans.putExtra(UserFansAct.EXTRA_USER_ID, mUser.getId());
                    fans.putExtra(UserFansAct.EXTRA_IS_SELF, true);
                    startActivity(fans);
                }

                break;
            case R.id.user_account://我的账号信息
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                } else {
                    Intent iAccount = new Intent(UserAct.this, UserAccountAct.class);
                    iAccount.putExtra(UserAccountAct.EXTRA_USER_COIN, mUser.getCoin());
                    startActivity(iAccount);
                }

                break;
            case R.id.user_income://我的收益
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                } else {
                    Intent iIncome = new Intent(UserAct.this, UserIncomeAct.class);
                    startActivity(iIncome);
                }
                break;
            case R.id.user_camera://我的相册
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                } else {
                    Intent myPhotos = new Intent(this, UserPhotosAct.class);
                    myPhotos.putExtra(UserPhotosAct.EXTRA_USER_ID, mUser.getId());
                    startActivity(myPhotos);
                }
                break;
            case R.id.user_fans_top://我的粉丝贡献排行榜
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                } else {
                    Intent contributionFans = new Intent(this, UserFansContributeAct.class);
                    contributionFans.putExtra(UserFansContributeAct.ANCHOR_GUARD_ID, mUser.getId());
                    contributionFans.putExtra(UserFansContributeAct.USER_COIN, "123333星");
                    startActivity(contributionFans);
                }
                break;
            case R.id.user_setting://设置
                Intent setting = new Intent(UserAct.this, SettingAct.class);
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                    return;
                }
                setting.putExtra(SettingAct.EXTRA_IS_LOGIN, !isNeedInit);
                setting.putExtra(SettingAct.EXTRA_MUSER, mUser);
                startActivity(setting);
                break;

            case R.id.user_message://我的消息
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                } else {
                    Intent messageIntent = new Intent(UserAct.this, UserMsgAct.class);
                    messageIntent.putExtra(UserMsgAct.EXTRA_USER_ID, mUser.getId());
                    startActivity(messageIntent);
                }
                break;
            case R.id.user_back_btn://返回按钮
                onBackPressed();
                break;
            case R.id.user_edit_btn://个人信息编辑 按钮
                CommonUtil.log(TAG, "点击编辑按钮" + isNeedInit);
                if (isNeedInit) {
                    alert("获取数据失败，请检查网络连接...");
                } else {
                    Intent iUser = new Intent(UserAct.this, UserBaseInfoAct.class);
                    iUser.putExtra(UserBaseInfoAct.BASE_USER_INFO, mUser);
                    startActivityForResult(iUser, USER_EDIT_REQUESTCODE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case USER_EDIT_REQUESTCODE:
                request(XingBo.REQUEST_OPERA_REFRESH);
                break;
        }
    }


    /**
     * 请求初始化
     */
    private void request(int opera) {
        mOpera = opera;
        RequestParam param = RequestParam.builder(this);
        CommonUtil.request(this, HttpConfig.API_USER_GET_PROFILE, param, TAG, new XingBoResponseHandler<MainUserModel>(MainUserModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                Log.d(TAG, msg);
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                }
                alert("获取数据失败，请检查网络连接...");


            }

            @Override
            public void phpXiuSuccess(String response) {
                XingBoUtil.log(TAG, "用户中心初始化结果" + response);
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                }
//                if (userInfoScrollView.getVisibility()==View.INVISIBLE){
//                    userInfoScrollView.setVisibility(View.VISIBLE);
//                }
                mUser = model.getD();
                if (mUser == null) {
                    alert("初始化失败！");
                    return;
                }
                //  header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + mUser.getAvatar()));
                nick.setText(mUser.getNick());
                userId.setText("星播号：" + mUser.getId());
                if (mUser.getSex().equals("男")) {
                    sex.setImageResource(R.mipmap.male);
                } else {
                    sex.setImageResource(R.mipmap.female);
                }
                coin.setText(mUser.getCoin() + "星币");
                try {
                    currentCoin = Long.parseLong(mUser.getCoin());
                } catch (NumberFormatException e) {
                    currentCoin = 0;
                }
                //总收益
                user_income_num.setText(mUser.getTotalgain() + "星钻");
                fans_num.setText(mUser.getFollowers());
                try {
                    currentFavoriteNum = Integer.parseInt(mUser.getFollowings());
                } catch (NumberFormatException e) {

                }
                favorite_num.setText(currentFavoriteNum == 0 ? "0" : "" + currentFavoriteNum);

                if (!TextUtils.isEmpty(model.getD().getAvatar())) {
                    header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + model.getD().getAvatar()));
                } else {
                    header.setImageURI(Uri.parse("res:///" + R.mipmap.avatar_placeholder));
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

                //设置用户星播等级
                int slv = 0;
                try {
                    slv = Integer.parseInt(mUser.getAnchorlvl());
                } catch (NumberFormatException e) {
                    slv = 0;
                }
                if (slv < 41) {
                    startLevel.setImageResource(XingBoConfig.STAR_LV_ICONS[slv]);

                } else {
                    startLevel.setImageResource(XingBoConfig.STAR_LV_ICONS[40]);
                }
                isNeedInit = false;
            }
        });
    }

    /**
     * 更新余额
     */
    @Subscribe
    public void updateCoin(UpdateBalanceEvent event) {
        try {
            currentCoin = Long.parseLong(event.getCurrentCoin());
            coin.setText(currentCoin + "星币");
        } catch (NumberFormatException e) {

        }
    }

}
