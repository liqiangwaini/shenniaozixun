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
import com.xingbo.live.adapter.UserMsgSystemAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MessageSystem;
import com.xingbo.live.entity.model.MessageSystemModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.UserMsgAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Project：XingBoTV2.0
 * Author: Mengru Ren
 * Date:  2016/7/29
 */
public class UserMsgSystemFragment extends MBaseFragment implements  PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {
    private final  static String TAG="UserMsgSystemFragment";
    private final  static String ARG_POSITION= "arg_view_page_position";
    private boolean isInit= true;
    private  int mPosition =1;
    private final static  int REFRESH_COMPLETE=0x1;

    private PullToRefreshListView mPullToRefreshListView;

    private String uid;
    private UserMsgSystemAdapter mSystemAdapter;
    private List<MessageSystem> mSystemList;
    private  int nextPageIndex=-1;
    private  int pagesize=20;
    private TextView errMsg;
    private Button errBtn;
    private RelativeLayout emptyViewBox;
    private Context context;
    private boolean flag=true;

    private int mOper= XingBo.REQUEST_OPERA_INIT;

    public static MBaseFragment newInstance(int position){
        UserMsgSystemFragment fragment= new UserMsgSystemFragment();
        Bundle bundle=  new Bundle();
        bundle.putInt(ARG_POSITION,position);
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
        rootView = inflater.inflate(R.layout.user_msg_system_fragment,container,false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    private void initView(View view) {
        mPullToRefreshListView= (PullToRefreshListView) view.findViewById(R.id.msg_system_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(this);
//        uid=getActivity().getIntent().getStringExtra(UserMsgAct.EXTRA_USER_ID);
        uid=context.getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID, "");
        mPullToRefreshListView.setOnItemClickListener(this);
        //数据请求
        request(mOper);
    }

    /**
     * 数据请求
     * @param oper
     */
    private void request(int oper) {

        mOper=oper;
        RequestParam param= RequestParam.builder(act);
        param.put("uid", uid);
        param.put("page","1");
        param.put("pagesize", pagesize + "");
        if (mOper==XingBo.REQUEST_OPERA_LOAD&&nextPageIndex!=-1){
            param.put("page",nextPageIndex+"");
        }
        CommonUtil.request(act, HttpConfig.API_USER_GET_SYSTEM_MSG, param, TAG, new XingBoResponseHandler<MessageSystemModel>(this, MessageSystemModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {

                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete();
                }
                if (mOper == XingBo.REQUEST_OPERA_REFRESH || mOper == XingBo.REQUEST_OPERA_INIT) {
                    if (emptyViewBox == null) {
                        showErrViewByNetwork();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
//                    errMsg.setText("请检测网络连接,"+msg);
                    errMsg.setText("亲,网络不给力,请检查网络连接状态");//按要求 修改为
                } else {
                    alert(msg);
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.d(TAG, response);

                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete();
                }
                if (model.getD().getPage() == model.getD().getNext()) {
                    nextPageIndex = -1;
                } else {
                    nextPageIndex = model.getD().getNext();
                }
                if (mOper == XingBoConfig.REQUEST_OPERA_REFRESH) {
                    mSystemList.clear();
                }
                if (model.getD().getItems() != null && model.getD().getItems().size() > 0) {
                    isInit = false;
                }
                if (model.getD().getItems() != null) {
                    mSystemList = new ArrayList<MessageSystem>();
                    mSystemList.addAll(model.getD().getItems());
                    mSystemAdapter = new UserMsgSystemAdapter(getActivity(), mSystemList);
                    mPullToRefreshListView.setAdapter(mSystemAdapter);
                }

                if (mSystemList.size() == 0) {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    errMsg.setText("此时没有系统消息的显示");
                }
                mSystemAdapter.notifyDataSetChanged();
            }
        });
    }

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mSystemList.clear();
        mSystemAdapter.notifyDataSetChanged();
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshListView.onRefreshComplete();
                request(mOper);
            }
        }, 1000);
    }
    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (nextPageIndex==-1){
            if (refreshView != null) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshListView.onRefreshComplete();
                        Toast.makeText(getActivity(), "已加载至最新的数据", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
//                refreshView.onRefreshComplete();
            }
        }else {
            request(XingBo.REQUEST_OPERA_LOAD);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSystemAdapter.setCurrentPosition(position,flag);
        flag=!flag;
        mSystemAdapter.notifyDataSetChanged();
    }

    public void showErrView() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.common_empty_view_icon));
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
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }

}
