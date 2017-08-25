package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.LoginUser;
import com.xingbo.live.http.HttpConfig;
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
 * Created by Terry on 2016/7/13.
 */
public class CompleteInfoAct extends BaseAct implements View.OnClickListener {
    private final static String TAG="CompleteInfoAct";
    private final static int UPLOAD_HEADER=0x516;
    public final static int SOURCE_TAG_CODE_HEADER=0x202;

    private LoginUser user;
    private FrescoImageView header;
    private EditText nicktext;
    private TextView uidtext;
    private RadioGroup sex_group;
    private RadioButton checkRadioButton;
    private RadioButton male_sex_btn, female_sex_btn;
    private Button complete_btn;
    private PicSelectorMenu picSelector;

    private SharedPreferences sp;
    private String avatar;

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.register_complete_info);

        header=(FrescoImageView)findViewById(R.id.header);
        header.setOnClickListener(this);

        nicktext=(EditText)findViewById(R.id.nick_edit_text);

        uidtext=(TextView)findViewById(R.id.uid_text);
        sp=getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE,Context.MODE_PRIVATE);
        uidtext.setText(sp.getString(XingBo.PX_USER_LOGIN_UID, "0"));

        sex_group=(RadioGroup)findViewById(R.id.sex_group);
        male_sex_btn=(RadioButton)findViewById(R.id.male);
        female_sex_btn=(RadioButton)findViewById(R.id.female);
        checkRadioButton=(RadioButton)sex_group.findViewById(sex_group.getCheckedRadioButtonId());

        complete_btn=(Button)findViewById(R.id.complete_btn);
        complete_btn.setOnClickListener(this);

        findViewById(R.id.top_back_btn).setOnClickListener(this);
        findViewById(R.id.jump_next).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header:
                editHeader();
                break;
            case R.id.complete_btn:
                completeInfo();
                break;
            case R.id.jump_next:
            case R.id.top_back_btn:
                goHome();
                break;
            default:
                break;
        }
    }

    /**
     * 编辑头像
     */
    private void editHeader(){
        CommonUtil.log(TAG, "点击了头像");
        if(picSelector==null){
            picSelector=new PicSelectorMenu(this,SOURCE_TAG_CODE_HEADER);
        }
        picSelector.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 完善信息
     */
    private void  completeInfo(){
        if(TextUtils.isEmpty(avatar)){
            alert("请上传头像");
            return;
        }
        if(TextUtils.isEmpty(nicktext.getText())){
            alert("请输入昵称");
            return;
        }
        if(TextUtils.isEmpty(checkRadioButton.getText())){
            alert("请选择性别");
            return;
        }
        RequestParam param=RequestParam.builder(this);
        sp=getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        param.put("id",sp.getString(XingBo.PX_USER_LOGIN_UID,"0"));
        param.put("nick",nicktext.getText().toString().trim());
        param.put("sex", checkRadioButton.getText().toString().trim());
        param.put("avatar", avatar);
        complete_btn.setEnabled(false);
        completeInfo(param);
    }

    private void completeInfo(RequestParam param){
        CommonUtil.request(this, HttpConfig.API_UPDATE_USER_INFO, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
            @Override
            public void phpXiuSuccess(String response) {
                CommonUtil.log(TAG, "修改结果：" + response);
                done();
                complete_btn.setEnabled(true);
                CommonUtil.tip(CompleteInfoAct.this, "保存成功", Gravity.BOTTOM);
                sp=getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sp.edit();
                editor.putString(XingBo.PX_USER_LOGIN_NICK, nicktext.getText().toString().trim());
                editor.putString(XingBo.PX_USER_LOGIN_AVATAR, avatar);
                editor.commit();
                goHome();
            }

            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                done();
                complete_btn.setEnabled(true);
            }
        });
    }

    @Subscribe
    public void uploadFile(CropEvent event){
        if(event.getSourceTagCode()==SOURCE_TAG_CODE_HEADER) {
            if(event.getPath()==null){
                return;
            }
            Message msg=handler.obtainMessage();
            msg.what=UPLOAD_HEADER;
            msg.obj=event.getPath();
            handler.sendMessage(msg);
        }
    }

    /**
     * 上传头像
     */
    public void uploadHeader(final String path){
        XingBoUtil.log(TAG, "新头像图片路径:" + path);
//        if(loadingBox.getVisibility()==View.GONE) {
//            loadingBox.setVisibility(View.VISIBLE);
//            uploadAni.start();
//        }
        CommonUtil.uploadFile(this,path, new XingBoUploadHandler<UploadFileResponseModel>(UploadFileResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
//                if (loadingBox.getVisibility() == View.VISIBLE) {
//                    loadingBox.setVisibility(View.GONE);
//                    uploadAni.stop();
//                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                XingBoUtil.log(TAG, "上传结果" + response);
                avatar = model.getUrl();
//                updateHeader(model.getUrl());
                header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + model.getUrl()));
            }

            @Override
            public void phpXiuProgress(long bytesWritten, long totalSize) {
//                PHPXiuUtil.log("上传进度", bytesWritten + "@@@@@" + totalSize);
//                uploadPro.setText(((int) (bytesWritten / totalSize) * 100) + "%");
            }
        });
    }

    public void handleMsg(Message msg) {
        switch (msg.what){
            case UPLOAD_HEADER:
                uploadHeader(msg.obj.toString());
            default:
                break;
        }
    }

    /**
     * 前往主界面
     */
    public void goHome(){
        sp=getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE,Context.MODE_PRIVATE);
        if(sp.getString(XingBo.PX_USER_LOGIN_UID,"0").equals("0")){ //往登录界面
            Intent intent = new Intent(this, LoginLocalAct.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
