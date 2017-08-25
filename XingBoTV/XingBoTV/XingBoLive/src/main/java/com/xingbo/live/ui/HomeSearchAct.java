package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.SearchAnchorsAdapter;
import com.xingbo.live.emotion.EmotionEditText;
import com.xingbo.live.entity.SearchAnchors;
import com.xingbo.live.entity.model.RoomModel;
import com.xingbo.live.entity.model.SearchAnchorsModel;
import com.xingbo.live.eventbus.HomeSearchEvent;
import com.xingbo.live.fragment.MBaseFragment;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/12
 */
public class HomeSearchAct extends BaseAct implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final static String TAG = "HomeSearchAct";

    private EmotionEditText searchAnchorsEdit;
    private TextView ensureSearch;
    private PullToRefreshListView mPullToRefreshListView;
    private List<SearchAnchors> mList = new ArrayList<>();
    private SearchAnchorsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_search_activity);
        EventBus.getDefault().register(this);
        initView();
    }

    @Subscribe
    public void updateDataSearch(HomeSearchEvent homeSearchEvent){
      request();

    }

    private void initView() {
        searchAnchorsEdit = (EmotionEditText) findViewById(R.id.home_search_edit);
        searchAnchorsEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_DOWN ) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(HomeSearchAct.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //接下来在这里做你自己想要做的事，实现自己的业务。
                    request();
                }
                return false;
            }
        });
        ensureSearch = (TextView) findViewById(R.id.ensure_search);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.home_search_result);
        mPullToRefreshListView.setOnItemClickListener(this);
        ensureSearch.setOnClickListener(this);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.ensure_search:
                request();
                //收起软件盘
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
        }
    }

    private void request() {
        RequestParam param = RequestParam.builder(this);
        param.put("skey", searchAnchorsEdit.getText().toString());
        param.put("device", "android");
        CommonUtil.request(this, HttpConfig.API_USER_GET_SEARCH_ANCHORS, param, TAG, new XingBoResponseHandler<SearchAnchorsModel>(SearchAnchorsModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {

                if (model.getD() != null) {
                    mList.clear();
                    List<SearchAnchors> items = model.getD().getItems();
                    if (items.size() != 0) {
                        mList.addAll(items);
                        mAdapter = new SearchAnchorsAdapter(HomeSearchAct.this, mList);
                        mPullToRefreshListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        //Toast.makeText(HomeSearchAct.this,"不存在你输入的昵称，请换一个试试",Toast.LENGTH_SHORT).show();
                        alert("你输入的昵称不存在，亲，换一个试试哦！");
                        searchAnchorsEdit.setText("");
                    }

                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final SearchAnchors searchAnchors = mList.get(position-1);
        String rid = searchAnchors.getId();
        final String liveLogo = searchAnchors.getLiveLogo();
        int livestatus = searchAnchors.getLivestatus();
        if (livestatus==0){//未开播
            Intent home= new Intent(HomeSearchAct.this, UserHomepageAct.class);
            home.putExtra(UserHomepageAct.EXTRA_USER_ID,rid);
            startActivity(home);

        }else if (livestatus==1){//跳转到直播间
            RequestParam param = RequestParam.builder(HomeSearchAct.this);
            param.put("device", "android");
            param.put("rid",rid);
            CommonUtil.request(HomeSearchAct.this, HttpConfig.API_ENTER_ROOM, param, TAG,
                    new XingBoResponseHandler<RoomModel>(this, RoomModel.class, this) {
                        @Override
                        public void phpXiuErr(int errCode, String msg) {
//                                alert(msg);
                            alert("网络不给力,请检查网络状态");
                        }
                        @Override
                        public void phpXiuSuccess(String response) {
                            XingBoUtil.log(TAG, "请求进入直播房间结果：" + response);
                            if (model.getD().getAnchor().getLivestatus().equals("0")) {
                                Intent intent = new Intent(HomeSearchAct.this, LiveFinishedAct.class);
                                intent.putExtra(LiveFinishedAct.LIVE_ROOM_BG_LOGO, HttpConfig.FILE_SERVER + liveLogo);
                                intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ID, model.getD().getAnchor().getId());
                                intent.putExtra(LiveFinishedAct.LIVE_ROOM_ANCHOR_ONLINES, model.getD().getAnchor().getLiveonlines());
                                intent.putExtra(LiveFinishedAct.LIVE_ROOM_IS_FOLLOWED, model.getD().isFollowed());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(HomeSearchAct.this, MainRoom.class);
                                intent.putExtra(MainRoom.EXTRA_MAIN_ROOM_INFO, model.getD());
                                intent.putExtra(MainRoom.ANCHOR_LIVE_LOGO, HttpConfig.FILE_SERVER + liveLogo);
                                startActivity(intent);
                            }
                        }
                    });
        }
//        Toast.makeText(HomeSearchAct.this, "livestatus->"+livestatus+",id->"+rid, Toast.LENGTH_SHORT).show();

    }
}
