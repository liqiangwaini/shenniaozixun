package com.xingbo.live.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.adapter.UserIncomeListAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.UserIncomeListItem;
import com.xingbo.live.entity.model.UserIncomeListModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.api.OnHttpStateCallback;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshListView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terry on 2016/7/19.
 */
public class UserIncomeListAct extends BaseAct implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    public final static String TAG="UserIncomeListAct";
    private final static int HANDLER_MSG_REFRESH_COMPLETE=0x556;
    private PullToRefreshListView refreshView;
    private ListView listView;
    private UserIncomeListAdapter mAdapter;
    private List<UserIncomeListItem> incomeList=new ArrayList<UserIncomeListItem>();
    private int mOpera= XingBo.REQUEST_OPERA_INIT;
    private int nextPageIndex=-1;
    private boolean isSelf=false;
    private TextView errMsg;
    private Button  errBtn;
    private RelativeLayout emptyViewBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_income);
        findViewById(R.id.top_back_btn).setOnClickListener(this);

        initView();
    }

    private void initView() {
        refreshView = (PullToRefreshListView) findViewById(R.id.user_income_pull_refresh_list);
        refreshView.setOnRefreshListener(this);
        listView = refreshView.getRefreshableView();
        listView.setDivider(null);
        mAdapter=new UserIncomeListAdapter(this, incomeList);
        listView.setAdapter(mAdapter);
        request(mOpera);
    }

    private void request(int opera){
        mOpera=opera;
        RequestParam param= RequestParam.builder(this);
        if(mOpera== XingBo.REQUEST_OPERA_LOAD&&nextPageIndex!=-1){
            param.put("page",nextPageIndex+"");
        }
        OnHttpStateCallback callback=null;
        if(mOpera== XingBo.REQUEST_OPERA_INIT){
            callback=this;
        }
        CommonUtil.request(this, HttpConfig.API_APP_GET_USER_INCOME_LIST, param, TAG, new XingBoResponseHandler<UserIncomeListModel>(this, UserIncomeListModel.class, callback) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                if (mOpera == XingBo.REQUEST_OPERA_REFRESH || mOpera == XingBo.REQUEST_OPERA_INIT) {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    errMsg.setText("获取数据失败，请检测网络连接" + msg);
                } else {
                    alert(msg);
                }
            }

            @Override
            public void phpXiuSuccess(String response) {
                XingBoUtil.log(TAG, "用户收益记录列表：" + response);
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                if (model.getD().getPage() == model.getD().getNext()) {
                    nextPageIndex = -1;
                } else {
                    nextPageIndex = model.getD().getNext();
                }
                if (mOpera == XingBoConfig.REQUEST_OPERA_REFRESH) {
                    incomeList.clear();
                }
                if (model.getD() != null) {
                    incomeList.addAll(model.getD().getItems());
                }
                if (incomeList.size() == 0) {
                    if (emptyViewBox == null) {
                        showErrView();
                    }
                    if (emptyViewBox.getVisibility() == View.GONE) {
                        emptyViewBox.setVisibility(View.VISIBLE);
                    }
                    if (isSelf) {
                        errMsg.setText("还没有任何收益记录");
                    } else {
                        errMsg.setText("还没有任何收益记录");
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

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what){
            case HANDLER_MSG_REFRESH_COMPLETE:
                if(refreshView!=null){
                    refreshView.onRefreshComplete();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.empty_view_refresh_btn:
                request(XingBo.REQUEST_OPERA_REFRESH);
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        request(XingBo.REQUEST_OPERA_REFRESH);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if(nextPageIndex==-1){
            handler.sendEmptyMessage(HANDLER_MSG_REFRESH_COMPLETE);
            return;
        }
        request(XingBo.REQUEST_OPERA_LOAD);
    }

    @Override
    public void requestStart() {
        showDoing(TAG, null);
    }

    @Override
    public void requestFinish() {
        done();
    }

    public void showErrView(){
        ViewStub stub=(ViewStub)findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg=(TextView)findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView=(FrescoImageView)findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.empty_money));
        errBtn= (Button) findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox=(RelativeLayout)findViewById(R.id.empty_view_box);
    }
}
