package com.xingbo.live.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.xingbo.live.adapter.MainRoomPriMsgAdapter;
import com.xingbo.live.adapter.MainRoomSysMsgAdapter;
import com.xingbo.live.adapter.UserMsgSystemAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MessagePrivate;
import com.xingbo.live.entity.MessageSystem;
import com.xingbo.live.entity.model.MessageSystemModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;


public class MainRoomSysMsgFragment extends MBaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {
    private final static String TAG = "UserMsgSystemFragment";
    private final static String ARG_POSITION = "arg_view_page_position";
    private boolean isInit = true;
    private int mPosition = 1;
    private final static int REFRESH_COMPLETE = 0x1;

    private PullToRefreshListView mPullToRefreshListView;
    private RelativeLayout emptyViewBox;
    private String uid;
    private MainRoomSysMsgAdapter mSystemAdapter;
    private List<MessageSystem> mSystemList = new ArrayList<>();
    private Context context;
    private int currentPage = 1;
    private Button errBtn;
    private TextView errMsg;

    public static MBaseFragment newInstance(int position) {
        MainRoomSysMsgFragment fragment = new MainRoomSysMsgFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_msg_system_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    private void initView(View view) {
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.msg_system_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setOnItemClickListener(this);
//        uid=getActivity().getIntent().getStringExtra(UserMsgAct.EXTRA_USER_ID);
        uid = context.getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID, "");
        mSystemAdapter = new MainRoomSysMsgAdapter(getActivity(), mSystemList);
        mPullToRefreshListView.setAdapter(mSystemAdapter);
        //数据请求
        request();
    }

    public void showErrView() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_message));
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }

    public void showErrViewByNetwork() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }

    /**
     * 数据请求
     */
    private void request() {
        requestStart();
        RequestParam param = RequestParam.builder(act);
        param.put("uid", uid);
        param.put("page", currentPage + "");
        param.put("pagesize", XingBoConfig.COMMON_PAGE_SIZE + "");
        CommonUtil.request(act, HttpConfig.API_USER_GET_SYSTEM_MSG, param, TAG, new XingBoResponseHandler<MessageSystemModel>(this, MessageSystemModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete();
                }
                if (currentPage == 1) {
                    if (emptyViewBox == null) {
                        showErrViewByNetwork();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    errMsg.setText("亲,网络不给力,请检查网络连接状态");//按要求 修改为
                } else {
                    alert(msg);
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete();
                }
                if (model.getD().getNext() == model.getD().getPage()) {
                    currentPage = -1;
                } else {
                    currentPage = model.getD().getNext();
                }
                if (model.getD() != null) {
                    if(currentPage==1){
                        mSystemList.clear();
                    }
                    mSystemList.addAll(model.getD().getItems());
                    if (mSystemList.size() == 0) {
                        if (emptyViewBox == null) {
                            showErrView();
                        }
                        if (emptyViewBox.getVisibility() == View.GONE) {
                            emptyViewBox.setVisibility(View.VISIBLE);
                        }
                        errMsg.setText("暂时没有系统消息哦");
                    }else{
                        mSystemAdapter.setData(mSystemList);
                        mSystemAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        currentPage=1;
        request();
    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (currentPage == -1) {
            alert("数据已全部加载完成");
            if (mPullToRefreshListView != null) {
                mPullToRefreshListView.onRefreshComplete();
            }
            return;
        }
        request();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        mSystemAdapter.setCurrentPosition(position);
        mSystemAdapter.notifyDataSetChanged();
    }
}
