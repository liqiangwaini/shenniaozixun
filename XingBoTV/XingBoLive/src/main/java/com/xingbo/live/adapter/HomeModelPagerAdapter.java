package com.xingbo.live.adapter;

import android.support.v4.app.FragmentManager;

import com.xingbo.live.fragment.HomeCareFragment;
import com.xingbo.live.fragment.HomeHotFragment;
import com.xingbo.live.fragment.HomePhoneLiveFragment;
import com.xingbobase.adapter.XingBoFragmentPagerAdapter;
import com.xingbobase.fragment.BaseFragment;

/**
 * Created by WuJinZhou on 2016/2/2.
 */
public class HomeModelPagerAdapter extends XingBoFragmentPagerAdapter {

    public HomeModelPagerAdapter(FragmentManager fm, String[] title) {
        super(fm, title);
    }

    @Override
    public BaseFragment getItem(int position) {
        BaseFragment fragment=null;
        switch (position){
            case 0:
                fragment= HomeCareFragment.newInstance(position);
                break;
            case 1:
                fragment= HomeHotFragment.newInstance(position);
                break;
            case 2: fragment = HomePhoneLiveFragment.newInstance(position);
                break;
        }
        return fragment;
    }
}
