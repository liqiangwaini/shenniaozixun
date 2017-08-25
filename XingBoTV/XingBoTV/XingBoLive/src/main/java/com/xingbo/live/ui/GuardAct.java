package com.xingbo.live.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.ContributionAdapter;
import com.xingbo.live.adapter.GuardAdapter;
import com.xingbo.live.dialog.UserInfoDialog;
import com.xingbo.live.entity.Contribution;
import com.xingbo.live.entity.ContributionPage;
import com.xingbo.live.entity.Guard;
import com.xingbo.live.entity.GuardInfo;
import com.xingbo.live.entity.GuardTimeAndPrice;
import com.xingbo.live.entity.model.ContributionModle;
import com.xingbo.live.entity.model.GuardModel;
import com.xingbo.live.entity.model.GuardPopWinModel;
import com.xingbo.live.eventbus.UserInfoEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.popup.GuardPop;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 贡献榜
 */
public class GuardAct extends BaseAct implements GuardAdapter.GuardClickCallback, AbsListView.OnScrollListener, View.OnClickListener,
        PullToRefreshBase.OnRefreshListener2<ListView>, DialogInterface.OnDismissListener, GuardPop.OnOpenGuardSuccess, AdapterView.OnItemClickListener {
    private static final String TAG = "GuardAct";

    private int currentPage = 1;
    private int pagesize = 10;
    private int pageTotal = 0;
    private int totalNum = 0;
    private static final int CONTRIBUTION_FANS_MSG = 0x555;

    public static final String ANCHOR_GUARD_ID = "anchor_guard_id";
    public static final String ANCHOR_GUARD_NICK = "anchor_guard_nick";
    public static final String USER_COIN = "user_coin";
    public static final String ANCHOR_GUARD_AVATAR = "anchor_guard_avatar";

    public static final String USER_CONTRIBUTIONS_ID = "user_contribution_id";

    private TextView tvAnchorNick;
    private RecyclerView recyclerView;
    private PullToRefreshListView pulltoRefreshListView;
    private List<Guard> guardList = new ArrayList<Guard>();
    private List<Contribution> contributionList = new ArrayList<Contribution>();
    private UserInfoDialog userInfoDialog = null;

    private String rid;
    private String uid;
    private GuardAdapter guardAdapter;
    private ContributionAdapter contributionAdapter;
    private View parentView;
    private FrameLayout back;
    private String coin;
    private String anchorNick;
    private TextView guardNum;
    private String anchorAvatar;
    private GuardPop guardPop;
    private RelativeLayout rlGuardSnap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = View.inflate(this, R.layout.contribution_list, null);
        setContentView(parentView);
        setRootView(parentView);
        EventBus.getDefault().register(this);
        rid = getIntent().getStringExtra(ANCHOR_GUARD_ID);
        anchorNick = getIntent().getStringExtra(ANCHOR_GUARD_NICK);
        anchorAvatar = getIntent().getStringExtra(ANCHOR_GUARD_AVATAR);
        uid = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID, "");
        init();
        getGuardData();
        getFansData();
    }

    @Subscribe
    public void showUserInfo(UserInfoEvent userInfoEvent) {
        if (userInfoDialog != null) {
            userInfoDialog.showUserInfoCard(userInfoEvent);
        }
    }

    public void init() {
        back = (FrameLayout) findViewById(R.id.top_back_btn);
        pulltoRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_contribution_list_fans);
        //header
        View headerView = View.inflate(this, R.layout.guard_header_view, null);
        tvAnchorNick = (TextView) headerView.findViewById(R.id.tv_anchor_nick);
        guardNum = (TextView) headerView.findViewById(R.id.tv_guard_number);
        rlGuardSnap = (RelativeLayout) headerView.findViewById(R.id.rl_guard_snap);
        recyclerView = (RecyclerView) headerView.findViewById(R.id.rv_contribution_list_guard);
        rlGuardSnap.setOnClickListener(this);
        //refresh
        ListView listView = pulltoRefreshListView.getRefreshableView();
        listView.setItemsCanFocus(false);
        pulltoRefreshListView.setOnRefreshListener(this);
        back.setOnClickListener(this);
        //setData
        tvAnchorNick.setText(anchorNick + "的守护神");
        //guard
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        guardAdapter = new GuardAdapter(this, guardList);
        guardAdapter.setGuardClickLietener(this);
        if (android.os.Build.VERSION.SDK_INT >= 9) {
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        recyclerView.setAdapter(guardAdapter);
        //fans
        contributionAdapter = new ContributionAdapter(this, contributionList);
        pulltoRefreshListView.setOnItemClickListener(this);
//        if (android.os.Build.VERSION.SDK_INT >= 9) {
//            listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
//        }
        listView.setDividerHeight(2);
        listView.setAdapter(contributionAdapter);
        listView.addHeaderView(headerView);
    }

    //获取守护者列表
    public void getGuardData() {
        requestStart();
        RequestParam params = RequestParam.builder(this);
        params.put("rid", rid);
        CommonUtil.request(this, HttpConfig.API_APP_GET_GUARD_LIST, params, TAG, new XingBoResponseHandler<GuardModel>(this, GuardModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
                guardList.addAll(model.getD());
                guardNum.setText("(" + model.getD().size() + ")");
                if (guardList.size() < 1) {
                    guardList.add(new Guard());
                }
                if (guardList.size() < 2) {
                    guardList.add(new Guard());
                }
                if (guardList.size() < 3) {
                    guardList.add(new Guard());
                }
                guardAdapter.setList(guardList);
                guardAdapter.notifyDataSetChanged();
            }
        });
    }


    //获取贡献榜列表
    public void getFansData() {
        requestStart();
        RequestParam params = RequestParam.builder(this);
        params.put("uid", rid);
        params.put("page", "" + currentPage);
        params.put("pagesize", "" + pagesize);
        CommonUtil.request(this, HttpConfig.API_APP_GET_USER_FANS_LIST, params, TAG, new XingBoResponseHandler<ContributionModle>(this, ContributionModle.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                if (pulltoRefreshListView != null) {
                    pulltoRefreshListView.onRefreshComplete();
                }
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
                Log.e(TAG, response);
                if (pulltoRefreshListView != null) {
                    pulltoRefreshListView.onRefreshComplete();
                }
                ContributionPage contributionPage = model.getD();
                pageTotal = contributionPage.getPagetotal();
                totalNum = contributionPage.getTotal();
                if (contributionPage.getPage() == contributionPage.getPagetotal()) {
                    currentPage = -1;
                    Toast.makeText(GuardAct.this, "数据已全部加载完成", Toast.LENGTH_SHORT).show();
                } else {
                    currentPage++;
                }
                contributionList.addAll(contributionPage.getItems());
                Message message = Message.obtain();
                message.arg1 = CONTRIBUTION_FANS_MSG;
                message.what = 0;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void handleMsg(Message message) {
        switch (message.what) {
            case 0:
                if (message.arg1 == CONTRIBUTION_FANS_MSG) {
                    contributionAdapter.setList(contributionList);
                    contributionAdapter.notifyDataSetChanged();
                }
                break;
            case 1:
                if (pulltoRefreshListView != null) {
                    pulltoRefreshListView.onRefreshComplete();
                }
                break;
            case 2:
                if (months != null) {
                    if (guardPop == null) {
                        guardPop = new GuardPop(GuardAct.this, rid, uid, anchorNick, anchorAvatar);
                    }
                    guardPop.setOpenGuardListener(this);
                    guardPop.setFocusable(true);
                    guardPop.setMainUser(months);
                    guardPop.setOutsideTouchable(false);
                    guardPop.setFocusable(true);
                    guardPop.setBackgroundDrawable(new BitmapDrawable());
                    showPopupBg();
                    guardPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 1f;
                            getWindow().setAttributes(lp);
                        }
                    });
                    guardPop.setAnimationStyle(R.style.style_popup);
                    guardPop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //守护者点击
    @Override
    public void onItemClick(boolean isEmpty, int position) {
        if (isEmpty) {
            getGuardPrice(this, rid, uid);
        } else {
           /* if (userInfoDialog == null) {
                userInfoDialog = new UserInfoDialog(this, R.style.dialog);
                userInfoDialog.isAdmin("0");
                userInfoDialog.setOnDismissListener(this);
            }
            userInfoDialog.setData("", guardList.get(position).getId());
            showBg();
            userInfoDialog.show();*/
            Intent homepageIntent = new Intent(this, UserHomepageAct.class);
            homepageIntent.putExtra(UserHomepageAct.EXTRA_USER_ID, guardList.get(position).getId());
            Log.e(TAG, guardList.get(position).getId() + "()");
            startActivity(homepageIntent);
        }
    }

    public void showBg() {
        //背景变暗
        showPopupBg();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == contributionAdapter.getCount()) {
            getFansData();
        }
    }

    private int lastVisibleIndex = 0;

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 计算最后可见条目的索引
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        if (totalItemCount == totalNum + 1) {
            alert("所有已经加载完毕");
        }
    }

    private List<GuardTimeAndPrice> months;

    //获取守护信息
    public void getGuardPrice(final Context context, String rid, String uid) {
        requestStart();
        RequestParam param = RequestParam.builder(context);
        param.put("rid", rid);
        param.put("uid", uid);
        CommonUtil.request(context, HttpConfig.API_APP_GET_GUARD_INFO, param, TAG, new XingBoResponseHandler<GuardPopWinModel>(this, GuardPopWinModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
                GuardInfo guardInfo = model.getD();
                months = guardInfo.getMonths();
                handler.sendEmptyMessage(2);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_guard_snap:
                getGuardPrice(this, rid, uid);
                break;
            case R.id.top_back_btn:
                finish();
                break;
        }
    }

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        currentPage = 0;
        contributionList.clear();
        guardList.clear();
        getGuardData();
        getFansData();
    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (currentPage == -1) {
            handler.sendEmptyMessageDelayed(1, 500);
            return;
        }
        if (contributionList.size() == totalNum) {
            Toast.makeText(GuardAct.this, "数据已全部加载完成", Toast.LENGTH_SHORT).show();
            return;
        }
        getFansData();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onOpenGuardSuccess() {
        guardList.clear();
        getGuardData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent homepageIntent = new Intent(this, UserHomepageAct.class);
        homepageIntent.putExtra(UserHomepageAct.EXTRA_USER_ID, contributionList.get(position - 2).getFid());
        homepageIntent.putExtra(UserHomepageAct.EXTRA_ANCHOR_ID, rid);
        startActivity(homepageIntent);
    }
}
