package com.xingbo.live.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.xingbo.live.adapter.HotAnchorStickyHeaderGridAdapter;
import com.xingbo.live.ui.LiveFinishedAct;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.FrescoUtils;
import com.xingbo.live.util.TimeUtils;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshStickyHeaderGridView;
import com.xingbobase.extra.sticky.StickyGridHeadersGridView;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.RequestParam;
import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.adapter.holder.AnchorViewHolder;
import com.xingbo.live.adapter.PicWallPagerAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbo.live.entity.PicWall;
import com.xingbo.live.entity.model.HomeAnchorModel;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.widget.CirclePageIndicator;
import com.xingbobase.view.widget.XingBoLoadingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by WuJinZhou on 2016/1/6.
 */
public class HomeIndexFragment extends MBaseFragment implements AbsListView.OnScrollListener,
        PullToRefreshBase.OnRefreshListener2<StickyGridHeadersGridView>,
        AdapterView.OnItemClickListener, DialogInterface.OnCancelListener {
    public final static String TAG = "HomeIndexFragment";
    public final static String ARG_POSITION = "arg_view_pager_position";
    public final static String ARG_PIC_WALL = "arg_pic_wall";
    private final static int REFRESH_COMPLETE = 0x1;
    private final static int HANDLE_MSG_VIEWPAGER_NEXT = 0x2;
    private int mPosition = 0;
    private int sw;
    private RelativeLayout picWallBox;
    private ViewPager picWallPager;
    private CirclePageIndicator pageIndicator;
    private PicWallPagerAdapter wallPagerAdapter;
    private List<PicWall> picWalls;
    private int mHeaderHeight = 0;
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
    private HotAnchorStickyHeaderGridAdapter mAdapter;
    private List<HomeAnchor> hots = new ArrayList<HomeAnchor>();

    private int mOpera = XingBo.REQUEST_OPERA_INIT;
    private int nextPagerIndex = -1;

    public static MBaseFragment newInstance(int position, ArrayList<PicWall> walls) {
        HomeIndexFragment fragment = new HomeIndexFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putSerializable(ARG_PIC_WALL, walls);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
        picWalls = (ArrayList<PicWall>) getArguments().getSerializable(ARG_PIC_WALL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_fragment_index, null);
        ptrGridView = (PullToRefreshStickyHeaderGridView) rootView.findViewById(R.id.hot_anchor_grid_refresh);
        initPicWall();
        initStickyView();
        return rootView;
    }

    /**
     * 初始化轮播图
     */
    private void initPicWall() {
        ;
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
        int padding = XingBoUtil.dip2px(act, 10);
        mGridView.setPadding(0, 0, 0, 0);
        mGridView.setHeadersIgnorePadding(true);
        mAdapter = new HotAnchorStickyHeaderGridAdapter(act, hots, mHeaderHeight + 15);
        mGridView.setAdapter(mAdapter);
        mGridView.setAreHeadersSticky(false);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        playViewPager();
        request(mOpera);
    }

    /***
     * 请求数据
     */
    public void request(int opera) {
        mOpera = opera;
        RequestParam param = RequestParam.builder(act);
        param.put("page", "1");
        param.put("classid", "" + mPosition);
        if (mOpera == XingBo.REQUEST_OPERA_LOAD && nextPagerIndex != -1) {
            //加载更多
            param.put("page", nextPagerIndex + "");
        }
        CommonUtil.request(act, HttpConfig.API_GET_ANCHORS_BY_TYPE, param, TAG, new XingBoResponseHandler<HomeAnchorModel>(this, HomeAnchorModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (ptrGridView != null) {
                    ptrGridView.onRefreshComplete();
                }
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                if (ptrGridView != null) {
                    ptrGridView.onRefreshComplete();
                }
                if (model.getD().getPage() == model.getD().getNext()) {
                    nextPagerIndex = -1;
                } else {
                    nextPagerIndex = model.getD().getNext();
                }
                if (mOpera == XingBoConfig.REQUEST_OPERA_REFRESH) {
                    hots.clear();
                }
                hots.addAll(model.getD().getItems());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case REFRESH_COMPLETE:
                if (ptrGridView != null) {
                    ptrGridView.onRefreshComplete();
                    Toast.makeText(act, "数据已全部加载完成", Toast.LENGTH_SHORT).show();
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
            default:
                break;
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<StickyGridHeadersGridView> refreshView) {
        request(XingBo.REQUEST_OPERA_REFRESH);
    }

    /**
     * 上拉加载
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<StickyGridHeadersGridView> refreshView) {
        if (nextPagerIndex == -1) {
            handler.sendEmptyMessage(REFRESH_COMPLETE);
            return;
        }
        request(XingBo.REQUEST_OPERA_LOAD);
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
                param.put("device", "android");
                CommonUtil.request(act, HttpConfig.API_ENTER_ROOM, param, TAG,
                        new XingBoResponseHandler<RoomModel>(this, RoomModel.class, this) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                alert(msg);
//                                alert("网络不给力,请检查网络状态");
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                XingBoUtil.log(TAG, "请求进入直播房间结果：" + response);
                                if (act != null) {
                                    if (model.getD().getAnchor().getLivestatus().equals("0")) {
                                        Intent intent = new Intent(act, LiveFinishedAct.class);
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, hots.get(position).getPosterlogo());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ID, model.getD().getAnchor().getId());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ONLINES, model.getD().getAnchor().getLiveonlines());
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_IS_FOLLOWED, model.getD().isFollowed());
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(act, MainRoom.class);
                                        intent.putExtra(MainRoom.EXTRA_MAIN_ROOM_INFO, model.getD());
                                        intent.putExtra(MainRoom.ANCHOR_LIVE_LOGO, (TextUtils.isEmpty(hots.get(position).getPosterlogo())) ? (hots.get(position).getAvatar()) : (hots.get(position).getPosterlogo()));
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }
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
}
