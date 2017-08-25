package com.xingbo.live.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingbo.live.R;
import com.xingbo.live.adapter.GiftViewPagerAdapter;
import com.xingbo.live.entity.Gift;
import com.xingbo.live.entity.GiftType;
import com.xingbo.live.entity.model.GiftPanModel;
import com.xingbo.live.eventbus.GiftBagPagerChange;
import com.xingbo.live.eventbus.GiftItemSelectedEvent;
import com.xingbo.live.eventbus.UpdateGiftBagNum;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by WuJinZhou on 2015/12/6.
 */
public class GiftFragment extends MBaseFragment implements ViewPager.OnPageChangeListener {
    private final static String TAG = "GiftFragment";
    public static final String PAGER_LIST = "list";
    private final static int REFRESH_GIFT_STATE = 0x1;
    private View rootView;
    private ViewPager giftPager;//礼物面板,礼物分页器容器
    public GiftViewPagerAdapter giftViewPagerAdapter;//礼物面板,礼物分页适配器
    public CirclePageIndicator indicator;//礼物面板,礼物分页指示器
    private ArrayList<ArrayList<Gift>> list;
    private  Gift gift;

    public static GiftFragment newInstance(ArrayList<ArrayList<Gift>> pagers) {
        GiftFragment fragment = new GiftFragment();
        Bundle b = new Bundle();
        b.putSerializable(PAGER_LIST, pagers);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        list = (ArrayList<ArrayList<Gift>>) getArguments().getSerializable(PAGER_LIST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_room_gift_pan_fragment, null);
        giftPager = (ViewPager) rootView.findViewById(R.id.gift_view_pager);
        indicator = (CirclePageIndicator) rootView.findViewById(R.id.gift_pager_indicator);
        initGiftViewPager(list);
        return rootView;
    }

    /**
     * 初始化礼物分页内容
     */
    public void initGiftViewPager(ArrayList<ArrayList<Gift>> pagers) {
        giftPager.setOffscreenPageLimit(pagers.size());
        giftViewPagerAdapter = new GiftViewPagerAdapter(pagers);
        giftPager.setAdapter(giftViewPagerAdapter);
        indicator.setOnPageChangeListener(this);
        indicator.setViewPager(giftPager);
        indicator.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case REFRESH_GIFT_STATE:
                if (giftViewPagerAdapter != null) {
                    GiftItemSelectedEvent event = (GiftItemSelectedEvent) msg.obj;
//                    if (event.getGiftItem().isBag()) {//包裹
//                        giftViewPagerAdapter.resetAll();
//                    }
                    giftViewPagerAdapter.updateSelected(event.getGiftItem());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 有礼物项被先中
     */
    @Subscribe
    public void GiftSelected(GiftItemSelectedEvent event) {
        Message msg = handler.obtainMessage();
        msg.what = REFRESH_GIFT_STATE;
        msg.obj = event;
        handler.sendMessage(msg);
        gift=event.getGiftItem();
    }


    //获取礼物列表
    public void getGiftList() {
        RequestParam param = RequestParam.builder(act);
        Log.e(TAG, HttpConfig.API_GET_GIFTS);
        CommonUtil.request(act, HttpConfig.API_GET_GIFTS, param, TAG, new XingBoResponseHandler<GiftPanModel>(this, GiftPanModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                  alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                act.runOnUiThread(new Runnable() {
                    private List<GiftType> giftTypes;
                    @Override
                    public void run() {
                        giftTypes = (List<GiftType>) model.getD();
                        if (giftTypes == null) {
                            return;
                        }
                        for (int i = 0; i < giftTypes.size(); i++) {
                            GiftType type = giftTypes.get(i);
                              if (type.getId().equals("9999")) {
                                list = type.pagers();
                            }
                        }
                    }
                });
            }
        });
    }


    /**
     * 礼包扣除成功
     */
    @Subscribe
    public void GiftBagUpdate(final UpdateGiftBagNum event) {
//        giftViewPagerAdapter.updateGiftBagNum(event.getId(), event.getNum());
//        getGiftList();
        new Thread(){
            @Override
            public void run() {
                String removeGiftIndex=null;
                ArrayList<Gift> bagList=new ArrayList<Gift>();
                for(int i=0;i<list.size();i++){
                    for(int j=0;j<list.get(i).size();j++){
                        if(list.get(i).get(j).getId().equals(event.getId())){
                            int lastNumber=Integer.parseInt(list.get(i).get(j).getNum())-event.getNum();
                            if(lastNumber>0){
                                list.get(i).get(j).setNum(lastNumber+"");
                            }else{
                                removeGiftIndex=i+"##"+j;
                            }
                            break;
                        }
                    }
                }
                if(removeGiftIndex!=null){
                    list.get(Integer.parseInt(removeGiftIndex.split("##")[0])).remove(Integer.parseInt(removeGiftIndex.split("##")[1]));
                }
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        giftViewPagerAdapter.setData(list);
                        giftViewPagerAdapter.updateAllSelected();
                    }
                });
            }
        }.start();
//        indicator.notifyDataSetChanged();
//        initGiftViewPager(list);
    }

    /**
     * 当有包裹被使用完时，检查是否页面是否有礼包，没有则减页
     */
    @Subscribe
    public void checkPagersChange(GiftBagPagerChange event) {
        for (List<Gift> bagList : list) {
            if (bagList.size() == 0) {
                CommonUtil.log(TAG, "减页...");
                list.remove(bagList);
                giftViewPagerAdapter.notifyDataSetChanged();
                indicator.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        giftViewPagerAdapter.resetAll(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
