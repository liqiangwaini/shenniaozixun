package com.xingbo.live.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.xingbo.live.Constants;
import com.xingbo.live.SystemApp;
import com.xingbo.live.entity.model.GetChannelIdModel;
import com.xingbo.live.entity.model.LoginUserModel;
import com.xingbo.live.eventbus.LocalStateEvent;
import com.xingbo.live.util.GetMetaDataInfo;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.RequestParam;
import com.xingbo.live.R;
import com.xingbo.live.eventbus.LoginEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.util.XingBoUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import java.util.Map;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.vov.vitamio.utils.Log;


/**
 * Created by WuJinZhou on 2015/10/21.
 */
public class LoginAct extends BaseAct implements View.OnClickListener, DialogInterface.OnCancelListener {
    private final static String TAG = "LoginAct";
    private final static int REGISTER_REQUEST_CODE = 0x1;
    private final static int RESET_PWD_REQUEST_CODE = 0x2;
    private final static int LOCAL_LOGIN_REQUEST_CODE = 0x3;
    private UMShareAPI umApi;//友盟第三方登录及分享api
    private EditText accountInput, pwdInput;
    private Button loginBtn, registerBtn;
    private TextView resetPwd;
    private SharedPreferences sp;
    private String netName;
    private String channelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umApi = UMShareAPI.get(this);
        EventBus.getDefault().register(this);
        setContentView(R.layout.login_other);
        ImageView welcome = (ImageView) findViewById(R.id.login_other_bg_image);
//        welcome.setImageBitmap(FastBlur.blurBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.welcome),this));
        findViewById(R.id.login_sina).setOnClickListener(this);
        findViewById(R.id.login_qq).setOnClickListener(this);
        findViewById(R.id.login_weixin).setOnClickListener(this);
        findViewById(R.id.login_goto_local_text).setOnClickListener(this);

    }

//    public String requestChannalID(){
//        //获渠道名称
//        String channel_app_name = GetMetaDataInfo.getChannelName(this);
//        RequestParam param = RequestParam.builder(this);
//        if(!TextUtils.isEmpty(channel_app_name)){
//            param.put("channel_app_name",channel_app_name);
//        }
//        CommonUtil.request(this, HttpConfig.API_APP_USER_GET_CHANNEL_ID, param, TAG, new XingBoResponseHandler<GetChannelIdModel>(GetChannelIdModel.class) {
//            @Override
//            public void phpXiuErr(int errCode, String msg) {
//                alert("请求错误" + msg);
//            }
//
//            @Override
//            public void phpXiuSuccess(String response) {
//                channelId = model.getD();
//                Log.d(TAG, "channelId-->" + channelId);
//            }
//        });
//        return channelId;
//    }

    @Subscribe  //结束
    public void localState(LocalStateEvent localStateEvent) {
        if (localStateEvent.getFlag() == 0) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_goto_local_text:
                loginLocal();
                break;
            case R.id.login_sina:
                callOtherLogin(SHARE_MEDIA.SINA);
                break;
            case R.id.login_qq:
                callOtherLogin(SHARE_MEDIA.QQ);
                break;
            case R.id.login_weixin:
                callOtherLogin(SHARE_MEDIA.WEIXIN);
                break;

            default:
                break;
        }
    }

    private void goRegister() {
        Intent register = new Intent(this, RegisterAct.class);
        registerBtn.setEnabled(false);
        startActivityForResult(register, REGISTER_REQUEST_CODE);
    }

    private void resetPwd() {
        Intent resetPwd = new Intent(this, ResetPwd.class);
        startActivityForResult(resetPwd, RESET_PWD_REQUEST_CODE);
    }

    /**
     * 官方账号登录
     */
    private void loginLocal() {
        Intent intent = new Intent(this, LoginLocalAct.class);
        startActivity(intent);
    }

    private void login(RequestParam param) {
        //渠道名称
        String channel_app_name = GetMetaDataInfo.getChannelName(this);
        if (!TextUtils.isEmpty(channel_app_name)) {
            param.put("channel_app_name", channel_app_name);
        }
        //设备类型
        param.put("from_device", Constants.LOGIN_DEVICE_TYPE);
        CommonUtil.request(this, HttpConfig.API_USER_LOGIN, param, TAG, new XingBoResponseHandler<LoginUserModel>(this, LoginUserModel.class) {
            @Override
            public void phpXiuSuccess(String response) {

                CommonUtil.log(TAG, "登录结果：" + response);
                done();
//                loginBtn.setEnabled(true);
                EventBus.getDefault().post(new LoginEvent());

                CommonUtil.tip(LoginAct.this, "登录成功", Gravity.BOTTOM);
                sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(XingBo.PX_USER_LOGIN_UID, model.getD().getId());
                editor.putString(XingBo.PX_USER_LOGIN_NICK, model.getD().getNick());
                editor.putString(XingBo.PX_USER_LOGIN_AVATAR, model.getD().getAvatar());
                editor.putString(XingBo.PX_USER_LOGIN_LIVENAME, model.getD().getLivename());
                editor.putString(XingBo.PX_USER_LOGIN_VERSION_CHECK,"0");
                //设置中消息推送设置的缓存
                editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_FLAG,"0");
                editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_VOICE,"0");
                editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_SHOKE,"0");
                editor.putString(XingBo.PX_USER_SETTING_NOTIFYCATION_NO,"1");
                editor.putString(XingBo.PX_USER_SETTING_IS_IN_MAINROOM,"0");//不在直播间
                editor.putString(XingBo.PX_USER_SETTING_IS_ANCHORLIVE,"0");//不在开播间
                editor.commit();
                goHome();
            }

            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
                done();
