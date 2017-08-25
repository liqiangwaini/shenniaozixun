package com.xingbo.live.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.popup.AreaPopup;
import com.xingbo.live.popup.BankPopup;
import com.xingbo.live.popup.CityPopup;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.view.widget.PicSelectorMenu;
import com.xingbobase.config.XingBo;
import com.xingbobase.eventbus.CropEvent;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.UploadFileResponseModel;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.XingBoUploadHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by xingbo_szd on 2016/8/15.
 */
public class ApplyTobeAnchorAct extends BaseAct implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, BankPopup.OnBankNameCallback, CityPopup.OnCityAreaNameCallback, AreaPopup.OnAreaNameCallback {
    private static final String TAG = "ApplyTobeAnchorAct";
    public final static int SOURCE_TAG_CODE_ID = 0x100;
    public final static int SOURCE_TAG_CODE_COVER_LOGO = 0x101;
    public final static int SOURCE_TAG_CODE_ART_LOGO = 0x102;

    private FrameLayout back;
    private TextView id;
    private EditText realName;
    private RadioGroup sex;
    private RadioButton male;
    private RadioButton female;
    private EditText ID;
    private EditText phone;
    private EditText qq;
    private EditText bankNo;
    private TextView bankId;
    private TextView city;
    private TextView area;
    private EditText branch;
    private EditText account;
    private FrescoImageView idPhonto;
    private FrescoImageView logo;
    private FrescoImageView artLogo;
    private EditText acquirement;
    private TextView commit;

    private String gender = XingBoConfig.MALE;
    private String uid;
    private View rootView;
    private String coverLogo;
    private String idPhoto;
    private String anchorArtLogo;
    private int currentCityId;
    private RelativeLayout loadingBoxID;
    private ImageView loadingHeaderviewID;
    private TextView uploadProID;
    private RelativeLayout loadingBoxCoverlogo;
    private ImageView loadingHeaderviewCoverLogo;
    private TextView uploadProCoverLogo;
    private RelativeLayout loadingBoxArtLogo;
    private ImageView loadingHeaderviewArtLogo;
    private TextView uploadProArtLogo;
    private AnimationDrawable uploadAniID;
    private AnimationDrawable uploadAniCoverLogo;
    private AnimationDrawable uploadAniArtLogo;
    private EditText addr;
    private String idUrl;
    private String artLogoUrl;
    private String coverLogoUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        rootView = View.inflate(this, R.layout.apply_tobe_anchor, null);
        setContentView(rootView);
        SharedPreferences sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        uid = sp.getString(XingBo.PX_USER_LOGIN_UID, "");

