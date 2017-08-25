package com.xingbo.live.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.controller.RoomController;
import com.xingbo.live.entity.MainUser;
import com.xingbo.live.entity.model.UserInfoModel;
import com.xingbo.live.eventbus.MFavoriteEvent;
import com.xingbo.live.eventbus.PriMessageEvent;
import com.xingbo.live.eventbus.ReplyEvent;
import com.xingbo.live.eventbus.UserInfoEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.popup.ManagePopup;
import com.xingbo.live.popup.ReportPopup;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.ui.UserFansAct;
import com.xingbo.live.ui.UserFavoriteAct;
import com.xingbo.live.ui.UserHomepageAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.extra.cropzoom.imagecropzoom.easing.Linear;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.FrescoImageView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class UserInfoDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "UserInfoDialog";

    private String uid;
    private String rid;
    private BaseAct act;
    private boolean isSelf;
  //  private boolean isInLive;

    private TextView home;
    private TextView reply;
    private TextView secret;
    private TextView concern;
    private TextView fansNum;
    private TextView favorateNum;
    private TextView location;
    private TextView id;
    private ImageView richlvl;
    private ImageView sex;
    private TextView nick;
    private FrescoImageView closeuser;
    private FrescoImageView avator;
    private ImageView userInfoCardClose;

    private TextView manage;
    private View rootView;

    private TextView desc;
    private MainUser userRoomInfo;
    private TextView report;
    private Button errBtn;
    private TextView errMsg;
    private RelativeLayout emptyViewBox;
    private RelativeLayout loadingRl;
    private ImageView loading;
    private AnimationDrawable animationDrawable;
    private RelativeLayout detailRl;
    private LinearLayout operation;
    private ImageView closeImage;

    public UserInfoDialog(Context context) {
        super(context);
        act = (MainRoom) context;
    }

    public UserInfoDialog(Context context, int theme) {
        super(context, theme);
        act = (BaseAct) context;
    }

    public void setData(boolean isSelf, String rid, String uid) {
        this.isSelf = isSelf;
        this.uid = uid;
        this.rid = rid;
        init(act);
        getUserData();
    }

