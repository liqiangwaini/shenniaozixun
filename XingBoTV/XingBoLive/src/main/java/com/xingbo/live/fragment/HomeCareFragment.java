package com.xingbo.live.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.xingbo.live.adapter.CommonAnchorListCareAdapter;
import com.xingbo.live.adapter.holder.AnchorViewHolder;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.HomeAnchor;
import com.xingbo.live.entity.model.HomeAnchorModel;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.eventbus.Skip2HotEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.LiveFinishedAct;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.TimeUtils;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;
import com.xingbobase.view.widget.XingBoLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

//import com.xingbo.live.ui.MainRoom;

/**
 * Created by WuJinZhou on 2016/1/12.
 */
public class HomeCareFragment extends MBaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>,
        AdapterView.OnItemClickListener, DialogInterface.OnCancelListener, View.OnClickListener {
    public final static String TAG = "HomeCareFragment";
    public final static String ARG_POSITION = "arg_view_pager_position";
    private int mPosition = 1;
    private final static int REFRESH_COMPLETE = 0x1;
    private PullToRefreshListView refreshView;
    private ListView listView;
    //    private PullToRefreshGridView ptrRefreshView;
//    private GridView mGridView;
    private CommonAnchorListCareAdapter mAdapter;
    private List<HomeAnchor> anchors = new ArrayList<HomeAnchor>();
    private int mOpera = XingBo.REQUEST_OPERA_INIT;
    private int nextPagerIndex = -1;
    private TextView errMsg;
    private Button errBtn;
    private RelativeLayout emptyViewBox;
    private FrescoImageView errImageView;

    public static MBaseFragment newInstance(int position) {
        HomeCareFragment fragment = new HomeCareFragment();
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
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.home_fragment_care, container, false);
        refreshView = (PullToRefreshListView) rootView.findViewById(R.id.common_anchor_list_refresh);
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        refreshView.setOnRefreshListener(this);
        init();
        return rootView;
    }

    public void init() {
        listView = refreshView.getRefreshableView();
        mAdapter = new CommonAnchorListCareAdapter(act, anchors);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        request(mOpera);
    }

//    public void onDestroy() {
//        super.onDestroy();
//    }

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
        CommonUtil.request(act, HttpConfig.API_APP_MYFOLLOW, param, TAG, new XingBoResponseHandler<HomeAnchorModel>(this, HomeAnchorModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                if (mOpera == XingBo.REQUEST_OPERA_REFRESH || mOpera == XingBo.REQUEST_OPERA_INIT) {
                    if (emptyViewBox == null) {
                        showErrViewByNetworkOrEmpty(false);
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
//                    errMsg.setText("请检测网络连接,"+msg);
                    errMsg.setText("亲,网络不给力,请检查网络连接状态");//按要求 修改为
                    errBtn.setVisibility(View.INVISIBLE);
                    errImageView.setVisibility(View.VISIBLE);
                    errImageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));
                } else {
                    alert(msg);
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                CommonUtil.log(TAG, "请求结果" + response);
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                if (model.getD().getPage() == model.getD().getNext()) {
                    nextPagerIndex = -1;
                } else {
                    nextPagerIndex = model.getD().getNext();
                }
                if (mOpera == XingBoConfig.REQUEST_OPERA_REFRESH) {
                    anchors.clear();
                }
//                if (model.getD().getItems() != null) {
                if ((mOpera == XingBo.REQUEST_OPERA_REFRESH || mOpera == XingBo.REQUEST_OPERA_INIT) && model.getD().getItems().size() == 0) {
                    if (emptyViewBox == null) {
                        showErrViewByNetworkOrEmpty(true);
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    errMsg.setText("此时没有关注好友的直播");
                    errImageView.setVisibility(View.INVISIBLE);
                    errBtn.setVisibility(View.VISIBLE);
                } else {
                    if (emptyViewBox == null) {
                        showErrViewByNetworkOrEmpty(true);
                    }
                    if (emptyViewBox.getVisibility() == View.VISIBLE) {
                        emptyViewBox.setVisibility(View.GONE);
                    }
                }
                anchors.addAll(model.getD().getItems());
                listView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                //          }

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
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (TimeUtils.isFastDoubleClick(R.id.common_anchor_list_refresh)) {
            return;
        }
        if (view.getTag() instanceof AnchorViewHolder) {
            AnchorViewHolder holder = (AnchorViewHolder) view.getTag();
            if (holder.getAnchor() != null) {
                //请求进入直播房间，准备跳转到房间
                RequestParam param = RequestParam.builder(act);
                param.put("device", "android");
                param.put("rid", holder.getAnchor().getId());
                CommonUtil.request(act, HttpConfig.API_ENTER_ROOM, param, TAG,
                        new XingBoResponseHandler<RoomModel>(this, RoomModel.class, this) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                alert("网络不给力,请检查网络状态");
                            }

                            @Override
                            public void phpXiuSuccess(String response) {
                                XingBoUtil.log(TAG, "请求进入直播房间结果：" + response);
                                if (model.getD().getAnchor().getLivestatus().equals("0")) {
                                    Intent intent = new Intent(act, LiveFinishedAct.class);
                                    intent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, HttpConfig.FILE_SERVER + anchors.get(position - 1).getPosterlogo());
                                    intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ID, model.getD().getAnchor().getId());
                                    intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ONLINES, model.getD().getAnchor().getLiveonlines());
                                    intent.putExtra(LiveFinishedAct.LIVE_ROOM_IS_FOLLOWED, model.getD().isFollowed());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(act, MainRoom.class);
                                    intent.putExtra(MainRoom.EXTRA_MAIN_ROOM_INFO, model.getD());
                                    intent.putExtra(MainRoom.ANCHOR_LIVE_LOGO, TextUtils.isEmpty(anchors.get(position - 1).getPosterlogo()) ? anchors.get(position - 1).getAvatar() : anchors.get(position - 1).getPosterlogo());
                                    startActivity(intent);
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
                //跳转到热门页面
                EventBus.getDefault().post(new Skip2HotEvent());
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

    public void showErrViewByNetworkOrEmpty(boolean isEmpty) {
        View stub = View.inflate(getActivity(), R.layout.common_empty_list_view, null);
        errMsg = (TextView) stub.findViewById(R.id.empty_view_err_msg);
        errImageView = (FrescoImageView) stub.findViewById(R.id.empty_view_bg_icon);
        errBtn = (Button) stub.findViewById(R.id.empty_view_refresh_btn);
        emptyViewBox = (RelativeLayout) stub.findViewById(R.id.empty_view_box);
        errBtn.setOnClickListener(this);
        if (isEmpty) {
            errImageView.setVisibility(View.INVISIBLE);
        } else {
            errImageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_network_state));
            errBtn.setVisibility(View.INVISIBLE);
        }
        listView.setEmptyView(stub);
    }

    /**
     * 下拉刷新
     *
     * @param refreshView
     */

    @Override
    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
        mOpera = XingBoConfig.REQUEST_OPERA_REFRESH;
        request(mOpera);
    }

    /**
     * 上拉加载
     *
     * @param refreshView
     */
    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
        if (nextPagerIndex == -1) {
            handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
            Toast.makeText(getActivity(), "数据已全部加载完成", Toast.LENGTH_SHORT).show();
            return;
        }
        request(mOpera);

    }

}
