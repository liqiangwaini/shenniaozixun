package com.xingbo.live.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xingbo.live.R;
import com.xingbo.live.entity.Gift;
import com.xingbo.live.eventbus.GiftItemSelectedEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by WuJinZhou on 2015/12/3.
 */
public class GiftViewPagerAdapter extends PagerAdapter {

    private ArrayList<ArrayList<Gift>> pagers = new ArrayList<ArrayList<Gift>>();
    private WeakReference<ViewGroup> c;
    private GridView gridView;

    public GiftViewPagerAdapter(ArrayList<ArrayList<Gift>> pagers) {
        if (pagers != null) {
            this.pagers = pagers;
        }
    }

    public void setData(ArrayList<ArrayList<Gift>> pagers) {
        if (pagers != null) {
            this.pagers = pagers;
            updateAllSelected();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        c = new WeakReference<ViewGroup>(container);
        gridView = (GridView) LayoutInflater.from(container.getContext()).inflate(R.layout.main_room_gift_pan_grid_view, null);
        final GiftGridAdapter mAdapter = new GiftGridAdapter(container.getContext(), pagers.get(position));
        mAdapter.setSelection(-1);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(position<=pagers.get(position).size()){
                    resetAll(position);
                    mAdapter.setSelection(i);
                    mAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new GiftItemSelectedEvent((Gift)view.getTag()));
                }
            }
        });
        container.addView(gridView);
        return gridView;
    }

    /**
     * 更新数量
     */
    public void updateGiftBagNum(String id, int cost) {
        if (c.get().getChildCount() > 0) {
            for (int i = 0; i < c.get().getChildCount(); i++) {
                View v = c.get().getChildAt(i);
                GridView gridView = (GridView) v;
                GiftGridAdapter giftGridAdapter = (GiftGridAdapter) gridView.getAdapter();
                giftGridAdapter.updateNum(id, cost);
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (pagers != null) {
            return pagers.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 清除选中状态
     */
    public void resetAll() {
        if (c == null || c.get() == null) {
            return;
        }
        if (c.get().getChildCount() > 0) {
            for (int i = 0; i < c.get().getChildCount(); i++) {
                View v = c.get().getChildAt(i);
                GridView gridView = (GridView) v;
                GiftGridAdapter giftGridAdapter = (GiftGridAdapter) gridView.getAdapter();
                if(giftGridAdapter==null){
                    break;
                }
                giftGridAdapter.clearSelected();
                giftGridAdapter.setSelection(-1);
                giftGridAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 清除选中状态
     */
    public void resetAll(int page) {
        if (c == null || c.get() == null) {
            return;
        }
        if (c.get().getChildCount() > 0) {
            for (int i = 0; i < c.get().getChildCount(); i++) {
                if(page==i){
                    continue;
                }
                View v = c.get().getChildAt(i);
                GridView gridView = (GridView) v;
                GiftGridAdapter giftGridAdapter = (GiftGridAdapter) gridView.getAdapter();
                if(giftGridAdapter==null){
                    continue;
                }
                giftGridAdapter.clearSelected();
                giftGridAdapter.setSelection(-1);
                giftGridAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 更新各个GridView的数据状态
     */
    public void updateSelected(Gift item) {
        if (c == null || c.get() == null) {
            return;
        }
        if (c.get().getChildCount() > 0) {
            for (int i = 0; i < c.get().getChildCount(); i++) {
                View v = c.get().getChildAt(i);
                GridView gridView = (GridView) v;
                GiftGridAdapter giftGridAdapter = (GiftGridAdapter) gridView.getAdapter();
                giftGridAdapter.updateState(item);
            }
        }
    }

    /**
     * 更新所有GridView的数据状态
     */
    public void updateAllSelected() {
        if (c == null || c.get() == null) {
            return;
        }
        if (c.get().getChildCount() > 0) {
            for (int i = 0; i < c.get().getChildCount(); i++) {
                View v = c.get().getChildAt(i);
                GridView gridView = (GridView) v;
                GiftGridAdapter giftGridAdapter = (GiftGridAdapter) gridView.getAdapter();
                giftGridAdapter.notifyDataSetChanged();
            }
        }
    }
}
