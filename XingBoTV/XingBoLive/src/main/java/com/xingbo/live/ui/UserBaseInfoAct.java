package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.BaseUser;
import com.xingbo.live.eventbus.UserEditEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.view.widget.PicSelectorMenu;
import com.xingbobase.config.XingBo;
import com.xingbobase.eventbus.CropEvent;
import com.xingbobase.extra.pickerview.OptionsPopupWindow;
import com.xingbobase.extra.pickerview.TimePopupWindow;
import com.xingbobase.extra.pickerview.TwoOptionsPopupWindow;
import com.xingbobase.extra.pickerview.WheelItem;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.UploadFileResponseModel;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.XingBoUploadHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.vov.vitamio.utils.Log;

/**
 * Created by WuJinZhou on 2015/11/3.
 */
public class UserBaseInfoAct extends BaseAct implements View.OnClickListener {
    public final static String TAG = "UserBaseInfoAct";
    public final static String BASE_USER_INFO = "extra_base_user_info";
    private final static int UPLOAD_HEADER = 0x516;
    public final static int SOURCE_TAG_CODE_HEADER = 0x202;

    public final static int EDIT_AVATAR_REQUEST=0x514;
    public final static int EDIT_NICK_REQUEST = 0x518;
    public final static int EDIT_INTRO_REQUEST = 0x521;
    public final static int EDIT_BIRTH_REQUEST = 0x523;
    public final static int EDIT_SEX_REQUEST = 0x600;


    private BaseUser user;
    private FrescoImageView header;
    private TextView nameTv, birthTv, sexTv, addrTv, introTv;
    private TwoOptionsPopupWindow sexOption;
    private TimePopupWindow pwTime;
    private OptionsPopupWindow popupWindowAddress;
    private ArrayList<WheelItem> provinceItems = new ArrayList<WheelItem>();
    private ArrayList<ArrayList<WheelItem>> cityItems = new ArrayList<ArrayList<WheelItem>>();
    private PicSelectorMenu picSelector;
//    private String birthday;

