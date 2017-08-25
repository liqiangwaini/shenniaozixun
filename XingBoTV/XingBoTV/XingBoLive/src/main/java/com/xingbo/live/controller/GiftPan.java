package com.xingbo.live.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.Gift;
import com.xingbo.live.entity.GiftTagSelectorItem;
import com.xingbo.live.entity.GiftType;
import com.xingbo.live.entity.RoomInfo;
import com.xingbo.live.entity.model.GiftPanModel;
import com.xingbo.live.entity.msg.CancelUserBagItemBody;
import com.xingbo.live.eventbus.GiftItemSelectedEvent;
import com.xingbo.live.fragment.GiftFragment;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.ui.RechargeAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.SoftInputUtils;
import com.xingbo.live.view.widget.InputFilterMinMax;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/9/1.
 */
public class GiftPan implements View.OnClickListener {
    private static final String TAG = "GiftPan";
    private MainRoom act;
    private RoomInfo roomInfo;
    private RoomController controller;

    private RadioButton common, advanced, exclusive, bag, luck;//礼物面板，普通、高级、专属,幸运和包裹 导航
    private GiftFragment commonFragment, advancedFragment, exclusiveFragment, bagFragment, luckFragment;

    private Animation animationShow, animationHide;//礼物面板弹出收回动画

    private boolean isGiftPanViewInit = false;//礼物面板是否初始化

    private List<GiftTagSelectorItem> tagSelectorItems = new ArrayList<GiftTagSelectorItem>();
    private List<GiftType> giftTypes;

    private FragmentTransaction fragmentTransaction;
    private boolean isGiftPanDataInit = false;//礼物面板数据是否初始化
    private ArrayList<ArrayList<Gift>> commonGiftList, advancedGiftList, exclusiveGiftList, bagList, luckList;
    private FragmentManager fragmentManager;
    private View giftPanBlank;
    private LinearLayout llGiftPan;
    private RelativeLayout giftPan;
    private View commonLine;
    private View advancedLine;
    private View exclusiveLine;
    private View bagLine;
    public TextView balanceInfo;
    private TextView recharge;
    private Button sendGift;
    private TextView sendNumber;
    private FrameLayout pageContainer;
    private LinearLayout llGiftNumber;
    private TextView undefined;
    private LinearLayout number1314;
    private LinearLayout number520;
    private LinearLayout number188;
    private LinearLayout number66;
    private LinearLayout number10;
    private RelativeLayout rlEditGiftNumber;
    private LinearLayout llEditGiftBumber;
    private View editGiftNumberBlank;
    private EditText etNumber;
    private Button confirmEtNumber;
    private boolean giftSendNumFlag = true;


    public GiftPan(MainRoom act, View rootView, RoomInfo roomInfo, RoomController controller) {
        this.act = act;
        this.roomInfo = roomInfo;
        this.controller = controller;
        initView(rootView);
        initClick();
        getGiftList();
    }

