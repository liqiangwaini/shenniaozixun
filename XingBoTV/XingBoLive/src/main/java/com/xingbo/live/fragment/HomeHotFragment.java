package com.xingbo.live.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nineoldandroids.view.ViewHelper;
import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.adapter.CommonAnchorListCareAdapter;
import com.xingbo.live.adapter.CommonAnchorListHotAdapter;
import com.xingbo.live.adapter.HotAnchorStickyHeaderGridAdapter;
import com.xingbo.live.adapter.PicWallPagerAdapter;
import com.xingbo.live.adapter.holder.AnchorViewHolder;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbo.live.entity.HomeTab;
import com.xingbo.live.entity.PicWall;
import com.xingbo.live.entity.model.HomeAnchorModel;
import com.xingbo.live.entity.model.HomeBaseModel;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.LiveFinishedAct;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.TimeUtils;
import com.xingbo.live.view.widget.PagerSlidingTabStrip;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.extra.pulltorefresh.PullToRefreshStickyHeaderGridView;
import com.xingbobase.extra.sticky.StickyGridHeadersGridView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;
import com.xingbobase.view.widget.CirclePageIndicator;
import com.xingbobase.view.widget.XingBoLoadingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by WuJinZhou on 2016/1/12.
 */
public class HomeHotFragment extends MBaseFragment implements PullToRefreshBase.OnRefreshListener2<StickyGridHeadersGridView>,
        AdapterView.OnItemClickListener, DialogInterface.OnCancelListener, View.OnClickListener, AbsListView.OnScrollListener {
    public final static String TAG = "HomeHotFragment";
    public final static String ARG_POSITION = "arg_view_pager_position";
    private final static int HANDLE_MSG_VIEWPAGER_NEXT = 0x2;
    private boolean isInit = true;
    private int mPosition = 1;
    private final static int REFRESH_COMPLETE = 0x1;

    private PullToRefreshListView refreshView;
    private ListView listView;
    private CommonAnchorListHotAdapter mAdapter;
    private List<HomeAnchor> anchors = new ArrayList<HomeAnchor>();

    private int mOpera = XingBo.REQUEST_OPERA_INIT;
    private int nextPagerIndex = -1;
    private TextView errMsg;
    private Button errBtn;
    private FrescoImageView errImageView;
    private RelativeLayout emptyViewBox;
    //轮播图banner
    private Gson gson;
    private int sw;
    private int mHeaderHeight = 0;
    private RelativeLayout picWallBox;
    private ViewPager picWallPager;
    private CirclePageIndicator pageIndicator;
    private PicWallPagerAdapter wallPagerAdapter;
    private List<PicWall> picWalls = new ArrayList<>();
    private TimerTask task;
    private Timer timer;
    //轮播图当前位置，默认为第一张
    int currentPosition = 0;
    //轮播图最后一张图位置
    int maxPosition = 0;
    //是否反序播放
    boolean isReverse = false;

    private PullToRefreshStickyHeaderGridView ptrGridView;
    private StickyGridHeadersGridView mGridView;

    public static MBaseFragment newInstance(int position) {
        HomeHotFragment fragment = new HomeHotFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment_hot, container, false);
//        refreshView = (PullToRefreshListView) rootView.findViewById(R.id.common_anchor_list_refresh);
//        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
//        refreshView.setOnRefreshListener(this);
        ptrGridView = (PullToRefreshStickyHeaderGridView) rootView.findViewById(R.id.hot_anchor_grid_refresh);
        gson = new Gson();
        getWallList();
        initPicWall();
        initStickyView();
        return rootView;
    }

