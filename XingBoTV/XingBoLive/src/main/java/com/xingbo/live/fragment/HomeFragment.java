package com.xingbo.live.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.ui.HomeSearchAct;
import com.xingbo.live.ui.UserAct;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.http.RequestParam;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;
import com.xingbo.live.R;
import com.xingbo.live.adapter.HomeFragmentStatePagerAdapter;
import com.xingbo.live.entity.HomeTab;
import com.xingbo.live.entity.PicWall;
import com.xingbo.live.entity.model.HomeBaseModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.view.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/6.
 */
public class HomeFragment extends MBaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    public final static String TAG = "HomeFragment";
    private Gson gson;
    private RelativeLayout contentBox;
    private PagerSlidingTabStrip tabStrip;
    private ViewPager mViewPager;
    private HomeFragmentStatePagerAdapter mAdapter;
    private ImageButton nextFrg;
    private String[] titles;
    private ArrayList<PicWall> picWalls = new ArrayList<PicWall>();
    private RelativeLayout loadingBox;
    private TextView errMsg;
    private Button errBtn;
    private RelativeLayout emptyViewBox;
    private SharedPreferences sp;
    private String userUrl;
    private FrescoImageView header;

    public static MBaseFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        sp = getActivity().getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        userUrl = sp.getString(XingBo.PX_USER_LOGIN_AVATAR, "1");
        CommonUtil.log(TAG, "fragment头像结果" + userUrl);
        header.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + userUrl));//测试头像图片
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences(XingBo.PX_CONFIG_CACHE_FILE, Context.MODE_PRIVATE);
        if (rootView==null) {
            rootView = inflater.inflate(R.layout.home_fragment, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        contentBox = (RelativeLayout) rootView.findViewById(R.id.home_fragment_content_box);
        // rootView.findViewById(R.id.history_btn).setOnClickListener(this);
        rootView.findViewById(R.id.search_btn).setOnClickListener(this);
        header = (FrescoImageView) rootView.findViewById(R.id.header);
        header = (FrescoImageView) rootView.findViewById(R.id.header);
        header.setOnClickListener(this);
        gson = new Gson();
        loadingBox = (RelativeLayout) rootView.findViewById(R.id.loading_view_box);
        ImageView loadView = (ImageView) rootView.findViewById(R.id.loading_view);
        AnimationDrawable aniDraw = (AnimationDrawable) loadView.getBackground();
        aniDraw.start();
        initHomeFragment();
        return rootView;
    }

    /**
     * 请求初始化首页
     */
    private void initHomeFragment() {
        RequestParam param = RequestParam.builder(act);
        CommonUtil.request(act, HttpConfig.API_GET_HOME, param, TAG, new XingBoResponseHandler<HomeBaseModel>(this, HomeBaseModel.class, this) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                }
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
                int i = 0;
                List<HomeTab> tabs = baseModel.getD().getLiveclass();
                titles = new String[tabs.size()];
                for (HomeTab tab : tabs) {
                    titles[i] = tab.getName();
                    i++;
                }
                picWalls.addAll(baseModel.getD().getPicwalls());
                if (loadingBox.getVisibility() == View.VISIBLE) {
                    loadingBox.setVisibility(View.GONE);
                }
                if (emptyViewBox != null && emptyViewBox.getVisibility() == View.VISIBLE) {
                    emptyViewBox.setVisibility(View.GONE);
                }
                contentBox.setVisibility(View.VISIBLE);
                tabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.home_fragment_tab_bar);
                mViewPager = (ViewPager) rootView.findViewById(R.id.home_fragment_view_pager);
                initView(tabs);
            }
        });
    }

    public void initView(List<HomeTab> tabs) {
        try {
            //mAdapter = new HomeFragmentStatePagerAdapter(((FragmentActivity) act).getSupportFragmentManager(),titles,picWalls);
        } catch (ClassCastException e) {
            XingBoUtil.log(TAG, "Activity-->FragmentActivity@类型转换失败");
        } catch (NoSuchMethodError error) {
            XingBoUtil.log(TAG, "Activity-->FragmentActivity@类型转换失败,没有换到方法");
        }
        mAdapter = new HomeFragmentStatePagerAdapter(getChildFragmentManager(), titles, picWalls, tabs);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(titles.length);
        tabStrip.setViewPager(mViewPager);
        tabStrip.setOnPageChangeListener(pageChangeListener);
        // nextFrg = (ImageButton) rootView.findViewById(R.id.move_right);
        if (tabStrip.isShowRightBtn) {
            //   nextFrg.setOnClickListener(this);
        } else {
            //   nextFrg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.move_right:
//                int position=mViewPager.getCurrentItem();
//                int totalPage=mAdapter.getCount();
//                if(position<totalPage-1){
//                   mViewPager.setCurrentItem(position+1,false);
//                }
//                break;
            case R.id.header:
                Intent userCenter = new Intent(act, UserAct.class);
                startActivity(userCenter);
                break;

            case R.id.search_btn:
                Intent search = new Intent(act, HomeSearchAct.class);
                startActivity(search);
                break;
            case R.id.empty_view_refresh_btn://点击进入网络设置 刷新页面重新请求网络
//                CommonUtil.log(TAG,"网络状态"+sp.getString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_NO));
////                if (sp.getString(XingBo.PX_CONFIG_CACHE_NET_WORK, InternetStateBroadcast.NET_NO).equals("NO")){
////                     startActivity(new Intent(Settings.ACTION_SETTINGS));
////                }
//                rootView.findViewById(R.id.empty_view_refresh_btn).setClickable(false);
                initHomeFragment();
//                EventBus.getDefault().post(new HomeFragmentRefreshEvent());
                break;
            default:
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

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void showErrView() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.common_empty_view_icon));
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        rootView.findViewById(R.id.empty_view_refresh_btn).setOnClickListener(this);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }
}
