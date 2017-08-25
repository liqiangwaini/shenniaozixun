package com.xingbo.live.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xingbo.live.R;
import com.xingbo.live.entity.Gift;
import com.xingbo.live.entity.GiftTagSelectorItem;
import com.xingbo.live.entity.GiftType;
import com.xingbo.live.entity.RoomInfo;
import com.xingbo.live.fragment.GiftFragment;
import com.xingbobase.util.XingBoUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/12.
 */
public class GiftPopup extends PopupWindow implements View.OnClickListener{
    private static final String TAG="GiftPopup";

    private Activity act;
    private final TextView balanceInfo;
    private final TextView sendNumber;
    private final Button sendGift;
    private FragmentManager fragmentManager;

    public GiftPopup(Activity act,RoomInfo roomInfo,int screenWidth,GiftTagSelectorItem item,FragmentManager fragmentManager){
        this.act=act;
        this.fragmentManager=fragmentManager;
        View rootView = View.inflate(act, R.layout.main_room_gift_pan, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        common = (Button) rootView.findViewById(R.id.gift_btn_bar_common);
        advanced = (Button) rootView.findViewById(R.id.gift_btn_bar_advanced);//高级按钮
        exclusive = (Button) rootView.findViewById(R.id.gift_btn_bar_exclusive);//专属按钮
        bag = (Button) rootView.findViewById(R.id.gift_btn_bar_bag);//包裹按钮
        balanceInfo = (TextView) rootView.findViewById(R.id.coin_info_value);
        luck = (Button) rootView.findViewById(R.id.gift_btn_bar_luck);//幸运礼物按钮
        sendGift = (Button) rootView.findViewById(R.id.send_gift_btn);
        sendNumber = (TextView) rootView.findViewById(R.id.tv_gift_send_number);
        FrameLayout pageContainer = (FrameLayout) rootView.findViewById(R.id.gift_container);

        balanceInfo.setText(roomInfo.getCoin() + "星币");
        FrameLayout.LayoutParams pageContainerParam = (FrameLayout.LayoutParams) pageContainer.getLayoutParams();
        //可在此处调整底部指示器的区域的高度,底部指示器高度为固定15dp,ViewPager顶部底部补白为1，列分垂直分割线高度为1dp，只有两行所以总占3dp高度
        pageContainerParam.height = (int) ((screenWidth * 11) / 25 + XingBoUtil.dip2px(act, 15));//+ PHPXiuUtil.dip2px(this,18);
        pageContainer.setLayoutParams(pageContainerParam);
        common.setOnClickListener(this);
        advanced.setOnClickListener(this);
        exclusive.setOnClickListener(this);
        bag.setOnClickListener(this);
        luck.setOnClickListener(this);
        sendGift.setOnClickListener(this);
        openGiftPan(item);
        showCommonGiftFragment();

    }

    private boolean isGiftPanViewInit = false;//礼物面板是否初始化
    private TextView numSelectedInfo, tagSelectedInfo;//当前选中的个数、收礼对象项信息
    private String currentTagId;//当前收礼对象id
    private List<GiftTagSelectorItem> tagSelectorItems = new ArrayList<GiftTagSelectorItem>();
    private List<GiftType> giftTypes;



    private boolean isOpenGiftPan = false;//礼物面板是否弹出
    private RelativeLayout giftPan;//礼物管理面板

    /**
     * 打开礼物面板
     */
    public void openGiftPan(GiftTagSelectorItem item) {
        if (!isGiftPanViewInit) {
            isGiftPanViewInit = true;
        } else {
            if (item != null) {
                if (tagSelectedInfo != null) {
                    tagSelectedInfo.setText(item.getNick());
                }
                currentTagId = item.getId();
            }
        }
//        isOpenGiftPan=!isOpenGiftPan;
    }

    /**
     * 收回礼物面板
     */
    public void hideGiftPan() {
        giftPan.clearAnimation();
        if (isOpenGiftPan) {
            if (animationHide == null) {
                animationHide = AnimationUtils.loadAnimation(act,
                        R.anim.gift_selector_hide);
            }
            giftPan.startAnimation(animationHide);
            animationHide.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    giftPan.setVisibility(View.GONE);
//                    title.setVisibility(View.VISIBLE);
                }
            });
            isOpenGiftPan = !isOpenGiftPan;
        }
    }

    // private Button common, advanced, exclusive, bag, luck;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gift_btn_bar_common:
                break;
            case R.id.gift_btn_bar_advanced:
                break;
            case R.id.gift_btn_bar_exclusive:
                break;
            case R.id.gift_btn_bar_bag:
                break;
            case R.id.gift_btn_bar_luck:
                break;
            case R.id.send_gift_btn:
                break;
        }
    }

    /**
     * 动画监听器
     */
    class AnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    }

    private Animation animationShow, animationHide;//礼物面板弹出收回动画
    private final static int OPEN_OR_HIDE_GIFT_PAN = 0x3;//关闭或打开礼物面板

    /**
     * 弹出礼物面板
     */
    public void showGiftPan() {
        giftPan.clearAnimation();
        if (animationShow == null) {
            animationShow = AnimationUtils.loadAnimation(act,
                    R.anim.gift_selector_show);
        }
        giftPan.setVisibility(View.VISIBLE);
        giftPan.startAnimation(animationShow);
        isOpenGiftPan = !isOpenGiftPan;
    }

    /**
     * 初始化礼物分页内容
     */
    public void initGiftViewPager(Context context, ArrayList<ArrayList<Gift>> pagers) {
    }


    /**
     * 显示普通礼物列表
     */
    public void showCommonGiftFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        if (commonFragment == null) {
            // 如果commonFragment为空，则创建一个并添加到界面上
            commonFragment = GiftFragment.newInstance(commonGiftList);
            fragmentTransaction.add(R.id.gift_container, commonFragment);
        } else {
            // 如果commonFragment不为空，则直接将它显示出来
            fragmentTransaction.show(commonFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private FragmentTransaction fragmentTransaction;
    private boolean isGiftPanDataInit = false;//礼物面板数据是否初始化
    private ArrayList<ArrayList<Gift>> commonGiftList, advancedGiftList, exclusiveGiftList, bagList, luckList;

    /**
     * 隐藏所有礼物选择界面
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (commonFragment != null) {
            transaction.hide(commonFragment);
        }
        if (advancedFragment != null) {
            transaction.hide(advancedFragment);
        }
        if (exclusiveFragment != null) {
            transaction.hide(exclusiveFragment);
        }
        if (bagFragment != null) {
            transaction.hide(bagFragment);
        }
        if (luckFragment != null) {
            transaction.hide(luckFragment);//增加幸运礼物
        }
    }

    private Button common, advanced, exclusive, bag, luck;//礼物面板，普通、高级、专属和包裹四个导航按钮 增加幸运礼物按钮
    private int GIFT_TOP_BAR_BLACK_COLOR = Color.parseColor("#ff424242");
    private int GIFT_TOP_BAR_WHITE_COLOR = Color.parseColor("#ffffff");
    private GiftFragment commonFragment, advancedFragment, exclusiveFragment, bagFragment, luckFragment;//增加幸运礼物Fragment

    /**
     * 更新礼物面板导航按钮状态
     */
    public void updateGiftPanBarState() {
        common.setBackgroundResource(R.mipmap.gift_pan_top_bar_button_normal);
        common.setTextColor(GIFT_TOP_BAR_BLACK_COLOR);
        advanced.setBackgroundResource(R.mipmap.gift_pan_top_bar_button_normal);
        advanced.setTextColor(GIFT_TOP_BAR_BLACK_COLOR);
        exclusive.setBackgroundResource(R.mipmap.gift_pan_top_bar_button_normal);
        exclusive.setTextColor(GIFT_TOP_BAR_BLACK_COLOR);
        bag.setBackgroundResource(R.mipmap.gift_pan_top_bar_button_normal);
        bag.setTextColor(GIFT_TOP_BAR_BLACK_COLOR);
        luck.setBackgroundResource(R.mipmap.gift_pan_top_bar_button_normal);//增加幸运礼物
        luck.setTextColor(GIFT_TOP_BAR_BLACK_COLOR);
    }

    /**
     * 弹出礼物窗口
     */
/*
    public void onOpenGiftWin(String uid, String nick) {
        CommonUtil.log(TAG, "打开礼物面板");
        //键盘或表情键盘是打开状态，则关闭。
        Message msg = handler.obtainMessage();
        msg.what = OPEN_OR_HIDE_GIFT_PAN;
        if (uid != null) {
            msg.obj = new GiftTagSelectorItem(uid, nick);
        }
        handler.sendMessageDelayed(msg, 100);//延迟100毫秒操作
    }*/


}
