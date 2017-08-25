package com.xingbo.live.popup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.entity.MainUser;
import com.xingbo.live.entity.Mute;
import com.xingbo.live.entity.UserRoomInfo;
import com.xingbo.live.entity.model.MuteModle;
import com.xingbo.live.eventbus.ManagerEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;

import java.net.HttpCookie;

import de.greenrobot.event.EventBus;

/**
 * Created by xingbo_szd on 2016/7/18.
 */
public class ManagePopup extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "ManagePopup";

    private BaseAct act;
    private String rid;//主播id
    private String uid;//被设置的用户id
    private MainUser userRoomInfo;

    public ManagePopup(BaseAct act, String rid, String uid, MainUser userRoomInfo) {
        this.act = act;
        this.rid = rid;
        this.uid = uid;
        this.userRoomInfo = userRoomInfo;
        View manageView = View.inflate(act, R.layout.popup_manage, null);
        this.setContentView(manageView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView addAdmin = (TextView) manageView.findViewById(R.id.tv_popup_manage_addadmin);
        TextView gag = (TextView) manageView.findViewById(R.id.tv_popup_manage_gag_thistime);
        TextView gagforever = (TextView) manageView.findViewById(R.id.tv_popup_manage_gag_forever);
        TextView cancel = (TextView) manageView.findViewById(R.id.tv_popup_manage_cancel);
        //initdata
        if(userRoomInfo.getAddAdmin()==1&&userRoomInfo.getCancelAdmin()==0){
            addAdmin.setText("设置为管理员");
        }else{
            addAdmin.setText("取消管理员权限");
        }
        //click
        addAdmin.setOnClickListener(this);
        gag.setOnClickListener(this);
        gagforever.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_popup_manage_addadmin: //设置为管理员
                dismiss();
                if(userRoomInfo.getAddAdmin()==1&&userRoomInfo.getCancelAdmin()==0){
                    addAdmin();
                }else{
                    cancelAdmin();
                }
                break;
            case R.id.tv_popup_manage_gag_thistime:  //本场禁言
                dismiss();
                XingBoUtil.dialog(act, "提示", "确定禁言该用户吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestParam param2 = RequestParam.builder(act);
                        param2.put("rid", rid);
                        param2.put("uid", uid);
                        param2.put("expire", "" + 300);  //5分钟
                        CommonUtil.request(act, HttpConfig.API_APP_ADD_MUTE, param2, TAG, new XingBoResponseHandler<BaseResponseModel>(act, BaseResponseModel.class) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                act.alert(msg);
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                Toast.makeText(act,"本场禁言成功",Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new MuteModle(new Mute(0, userRoomInfo.getNick())));
                            }
                        });
                    }
                }).show();
                break;
            case R.id.tv_popup_manage_gag_forever:  //永久禁言
                dismiss();
                XingBoUtil.dialog(act, "提示", "确定永久禁言该用户吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestParam param3 = RequestParam.builder(act);
                        param3.put("rid", rid);
                        param3.put("uid", uid);
                        param3.put("expire", "" + 3600 * 24);  //24小时
                        CommonUtil.request(act, HttpConfig.API_APP_ADD_MUTE, param3, TAG, new XingBoResponseHandler<BaseResponseModel>(act, BaseResponseModel.class) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                act.alert(msg);
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                EventBus.getDefault().post(new MuteModle(new Mute(1, userRoomInfo.getNick())));
                                Toast.makeText(act,"永久禁言成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();
                break;
            case R.id.tv_popup_manage_cancel:
                dismiss();
                break;
        }
    }

    public void addAdmin(){
        RequestParam param1 = RequestParam.builder(act);
        param1.put("rid", rid);
        param1.put("uid", uid);
        CommonUtil.request(act, HttpConfig.API_APP_ADD_ADMIN, param1, TAG, new XingBoResponseHandler<BaseResponseModel>(act, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
            }
            @Override
            public void phpXiuSuccess(String response) {
                Toast.makeText(act,"设置管理员成功",Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new ManagerEvent(true,uid));
            }
        });
    }

    public void cancelAdmin(){
        RequestParam param1 = RequestParam.builder(act);
        param1.put("rid", rid);
        param1.put("uid", uid);
        CommonUtil.request(act, HttpConfig.API_APP_CANCEL_ADMIN, param1, TAG, new XingBoResponseHandler<BaseResponseModel>(act, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                act.alert("取消管理员权限成功");
                EventBus.getDefault().post(new ManagerEvent(false, uid));
            }
        });
    }
}
