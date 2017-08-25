package com.xingbo.live.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


import com.xingbo.live.fragment.MBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/1
 */
public class PictureShowPagerAdapter extends FragmentPagerAdapter{
    private List<MBaseFragment> fragments = new ArrayList<MBaseFragment>();
    Fragment currentFragment;

    public PictureShowPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public PictureShowPagerAdapter(FragmentManager fm, List<MBaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        int ret=0;
        if (fragments!=null){
            ret=fragments.size();
        }
        return ret;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        currentFragment =(PictureShowFragment) object;
        super.setPrimaryItem(container, position, object);
    }
}
