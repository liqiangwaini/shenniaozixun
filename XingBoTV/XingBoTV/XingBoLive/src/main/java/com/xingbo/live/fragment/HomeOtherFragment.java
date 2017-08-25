package com.xingbo.live.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.ui.LiveFinishedAct;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.TimeUtils;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshGridView;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.RequestParam;
import com.xingbo.live.R;
import com.xingbo.live.adapter.holder.AnchorViewHolder;
import com.xingbo.live.adapter.CommonAnchorListAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbo.live.entity.model.HomeAnchorModel;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;
import com.xingbobase.view.widget.XingBoLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/12.
 */
public class HomeOtherFragment extends MBaseFragment implements PullToRefreshBase.OnRefreshListener2<GridView>,
        AdapterView.OnItemClickListener, DialogInterface.OnCancelListener, View.OnClickListener {
    public final static String TAG = "HomeOtherFragment";
    public final static String ARG_POSITION = "arg_view_pager_position";
    private boolean isInit = true;
    private int mPosition = 1;
    private final static int REFRESH_COMPLETE = 0x1;

    private PullToRefreshGridView ptrRefreshView;
    private GridView mGridView;
    private CommonAnchorListAdapter mAdapter;
    private List<HomeAnchor> anchors = new ArrayList<HomeAnchor>();

    private int mOpera = XingBo.REQUEST_OPERA_INIT;
    private int nextPagerIndex = -1;
    private TextView errMsg;
    private Button errBtn;
    private RelativeLayout emptyViewBox;

    public static MBaseFragment newInstance(int position) {
        HomeOtherFragment fragment = new HomeOtherFragment();
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
        rootView = inflater.inflate(R.layout.home_fragment_other, null);
        ptrRefreshView = (PullToRefreshGridView) rootView.findViewById(R.id.common_anchor_grid_refresh);
        ptrRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        ptrRefreshView.setOnRefreshListener(this);
        init();
        return rootView;
    }

    public void init() {
        int padding = XingBoUtil.dip2px(act, 10);
        mGridView = ptrRefreshView.getRefreshableView();
        mAdapter = new CommonAnchorListAdapter(act, anchors);
        mGridView.setAdapter(mAdapter);
        mGridView.setPadding(0, padding, 0, 0);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onShow() {
        if (isInit) {//需要初始化
            request(mOpera);
        }
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
                if (ptrRefreshView != null) {
                    ptrRefreshView.onRefreshComplete();
                }
                if (mOpera == XingBo.REQUEST_OPERA_REFRESH || mOpera == XingBo.REQUEST_OPERA_INIT) {
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
                rootView.findViewById(R.id.empty_view_refresh_btn).setClickable(true);
            }

            @Override
            public void phpXiuSuccess(String response) {
                Log.d("tag", "发现页签的" + response);
                if (ptrRefreshView != null) {
                    ptrRefreshView.onRefreshComplete();
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
                    errMsg.setText("暂时没有数据,请稍后再试!");
                    errBtn.setText("刷新页面");

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

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case REFRESH_COMPLETE:
                if (ptrRefreshView != null) {
                    ptrRefreshView.onRefreshComplete();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
        request(XingBo.REQUEST_OPERA_REFRESH);
    }

    /**
     * 上拉加载
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
        if (nextPagerIndex == -1) {
            handler.sendEmptyMessage(REFRESH_COMPLETE);
            Toast.makeText(getActivity(), "数据已全部加载完成", Toast.LENGTH_SHORT).show();
            return;
        }
        request(XingBo.REQUEST_OPERA_LOAD);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        if (TimeUtils.isFastDoubleClick(R.id.common_anchor_grid_refresh)) {
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
//                                alert(msg);
                                alert("网络不给力,请检查网络状态");
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                XingBoUtil.log(TAG, "请求进入直播房间结果：" + response);
                                if (act != null) {
                                    if (model.getD().getAnchor().getLivestatus().equals("0")) {
                                        Intent intent = new Intent(act, LiveFinishedAct.class);
                                        intent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, anchors.get(position).getPosterlogo());
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
                //  rootView.findViewById(R.id.empty_view_refresh_btn).setClickable(false);
                request(XingBo.REQUEST_OPERA_REFRESH);
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
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.common_empty_view_icon));
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setOnClickListener(this);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }

    public void showErrViewByNetwork() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));
        rootView.findViewById(R.id.empty_view_refresh_btn).setOnClickListener(this);
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setOnClickListener(this);
        errBtn.setVisibility(View.VISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);

    }

}
