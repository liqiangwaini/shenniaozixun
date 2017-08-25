package com.xingbo.live.controller;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.UserMsgPrivateDetailAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MessageOwner;
import com.xingbo.live.entity.MessagePrivateDetail;
import com.xingbo.live.entity.MessagePrivateDetailPage;
import com.xingbo.live.entity.MessageUser;
import com.xingbo.live.entity.model.MessagePrivateDetailModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.SoftInputUtils;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/8/8.
 */
public class PrivateMsgDetaiController implements View.OnClickListener/*,View.OnLayoutChangeListener, AbsListView.OnScrollListener */, PullToRefreshBase.OnRefreshListener2 {
    private static final String TAG = "PrivateMsgDetaiController";
    public static final String SENDER_ID = "sernder_id";

    private View view;
    private FrameLayout back;
    private TextView nick;
    private PullToRefreshListView listView;

    private String senderId;
    private String nickname;
    private BaseAct act;

    public List<MessagePrivateDetail> detailList = new ArrayList<>();
    private MessageOwner owner;
    private MessageUser user;
    public UserMsgPrivateDetailAdapter mAdapter;
    private EditText edit;
    private TextView send;
    private FrameLayout placeHolder;
    private View rootView;

    public PrivateMsgDetaiController(BaseAct act, View rootView) {
        this.act = act;
        this.rootView = rootView;
    }

    public void setData(String senderId, String nickname) {
        this.senderId = senderId;
        this.nickname = nickname;
        nick.setText(nickname);
        mAdapter=null;
        request();
    }

    public TextView getNick() {
        return nick;
    }

    public void setNick(TextView nick) {
        this.nick = nick;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void initView() {
        back = (FrameLayout) rootView.findViewById(R.id.pri_msg_detail_back);
        nick = (TextView) rootView.findViewById(R.id.pri_msg_detail_nick);
        listView = (PullToRefreshListView) rootView.findViewById(R.id.pri_msg_detail_lv);
        listView.setPullLabel("", PullToRefreshBase.Mode.PULL_FROM_START);
        edit = (EditText) rootView.findViewById(R.id.pri_msg_detail_edit);
        send = (TextView) rootView.findViewById(R.id.pri_msg_detail_send);
        //click
        edit.addTextChangedListener(new MyTextWatcher());
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        listView.setOnRefreshListener(this);
//        listView.setOnScrollListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        currentPage++;
        request();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }

  /*  @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e(TAG,"first:"+firstVisibleItem);
        if (firstVisibleItem == 0) {
            if (total != detailList.size()) {
                currentPage++;
                request();
            }
        }
    }*/

    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable.toString())) {
                send.setBackgroundResource(R.drawable.chat_input_bar_send_btn_bg);
            }
        }
    }

    private int currentPage = 1;
    private int pageTotal = 1;
    private int total = 0;

    //获取私信详情
    public void request() {
        RequestParam param = RequestParam.builder(act);
        param.put("uid", senderId);  //发送人id
        param.put("page", currentPage + "");
        param.put("pagesize", XingBoConfig.COMMON_PAGE_SIZE + "");
        CommonUtil.request(act, HttpConfig.API_USER_GET_PRIVITE_MSG_DETAIL, param, TAG, new XingBoResponseHandler<MessagePrivateDetailModel>(act, MessagePrivateDetailModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (listView != null) {
                    listView.onRefreshComplete();
                }
                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                if (listView != null) {
                    listView.onRefreshComplete();
                }
                MessagePrivateDetailPage messagePrivateDetailPage = model.getD();
                if (mAdapter == null) {
                    detailList.clear();
                    detailList.addAll(messagePrivateDetailPage.getItems());
                    Collections.reverse(detailList);
                    owner = messagePrivateDetailPage.getOwner();
                    user = messagePrivateDetailPage.getUser();
                    mAdapter = new UserMsgPrivateDetailAdapter(act, detailList, owner, user);
                    listView.setAdapter(mAdapter);
                    listView.getRefreshableView().setSelection(detailList.size());
                } else {
                    Collections.reverse(messagePrivateDetailPage.getItems());
                    detailList.addAll(0, messagePrivateDetailPage.getItems());
                    mAdapter.setData(detailList);
                    mAdapter.notifyDataSetChanged();
                    listView.getRefreshableView().setSelection(messagePrivateDetailPage.getItems().size());
                }
                pageTotal = messagePrivateDetailPage.getPagetotal();
                total = messagePrivateDetailPage.getTotal();
                if(detailList.size()>4&&total==detailList.size()){
                    Toast.makeText(act,"数据已全部加载完成",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pri_msg_detail_back:
                listener.clickCallback();
                break;
            case R.id.pri_msg_detail_send:
                String msg = edit.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    act.alert("发送内容不能为空");
                } else {
                    send(senderId, msg);
                }
                break;
        }
    }

    //发送私信
    public void send(String uid, String msg) {
        RequestParam param = RequestParam.builder(act);
        param.put("rid", act.getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_ROOM_CURRENT_ANCHOR_ID, ""));
        param.put("uid", uid);
        param.put("msg", msg);
        CommonUtil.request(act, HttpConfig.API_SEND_PRIVATE_MSG, param, TAG, new XingBoResponseHandler<BaseResponseModel>(act, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                edit.setText("");
                SoftInputUtils.hideInput(act,edit);
                clearInitData();
                request();
            }
        });
    }

    public void clearInitData() {
        currentPage = 1;
        detailList = new ArrayList<MessagePrivateDetail>();
        mAdapter = null;
    }

    private OnClickCallback listener;
    public void setBackClick(OnClickCallback listener){
        this.listener=listener;
    }
    public interface OnClickCallback{
        public void clickCallback();
    }
}

