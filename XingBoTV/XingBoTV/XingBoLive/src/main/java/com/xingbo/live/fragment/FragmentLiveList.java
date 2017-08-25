package com.xingbo.live.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.HomeModelPagerAdapter;
import com.xingbo.live.eventbus.Skip2HotEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.ui.HomeSearchAct;
import com.xingbo.live.ui.UserAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;
import com.xingbo.live.view.widget.PagerSlidingTabStrip;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by 123 on 2016/7/2.
 * @package com.xingbo.live.fragment
 * @description 描述
 */
public class FragmentLiveList extends MBaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener{
    public final static String TAG = "FragmentLiveList";
    public final  static  String VIEW_CURRENT_POSITION="viewpager_current_positon";

    private final static String[] TITLE = {"关注", "热门","最新"};
    private PagerSlidingTabStrip tabBar;
    private ViewPager modelViewPager;
    private HomeModelPagerAdapter mAdapter;
    private FrescoImageView frescoImageView;
    private String userUrl;
    private SharedPreferences sp;
    public static final int MODE_PRIVATE = 0x0000;

    public static MBaseFragment newInstance() {
        FragmentLiveList fragment = new FragmentLiveList();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        sp=getActivity().getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE);
        userUrl= sp.getString(XingBo.PX_USER_LOGIN_AVATAR, "1");
        CommonUtil.log(TAG, "fragment头像结果" + userUrl);
        frescoImageView.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + userUrl));//测试头像图片
    }

    @Override
      public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe  //跳转到热门页面
    public void skip2Hot(Skip2HotEvent skip2HotEvent){
        modelViewPager.setCurrentItem(1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        sp = getActivity().getSharedPreferences(XingBo.PX_CONFIG_CACHE_FILE, Context.MODE_PRIVATE);
        if (rootView==null) {
            rootView = inflater.inflate(R.layout.live_list_fragment, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        frescoImageView = (FrescoImageView) rootView.findViewById(R.id.header);
        rootView.findViewById(R.id.header).setOnClickListener(this);
        rootView.findViewById(R.id.search_btn).setOnClickListener(this);
        initView();
        CommonUtil.log(TAG, "头像地址" + userUrl);
        return rootView;
    }

    private void initView() {
        tabBar = (PagerSlidingTabStrip) rootView.findViewById(R.id.main_top_models_tab_bar);
        modelViewPager = (ViewPager) rootView.findViewById(R.id.main_hot_models_view_pager);
        try {
            //mAdapter = new TopModelPagerAdapter(((FragmentActivity) act).getSupportFragmentManager(),TITLES);
        } catch (ClassCastException e) {
            XingBoUtil.log(TAG, "Activity-->FragmentActivity@类型转换失败");
        } catch (NoSuchMethodError error) {
            XingBoUtil.log(TAG, "Activity-->FragmentActivity@类型转换失败,没有换到方法");
        }
        modelViewPager.setOffscreenPageLimit(3);
        mAdapter = new HomeModelPagerAdapter(getChildFragmentManager(), TITLE);
        modelViewPager.setAdapter(mAdapter);
        tabBar.setViewPager(modelViewPager);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header:
                Intent userCenter = new Intent(act, UserAct.class);
                startActivity(userCenter);
                break;
            case R.id.search_btn:
                Intent search=new Intent(act, HomeSearchAct.class);
                startActivity(search);
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
}
