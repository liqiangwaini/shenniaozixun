package com.xingbo.live.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.UserFavoriteListAdapter;
import com.xingbo.live.adapter.holder.UserFavoriteViewHolder;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MainUser;
import com.xingbo.live.entity.UserFavorite;
import com.xingbo.live.entity.model.MainUserModel;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.entity.model.UserFavoriteModel;
import com.xingbo.live.entity.model.UserHomeModel;
import com.xingbo.live.eventbus.MFavoriteEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.api.OnHttpStateCallback;
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
 * Created by WuJinZhou on 2016/2/4.
 * 我的关注
 */
public class UserFavoriteAct extends BaseAct implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {
    public final static String TAG = "UserFavoriteAct";
    private final static int HANDLER_MSG_REFRESH_COMPLETE = 0x555;
    public static final int CLICK_FAVORATE = 2;
    public final static String EXTRA_USER_ID = "extra_user_id";
    public final static String EXTRA_IS_SELF = "extra_is_self";
    private String uid;
    private PullToRefreshListView refreshView;
    private ListView listView;
    private UserFavoriteListAdapter mAdapter;
    private List<UserFavorite> favorites = new ArrayList<UserFavorite>();
    private int mOpera = XingBoConfig.REQUEST_OPERA_INIT;
    private int nextPageIndex = -1;
    private boolean isSelf = false;
    private TextView errMsg;
    private Button errBtn;
    private RelativeLayout emptyViewBox;
    private boolean isFollowed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        uid = getIntent().getStringExtra(EXTRA_USER_ID);
        if (uid == null || uid.equals("")) {
            finish();
            return;
        }
        setContentView(R.layout.user_favorite);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        initView();
    }

    private void initView() {
        refreshView = (PullToRefreshListView) findViewById(R.id.user_favorite_pull_refresh_list);
        refreshView.setOnRefreshListener(this);
        listView = refreshView.getRefreshableView();
        listView.setDivider(null);
        mAdapter = new UserFavoriteListAdapter(this, favorites, this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        request(mOpera);
    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case HANDLER_MSG_REFRESH_COMPLETE:
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                onBackPressed();
                break;
//            case R.id.room_linker_btn:
//                try {
////                    UserFavorite u=(UserFavorite)v.getTag();
////                    if(u!=null){
////                        requestRoom(u);
////                    }
//                }catch (ClassCastException e){
//
//                }
//                break;
            case R.id.user_header:
                String uid = v.getTag().toString();
                Log.d("tag", "userFavorite--->" + uid);
                if (uid != null) {
                    Intent home = new Intent(this, UserHomepageAct.class);
                    home.putExtra(UserHomepageAct.EXTRA_USER_ID, uid);
                    startActivity(home);
                }
                break;
            case R.id.empty_view_refresh_btn:
                request(XingBo.REQUEST_OPERA_REFRESH);
                break;
            default:
                break;
        }
    }

    private void request(int opera) {
        mOpera = opera;
        RequestParam param = RequestParam.builder(this);
        param.put("uid", uid);
        param.put("page", "1");
        if (mOpera == XingBo.REQUEST_OPERA_LOAD && nextPageIndex != -1) {
            param.put("page", nextPageIndex + "");
        }
        OnHttpStateCallback callback = null;
        if (mOpera == XingBo.REQUEST_OPERA_INIT) {
            callback = this;
        }
        CommonUtil.request(this, HttpConfig.API_USER_GET_USER_FAVORITE, param, TAG, new XingBoResponseHandler<UserFavoriteModel>(this, UserFavoriteModel.class, callback) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
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
                XingBoUtil.log(TAG, "用户关注数据结果：" + response);
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                if (model.getD().getPage() == model.getD().getNext()) {
                    nextPageIndex = -1;
                } else {
                    nextPageIndex = model.getD().getNext();
                }
                if (mOpera == XingBoConfig.REQUEST_OPERA_REFRESH) {
                    favorites.clear();
                }
                if (model.getD() != null) {
                    favorites.addAll(model.getD().getItems());
                }
                if (favorites.size() == 0) {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    if (isSelf) {
                        errMsg.setText("您还未关注过任何主播");
                    } else {
                        errMsg.setText("TA还未关注过任何主播");
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

    /**
     * 请求进入房间
     */
//    public void requestRoom(UserFavorite userFavorite){
//        RequestParam param= RequestParam.builder(this);
//        param.put("livename",userFavorite.getLivename());
//        param.put("rid",userFavorite.getId());
//        CommonUtil.request(this, HttpConfig.API_ENTER_ROOM, param, TAG,
//                new XingBoResponseHandler<RoomModel>(this, RoomModel.class, this) {
//                    @Override
//                    public void phpXiuErr(int errCode, String msg) {
//                        alert(msg);
//                    }
//
//                    @Override
//                    public void phpXiuSuccess(String response) {
//                        XingBoUtil.log(TAG, "请求进入直播房间结果：" + response);
////                        Intent intent = new Intent(UserFavoriteAct.this, MainRoom.class);
////                        intent.putExtra(MainRoom.EXTRA_MAIN_ROOM_INFO, model.getD());
////                        startActivity(intent);
//                    }
//                });
//    }
    @Subscribe
    public void refresh(MFavoriteEvent event) {
        request(XingBo.REQUEST_OPERA_REFRESH);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        request(XingBo.REQUEST_OPERA_REFRESH);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (nextPageIndex == -1) {
            handler.sendEmptyMessage(HANDLER_MSG_REFRESH_COMPLETE);
            return;
        }
        request(XingBo.REQUEST_OPERA_LOAD);
    }

    public void showErrView() {
        ViewStub stub = (ViewStub) findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_fans_concern));
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
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) findViewById(R.id.empty_view_box);

    }

    @Override
    public void requestStart() {
        showDoing(TAG, null);
    }

    @Override
    public void requestFinish() {
        done();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (view.getTag() instanceof UserFavoriteViewHolder) {
            UserFavoriteViewHolder holder = (UserFavoriteViewHolder) view.getTag();
            if (holder.getFavorite() != null) {


            }
        }
    }
}
