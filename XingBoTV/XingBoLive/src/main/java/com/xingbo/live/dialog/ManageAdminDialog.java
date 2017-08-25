package com.xingbo.live.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.ManageAdminAdapter;
import com.xingbo.live.entity.Mute;
import com.xingbo.live.entity.model.MuteModle;
import com.xingbo.live.entity.msg.MsgFUser;
import com.xingbo.live.eventbus.ManagerEmptyEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by xingbo_szd on 2016/7/14.
 */
public class ManageAdminDialog extends Dialog implements ManageAdminAdapter.DeleteAdminListener {
    private static final String TAG = "ManageAdminDialog";
    private View rootView;
    private ListView listView;
    private List<MsgFUser> adminList = new ArrayList<>();
    private ManageAdminAdapter mAdapter;
    private BaseAct act;


    public ManageAdminDialog(Context context) {
        super(context);
        act = (BaseAct) context;
    }

    public ManageAdminDialog(Context context, int theme) {
        super(context, theme);
        act = (BaseAct) context;
    }

    public void init(Context context) {
        rootView = View.inflate(context, R.layout.manage_admin, null);
        listView = (ListView) rootView.findViewById(R.id.listview_manage_admin);
        this.setContentView(rootView);

    }

    public void setData(List<MsgFUser> adminList) {
        this.adminList = adminList;
        init(act);
        mAdapter = new ManageAdminAdapter(act, adminList);
        mAdapter.setAdminDeleteListener(this);
        listView.setAdapter(mAdapter);
    }

    //取消管理员权限
    @Override
    public void deleteAdmin(final int position) {
        XingBoUtil.dialog(act, "提示", "确定取消该用户的管理员权限吗？", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dismiss();
                delete(position);
            }
        }).show();
    }

    public void delete(final int position) {
        RequestParam params = RequestParam.builder(getContext());
        params.put("rid", getContext().getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID, ""));
        params.put("uid", adminList.get(position).getId());
        adminList.remove(position);
        if (adminList.size() <= 0) {
            EventBus.getDefault().post(new ManagerEmptyEvent());
        } else {
            mAdapter.setData(adminList);
            mAdapter.notifyDataSetChanged();
        }
        CommonUtil.request(getContext(), HttpConfig.API_APP_CANCEL_ADMIN, params, TAG, new XingBoResponseHandler<BaseResponseModel>(act, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
              /*  adminList.remove(position);
                if (adminList.size() <= 0) {
                    EventBus.getDefault().post(new ManagerEmptyEvent());
                } else {
                    mAdapter.setData(adminList);
                    mAdapter.notifyDataSetChanged();
                }*/
            }
        });
    }
}
