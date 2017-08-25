package com.xingbo.live.popup;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.xingbo.live.R;
import com.xingbo.live.entity.ShareInfo;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.util.XingBoUtil;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class SharePop extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "SharePop";
    public final static int MEDIA_IMAGE = 0x1;
    public final static int MEDIA_MUSIC = 0x2;
    public final static int MEDIA_VIDEO = 0x3;
    public final static int RESET_BTN = 0X111;

    private final ImageView shareWeixin;
    private final ImageView shareFriends;
    private final ImageView shareQq;
    private final ImageView shareSina;

    private ShareInfo shareInfo;
    private BaseAct activity;
    private UMShareAPI umApi;
    private final ImageView cancel;

    public SharePop(BaseAct activity, UMShareAPI umApi) {
        this.activity = activity;
        this.umApi = umApi;
        View view = View.inflate(activity.getBaseContext(), R.layout.share_popup, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        cancel = (ImageView) view.findViewById(R.id.iv_share_popup_cancel);
        shareWeixin = (ImageView) view.findViewById(R.id.share_weixin);
        shareFriends = (ImageView) view.findViewById(R.id.share_friends);
        shareQq = (ImageView) view.findViewById(R.id.share_qq);
        shareSina = (ImageView) view.findViewById(R.id.share_sina);
        cancel.setOnClickListener(this);
        shareWeixin.setOnClickListener(this);
        shareFriends.setOnClickListener(this);
        shareQq.setOnClickListener(this);
        shareSina.setOnClickListener(this);
    }

    public ShareInfo getShareContent() {
        return shareInfo;
    }

    public void setShareContent(ShareInfo shareInfo) {
        this.shareInfo = shareInfo;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_share_popup_cancel:
                dismiss();
                break;
            case R.id.share_weixin:
                dismiss();
                shareWeixin.setEnabled(false);
                handler.sendEmptyMessageDelayed(RESET_BTN, 3000);
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_friends:
                dismiss();
                shareFriends.setEnabled(false);
                handler.sendEmptyMessageDelayed(RESET_BTN, 3000);
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_qq:
                dismiss();
                shareQq.setEnabled(false);
                handler.sendEmptyMessageDelayed(RESET_BTN, 3000);
                share(SHARE_MEDIA.QQ);
                return;
            case R.id.share_sina:
                dismiss();
                shareSina.setEnabled(false);
                handler.sendEmptyMessageDelayed(RESET_BTN, 3000);
                share(SHARE_MEDIA.SINA);
                break;
        }
    }

    public void share(SHARE_MEDIA platform/*, int mediaType*/) {
        if (!umApi.isInstall(activity, platform)) {
            if (platform == SHARE_MEDIA.QQ) {
                ((BaseAct)activity).alert("QQ程序未安装");
                return;
            } else if (platform == SHARE_MEDIA.WEIXIN) {
                ((BaseAct)activity).alert("微信程序未安装");
                return;
            } else if (platform == SHARE_MEDIA.SINA) {
                ((BaseAct)activity).alert("微博未安装");
                return;
            }
        }
        Config.dialog=activity.loading;
        ShareAction shareAction=new ShareAction(activity).setPlatform(platform).setCallback(umShareListener)
                .withTitle(shareInfo.getTitle())
                .withText(shareInfo.getDesc())
                .withTargetUrl(shareInfo.getHref());
        if(platform != SHARE_MEDIA.SINA){
            shareAction.withMedia(new UMImage(activity, HttpConfig.FILE_SERVER + shareInfo.getLogo())); //"http://dev.umeng.com/images/tab2_1.png"
        }else{
            shareAction.withMedia(new UMImage(activity, HttpConfig.FILE_SERVER + shareInfo.getSite_logo()));
        }
        shareAction.share();
    }

    private UMShareListener umShareListener=new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(activity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(activity,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(activity,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESET_BTN:
                    reset();
                    break;
            }
        }
    };

    public void reset() {
        shareWeixin.setEnabled(true);
        shareFriends.setEnabled(true);
        shareQq.setEnabled(true);
        shareSina.setEnabled(true);
    }
}
