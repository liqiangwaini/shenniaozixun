package com.xingbo.live.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.UserMsgPrivateAdapter;
import com.xingbo.live.dialog.PrivateMsgDetailDialog;
import com.xingbo.live.entity.MessagePrivate;
import com.xingbo.live.entity.model.MessagePrivateModel;
import com.xingbo.live.eventbus.PriMsgEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.UserMsgAct;
import com.xingbo.live.ui.UserMsgPriDetailAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Project：XingBoTV2.0
 * Author: Mengru Ren
 * Date:  2016/7/29
 */
public class UserMsgPrivateFragment extends MBaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>,AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private final static String TAG = "UserMsgPrivateFragment";
    private final static String ARG_POSITION = "arg_view_page_position";
    private final static int REFRESH_COMPLETE = 0x1;
    private  final static  int REQUEST_CODE=0x2;
    private boolean isInit = true;
    private int mPositon = 1;
    private static final int REFRESH_COMPELETE = 0x1;
    private String uid;//当前用户的id
    private String sid;//发送人 id
    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private UserMsgPrivateAdapter mAdapter;
    private List<MessagePrivate> list;
    private boolean isSelf=false;
    private int nextPageIndex = -1;
    private int pagesize = 10;
    private RelativeLayout emptyViewBox;
    private TextView errMsg;
    private Button errBtn;
    private int mOper = XingBo.REQUEST_OPERA_INIT;
    private Context context;

    private boolean isDialog=false;

    public static MBaseFragment newInstance(int position) {
        UserMsgPrivateFragment fragment = new UserMsgPrivateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPositon = getArguments().getInt(ARG_POSITION);
        Log.d("UserMsgPrivate", "---->onCreat()方法执行");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context=activity;
    }

    public void isDialog(boolean isDialog){
        this.isDialog=isDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_msg_private_fragment, container, false);
        EventBus.getDefault().register(this);
        uid = getActivity().getIntent().getStringExtra(UserMsgAct.EXTRA_USER_ID);
        uid = context.getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID, "");
        initView(rootView);
        return rootView;
    }

    @Subscribe
    public void ignoreMessage(PriMsgEvent priMsgEvent){
        list.clear();
        request(mOper);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View v) {
        pullToRefreshListView = (PullToRefreshListView) v.findViewById(R.id.msg_private_list);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView=pullToRefreshListView.getRefreshableView();
        /**
         * 为什么不直接用pullToReFreshListView
         */
        pullToRefreshListView.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        request(mOper);

    }

    @Override
    public void onResume() {
        super.onResume();
       request(mOper);
    }

    /**
     * 数据请求
     *
     * @param oper
     */
    public void request(int oper) {
        mOper = oper;
        final RequestParam param = RequestParam.builder(act);
        param.put("uid", uid);
        param.put("page", "1");
        param.put("pagesize", pagesize + "");
        if (mOper == XingBo.REQUEST_OPERA_LOAD && nextPageIndex != -1) {
            param.put("page", nextPageIndex + "");
        }
        CommonUtil.request(act, HttpConfig.API_USER_GET_PRIVATE_MSG, param, TAG, new XingBoResponseHandler<MessagePrivateModel>(this, MessagePrivateModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (pullToRefreshListView != null) {
                    pullToRefreshListView.onRefreshComplete();
                }

            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.d("fragmentPrivate", response);
                if (pullToRefreshListView != null) {
                    pullToRefreshListView.onRefreshComplete();
                }
                if (model.getD().getNext() == model.getD().getPage()) {
                    nextPageIndex = -1;
                } else {
                    nextPageIndex = model.getD().getNext();
                    pagesize = model.getD().getPagesize();
                }
                if (model.getD() != null) {
                    list = new ArrayList<MessagePrivate>();
                    list.addAll(model.getD().getItems());
                    mAdapter = new UserMsgPrivateAdapter(context, list);
                    listView.setAdapter(mAdapter);

                }
                if (list.size() == 0) {

                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    if (isSelf) {
                        errMsg.setText("还没有收到任何私信");
                    } else {
                        errMsg.setText("还没有收到任何私信");
                    }
                } else {
                    if (emptyViewBox != null && emptyViewBox.getVisibility() == View.VISIBLE) {
                        emptyViewBox.setVisibility(View.GONE);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void handleMsg(Message message) {
        switch (message.what) {
            case REFRESH_COMPLETE:
                if (pullToRefreshListView != null) {
                    pullToRefreshListView.onRefreshComplete();
                }
                break;
            default:
                break;
        }
    }

    public void showErrView() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_message));
        errBtn= (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }
    public void  showErrViewByNetwork(){
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));

        errBtn= (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn= (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }


    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        request(XingBo.REQUEST_OPERA_REFRESH);
    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (nextPageIndex!=-1){
            request(XingBo.REQUEST_OPERA_LOAD);
        }else {
            pullToRefreshListView.onRefreshComplete();
            Toast.makeText(act, "数据已全部加载完成", Toast.LENGTH_SHORT).show();
        }
       
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.e(TAG,position+"--");
        MessagePrivate messagePrivate= list.get(position-1);
        Log.e(TAG,position+"--"+messagePrivate.getNick());
        if(isDialog){
            PrivateMsgDetailDialog privateMsgDetailDialog=new PrivateMsgDetailDialog();
            privateMsgDetailDialog.setInitData(messagePrivate.getUid(), messagePrivate.getNick());
            privateMsgDetailDialog.show(getFragmentManager(),"PrivateMsgDetailDialog");
        }else{
            Intent userMsgIntent = new Intent(getActivity(), UserMsgPriDetailAct.class);
            userMsgIntent.putExtra(UserMsgPriDetailAct.EXTRA_USER_ID, messagePrivate.getUid());
            userMsgIntent.putExtra("userid", uid);
            getActivity().startActivityForResult(userMsgIntent, REQUEST_CODE);
        }
        EventBus.getDefault().post(new PriMsgEvent());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE:
                mAdapter.notifyDataSetChanged();
                request(mOper);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
        MessagePrivate messagePrivate = list.get(position-1);
        final String sendId = messagePrivate.getUid();//发送人id
        XingBoUtil.dialog(act, "删除会话", "您确定要删除此会话么?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.px_dialog_ok:
                        RequestParam param  = RequestParam.builder(act);
                        param.put("uid",sendId);
                        CommonUtil.request(act,HttpConfig.API_APP_GET_USER_DELETE_USER_SESSION,param,TAG, new XingBoResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                alert("请求失败！");
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                mAdapter.notifyDataSetChanged();
                                request(XingBo.REQUEST_OPERA_INIT);
                            }
                        });
                        break;
                    case R.id.px_dialog_cancel:
                        dialog.dismiss();
                        break;
                }

            }
        }).show();

        return true;
    }
}