//    public void init() {
//        refreshView.setScrollingWhileRefreshingEnabled(false);
//        listView = refreshView.getRefreshableView();
//        listView.setOnItemClickListener(this);
//        //左右的padding
//        // int padding= XingBoUtil.dip2px(act, 10);
//        // listView.setPadding(padding, 0, padding, 0);
//        // listView.setHeadersIgnorePadding(true);
//        mAdapter = new CommonAnchorListHotAdapter(act, anchors, mHeaderHeight + 15);
//        listView.setAdapter(mAdapter);
//        // listView.setAreHeadersSticky(false);
//        listView.setOnScrollListener(this);
//        listView.setOnItemClickListener(this);
//    }


    /**
     * 初始化轮播图
     */
    private void initPicWall() {
        sw = (int) SystemApp.screenWidth;
        picWallBox = (RelativeLayout) rootView.findViewById(R.id.pic_wall_container);
        picWallPager = (ViewPager) rootView.findViewById(R.id.pic_wall);
        pageIndicator = (CirclePageIndicator) rootView.findViewById(R.id.pic_wall_indicator);
        wallPagerAdapter = new PicWallPagerAdapter(act, picWalls);
        picWallPager.setAdapter(wallPagerAdapter);
        pageIndicator.setViewPager(picWallPager);
        mHeaderHeight = (sw * 150) / 525;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) picWallBox.getLayoutParams();
        params.height = mHeaderHeight;
        picWallBox.setLayoutParams(params);
        ptrGridView.setExtraHeaderView(picWallBox);//下拉刷新时同步顶部轮播图移动
        maxPosition = picWalls.size() - 1;
    }

    /**
     * 初始化列表视图
     */
    private void initStickyView() {
        ptrGridView.setScrollingWhileRefreshingEnabled(false);
        ptrGridView.setMode(PullToRefreshBase.Mode.BOTH);
        mGridView = ptrGridView.getRefreshableView();
        ptrGridView.setOnRefreshListener(this);
//        int padding= XingBoUtil.dip2px(act, 10);
//        mGridView.setPadding(padding,0,padding,0);
        mGridView.setHeadersIgnorePadding(true);
        mAdapter = new CommonAnchorListHotAdapter(act, anchors, mHeaderHeight + 5);
        mGridView.setAdapter(mAdapter);
        mGridView.setAreHeadersSticky(false);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        playViewPager();
        request(mOpera);
    }


    private boolean isRefresh = false;  //点击重试按钮

    /***
     * 请求数据
     */
    public void request(int opera) {
        if (isRefresh) {
            requestStart();
        }
        mOpera = opera;
        RequestParam param = RequestParam.builder(act);
        param.put("page", "1");
        param.put("classid", "" + mPosition);
        if (mOpera == XingBo.REQUEST_OPERA_LOAD && nextPagerIndex != -1) {
            //加载更多
            param.put("page", nextPagerIndex + "");
        }
        CommonUtil.request(act, HttpConfig.API_APP_HOT_R_ANCHOR, param, TAG, new XingBoResponseHandler<HomeAnchorModel>(this, HomeAnchorModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                if (isRefresh) {
                    requestFinish();
                    isRefresh = false;
                }
                if (mOpera == XingBo.REQUEST_OPERA_REFRESH || mOpera == XingBo.REQUEST_OPERA_INIT) {
                    if (emptyViewBox == null) {
                        showErrViewByNetwork();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
//                    errMsg.setText("请检测网络连接,"+msg);
                    errBtn.setVisibility(View.INVISIBLE);
                    errMsg.setText("亲,网络不给力,请检查网络连接状态");//按要求 修改为
                    errImageView.setVisibility(View.VISIBLE);
                    errImageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));
                } else {
                    alert(msg);
                }
                rootView.findViewById(R.id.empty_view_refresh_btn).setClickable(true);
            }

            @Override
            public void phpXiuSuccess(String response) {

                CommonUtil.log(TAG, "请求结果" + response);
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                if (isRefresh) {
                    requestFinish();
                    isRefresh = false;
                }
                if (model.getD().getPage() == model.getD().getNext()) {
                    nextPagerIndex = -1;
                } else {
                    nextPagerIndex = model.getD().getNext();
                }
                if (mOpera == XingBoConfig.REQUEST_OPERA_REFRESH) {
                    anchors.clear();
                }
                if (model.getD().getItems() != null && model.getD().getItems().size() > 0) {
                    isInit = false;
                }
                if ((mOpera == XingBo.REQUEST_OPERA_REFRESH || mOpera == XingBo.REQUEST_OPERA_INIT) && model.getD().getItems().size() == 0) {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    errImageView.setImageURI(Uri.parse("res:///" + R.mipmap.home_hot_default));
                    errMsg.setText("暂时没有数据,请稍后再试!");
                    errBtn.setText("重试");
                    rootView.findViewById(R.id.empty_view_refresh_btn).setClickable(true);
                } else {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.VISIBLE) {
                        emptyViewBox.setVisibility(View.GONE);
                    }
                }
                anchors.addAll(model.getD().getItems());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    //获取广告图集合
    public void getWallList() {
        RequestParam param = RequestParam.builder(act);
        CommonUtil.request(act, HttpConfig.API_GET_HOME, param, TAG, new XingBoResponseHandler<HomeBaseModel>(this, HomeBaseModel.class, this) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (emptyViewBox == null) {
                    showErrView();
                }
                if (emptyViewBox.getVisibility() == View.GONE) {
                    emptyViewBox.setVisibility(View.VISIBLE);
                }
//                errMsg.setText("请检查网络设置，" + msg);
                errMsg.setText("亲,网络不给力,请检查网络连接状态");//按要求 修改为
                rootView.findViewById(R.id.empty_view_refresh_btn).setClickable(true);
            }

            @Override
            public void phpXiuSuccess(String response) {
                XingBoUtil.log(TAG, "获取首页基本信息结果" + response);
                HomeBaseModel baseModel = gson.fromJson(response, HomeBaseModel.class);
                picWalls.addAll(baseModel.getD().getPicwalls());
                initPicWall();
            }
        });
    }


    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case REFRESH_COMPLETE:
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                break;
            case HANDLE_MSG_VIEWPAGER_NEXT:
                picWallPager.setCurrentItem(currentPosition, true);
                if (isReverse) {
                    if (currentPosition == 0) {
                        isReverse = false;
                    } else {
                        currentPosition--;
                    }
                } else {
                    if (currentPosition == maxPosition) {
                        isReverse = true;
                    } else {
                        currentPosition++;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (TimeUtils.isFastDoubleClick(R.id.hot_anchor_grid_refresh)) {
            return;
        }
        if (view.getTag() instanceof AnchorViewHolder) {
            AnchorViewHolder holder = (AnchorViewHolder) view.getTag();
            if (holder.getAnchor() != null) {
                //请求进入直播房间，准备跳转到房间
                RequestParam param = RequestParam.builder(act);
                param.put("livename", holder.getAnchor().getLivename());
                param.put("rid", holder.getAnchor().getId());
                CommonUtil.request(act, HttpConfig.API_ENTER_ROOM, param, TAG,
                        new XingBoResponseHandler<RoomModel>(this, RoomModel.class, this) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                alert(msg);
//                                alert("网络不给力,请检查网络状态");
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                if (act != null&&anchors!=null&&anchors.size()>position&&anchors.get(position)!=null) {
                                    if (model.getD().getAnchor().getLivestatus().equals("0")) {
                                        Intent intent = new Intent(act, LiveFinishedAct.class);
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, HttpConfig.FILE_SERVER + anchors.get(position).getPosterlogo());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ID, model.getD().getAnchor().getId());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ONLINES, model.getD().getAnchor().getLiveonlines());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_IS_FOLLOWED, model.getD().isFollowed());
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(act, MainRoom.class);
                                        intent.putExtra(MainRoom.EXTRA_MAIN_ROOM_INFO, model.getD());
                                        intent.putExtra(MainRoom.ANCHOR_LIVE_LOGO, TextUtils.isEmpty(anchors.get(position).getPosterlogo()) ? anchors.get(position).getAvatar() : anchors.get(position).getPosterlogo());
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty_view_refresh_btn:
                isRefresh = true;
//                rootView.findViewById(R.id.empty_view_refresh_btn).setClickable(false);
                request(XingBo.REQUEST_OPERA_INIT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        CommonUtil.cancelRequest(((XingBoLoadingDialog) dialog).getTag());
    }

    @Override
    public void requestStart() {
        showDoing(TAG, this);
    }

    @Override
    public void requestFinish() {
        done();
    }

    public void showErrView() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        errImageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        errImageView.setImageURI(Uri.parse("res:///" + R.mipmap.home_hot_default));
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setText("重试");
        errBtn.setOnClickListener(this);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }

    public void showErrViewByNetwork() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        errImageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        errImageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));
        rootView.findViewById(R.id.empty_view_refresh_btn).setOnClickListener(this);
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setText("重试");
        errBtn.setOnClickListener(this);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);

    }

    /**
     * 播放轮播图
     */
    public void playViewPager() {
        if (task == null) {
            task = new TimerTask() {
                public void run() {
                    handler.sendEmptyMessage(HANDLE_MSG_VIEWPAGER_NEXT);
                }
            };
        }
        if (timer == null) {
            timer = new Timer(true);
        }
        timer.schedule(task, 3000, 3000);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int scrollY = getScrollY(view);

        ViewHelper.setTranslationY(picWallBox, -scrollY);
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();
        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<StickyGridHeadersGridView> refreshView) {

        anchors.clear();
        mAdapter.notifyDataSetChanged();
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrGridView.onRefreshComplete();
                request(mOpera);
            }
        }, 1000);

        mOpera = XingBoConfig.REQUEST_OPERA_REFRESH;
        request(mOpera);

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<StickyGridHeadersGridView> refreshView) {
        if (nextPagerIndex == -1) {
            if (refreshView != null) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrGridView.onRefreshComplete();
                        Toast.makeText(getActivity(), "数据已全部加载完成", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
            if (ptrGridView != null) {
                ptrGridView.onRefreshComplete();
            }
            Toast.makeText(getActivity(), "数据已全部加载完成", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 500);
            return;
        } else {
            request(XingBo.REQUEST_OPERA_LOAD);
        }
        request(mOpera);
    }

}