    private RelativeLayout loadingBox;
    private RelativeLayout headerBox, nameBox, introBox, sexBox, birthBox, addrBox;
    private ImageView loadingImage;
    private TextView uploadPro;
    private TextView xingboNum;
    private AnimationDrawable uploadAni;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        user = (BaseUser) getIntent().getSerializableExtra(BASE_USER_INFO);
        if (user == null) {
            finish();
            return;
        }
        setContentView(R.layout.user_base_info);
        headerBox = (RelativeLayout) findViewById(R.id.header_box);//头像
        headerBox.setOnClickListener(this);
        nameBox = (RelativeLayout) findViewById(R.id.name_box); //昵称
        nameBox.setOnClickListener(this);
        introBox = (RelativeLayout) findViewById(R.id.intro_box);//个人签名
        introBox.setOnClickListener(this);
        sexBox = (RelativeLayout) findViewById(R.id.sex_box);//性别
        sexBox.setOnClickListener(this);
        birthBox = (RelativeLayout) findViewById(R.id.birth_box);//生日
        birthBox.setOnClickListener(this);
        addrBox = (RelativeLayout) findViewById(R.id.addr_box);//地址
        addrBox.setOnClickListener(this);
        header = (FrescoImageView) findViewById(R.id.header);
        String headerUrl = user.getAvatar();
        header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + headerUrl));

        xingboNum = (TextView) findViewById(R.id.xingbo_num);
        xingboNum.setText(user.getId() + "");
        nameTv = (TextView) findViewById(R.id.name);
        birthTv = (TextView) findViewById(R.id.birth);
        sexTv = (TextView) findViewById(R.id.sex);
        addrTv = (TextView) findViewById(R.id.addr);
        introTv = (TextView) findViewById(R.id.intro);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        loadingBox = (RelativeLayout) findViewById(R.id.loading_box);
        loadingImage = (ImageView) findViewById(R.id.loading_header_view);
        uploadPro = (TextView) findViewById(R.id.header_upload_pro);
        uploadAni = (AnimationDrawable) loadingImage.getBackground();
        init();
    }

    @Subscribe
    public void userAvatarChange(UserEditEvent editEvent){

    }
    public void init() {
        nameTv.setText(user.getNick());
        birthTv.setText(user.getBirth());
        sexTv.setText(user.getSex());
        addrTv.setText(user.getAddr());
        introTv.setText(user.getIntro());
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_box:
                Intent header = new Intent(UserBaseInfoAct.this, UserAvatarChangeAct.class);
                if (!TextUtils.isEmpty(user.getAvatar())) {
                    header.putExtra(UserAvatarChangeAct.USER_AVATAR_URL, user.getAvatar());
                } else {
                    header.putExtra(UserAvatarChangeAct.USER_AVATAR_URL, "res:///" + R.mipmap.avatar_placeholder);
                }
                startActivity(header);
//                  editHeader();
                break;
            case R.id.name_box://昵称修改
                Intent iName = new Intent(this, UserNickAct.class);
                iName.putExtra(UserNickAct.EXTRA_NICK, nameTv.getText().toString());
                startActivityForResult(iName, EDIT_NICK_REQUEST);
                break;
            case R.id.sex_box://性别修改
                Intent iSex = new Intent(this, UserSexAct.class);
                iSex.putExtra(UserSexAct.EXTRA_SEX, sexTv.getText().toString());
                startActivityForResult(iSex, EDIT_SEX_REQUEST);
                break;
            case R.id.birth_box://生日修改
//                showDatePicker();
                Intent dataIntent = new Intent(this, UserBirthAct.class);
                dataIntent.putExtra(UserBirthAct.EXTRA_USER_BIRTH, birthTv.getText().toString());
                startActivityForResult(dataIntent, EDIT_BIRTH_REQUEST);
                break;
            case R.id.addr_box:
                showZonePop();
                break;
            case R.id.intro_box:
                Intent intent = new Intent(this, UserIntroAct.class);
                intent.putExtra(UserIntroAct.EXTRA_USER_INTRO, introTv.getText().toString());
                startActivityForResult(intent, EDIT_INTRO_REQUEST);
                break;
            case R.id.top_back_btn:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    public void showSexPop() {
        if (sexOption == null) {
            sexOption = new TwoOptionsPopupWindow(this);
            sexOption.setListener(this);
        }
        sexOption.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    public void showDatePicker() {
        if (pwTime == null) {
            pwTime = new TimePopupWindow(this, TimePopupWindow.Type.YEAR_MONTH_DAY);
            pwTime.setRange(1980, 2020);
            pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    birthTv.setText(CommonUtil.dateFormatYMD(date));
                    updateBirth(CommonUtil.dateFormatYMD(date));

                }
            });
        }
        pwTime.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0, new Date());
    }

    /**
     * 地区选择
     */
    public void showZonePop() {
        if (popupWindowAddress == null) {
            popupWindowAddress = new OptionsPopupWindow(this);
            popupWindowAddress.setOnoptionsSelectListener(new OptionsPopupWindow.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3) {
                    WheelItem prov = provinceItems.get(options1);
                    WheelItem city = cityItems.get(options1).get(option2);
                    addrTv.setText(prov.getTitle().equals(city.getTitle()) ? prov.getTitle() : prov.getTitle() + " " + city.getTitle());
                    updateAddress(addrTv.getText().toString());
                }
            });
            String[] province = getResources().getStringArray(R.array.province_item);
            String[] cities = new String[0];
            for (int i = 0; i < province.length; i++) {
                provinceItems.add(new WheelItem(i + "", province[i]));
                ArrayList<WheelItem> cityList = new ArrayList<WheelItem>();
                cities = getResources().getStringArray(XingBoConfig.ARRAY_CITY[i]);
                for (int j = 0; j < cities.length; j++) {
                    cityList.add(new WheelItem(j + "", cities[j]));
                }
                cityItems.add(cityList);
            }
            popupWindowAddress.setPicker(provinceItems, cityItems, true);
            popupWindowAddress.setSelectOptions(0, 0);
        }
        popupWindowAddress.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 更改性别
     */
    public void updateSex(final String sex) {
        findViewById(R.id.sex).setClickable(false);
        findViewById(R.id.open_right_icon3).setClickable(false);
        RequestParam param = RequestParam.builder(this);
        param.put("sex", sex);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                findViewById(R.id.sex).setClickable(true);
                findViewById(R.id.open_right_icon3).setClickable(true);
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                findViewById(R.id.sex).setClickable(true);
                findViewById(R.id.open_right_icon3).setClickable(true);
                XingBoUtil.log(TAG, "提交保存性别结果" + response);
                user.setSex(sex);
                EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_SEX, user));
                //alert("性别修改成功");
                XingBoUtil.tip(UserBaseInfoAct.this, "性别修改成功!", Gravity.CENTER);
            }
        });
    }


    /***
     * 修改生日
     */
    public void updateBirth(final String birth) {
        findViewById(R.id.birth).setClickable(false);
        findViewById(R.id.open_right_icon5).setClickable(false);
        RequestParam param = RequestParam.builder(this);
        param.put("birth", birth);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                findViewById(R.id.birth).setClickable(true);
                findViewById(R.id.open_right_icon5).setClickable(true);
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                findViewById(R.id.birth).setClickable(true);
                findViewById(R.id.open_right_icon5).setClickable(true);
                XingBoUtil.log(TAG, "提交保存生日结果" + response);
                user.setBirth(birth);
                EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_BIRTH, user));
                //alert("生日修改成功");
                XingBoUtil.tip(UserBaseInfoAct.this, "生日修改成功!", Gravity.CENTER);
            }
        });
    }

    /***
     * 修改地区
     */
    public void updateAddress(final String address) {
        findViewById(R.id.addr).setClickable(false);
        findViewById(R.id.open_right_icon6).setClickable(false);
        RequestParam param = RequestParam.builder(this);
        param.put("addr", address);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                findViewById(R.id.addr).setClickable(true);
                findViewById(R.id.open_right_icon6).setClickable(true);
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                findViewById(R.id.addr).setClickable(true);
                findViewById(R.id.open_right_icon6).setClickable(true);
                XingBoUtil.log(TAG, "提交保存地址结果" + response);
                user.setAddr(address);
                EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_ADDRESS, user));
                //alert("地址修改成功");
                XingBoUtil.tip(UserBaseInfoAct.this, "地址修改成功!", Gravity.CENTER);
            }
        });
    }

    /**
     * 编辑头像
     */
    private void editHeader() {
        CommonUtil.log(TAG, "点击了头像");
        if (picSelector == null) {
            picSelector = new PicSelectorMenu(this, SOURCE_TAG_CODE_HEADER);
        }
        picSelector.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Subscribe
    public void uploadFile(CropEvent event) {
        if (event.getSourceTagCode() == SOURCE_TAG_CODE_HEADER) {
            if (event.getPath() == null) {
                return;
            }
            Message msg = handler.obtainMessage();
            msg.what = UPLOAD_HEADER;
            msg.obj = event.getPath();
            handler.sendMessage(msg);
        }
    }

    /**
     * 上传头像
     */
    public void uploadHeader(final String path) {
        XingBoUtil.log(TAG, "新头像图片路径:" + path);
        if (loadingBox.getVisibility() == View.GONE) {
            loadingBox.setVisibility(View.VISIBLE);
            uploadAni.start();
        }
        CommonUtil.uploadFile(this,path, new XingBoUploadHandler<UploadFileResponseModel>(UploadFileResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                    uploadAni.stop();
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                XingBoUtil.log(TAG, "上传结果" + response);
                Log.d("tag123", "上传结果:" + model.getUrl());
                sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(XingBo.PX_USER_LOGIN_AVATAR, model.getUrl());
                editor.commit();
                updateHeader(model.getUrl());

                header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + model.getUrl()));
            }

            @Override
            public void phpXiuProgress(long bytesWritten, long totalSize) {
                XingBoUtil.log("上传进度", bytesWritten + "@@@@@" + totalSize);
                uploadPro.setText(((int) (bytesWritten / totalSize) * 100) + "%");
            }
        });
    }

    /**
     * 提交更新头像
     */
    private void updateHeader(final String url) {
        RequestParam param = RequestParam.builder(this);
        param.put("avatar", url);
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                    uploadAni.stop();
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                    uploadAni.stop();
                }
                XingBoUtil.log(TAG, "提交保存头像结果" + response);
                Log.d("tag", "提交保存头像结果" + response);
                header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + url));
                //alert("头像修改成功");
                XingBoUtil.tip(UserBaseInfoAct.this, "头像修改成功!", Gravity.CENTER);
                user.setAvatar(url);
                EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_HEADER, user));
            }
        });
    }

    public void handleMsg(Message msg) {
        switch (msg.what) {
            case UPLOAD_HEADER:
                uploadHeader(msg.obj.toString());
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case EDIT_NICK_REQUEST:// 昵称
                    nameTv.setText(data.getStringExtra("nick_name"));
                    user.setNick(data.getStringExtra("nick_name"));
                    EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_NICK, user));
                    break;
                case EDIT_INTRO_REQUEST:// 签名
                    introTv.setText(data.getStringExtra("user_intro"));
                    user.setIntro(data.getStringExtra("user_intro"));
                    EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_INTRO, user));
                    break;
                case EDIT_BIRTH_REQUEST://生日
                    birthTv.setText(data.getStringExtra("user_birth"));
                    user.setBirth(data.getStringExtra("user_birth"));
                    EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_BIRTH, user));
                    break;
                case EDIT_SEX_REQUEST:// 性别
                    sexTv.setText(data.getStringExtra("user_sex"));
                    user.setSex(data.getStringExtra("user_sex"));
                    EventBus.getDefault().post(new UserEditEvent(UserEditEvent.UPDATE_SEX, user));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        handler.removeCallbacksAndMessages(null);
        super.finish();
    }
}