    public void initView(View rootView) {
        giftPanRl = (RelativeLayout) rootView.findViewById(R.id.gift_pan);
        fragmentManager = act.getSupportFragmentManager();
        giftPanBlank = rootView.findViewById(R.id.gift_pan_blank);
        llGiftPan = (LinearLayout) rootView.findViewById(R.id.ll_giftPan);
        giftPan = (RelativeLayout) rootView.findViewById(R.id.include_gift_pan);
        common = (RadioButton) rootView.findViewById(R.id.gift_btn_bar_common);
        advanced = (RadioButton) rootView.findViewById(R.id.gift_btn_bar_advanced);//高级按钮
        exclusive = (RadioButton) rootView.findViewById(R.id.gift_btn_bar_exclusive);//专属按钮
        bag = (RadioButton) rootView.findViewById(R.id.gift_btn_bar_bag);//包裹按钮
        luck = (RadioButton) rootView.findViewById(R.id.gift_btn_bar_luck);//幸运礼物按钮
        commonLine = rootView.findViewById(R.id.gift_pan_line_common);
//        luckLine = rootView.findViewById(R.id.gift_pan_line_luck);
        advancedLine = rootView.findViewById(R.id.gift_pan_line_advanced);
        exclusiveLine = rootView.findViewById(R.id.gift_pan_line_exclusive);
        bagLine = rootView.findViewById(R.id.gift_pan_line_bag);

        balanceInfo = (TextView) rootView.findViewById(R.id.coin_info_value);
        recharge = (TextView) rootView.findViewById(R.id.gift_pan_recharge);
        sendGift = (Button) rootView.findViewById(R.id.send_gift_btn);
        sendNumber = (TextView) rootView.findViewById(R.id.tv_gift_send_number);
        pageContainer = (FrameLayout) rootView.findViewById(R.id.gift_container);
        //choose gift number
        llGiftNumber = (LinearLayout) rootView.findViewById(R.id.include_choose_gift_number);
        llGiftNumber.setVisibility(View.INVISIBLE);
        undefined = (TextView) rootView.findViewById(R.id.popup_choose_number_undefined);
        number1314 = (LinearLayout) rootView.findViewById(R.id.popup_choose_number_1314);
        number520 = (LinearLayout) rootView.findViewById(R.id.popup_choose_number_520);
        number188 = (LinearLayout) rootView.findViewById(R.id.popup_choose_number_188);
        number66 = (LinearLayout) rootView.findViewById(R.id.popup_choose_number_66);
        number10 = (LinearLayout) rootView.findViewById(R.id.popup_choose_number_10);
        //edit number
        rlEditGiftNumber = (RelativeLayout) rootView.findViewById(R.id.rl_edit_gift_number);

        editGiftNumberBlank = rootView.findViewById(R.id.edit_gift_number_blank);
        etNumber = (EditText) rootView.findViewById(R.id.et_edit_gift_number);
        confirmEtNumber = (Button) rootView.findViewById(R.id.btn_edit_gift_number);
        etNumber.setText("1");
        etNumber.setFilters(new InputFilter[]{new InputFilterMinMax("1", "99999")});
        balanceInfo.setText(roomInfo.getCoin() + "");
        sendNumber.setText("1");
//        LinearLayout.LayoutParams pageContainerParam = (LinearLayout.LayoutParams) pageContainer.getLayoutParams();
//        //可在此处调整底部指示器的区域的高度,底部指示器高度为固定15dp,ViewPager顶部底部补白为1，列分垂直分割线高度为1dp，只有两行所以总占3dp高度
//        pageContainerParam.height = (int) ((screenWidth * 11) / 25 + XingBoUtil.dip2px(this, 15));//+ PHPXiuUtil.dip2px(this,18);
//        pageContainer.setLayoutParams(pageContainerParam);
    }

    public void initClick() {
        llGiftPan.setOnClickListener(this);
        common.setOnClickListener(this);
        advanced.setOnClickListener(this);
        exclusive.setOnClickListener(this);
        bag.setOnClickListener(this);
        luck.setOnClickListener(this);
        recharge.setOnClickListener(this);
        sendNumber.setOnClickListener(this);
        sendGift.setOnClickListener(this);
        giftPanBlank.setOnClickListener(this);
        //choose gift number
        editGiftNumberBlank.setOnClickListener(this);
        undefined.setOnClickListener(this);
        number1314.setOnClickListener(this);
        number520.setOnClickListener(this);
        number188.setOnClickListener(this);
        number66.setOnClickListener(this);
        number10.setOnClickListener(this);
        confirmEtNumber.setOnClickListener(this);
    }


