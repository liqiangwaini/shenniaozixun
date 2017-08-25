package com.xingbo.live.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.entity.Gift;
import com.xingbo.live.eventbus.GiftBagPagerChange;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.FrescoUtils;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 普通礼物列表
 * Created by WuJinZhou on 2015/12/3.
 */
public class GiftGridAdapter extends BaseAdapter {
    private Context context;
    private List<Gift> list = new ArrayList<>();
    private Pair<Integer, Integer> pair;
    //    private int sw;
//    private int parentH = 0;
//    private int itemH = 0;
//    private int iconH = 0;
    private RelativeLayout.LayoutParams paramsIcon;

    private int selectPosition = -1;

    public GiftGridAdapter(Context context, List<Gift> mList) {
        this.context = context;
        list.addAll(mList);
//        sw = (int) SystemApp.screenWidth;
//        parentH = (sw * 11) / 25 + XingBoUtil.dip2px(context, 0.5f);//viewPager的高度
//        itemH = parentH / 2;//GridView单元格的高度
//        iconH = itemH - XingBoUtil.dip2px(context, 37);
    }

    public void setSelection(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position < list.size()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Gift item = null;
        if (list != null && position < list.size()) {
            item = list.get(position);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_room_gift_pan_grid_view_item, null);
//            convertView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, itemH));
        }
        FrescoImageView icon = (FrescoImageView) convertView.findViewById(R.id.gift_item_icon);
        ImageView lucky = (ImageView) convertView.findViewById(R.id.gift_item_lucky);
        ImageView action = (ImageView) convertView.findViewById(R.id.gift_item_action);
        ImageView week = (ImageView) convertView.findViewById(R.id.gift_item_week);
        if (paramsIcon == null) {
            paramsIcon = (RelativeLayout.LayoutParams) icon.getLayoutParams();
//            paramsIcon.height = iconH;
//            paramsIcon.width = iconH;
        }
        icon.setLayoutParams(paramsIcon);
        TextView name = (TextView) convertView.findViewById(R.id.gift_item_name);
        TextView price = (TextView) convertView.findViewById(R.id.gift_item_price);
        FrescoImageView cover = (FrescoImageView) convertView.findViewById(R.id.item_cover);

        if (item != null) {
//            icon.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + item.getIcon()));
            FrescoUtils.showThumb(Uri.parse(HttpConfig.FILE_SERVER + item.getIcon()), icon, 50, context);
            name.setText(item.getName());
            if (item.isBag()) {
                price.setText(item.getNum() + "个");
            } else {
                price.setText(item.getConsume() + "星币");
            }
            if (item.getLucky_gift() == 1) {
                lucky.setVisibility(View.VISIBLE);
            } else {
                lucky.setVisibility(View.GONE);
            }
            if (item.getActivity_gift() == 1) {
                action.setVisibility(View.VISIBLE);
            } else {
                action.setVisibility(View.GONE);
            }
            if (item.getWeek_gift() == 1) {
                week.setVisibility(View.VISIBLE);
            } else {
                week.setVisibility(View.GONE);
            }
        }
        if (item != null) {
            if (position == selectPosition && selectPosition != -1) {
                cover.setVisibility(View.VISIBLE);
            } else {
                cover.setVisibility(View.GONE);
            }
            convertView.setTag(item);
        }
        return convertView;
    }

    /**
     * 更新数量
     *
     * @param cost 消费使用数量
     */
    public void updateNum(String id, int cost) {
        boolean isRefresh = false;//是否刷新UI
        boolean isRemoveItem = false;
        List<Gift> lists = new ArrayList<>();
        lists.addAll(list);
        for (int i = 0; i < lists.size(); i++) {
            Gift temp = list.get(i);
            if (temp.getId().equals(id) && temp.isBag()) {
                isRefresh = true;
                int currentNum = 0;
                try {
                    currentNum = Integer.parseInt(temp.getNum());
                } catch (NumberFormatException e) {
                    isRefresh = false;
                    return;
                }
                int lastNum = currentNum - cost;
                if (lastNum < 1) {
                    list.remove(i);
                    isRemoveItem = true;
                } else {
                    temp.setNum(lastNum + "");
                }
                break;
            }
        }
        if (isRefresh) {
            //刷新UI
            list.clear();
            list.addAll(lists);
            this.notifyDataSetChanged();
            lists=null;
            if (isRemoveItem) {
                //减页检查通知
                EventBus.getDefault().post(new GiftBagPagerChange());
            }
        }
    }

    /**
     * 更新列表数据项
     */
    public void updateState(Gift item) {
        boolean isRefresh = false;//是否刷新UI
        for (int i = 0; i < list.size(); i++) {
            Gift temp = list.get(i);
            //如果选中项为包裹
            if (item == null) {
                break;
            }
            if (item.isBag()) {
                if (temp.getId().equals(item.getId()) && !temp.isBag() && temp.isSelected()) {
                    //同id但不是包裹，且为选中状态，则刷新
                    isRefresh = true;
                    temp.setSelected(false);
                } else if (temp.getId().equals(item.getId()) && temp.isBag() && !temp.isSelected()) {
                    //同id包裹，且为未选中状态，则刷新
                    isRefresh = true;
                    temp.setSelected(true);
                } else if (!temp.getId().equals(item.getId()) && temp.isBag() && temp.isSelected()) {
                    //不同id包裹，且为选中状态，则刷新
                    isRefresh = true;
                    temp.setSelected(false);
                }
            } else {
                if (temp.getId().equals(item.getId()) && !temp.isSelected() && !temp.isBag()) {
                    isRefresh = true;
                    temp.setSelected(true);
                } else if (temp.getId().equals(item.getId()) && temp.isSelected() && temp.isBag()) {
                    isRefresh = true;
                    temp.setSelected(false);
                } else if (!temp.getId().equals(item.getId()) && temp.isSelected()) {
                    isRefresh = true;
                    temp.setSelected(false);
                }
            }

        }
        if (isRefresh) {
            //刷新UI
            notifyDataSetChanged();
        }
    }

    /**
     * 清除列表选择状态
     */
    public void clearSelected() {
        boolean isRefresh = false;//是否刷新UI
        for (int i = 0; i < list.size(); i++) {
            Gift temp = list.get(i);
            if (temp.isSelected()) {
                isRefresh = true;
                temp.setSelected(false);
            }
        }
        if (isRefresh) {
            //刷新UI
            notifyDataSetChanged();
        }
    }
}