//    public void setData(boolean isSelf,boolean isInLive,String rid,String uid){
//        this.isSelf = isSelf;
//        this.isInLive=isInLive;
//        this.uid = uid;
//        this.rid = rid;
//        init(act);
//        getUserData();
//
//
//    }
    public void init(Context context) {
        rootView = View.inflate(context, R.layout.user_info_card, null);
        detailRl = (RelativeLayout) rootView.findViewById(R.id.user_info_card_detail);
        manage = (TextView) rootView.findViewById(R.id.user_info_card_manage);  //管理
        avator = (FrescoImageView) rootView.findViewById(R.id.user_info_card_avatar);
        closeuser = (FrescoImageView) rootView.findViewById(R.id.user_info_card_close);
        userInfoCardClose = (ImageView) rootView.findViewById(R.id.user_card_close_img);
        userInfoCardClose.setOnClickListener(this);
        nick = (TextView) rootView.findViewById(R.id.user_info_card_nick);
        sex = (ImageView) rootView.findViewById(R.id.user_info_card_sex);
        richlvl = (ImageView) rootView.findViewById(R.id.user_info_card_richlvl);
        id = (TextView) rootView.findViewById(R.id.user_info_card_id);
        location = (TextView) rootView.findViewById(R.id.user_info_card_location);
        desc = (TextView) rootView.findViewById(R.id.user_info_card_desc);
        favorateNum = (TextView) rootView.findViewById(R.id.user_info_card_favorate_num);
        fansNum = (TextView) rootView.findViewById(R.id.user_info_card_fans_num);
        concern = (TextView) rootView.findViewById(R.id.user_info_card_concern);  //关注
        operation = (LinearLayout) rootView.findViewById(R.id.user_info_card_operation);
        report = (TextView) rootView.findViewById(R.id.user_info_card_report);
        secret = (TextView) rootView.findViewById(R.id.user_info_card_secret);
        reply = (TextView) rootView.findViewById(R.id.user_info_card_reply);
        home = (TextView) rootView.findViewById(R.id.user_info_card_home);
        //loading
        loadingRl = (RelativeLayout) rootView.findViewById(R.id.user_info_card_loading_rl);
        loading = (ImageView) rootView.findViewById(R.id.user_info_card_loading);
        this.setContentView(rootView);
        animationDrawable = (AnimationDrawable) loading.getBackground();
        animationDrawable.start();
        if (isSelf) {
            concern.setVisibility(View.GONE);
            operation.setVisibility(View.GONE);
            rootView.findViewById(R.id.info_card_concern).setOnClickListener(this);
            favorateNum.setOnClickListener(this);
            rootView.findViewById(R.id.info_card_fans).setOnClickListener(this);
            fansNum.setOnClickListener(this);
        } else {
            concern.setOnClickListener(this);
            report.setOnClickListener(this);
            secret.setOnClickListener(this);
            reply.setOnClickListener(this);
            home.setOnClickListener(this);
        }
        manage.setOnClickListener(this);
    }

    public void showErrView() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_message));
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }

    public void showErrViewByNetwork() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }

    public void getUserData() {  //API_APP_GET_ROOM_USER_INFO  API_USER_GET_USER_INFO
        //UserInfoModel
        RequestParam param = RequestParam.builder(getContext());
        param.put("rid", rid);  //主播id
        param.put("uid", uid);  //要查看的用户id
        CommonUtil.request(getContext(), HttpConfig.API_USER_GET_USER_INFO, param, TAG, new XingBoResponseHandler<UserInfoModel>(act, UserInfoModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                loadingRl.setVisibility(View.INVISIBLE);
                animationDrawable.stop();
                if (emptyViewBox == null) {
                    showErrViewByNetwork();
                }
                if (emptyViewBox.getVisibility() == View.GONE) {
                    emptyViewBox.setVisibility(View.VISIBLE);
                }
                errMsg.setText("亲,网络不给力,请检查网络连接状态");//按要求 修改为
//                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                loadingRl.setVisibility(View.INVISIBLE);
                animationDrawable.stop();
                detailRl.setVisibility(View.VISIBLE);
                MainUser userRoomInfo = model.getD();
                EventBus.getDefault().post(new UserInfoEvent(userRoomInfo));
            }
        });
    }

    public void showUserInfoCard(UserInfoEvent userInfoEvent) {
        userRoomInfo = userInfoEvent.getUserInfo();
        if (userRoomInfo.getAllow_manage() == 1) {
            manage.setVisibility(View.VISIBLE);
        } else {
            manage.setVisibility(View.GONE);
        }
        avator.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + userRoomInfo.getAvatar()));
        closeuser.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + userRoomInfo.getCloseAvatar()));
        concern.setText(userRoomInfo.isFollowed() ? "已关注" : "关注");
        concern.setBackgroundResource((userRoomInfo.isFollowed() ? R.drawable.user_info_card_hasconcerned : R.drawable.user_info_card_concern));
        nick.setText(userRoomInfo.getNick());
        sex.setImageResource(userRoomInfo.getSex() == "女" ? R.mipmap.female : R.mipmap.male);
        richlvl.setImageResource(XingBoConfig.RICH_LV_ICONS[Integer.parseInt(userRoomInfo.getRichlvl())]);
        id.setText("ID:" + userRoomInfo.getId());
        if (!TextUtils.isEmpty(userRoomInfo.getAddr())) {
            location.setText(userRoomInfo.getAddr());
        }
        if (!TextUtils.isEmpty(userRoomInfo.getIntro())) {
            desc.setText(userRoomInfo.getIntro());
        }
        if (TextUtils.isEmpty(userRoomInfo.getFollowings())) {
            favorateNum.setText("0");
        } else {
            favorateNum.setText(userRoomInfo.getFollowings());
        }
        if (TextUtils.isEmpty(userRoomInfo.getFollowers())) {
            fansNum.setText("0");
        } else {
            fansNum.setText(userRoomInfo.getFollowers());
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_info_card_manage:
                dismiss();
                ManagePopup managePopup = new ManagePopup(act, rid, uid, userRoomInfo);
                managePopup.setFocusable(true);
                managePopup.setOutsideTouchable(true);
                managePopup.setBackgroundDrawable(new BitmapDrawable());
                managePopup.setAnimationStyle(R.style.style_popup);
//                act.showPopupBg();
              /*  managePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                    }
                });*/
                managePopup.showAtLocation(act.rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.user_info_card_concern: //关注
                RoomController controller = new RoomController();
                controller.setMainroomControllerCallback(new RoomController.MainRoomControllerListener() {
                    @Override
                    public void onError(int tag, String msg) {
                        act.alert(msg);
                    }

                    @Override
                    public void onSuccess(int tag, Object modelD) {
                        if (modelD instanceof MFavoriteEvent) {
                            userRoomInfo.setFollowed(!userRoomInfo.isFollowed());
                            concern.setBackgroundResource((userRoomInfo.isFollowed() ? R.drawable.user_info_card_hasconcerned : R.drawable.user_info_card_concern));
                            concern.setText(userRoomInfo.isFollowed() ? "已关注" : "关注");
                            fansNum.setText(userRoomInfo.isFollowed() ? (Integer.parseInt(fansNum.getText().toString().trim()) + 1) + "" : (Integer.parseInt(fansNum.getText().toString().trim()) - 1) + "");
                            EventBus.getDefault().post((MFavoriteEvent) modelD);
                        }
                    }
                });
                controller.favoriteUser(act, userRoomInfo.isFollowed(), userRoomInfo.getId());
                break;
            case R.id.user_info_card_report:
                dismiss();
                ReportPopup reportPopup = new ReportPopup(act, uid);
                reportPopup.setOutsideTouchable(true);
                reportPopup.setFocusable(true);
                reportPopup.setBackgroundDrawable(new BitmapDrawable());
                reportPopup.setAnimationStyle(R.style.style_popup);
               /* reportPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                    }
                });*/
//                act.showPopupBg();
                reportPopup.showAtLocation((act.rootView), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.user_info_card_secret:
                dismiss();
                EventBus.getDefault().post(new PriMessageEvent(userRoomInfo.getId(), userRoomInfo.getNick()));
                break;
            case R.id.user_info_card_reply:
                dismiss();
                EventBus.getDefault().post(new ReplyEvent(userRoomInfo.getNick()));
                break;
            case R.id.user_info_card_home:
                dismiss();
                Intent userHomepageIntent = new Intent(act, UserHomepageAct.class);
                userHomepageIntent.putExtra(UserHomepageAct.EXTRA_USER_ID, uid);
                userHomepageIntent.putExtra(UserHomepageAct.EXTRA_ANCHOR_ID, rid);

                act.startActivity(userHomepageIntent);
                break;
            case R.id.user_card_close_img:
                dismiss();
                break;
            case R.id.info_card_concern:
            case R.id.user_info_card_favorate_num:
                Intent favorite = new Intent(act, UserFavoriteAct.class);
                favorite.putExtra(UserFavoriteAct.EXTRA_USER_ID,uid);
                favorite.putExtra(UserFavoriteAct.EXTRA_IS_SELF, true);
                act.startActivity(favorite);
                break;
            case R.id.info_card_fans:
            case R.id.user_info_card_fans_num:
                Intent fans = new Intent(act, UserFansAct.class);
                fans.putExtra(UserFansAct.EXTRA_USER_ID,uid);
                fans.putExtra(UserFansAct.EXTRA_IS_SELF, true);
                act.startActivity(fans);
                break;

        }
    }
}
