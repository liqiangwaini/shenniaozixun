package com.xingbo.live.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.UserContributionAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.Contribution;

import com.xingbo.live.entity.model.ContributionModle;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.api.OnHttpStateCallback;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MengruRen on 2016/7/26.
 */

/**
 * 粉丝贡献榜
 */
public class UserFansContributeAct extends BaseAct implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {

    private static final String TAG = "UserFansContributeAct";
    public static final String ANCHOR_GUARD_ID = "anchor_guard_id";
    public static final String USER_COIN = "user_coin";
    public static final String USER_CONTRIBUTIONS_ID = "user_contribution_id";

    private List<Contribution> contributionsList = new ArrayList<Contribution>();
    private PullToRefreshListView pullToRefreshListView;
    private UserContributionAdapter contributionAdapter;
    private ImageView backIv;
    private String coin;
    private String uid;
    private String rid;
    private ListView listView;
    private int nextPagerIndex = -1;

    private int mOpera = XingBoConfig.REQUEST_OPERA_INIT;
    private TextView errMsg;
    private Button errBtn;
    private RelativeLayout emptyViewBox;
    private boolean isSelf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fans_contribute_list);
        uid = getIntent().getStringExtra(ANCHOR_GUARD_ID);
        coin = getIntent().getStringExtra(USER_COIN);
        //初始化操作
        init();
        //获取贡献榜列表
        getFansData(mOpera);
    }

    private void getFansData(int oper) {
        mOpera = oper;
        final RequestParam param = RequestParam.builder(this);
        param.put("uid", uid);
        param.put("page", "1");
        if (mOpera == XingBo.REQUEST_OPERA_LOAD && nextPagerIndex != -1) {
            //加载更多
            param.put("page", nextPagerIndex + "");
        }
        OnHttpStateCallback callback=null;
        if(mOpera== XingBo.REQUEST_OPERA_INIT){
            callback=this;
        }
        CommonUtil.request(this, HttpConfig.API_APP_GET_USER_FANS_LIST, param, TAG, new XingBoResponseHandler<ContributionModle>(this, ContributionModle.class,callback) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (pullToRefreshListView != null) {
                    pullToRefreshListView.onRefreshComplete();
                }
                if (mOpera == XingBo.REQUEST_OPERA_REFRESH || mOpera == XingBo.REQUEST_OPERA_INIT) {
                    if (emptyViewBox == null) {
                        showErrViewByNetwork();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    errMsg.setText("获取数据失败，请检测网络连接");
                } else {
                    alert(msg);
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.d("tag", "contri" + response);
                if (pullToRefreshListView != null) {
                    pullToRefreshListView.onRefreshComplete();
                }
                if (model.getD().getPage() == model.getD().getNext()) {
                    nextPagerIndex = -1;
                } else {
                    nextPagerIndex = model.getD().getNext();
                }
                if (mOpera == XingBoConfig.REQUEST_OPERA_REFRESH) {
                    contributionsList.clear();
                }
                if ((mOpera == XingBo.REQUEST_OPERA_REFRESH || mOpera == XingBo.REQUEST_OPERA_INIT) && model.getD().getItems().size() == 0) {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    errMsg.setText("还没有任何粉丝为你贡献星币");
                } else {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.VISIBLE) {
                        emptyViewBox.setVisibility(View.GONE);
                    }
                    contributionsList.addAll(model.getD().getItems());

                    contributionAdapter.notifyDataSetChanged();
                }


            }
        });
    }


    private void init() {
        backIv = (ImageView) findViewById(R.id.top_contribute_back_btn);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_contribution_fans);
        //refresh
        listView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(this);
        backIv.setOnClickListener(this);
        contributionAdapter = new UserContributionAdapter(UserFansContributeAct.this, contributionsList,UserFansContributeAct.this);
        pullToRefreshListView.setAdapter(contributionAdapter);
        //findViewById(R.id.item_contribution_avator).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_contribute_back_btn:
                onBackPressed();
                break;
            case R.id.item_contribution_avator:
                String uid = v.getTag().toString();
                Log.d("tag", "UserFansContributeAct--->" + uid);
                if (uid != null) {
                    Intent home = new Intent(this, UserHomepageAct.class);
                    home.putExtra(UserHomepageAct.EXTRA_USER_ID, uid);
                    startActivity(home);
                }
                break;

        }
    }


    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        contributionsList.clear();
        contributionAdapter.notifyDataSetChanged();
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshListView.onRefreshComplete();
                getFansData(XingBo.REQUEST_OPERA_REFRESH);
            }
        }, 1000);


    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (nextPagerIndex == -1) {
            if (refreshView != null) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshListView.onRefreshComplete();
                        Toast.makeText(UserFansContributeAct.this, "数据已全部加载完成", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        }else
        {
            getFansData(XingBo.REQUEST_OPERA_LOAD);
        }
    }

    //没有数据的时候的显示界面
    public void showErrView() {
        ViewStub stub = (ViewStub) findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_fans_contibute));
        errBtn = (Button) findViewById(R.id.empty_view_refresh_btn);
        errBtn.setOnClickListener(this);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) findViewById(R.id.empty_view_box);
    }

    public void showErrViewByNetwork() {
        ViewStub stub = (ViewStub) findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));
        findViewById(R.id.empty_view_refresh_btn).setOnClickListener(this);
        errBtn = (Button) findViewById(R.id.empty_view_refresh_btn);
        errBtn.setText("点击刷新");
        errBtn.setOnClickListener(this);
//        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) findViewById(R.id.empty_view_box);

    }

}