        initView();

    }

    public void initView() {
        back = (FrameLayout) rootView.findViewById(R.id.fl_apply_anchor_back);
        id = (TextView) rootView.findViewById(R.id.apply_anchor_id);
        realName = (EditText) rootView.findViewById(R.id.apply_anchor_realname);
        sex = (RadioGroup) rootView.findViewById(R.id.apply_anchor_sex);
        male = (RadioButton) rootView.findViewById(R.id.apply_anchor_sex_male);
        female = (RadioButton) rootView.findViewById(R.id.apply_anchor_sex_female);
        addr = (EditText) rootView.findViewById(R.id.apply_anchor_addr);
        ID = (EditText) rootView.findViewById(R.id.apply_anchor_ID);
        phone = (EditText) rootView.findViewById(R.id.apply_anchor_phone);
        qq = (EditText) rootView.findViewById(R.id.apply_anchor_qq);
        bankNo = (EditText) rootView.findViewById(R.id.apply_anchor_card_no);
        bankId = (TextView) rootView.findViewById(R.id.apply_anchor_bankid);
        city = (TextView) rootView.findViewById(R.id.apply_anchor_city);
        area = (TextView) rootView.findViewById(R.id.apply_anchor_area);
        branch = (EditText) rootView.findViewById(R.id.apply_anchor_branch);
        account = (EditText) rootView.findViewById(R.id.apply_anchor_account);
        idPhonto = (FrescoImageView) rootView.findViewById(R.id.apply_anchor_id_photo);
        logo = (FrescoImageView) rootView.findViewById(R.id.apply_anchor_logo);
        artLogo = (FrescoImageView) rootView.findViewById(R.id.apply_anchor_art_logo);
        acquirement = (EditText) rootView.findViewById(R.id.apply_anchor_acqierement);
        commit = (TextView) rootView.findViewById(R.id.apply_anchor_commit);
        //pro
        loadingBoxID = (RelativeLayout) rootView.findViewById(R.id.loading_box_ID);
        loadingHeaderviewID = (ImageView) rootView.findViewById(R.id.loading_header_view_ID);
        uploadProID = (TextView) rootView.findViewById(R.id.header_upload_pro_ID);
        loadingBoxCoverlogo = (RelativeLayout) rootView.findViewById(R.id.loading_box_cover_logo);
        loadingHeaderviewCoverLogo = (ImageView) rootView.findViewById(R.id.loading_header_view_cover_logo);
        uploadProCoverLogo = (TextView) rootView.findViewById(R.id.header_upload_pro_cover_logo);
        loadingBoxArtLogo = (RelativeLayout) rootView.findViewById(R.id.loading_box_art_logo);
        loadingHeaderviewArtLogo = (ImageView) rootView.findViewById(R.id.loading_header_view_art_logo);
        uploadProArtLogo = (TextView) rootView.findViewById(R.id.header_upload_pro_art_logo);
        uploadAniID = (AnimationDrawable) loadingHeaderviewID.getBackground();
        uploadAniCoverLogo = (AnimationDrawable) loadingHeaderviewCoverLogo.getBackground();
        uploadAniArtLogo = (AnimationDrawable) loadingHeaderviewArtLogo.getBackground();
        //initdata
        id.setText(uid);

        //click
        back.setOnClickListener(this);
        sex.setOnCheckedChangeListener(this);
        bankId.setOnClickListener(this);
        city.setOnClickListener(this);
        area.setOnClickListener(this);
        idPhonto.setOnClickListener(this);
        logo.setOnClickListener(this);
        artLogo.setOnClickListener(this);
        commit.setOnClickListener(this);

    }

    public void applyTobeAnchor(String uid, String realname, String sex, String birth, String addr, String phone,
                                String qq, String bank_no, String bank_name, String bank_addr, String bank_user_name, String idcard,
                                String idcard_img, String live_img, String posterlogo, String video_url) {
        RequestParam param = RequestParam.builder(this);
        param.put("uid", uid);
        param.put("realname", realname);
        param.put("sex", sex);
        param.put("birth", birth);
        param.put("addr", addr);
        param.put("phone", phone);
        param.put("qq", qq);
        param.put("bank_no", bank_no);
        param.put("bank_name", bank_name);
        param.put("bank_addr", bank_addr);  //银行网点支行
        param.put("bank_user_name", bank_user_name);
        param.put("idcard", idcard);
        param.put("idcard_img", idcard_img);
        param.put("live_img", live_img);
        param.put("posterlogo", posterlogo);
        param.put("video_url", video_url);
        CommonUtil.request(this, HttpConfig.API_APP_APPLY_TOBE_ANCHOR, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
//                alert(msg);
                alert(model.getM());
            }

            @Override
            public void phpXiuSuccess(String response) {
                alert("提交申请成功，请耐心等待...");
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.apply_anchor_sex_female) {
            gender = XingBoConfig.FEMALE;
        } else {
            gender = XingBoConfig.MALE;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_anchor_commit:
                String anchorRealName = realName.getText().toString().trim();
                if (TextUtils.isEmpty(anchorRealName)) {
                    alert("真实姓名为空");
                    return;
                }
                String anchorAddr = addr.getText().toString().trim();
                if (TextUtils.isEmpty(anchorAddr)) {
                    alert("地址信息为空");
                    return;
                }
                String anchorID = ID.getText().toString().trim();
                if (TextUtils.isEmpty(anchorID)) {
                    alert("身份证为空");
                    return;
                }
                if (anchorID.length() != 18) {
                    alert("身份证输入错误，请重新输入");
                    return;
                }
                String anchorPhone = phone.getText().toString().trim();
                if (TextUtils.isEmpty(anchorPhone)) {
                    alert("手机号为空");
                    return;
                }
                if (!XingBoUtil.isMobileNo(anchorPhone)) {
                    alert("手机号格式错误,请重新输入");
                    return;
                }
                String anchorQQ = qq.getText().toString().trim();
                if (TextUtils.isEmpty(anchorQQ)) {
                    alert("QQ号为空");
                    return;
                }
                final String anchorBankNo = bankNo.getText().toString().trim();
                if (TextUtils.isEmpty(anchorBankNo)) {
                    alert("银行卡号为空");
                    return;
                }
                String anchorBranch = branch.getText().toString().trim();
                if (TextUtils.isEmpty(anchorBranch)) {
                    alert("支行信息为空");
                    return;
                }
                String anchorAccount = account.getText().toString().trim();
                if (TextUtils.isEmpty(anchorAccount)) {
                    alert("开户人姓名为空");
                    return;
                }
                String anchorAcquirement = acquirement.getText().toString().trim();
                if (TextUtils.isEmpty(anchorAcquirement)) {
                    alert("才艺视频链接为空");
                    return;
                }
                if (TextUtils.isEmpty(idUrl)) {
                    alert("身份证照为空");
                    return;
                }
                if (TextUtils.isEmpty(coverLogoUrl)) {
                    alert("直播封面照为空");
                    return;
                }
                if (TextUtils.isEmpty(artLogoUrl)) {
                    alert("直播艺术照为空");
                    return;
                }
                applyTobeAnchor(uid, anchorRealName, gender, "birth", anchorAddr, anchorPhone, anchorQQ, anchorBankNo, bankId.getText().toString().trim(),
                        anchorBranch, anchorAccount, anchorID, idUrl, coverLogoUrl, artLogoUrl, anchorAcquirement);
                break;
            case R.id.apply_anchor_bankid:
                BankPopup bankPopup = new BankPopup(this);
                bankPopup.setOutsideTouchable(true);
                bankPopup.setBackgroundDrawable(new BitmapDrawable());
                bankPopup.setFocusable(true);
                bankPopup.setBankNameCallback(this);
                bankPopup.showAsDropDown(bankId);
                break;
            case R.id.apply_anchor_city:
                CityPopup cityAreaPopup = new CityPopup(this);
                cityAreaPopup.setOutsideTouchable(true);
                cityAreaPopup.setBackgroundDrawable(new BitmapDrawable());
                cityAreaPopup.setFocusable(true);
                cityAreaPopup.setCityNameCallback(this);
                cityAreaPopup.showAsDropDown(city);
                break;
            case R.id.apply_anchor_area:
                AreaPopup areaPopup = new AreaPopup(this, currentCityId);
                areaPopup.setOutsideTouchable(true);
                areaPopup.setBackgroundDrawable(new BitmapDrawable());
                areaPopup.setFocusable(true);
                areaPopup.setAreaNameCallback(this);
                areaPopup.showAsDropDown(city);
                break;
            case R.id.apply_anchor_id_photo:
                if (picSelector == null) {
                    picSelector = new PicSelectorMenu(this, SOURCE_TAG_CODE_ID);
                } else {
                    picSelector.setSourceTagCode(SOURCE_TAG_CODE_ID);
                }
                picSelector.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.apply_anchor_logo:
                if (picSelector == null) {
                    picSelector = new PicSelectorMenu(this, SOURCE_TAG_CODE_COVER_LOGO);
                } else {
                    picSelector.setSourceTagCode(SOURCE_TAG_CODE_COVER_LOGO);
                }
                picSelector.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.apply_anchor_art_logo:
                if (picSelector == null) {
                    picSelector = new PicSelectorMenu(this, SOURCE_TAG_CODE_ART_LOGO);
                } else {
                    picSelector.setSourceTagCode(SOURCE_TAG_CODE_ART_LOGO);
                }
                picSelector.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.fl_apply_anchor_back:
                finish();
                break;
        }
    }

    private PicSelectorMenu picSelector;

    @Override
    public void getBankname(String bankname) {
        bankId.setText(bankname);
    }

    @Subscribe
    public void uploadFile(CropEvent event) {
        if (event.getPath() == null) {
            return;
        }
        Log.e(TAG, "what2:" + event.getSourceTagCode());
        if (event.getSourceTagCode() == SOURCE_TAG_CODE_ID) {
            idPhoto = event.getPath();
            handler.sendEmptyMessage(SOURCE_TAG_CODE_ID);
        } else if (event.getSourceTagCode() == SOURCE_TAG_CODE_COVER_LOGO) {
            coverLogo = event.getPath();
            handler.sendEmptyMessage(SOURCE_TAG_CODE_COVER_LOGO);
        } else if (event.getSourceTagCode() == SOURCE_TAG_CODE_ART_LOGO) {
            anchorArtLogo = event.getPath();
            handler.sendEmptyMessage(SOURCE_TAG_CODE_ART_LOGO);
        }
    }

    @Override
    public void handleMsg(Message message) {
        Log.e(TAG, "what1:" + message.what);
        if (message.what == SOURCE_TAG_CODE_ID) {
            uploadFile(idPhoto, SOURCE_TAG_CODE_ID);
        } else if (message.what == SOURCE_TAG_CODE_COVER_LOGO) {
            uploadFile(coverLogo, SOURCE_TAG_CODE_COVER_LOGO);
        } else if (message.what == SOURCE_TAG_CODE_ART_LOGO) {
            uploadFile(anchorArtLogo, SOURCE_TAG_CODE_ART_LOGO);
        }

    }

    public void hideLoading(int currentWhat, String url) {
        if (currentWhat == SOURCE_TAG_CODE_ID) {
            if (loadingBoxID.getVisibility() == View.VISIBLE) {
                loadingBoxID.setVisibility(View.GONE);
                uploadAniID.stop();
            }
            idUrl = url;
            if (!TextUtils.isEmpty(url)) {
                idPhonto.setImageURI(Uri.parse(url));
            }
        } else if (currentWhat == SOURCE_TAG_CODE_COVER_LOGO) {
            if (loadingBoxCoverlogo.getVisibility() == View.VISIBLE) {
                loadingBoxCoverlogo.setVisibility(View.GONE);
                uploadAniCoverLogo.stop();
            }
            coverLogoUrl = url;
            if (!TextUtils.isEmpty(url)) {
                logo.setImageURI(Uri.parse(url));
            }
        } else if (currentWhat == SOURCE_TAG_CODE_ART_LOGO) {
            if (loadingBoxArtLogo.getVisibility() == View.VISIBLE) {
                loadingBoxArtLogo.setVisibility(View.GONE);
                uploadAniArtLogo.stop();
            }
            artLogoUrl = url;
            if (!TextUtils.isEmpty(url)) {
                artLogo.setImageURI(Uri.parse(url));
            }
        }
    }

    /**
     * 上传封面
     */
    public void uploadFile(String path, int what) {
        Log.e(TAG, "what:" + what);
        final int currentWhat = what;
        if (currentWhat == SOURCE_TAG_CODE_ID) {
            if (loadingBoxID.getVisibility() == View.GONE) {
                loadingBoxID.setVisibility(View.VISIBLE);
                uploadAniID.start();
            }
        } else if (currentWhat == SOURCE_TAG_CODE_COVER_LOGO) {
            if (loadingBoxCoverlogo.getVisibility() == View.GONE) {
                loadingBoxCoverlogo.setVisibility(View.VISIBLE);
                uploadAniCoverLogo.start();
            }
        } else if (currentWhat == SOURCE_TAG_CODE_ART_LOGO) {
            if (loadingBoxArtLogo.getVisibility() == View.GONE) {
                loadingBoxArtLogo.setVisibility(View.VISIBLE);
                uploadAniArtLogo.start();
            }
        }
        CommonUtil.uploadFile(this,path, new XingBoUploadHandler<UploadFileResponseModel>(UploadFileResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                hideLoading(currentWhat, "");
            }

            @Override
            public void phpXiuSuccess(String response) {
                hideLoading(currentWhat, HttpConfig.FILE_SERVER + model.getUrl());
            }

            @Override
            public void phpXiuProgress(long bytesWritten, long totalSize) {
                if (currentWhat == SOURCE_TAG_CODE_ID) {
                    uploadProID.setText(((int) (bytesWritten / totalSize) * 100) + "%");
                } else if (currentWhat == SOURCE_TAG_CODE_COVER_LOGO) {
                    uploadProCoverLogo.setText(((int) (bytesWritten / totalSize) * 100) + "%");
                } else if (currentWhat == SOURCE_TAG_CODE_ART_LOGO) {
                    uploadProArtLogo.setText(((int) (bytesWritten / totalSize) * 100) + "%");
                }

            }
        });
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void getAreaName(String areaname) {
        area.setText(areaname);
    }

    @Override
    public void getCityName(int cityId, String cityname) {
        currentCityId = cityId;
        city.setText(cityname);
    }
}
