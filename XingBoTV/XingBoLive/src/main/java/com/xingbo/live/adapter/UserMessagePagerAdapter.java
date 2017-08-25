package com.xingbo.live.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xingbo.live.fragment.UserMsgPrivateFragment;
import com.xingbo.live.fragment.UserMsgSystemFragment;
import com.xingbobase.adapter.XingBoFragmentPagerAdapter;
import com.xingbobase.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by MengruRen on 2016/7/28.
 *
 * @package com.xingbo.live.adapter.holder
 */
public class UserMessagePagerAdapter extends XingBoFragmentPagerAdapter   {


    public UserMessagePagerAdapter(FragmentManager fm,String[] title) {
        super(fm,title);
    }

    @Override
    public BaseFragment getItem(int position) {
        BaseFragment fragment=null;
        switch (position){
            case 0:
                //私信fragment
                fragment=UserMsgPrivateFragment.newInstance(position);
                break;
            case 1:
                //系统信息的Fragmennt
                fragment= UserMsgSystemFragment.newInstance(position);
                break;
        }
        return fragment;
    }
}