    //获取礼物列表
    public void getGiftList() {
        RequestParam param = RequestParam.builder(act);
        Log.e(TAG, HttpConfig.API_GET_GIFTS);
        CommonUtil.request(act, HttpConfig.API_GET_GIFTS, param, TAG, new XingBoResponseHandler<GiftPanModel>(act, GiftPanModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
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
                            if (type.getId().equals("1")) {
                                commonGiftList = type.pagers();
                                showCommonGiftFragment();
                            } else if (type.getId().equals("2")) {
                                advancedGiftList = type.pagers();
                            } else if (type.getId().equals("3")) {
                                exclusiveGiftList = type.pagers();
//                    } else if (type.getId().equals("8")) {
//                        luckList = type.pagers();
                            } else if (type.getId().equals("9999")) {
                                bagList = type.pagers();
                            }
                        }
                    }
                });
            }
        });
    }

    public void setBags(final CancelUserBagItemBody body) {
        new Thread() {
            @Override
            public void run() {
                String removeGift = null;
                for (int i = 0; i < bagList.size(); i++) {
                    for (int j = 0; j < bagList.get(i).size(); j++) {
                        if (bagList.get(i).get(j).getId().equals(body.getGid())) {
                            if (body.getBagnum() <= 0) {
                                removeGift = i + "##" + j;
                            } else {
                                bagList.get(i).get(j).setNum(body.getBagnum() + "");
                            }
                            break;
                        }
                    }
                }
                if (removeGift != null) {
                    bagList.get(Integer.parseInt(removeGift.split("##")[0])).remove(Integer.parseInt(removeGift.split("##")[1]));
                }
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bagFragment == null) {
                            bagFragment = GiftFragment.newInstance(bagList);
                        } else {
                            if(bagFragment!=null&&bagFragment. giftViewPagerAdapter!=null){
                                bagFragment. giftViewPagerAdapter.setData(bagList);
                                bagFragment.giftViewPagerAdapter.updateAllSelected();
                            }
                        }
                    }
                });

            }
        }.start();
    }


    /**
     * 有礼物项被先中
     */
    public void GiftSelected(GiftItemSelectedEvent event) {
        if (llGiftNumber.getVisibility() == View.VISIBLE) {
            llGiftNumber.setVisibility(View.INVISIBLE);
        }
        currentGift = event.getGiftItem();
        if (currentGift == null) {
            return;
        }
    }

    /**
     * 弹出礼物窗口
     */
    public void onOpenGiftWin(String uid, String nick) {
        CommonUtil.log(TAG, "打开礼物面板");
//        //键盘或表情键盘是打开状态，则关闭。
//        Message msg = handler.obtainMessage();
//        msg.what = XingBoConfig.OPEN_OR_HIDE_GIFT_PAN;
//        if (uid != null) {
//            msg.obj = new GiftTagSelectorItem(uid, nick);
//        }
//        handler.sendMessageDelayed(msg, 100);//延迟100毫秒操作
    }


    //重置礼物柜的选中状态
    private void resetLuck() {
        if (luckFragment != null && luckFragment.giftViewPagerAdapter != null) {
            luckFragment.giftViewPagerAdapter.resetAll();
        }
    }

    private void resetBag() {
        if (bagFragment != null && bagFragment.giftViewPagerAdapter != null) {
            bagFragment.giftViewPagerAdapter.resetAll();
        }
    }

    private void resetExclusive() {
        if (exclusiveFragment != null && exclusiveFragment.giftViewPagerAdapter != null) {
            exclusiveFragment.giftViewPagerAdapter.resetAll();
        }
    }

    private void resetAdvanced() {
        if (advancedFragment != null && advancedFragment.giftViewPagerAdapter != null) {
            advancedFragment.giftViewPagerAdapter.resetAll();
        }
    }

    private void resetCommon() {
        if (commonFragment != null && commonFragment.giftViewPagerAdapter != null) {
            commonFragment.giftViewPagerAdapter.resetAll();
        }
    }

    private void giftStart() {
        if (fragmentTransaction == null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
//        hideFragments(fragmentTransaction);
    }

    private void giftEnd() {

    }

    private Gift currentGift = null;

    private int currentGiftNumber = 1;
    private boolean isOpenGiftPan = false;//礼物面板是否弹出
    public RelativeLayout giftPanRl;//礼物管理面板

    /**
     * 打开礼物面板
     */
    public void openGiftPan() {
        currentGift = null;
        resetAdvanced();
        resetBag();
        resetCommon();
        resetExclusive();
        resetLuck();
        currentGiftNumber = 1;
        if (isOpenGiftPan) {
            hideGiftPan();
        } else {
            showGiftPan();
        }
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
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.gift_container, commonFragment);
        } else {
            // 如果commonFragment不为空，则直接将它显示出来
            fragmentTransaction.show(commonFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }


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
            transaction.hide(luckFragment);
        }
    }

    //判断软键盘是否显示
    public boolean isSoftInputShow() {
        return act.getWindow().peekDecorView() == null;
    }

    /**
     * 弹出礼物面板
     */
    public void showGiftPan() {
        giftPanRl.clearAnimation();
        llGiftNumber.setVisibility(View.INVISIBLE);
        giftPanRl.setVisibility(View.VISIBLE);
        //判断隐藏软键盘是否弹出
      /*  if (act.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            //隐藏软键盘
            act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }*/
        if (animationShow == null) {
            animationShow = AnimationUtils.loadAnimation(act,
                    R.anim.gift_selector_show);
        }
        animationShow.setDuration(200);
        giftPanRl.startAnimation(animationShow);
        sendNumber.setText(currentGiftNumber + "");
        isOpenGiftPan = !isOpenGiftPan;
    }

    private boolean isShowEdit = false;

    /**
     * 收回礼物面板
     */
    public void hideGiftPan() {
        giftPanRl.clearAnimation();
        if (isOpenGiftPan) {
            if (animationHide == null) {
                animationHide = AnimationUtils.loadAnimation(act,
                        R.anim.gift_selector_hide);
            }
            animationHide.setDuration(200);
            giftPanRl.startAnimation(animationHide);
            animationHide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftPanRl.setVisibility(View.GONE);
//                    title.setVisibility(View.VISIBLE);
                    if (isShowEdit) {
                        rlEditGiftNumber.setVisibility(View.VISIBLE);
                        etNumber.setFocusable(true);
                        etNumber.setFocusableInTouchMode(true);
                        etNumber.requestFocus();
                        SoftInputUtils.showInput(act, etNumber);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            isOpenGiftPan = !isOpenGiftPan;
        }
    }

    //送礼成功
    public void sendGiftSuccess(Object modelD) {
        String[] strss = ((String) modelD).split("##");
        if (strss[1].equals("true")) {
            /*for (int i = 0; i < bagList.size(); i++) {
                for (int j = 0; j < bagList.get(i).size(); j++) {
                    if (bagList.get(i).get(j).getId().equals(currentGift.getId())) {
                        bagList.get(i).get(j).setNum(strss[0]);
                        break;
                    }
                }
            }*/
        } else {
//                        roomInfo.setCoin((String) modelD + "星币");
            balanceInfo.setText(strss[0] + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_choose_number_undefined:
                isShowEdit = true;
                hideGiftPan();
                break;
            case R.id.popup_choose_number_1314:
                llGiftNumber.setVisibility(View.GONE);
                sendNumber.setText("1314");
                break;
            case R.id.popup_choose_number_520:
                llGiftNumber.setVisibility(View.GONE);
                sendNumber.setText("520");
                break;
            case R.id.popup_choose_number_188:
                llGiftNumber.setVisibility(View.GONE);
                sendNumber.setText("188");
                break;
            case R.id.popup_choose_number_66:
                llGiftNumber.setVisibility(View.GONE);
                sendNumber.setText("66");
                break;
            case R.id.popup_choose_number_10:
                llGiftNumber.setVisibility(View.GONE);
                sendNumber.setText("10");
                break;
            case R.id.ll_giftPan:
                if (llGiftNumber.getVisibility() == View.VISIBLE) {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.gift_pan_recharge:  //充值
                if (llGiftNumber.getVisibility() == View.VISIBLE) {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
                Intent rechargeIntent = new Intent(act, RechargeAct.class);
                rechargeIntent.putExtra(RechargeAct.EXTRA_USER_COIN_VALUE, Long.parseLong(balanceInfo.getText().toString().trim()));
                act.startActivity(rechargeIntent);
                break;
            case R.id.tv_gift_send_number:  //选择礼物数量
                if (giftSendNumFlag) {
                    etNumber.setText("");
                    llGiftNumber.setVisibility(View.VISIBLE);
                } else {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
                giftSendNumFlag = !giftSendNumFlag;
                break;
            case R.id.send_gift_btn:  //赠送礼物
                if (llGiftNumber.getVisibility() == View.VISIBLE) {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
//                rlBottom.setVisibility(View.VISIBLE);
                if (Integer.parseInt(sendNumber.getText().toString()) <= 0) {
                    act.alert("送礼数量最小为1");
                    return;
                }
                sendGift(Integer.parseInt(sendNumber.getText().toString()));
                break;
            case R.id.edit_gift_number_blank:
            case R.id.btn_edit_gift_number:
                SoftInputUtils.hideInput(act, etNumber);
                rlEditGiftNumber.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(etNumber.getText().toString())) {
                    if (Long.parseLong(etNumber.getText().toString()) > 99999) {
                        CommonUtil.tip(act, "最多可赠送99999个", Gravity.BOTTOM);
                        etNumber.setText("1");
                    } else {
                        currentGiftNumber = Integer.parseInt(etNumber.getText().toString());
                    }
                }
                showGiftPan();

                break;
            case R.id.gift_btn_bar_common:
                if (llGiftNumber.getVisibility() == View.VISIBLE) {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
                commonLine.setVisibility(View.VISIBLE);
//                luckLine.setVisibility(View.INVISIBLE);
                exclusiveLine.setVisibility(View.INVISIBLE);
                advancedLine.setVisibility(View.INVISIBLE);
                bagLine.setVisibility(View.INVISIBLE);
                giftStart();
                if (commonFragment == null) {
                    commonFragment = GiftFragment.newInstance(commonGiftList);
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.gift_container, commonFragment);
                currentGift = null;
                resetAdvanced();
                resetBag();
                resetExclusive();
                resetLuck();
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case R.id.gift_btn_bar_advanced:
                if (llGiftNumber.getVisibility() == View.VISIBLE) {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
                commonLine.setVisibility(View.INVISIBLE);
//                luckLine.setVisibility(View.INVISIBLE);
                exclusiveLine.setVisibility(View.INVISIBLE);
                advancedLine.setVisibility(View.VISIBLE);
                bagLine.setVisibility(View.INVISIBLE);
                giftStart();
                if (advancedFragment == null) {
                    advancedFragment = GiftFragment.newInstance(advancedGiftList);
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.gift_container, advancedFragment);
                fragmentTransaction.commitAllowingStateLoss();
                currentGift = null;
                resetBag();
                resetCommon();
                resetExclusive();
                resetLuck();
                break;
            case R.id.gift_btn_bar_exclusive:
                if (llGiftNumber.getVisibility() == View.VISIBLE) {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
                commonLine.setVisibility(View.INVISIBLE);
//                luckLine.setVisibility(View.INVISIBLE);
                exclusiveLine.setVisibility(View.VISIBLE);
                advancedLine.setVisibility(View.INVISIBLE);
                bagLine.setVisibility(View.INVISIBLE);
                giftStart();
                if (exclusiveFragment == null) {
                    exclusiveFragment = GiftFragment.newInstance(exclusiveGiftList);
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.gift_container, exclusiveFragment);
                fragmentTransaction.commitAllowingStateLoss();
                currentGift = null;
                resetAdvanced();
                resetBag();
                resetCommon();
                resetLuck();
                break;
            case R.id.gift_btn_bar_bag:
                if (llGiftNumber.getVisibility() == View.VISIBLE) {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
                commonLine.setVisibility(View.INVISIBLE);
//                luckLine.setVisibility(View.INVISIBLE);
                exclusiveLine.setVisibility(View.INVISIBLE);
                advancedLine.setVisibility(View.INVISIBLE);
                bagLine.setVisibility(View.VISIBLE);
                giftStart();
                if (bagFragment == null) {
                    bagFragment = GiftFragment.newInstance(bagList);
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.gift_container, bagFragment);
                fragmentTransaction.commitAllowingStateLoss();
                currentGift = null;
                resetAdvanced();
                resetCommon();
                resetExclusive();
                resetLuck();
                break;
            case R.id.gift_btn_bar_luck:
                if (llGiftNumber.getVisibility() == View.VISIBLE) {
                    llGiftNumber.setVisibility(View.INVISIBLE);
                }
                commonLine.setVisibility(View.INVISIBLE);
//                luckLine.setVisibility(View.VISIBLE);
                exclusiveLine.setVisibility(View.INVISIBLE);
                advancedLine.setVisibility(View.INVISIBLE);
                bagLine.setVisibility(View.INVISIBLE);
                giftStart();
                if (luckFragment == null) {
                    luckFragment = GiftFragment.newInstance(luckList);
                }
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.gift_container, luckFragment);
                fragmentTransaction.commitAllowingStateLoss();
                currentGift = null;
                resetAdvanced();
                resetBag();
                resetCommon();
                resetExclusive();

                break;
            case R.id.gift_pan_blank:
                isShowEdit = false;
//                rlBottom.setVisibility(View.VISIBLE);
                if (giftPanRl.getVisibility() == View.VISIBLE) {
                    giftPanRl.setVisibility(View.GONE);
                    hideGiftPan();
                }
                callback.showRoomBottomButton();
                break;

        }
    }

    private String currentTagId;//当前收礼对象id
    private GiftTagSelectorItem item;

    /**
     * 赠送礼物
     */
    private void sendGift(int currentGiftNum) {
       /* if (currentTagId == null) {
            currentTagId = item.getId();
        }*/
        controller.sendGift(act, roomInfo.getAnchor(), currentGift, currentGiftNum);
    }

    private OnGiftPanCallback callback;

    public void setGiftPanCallback(OnGiftPanCallback callback) {
        this.callback = callback;
    }

    public interface OnGiftPanCallback {
        public void showRoomBottomButton();
    }

}
