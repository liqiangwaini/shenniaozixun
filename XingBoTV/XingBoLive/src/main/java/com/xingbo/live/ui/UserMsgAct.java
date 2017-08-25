package com.xingbo.live.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.UserMessagePagerAdapter;
import com.xingbo.live.entity.MessagePrivate;
import com.xingbo.live.entity.model.MessagePrivateModel;
import com.xingbo.live.eventbus.PriMsgEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.widget.PagerSlidingTabStrip;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by MengruRen on 2016/7/27.
 * 我的消息
 * @package com.xingbo.live.ui
 */
public class UserMsgAct extends BaseAct implements View.OnClickListener, ViewPager.OnPageChangeListener{

    private final static String TAG = "UserMsgAct";
    private final static String[] TITLE = {"私信", "系统消息"};
    public final static String EXTRA_USER_ID = "extra_user_id";

    private ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private TextView mTextViewRead;
    private ImageButton mImageViewBack;
    private UserMessagePagerAdapter mAdapter;
    private SharedPreferences sp;
    private String uid;
    private List<MessagePrivate> messagePrivate;



    public static final int MESSAGENUM = 0;
    private final static int MPDE_PRIVATE = 0x0000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_info);
        uid = getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_USER_LOGIN_UID,"");
        initView();
        initData();
        setData();
        setListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        request();
    }

    private void setListener() {
        mImageViewBack.setOnClickListener(this);
    }

    private void initData() {
        mAdapter = new UserMessagePagerAdapter(getSupportFragmentManager(), TITLE);
    }
    private void setData() {
        viewPager.setAdapter(mAdapter);
        tabStrip.setViewPager(viewPager);
        //  viewPager.setAdapter();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.msg_hot_models_view_pager);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.msg_top_models_tab_bar);
        mTextViewRead = (TextView) findViewById(R.id.user_msg_read);
        mImageViewBack = (ImageButton) findViewById(R.id.user_msg_top_back);
        messagePrivate= new ArrayList<>();
        mTextViewRead.setOnClickListener(this);
        request();
        //私信未读条数显示
    }

    private void request() {
        RequestParam param = RequestParam.builder(this);
        param.put("uid",uid);
        CommonUtil.request(this, HttpConfig.API_USER_GET_PRIVATE_MSG, param, TAG, new XingBoResponseHandler<MessagePrivateModel>(this,MessagePrivateModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {

            }

            @Override
            public void phpXiuSuccess(String response) {
                if (model.getD().getItems()!=null){
                    messagePrivate.addAll(model.getD().getItems());
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_msg_top_back:
                onBackPressed();
                break;
            case R.id.user_msg_read:
                //忽略未读信息
                RequestParam param = RequestParam.builder(this);
                param.put("uid",uid);
                CommonUtil.request(this, HttpConfig.API_USER_GET_CANCEL_UNREAD_COUNT, param, TAG, new XingBoResponseHandler<BaseResponseModel>(BaseResponseModel.class) {
                    @Override
                    public void phpXiuErr(int errCode, String msg) {

                    }

                    @Override
                    public void phpXiuSuccess(String response) {
                        Toast.makeText(UserMsgAct.this, "数据已全部忽略", Toast.LENGTH_SHORT).show();
                        if(messagePrivate.size()!=0){
                            for (int i = 0; i < messagePrivate.size(); i++) {
                                int readflag = messagePrivate.get(i).getReadflag();
                                if (readflag==0){
                                   messagePrivate.get(i).setReadflag(1);
                                }
                            }
                        }
                        EventBus.getDefault().post(new PriMsgEvent());
                    }
                });

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