//                loginBtn.setEnabled(true);
            }
        });
    }

    /**
     * 前往主界面
     */
    public void goHome() {
        sp = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        if (sp.getString(XingBo.PX_USER_LOGIN_UID, "0").equals("0")) { //往登录界面
            Intent intent = new Intent(this, LoginAct.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 调用第三方程序登录
     */
    private void callOtherLogin(final SHARE_MEDIA platform) {
        if (platform == SHARE_MEDIA.QQ || platform == SHARE_MEDIA.WEIXIN || platform == SHARE_MEDIA.SINA) {
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
        }
        //显示执行状态
        showDoing("login", this);

        //发起授权
        umApi.doOauthVerify(this, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> platInfo) {
                if (isFinishing()) {
                    return;
                }
                if (platInfo == null || platInfo.size() == 0) {
                    alert("获取平台信息异常");
                    done();
                    return;
                }
                final String token = platInfo.get("access_token");
                final String channel_app_name = GetMetaDataInfo.getChannelName(LoginAct.this);
                //授权成功，获取平台用户信息
                umApi.getPlatformInfo(LoginAct.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> userInfo) {
                        if (isFinishing()) {
                            return;
                        }
                        RequestParam param = RequestParam.builder(LoginAct.this);
                        try {
                            XingBoUtil.log(TAG, "用户信息：" + userInfo.toString());
                            String uid = null;
                            String nickName = null;
                            String sex = "男";
                            String header = null;
                            String authType = null;
                            if (platform == SHARE_MEDIA.SINA) {
                                XingBoUtil.log(TAG, "新浪：" + userInfo.toString());
                                uid = userInfo.get("uid");
                                nickName = userInfo.get("screen_name");
                                sex = userInfo.get("gender").equals("1") ? "男" : "女";
                                header = userInfo.get("profile_image_url");
                                authType = "sina";
                            } else if (platform == SHARE_MEDIA.QQ) {
                                XingBoUtil.log(TAG, "QQ：" + userInfo.toString());
                                uid = userInfo.get("openid");
                                nickName = userInfo.get("screen_name");
                                sex = userInfo.get("gender").equals("男") ? "男" : "女";
                                header = userInfo.get("profile_image_url");
                                authType = "qq";
                            } else if (platform == SHARE_MEDIA.WEIXIN) {
                                XingBoUtil.log(TAG, "微信：" + userInfo.toString());
                                uid = userInfo.get("openid");
                                nickName = userInfo.get("nickname");
                                sex = userInfo.get("sex").equals("1") ? "男" : "女";
                                header = userInfo.get("headimgurl");
                                authType = "weixin";
                            }
                            param.put("authtype", authType);
                            param.put("authtoken", uid);
                            param.put("authsecret", token);
                            param.put("nick", nickName);
                           // param.put("channel_id",channelId);
                            param.put("channel_app_name",channel_app_name);
                            param.put("from_device", Constants.LOGIN_DEVICE_TYPE);
                        } catch (NullPointerException e) {
                            alert("获取平台用户信息失败");
                            return;
                        }
                        login(param);
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        alert("获取平台信息失败");
                        done();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        alert("获取平台信息失败");
                        done();
                    }
                });

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if (platform == SHARE_MEDIA.SINA) {
                    alert("新浪授权失败");
                    XingBoUtil.log(TAG, throwable.getMessage());
                } else if (platform == SHARE_MEDIA.QQ) {
                    alert("QQ授权失败");
                } else if (platform == SHARE_MEDIA.WEIXIN) {
                    alert("微信授权失败");
                }
                done();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if (platform == SHARE_MEDIA.SINA) {
                    alert("新浪授权已取消");
                } else if (platform == SHARE_MEDIA.QQ) {
                    alert("QQ授权已取消");
                } else if (platform == SHARE_MEDIA.WEIXIN) {
                    alert("微信授权已取消");
                }
                done();
            }
        });
    }

    public void showSoftInput(View view) {
//        view.requestFocus();
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(view, 0);
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册返回
        if (requestCode == REGISTER_REQUEST_CODE) {
            registerBtn.setEnabled(true);
            if (resultCode == RESULT_OK) {
                CommonUtil.log(TAG, "注册成功");
                EventBus.getDefault().post(new LoginEvent());
                CommonUtil.tip(this, "注册成功,已登录", Gravity.BOTTOM);
                Intent intent = new Intent(this, CompleteInfoAct.class);
                startActivity(intent);
                finish();
//                finish();
            }
        } else if (requestCode == RESET_PWD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                alert("密码已重置成功");
            }
        } else {
            umApi.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //点击两次退出应用重写Activity中onKeyDown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //写一个退出方法，名称就是onKeyDown中的exit（）
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            SystemApp.getInstance().exit();
            System.exit(0);
        }
    }
}


