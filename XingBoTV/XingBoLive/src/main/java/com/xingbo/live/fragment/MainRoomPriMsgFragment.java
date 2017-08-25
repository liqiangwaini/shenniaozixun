package com.xingbo.live.fragment;

import android.app.Activity;
import android.content.Context;
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

import com.xingbo.live.R;
import com.xingbo.live.adapter.MainRoomPriMsgAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.dialog.PrivateMsgDetailDialog;
import com.xingbo.live.entity.MessagePrivate;
import com.xingbo.live.entity.msg.PriMsgDetailMsg;
import com.xingbo.live.entity.model.MessagePrivateModel;
import com.xingbo.live.eventbus.PriMessageEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.TimeUtils;
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

public class MainRoomPriMsgFragment extends MBaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private final static String TAG = "UserMsgPrivateFragment";
    private final static String ARG_POSITION = "arg_view_page_position";
    private final static int REFRESH_COMPLETE = 0x1;
    private boolean isInit = true;
    private int mPositon = 1;
    private static final int REFRESH_COMPELETE = 0x1;
    private String uid;//当前用户的id
    private String sid;//发送人 id
    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private MainRoomPriMsgAdapter mAdapter;
    private List<MessagePrivate> list = new ArrayList<>();
    private RelativeLayout emptyViewBox;
    private TextView errMsg;
    private Context context;
    private boolean isDialog = false;
    private ViewStub viewStub;
    private Button errBtn;

    public static MBaseFragment newInstance(int position) {
        MainRoomPriMsgFragment fragment = new MainRoomPriMsgFragment();
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
        this.context = activity;
    }

    public void isDialog(boolean isDialog) {
        this.isDialog = isDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_room_pri_msg_fragment, container, false);
        uid = context.getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID, "");
        initView(rootView);
        return rootView;
    }

    public void initView(View v) {
        viewStub = (ViewStub) v.findViewById(R.id.loading_err_view);
        pullToRefreshListView = (PullToRefreshListView) v.findViewById(R.id.msg_private_list);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
//        listView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(this);
        pullToRefreshListView.setOnItemClickListener(this);
        pullToRefreshListView.getRefreshableView().setOnItemLongClickListener(this);
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
        errBtn.setVisibility(View.VISIBLE);
        errBtn.setText("重试");
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
        errBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
    }

    private int currentPage = 1;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 获取私信列表
     */
    public void request() {
        requestStart();
        RequestParam param = RequestParam.builder(act);
        param.put("uid", uid);
        param.put("page", currentPage + "");
        param.put("pagesize", XingBoConfig.COMMON_PAGE_SIZE + "");
        CommonUtil.request(act, HttpConfig.API_USER_GET_PRIVATE_MSG, param, TAG, new XingBoResponseHandler<MessagePrivateModel>(this, MessagePrivateModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                requestFinish();
                if (pullToRefreshListView != null) {
                    pullToRefreshListView.onRefreshComplete();
                }
                alert(msg);
                if (currentPage == 1) {
                    if (emptyViewBox == null) {
                        showErrViewByNetwork();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    if (errCode == 5) {  //jsonParseException
                        errMsg.setText("数据异常，请重试");
                    } else {
                        errMsg.setText("亲,网络不给力,请检查网络连接状态");//按要求 修改为
                    }
                } else {
                    alert(msg);
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                requestFinish();
                Log.d("fragmentPrivate", response);
                if (pullToRefreshListView != null) {
                    pullToRefreshListView.onRefreshComplete();
                }

                if (model.getD() != null) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    if (model.getD().getItems() != null) {
                        list.addAll(model.getD().getItems());
                    }
                    if (list.size() == 0) {
                        if (emptyViewBox == null) {
                            showErrView();
                        }
                        if (emptyViewBox.getVisibility() == View.GONE) {
                            emptyViewBox.setVisibility(View.VISIBLE);
                        }
                        errMsg.setText("列表空空如也，快找小伙伴聊一聊吧");
                    } else {
                        mAdapter = new MainRoomPriMsgAdapter(context, list);
                        pullToRefreshListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                if (model.getD().getTotal() == list.size()) {
                    currentPage = -1;
                } else {
                    currentPage = model.getD().getPagesize();
                }
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

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        currentPage = 1;
        request();
    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (currentPage == -1) {
            if (pullToRefreshListView != null) {
                pullToRefreshListView.onRefreshComplete();
            }
            alert("数据已全部加载完成");
            return;
        }else{
            if (pullToRefreshListView!=null){
                pullToRefreshListView.onRefreshComplete();
            }
            currentPage++;
            request();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        list.get(position - 1).setReadflag(1);
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new PriMessageEvent(list.get(position - 1).getUid(), list.get(position - 1).getNick()));
    }
/*
    private OnPriMsgItemClick listener;
    public void setOnItemClickListener(OnPriMsgItemClick listener){
        this.listener=listener;
    }
    public interface OnPriMsgItemClick{
        public void setOnItemClick(String nick,String uid);
    }*/

    //获取到发送来的私信消息
    public void refreshPriMsgList(PriMsgDetailMsg priMsgDetail) {
        boolean hasShow = false;
        int currentPriMsg = -1;
        for (int i = 0; i < list.size(); i++) {
            if (priMsgDetail != null && priMsgDetail.getData().getFuser() != null && list.get(i).getUid().equals(priMsgDetail.getData().getFuser().getId())) {
                hasShow = true;
                currentPriMsg = i;
                break;
            }
        }
        if (currentPriMsg == -1) {
            request();
        } else {
            list.get(currentPriMsg).setReadflag(0);
            list.get(currentPriMsg).setCtime(TimeUtils.getHourAndMins(System.currentTimeMillis()));
            list.get(currentPriMsg).setLastmsg(priMsgDetail.getData().getMsg());
            mAdapter.setData(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final MessagePrivate messagePrivate = list.get(position-1);
        final String sendId = messagePrivate.getUid();
        XingBoUtil.dialog(act, "删除会话", "您确定要删除此会话么?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.px_dialog_ok:
                        RequestParam param = RequestParam.builder(act);
                        param.put("uid", sendId);
                        CommonUtil.request(act, HttpConfig.API_APP_GET_USER_DELETE_USER_SESSION, param, TAG, new XingBoResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
                            @Override
                            public void phpXiuErr(int errCode, String msg) {
                                alert("请求失败！");
                            }
                            @Override
                            public void phpXiuSuccess(String response) {
                             //   list.clear();
                                Log.d(TAG,response);
                                currentPage=1;
                                request();
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
