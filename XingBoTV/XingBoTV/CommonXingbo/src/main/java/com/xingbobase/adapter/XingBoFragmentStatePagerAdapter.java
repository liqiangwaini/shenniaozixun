package com.xingbobase.adapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;


/**
 * Created by WuJinZhou on 2015/8/8.
 */
public abstract class XingBoFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    public String [] title={
            "热门",
            "PHPXiu_TAG_2",
            "PHPXiu_TAG_3",
    "PHPXiu_TAG_4",
    "PHPXiu_TAG_5",
    "PHPXiu_TAG_6",
    "PHPXiu_TAG_7",
    "PHPXiu_TAG_8",
    "PHPXiu_TAG_9"};
    public XingBoFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public XingBoFragmentStatePagerAdapter(FragmentManager fm, String[] title) {
        super(fm);
        this.title=title;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

/**
    @Override
    public int getItemPosition(Object object) {
        // 设置返回PagerAdapter.POSITION_NONE，
        // 当调用notifyDataSetChanged时会调用destroyItem销掉之前预加载的几个fragment然后再创建预加载个数fragment，
        // 此处可能会导致性能下降，因为内存无法及时释放
        //return PagerAdapter.POSITION_NONE;
        return super.getItemPosition(object);
    }
    */
}
