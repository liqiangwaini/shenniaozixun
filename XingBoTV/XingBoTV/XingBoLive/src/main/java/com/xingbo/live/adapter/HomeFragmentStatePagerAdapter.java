package com.xingbo.live.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.xingbo.live.entity.HomeTab;
import com.xingbobase.adapter.XingBoFragmentPagerAdapter;
import com.xingbo.live.entity.PicWall;
import com.xingbo.live.fragment.HomeIndexFragment;
import com.xingbo.live.fragment.HomeOtherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuJinZhou on 2016/1/8.
 */
public class HomeFragmentStatePagerAdapter extends XingBoFragmentPagerAdapter {
    private ArrayList<PicWall>walls;
    private List<HomeTab> tabs;
    public HomeFragmentStatePagerAdapter(FragmentManager fm, String[] title,ArrayList<PicWall>walls,List<HomeTab> tabs) {
        super(fm, title);
        this.walls=walls;
        this.tabs=tabs;
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0){
           return HomeIndexFragment.newInstance(position,walls);
        }else{
           return HomeOtherFragment.newInstance(tabs.get(position).getId());
        }
    }
}
