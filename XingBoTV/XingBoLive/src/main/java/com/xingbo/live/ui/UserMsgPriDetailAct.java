package com.xingbo.live.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.UserMsgPrivateDetailAdapter;
import com.xingbo.live.entity.MessageOwner;

import com.xingbo.live.entity.MessagePrivateDetail;

import com.xingbo.live.entity.MessageUser;
import com.xingbo.live.entity.model.MessagePrivateDetailModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.api.OnHttpStateCallback;
import com.xingbobase.config.XingBo;

import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Project：XingBoTV2.0
 * Author: Mengru Ren
 * Date:  2016/7/31
 * 我的私信详情页面
 */
public class UserMsgPriDetailAct extends BaseAct implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {

    private final static String TAG = "UserMsgPriDetailAct";
    public final static String EXTRA_USER_ID = "extra_user_id";
    private final static int SEND_MESSAGE = 0x516;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView listView;
    private ImageView imageViewBack;
    private TextView nick;
    private EditText mEditTextMsg;
    private Button btnSend;
    private int mOper = XingBo.REQUEST_OPERA_INIT;
    private int pagesize = 10;

    private MessageOwner owners;
    private MessageUser users;
    private List<MessagePrivateDetail> detailsList;
    private UserMsgPrivateDetailAdapter mAdapter;
    private RelativeLayout emptyViewBox;
    private TextView errMsg;
    private String uid;
    private String ownerId;
    private int nextPageIndex = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_private_detail);
        uid = getIntent().getStringExtra(EXTRA_USER_ID);
        ownerId = getIntent().getStringExtra("userid");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.user_msg_chat_listview);
        mPullToRefreshListView.setOnRefreshListener(this);
        listView = mPullToRefreshListView.getRefreshableView();
        imageViewBack = (ImageView) findViewById(R.id.user_msg_private_detail_back);
        nick = (TextView) findViewById(R.id.user_msg_private_detail_nick);
        mEditTextMsg = (EditText) findViewById(R.id.user_msg_chat_edit);
        btnSend = (Button) findViewById(R.id.user_msg_chat_send);
        btnSend.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
        detailsList = new ArrayList<MessagePrivateDetail>();
        request(mOper);
    }

    /**
     * 数据请求
     *
     * @param oper
     */
    private void request(int oper) {
        mOper = oper;
        RequestParam param = RequestParam.builder(this);
        param.put("uid", uid);
        param.put("page", "1");
        if (mOper == XingBo.REQUEST_OPERA_LOAD && nextPageIndex != -1) {
            param.put("page", nextPageIndex + "");
        }
        OnHttpStateCallback callback = null;
        if (mOper == XingBo.REQUEST_OPERA_INIT) {
            callback = this;
        }
        //   param.put("pagesize", pagesize + "");

        CommonUtil.request(this, HttpConfig.API_USER_GET_PRIVITE_MSG_DETAIL, param, TAG, new XingBoResponseHandler<MessagePrivateDetailModel>(this, MessagePrivateDetailModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete();
                }
//                if (mOper == XingBo.REQUEST_OPERA_REFRESH || mOper == XingBo.REQUEST_OPERA_INIT) {
//                    if (emptyViewBox == null) {
//                        showErrView();
//                    }
//                    if (emptyViewBox.getVisibility() == View.GONE) {
//                        emptyViewBox.setVisibility(View.VISIBLE);
//                    }
//                    errMsg.setText("获取数据失败，请检测网络连接" + msg);
//                } else {
//                    alert(msg);
//                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete();
                }
                if (model.getD().getPage() == model.getD().getNext()) {
                    nextPageIndex = -1;
                } else {
                    nextPageIndex = model.getD().getNext();
                }
                if (mOper == XingBo.REQUEST_OPERA_REFRESH || mOper == XingBo.REQUEST_OPERA_INIT) {
                    detailsList.clear();
                }
                if (model.getD() != null) {
                    detailsList.addAll(model.getD().getItems());
                }
//                if (detailsList.size() == 0) {
//                    if (emptyViewBox == null) {
//                        showErrView();
//                    }
//                    if (emptyViewBox.getVisibility() == View.GONE) {
//                        emptyViewBox.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    if (emptyViewBox != null && emptyViewBox.getVisibility() == View.VISIBLE) {
//                        emptyViewBox.setVisibility(View.GONE);
//                    }
//                }
                owners = model.getD().getOwner();
                users = model.getD().getUser();
                nick.setText(model.getD().getUser().getNick());
                mAdapter = new UserMsgPrivateDetailAdapter(UserMsgPriDetailAct.this, detailsList, owners, users);
                Collections.reverse(detailsList);
                listView.setAdapter(mAdapter);
                if (listView != null) {
                    listView.setSelection(listView.getAdapter().getCount() - 1);
                }

            }
        });
    }


    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (nextPageIndex==-1){
            refreshView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPullToRefreshListView.onRefreshComplete();
//                    request(mOper);
                    Toast.makeText(UserMsgPriDetailAct.this, "数据已全部加载完成", Toast.LENGTH_SHORT).show();
                }
            }, 1000);

        }else {
            request(XingBo.REQUEST_OPERA_LOAD);
        }

    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (refreshView != null) {
            refreshView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPullToRefreshListView.onRefreshComplete();
                }
            }, 1000);
        }

    }

    @Override
    public void requestStart() {

        showDoing(TAG, null);
    }

    @Override
    public void requestFinish() {

        done();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_msg_private_detail_back:
                onBackPressed();
                break;
            case R.id.user_msg_chat_send:
                String str = mEditTextMsg.getText().toString();
                if (!str.equals("")) {
                    RequestParam param = RequestParam.builder(this);
                    param.put("uid", uid);
                    param.put("msg", str + "");
                    CommonUtil.request(this, HttpConfig.API_USER_GET_SENDMESSAGE, param, TAG, new XingBoResponseHandler<BaseResponseModel>(this, BaseResponseModel.class) {
                        @Override
                        public void phpXiuErr(int errCode, String msg) {
                            alert(msg);
                        }

                        @Override
                        public void phpXiuSuccess(String response) {
                            //软键盘收起
                            View view = getWindow().peekDecorView();
                            if (view != null) {
                                InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            //editText文本清空
                            mEditTextMsg.setText("");
                            mAdapter.notifyDataSetChanged();
                            request(XingBo.REQUEST_OPERA_REFRESH);
                        }
                    });
                } else {
                    alert("您还没有输入任何内容");
                }
                break;
        }
    }

    //错误提示页面
    public void showErrView() {
        ViewStub stub = (ViewStub) findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.common_empty_view_icon));
        findViewById(R.id.empty_view_refresh_btn).setOnClickListener(this);
        emptyViewBox = (RelativeLayout) findViewById(R.id.empty_view_box);
    }

}
