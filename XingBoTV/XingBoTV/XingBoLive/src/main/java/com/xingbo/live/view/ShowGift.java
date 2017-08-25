package com.xingbo.live.view;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.GiftShow;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.TextShowUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;


public class ShowGift extends RelativeLayout {
    private RelativeLayout llSendGift1;
    private RelativeLayout llSendGift2;
    private TextView people1;
    private TextView people2;
    private TextView gift1;
    private TextView gift2;
    private TextView number1;
    private TextView number2;

    private Context context;
    private ArrayBlockingQueue<GiftShow> gifts = new ArrayBlockingQueue<GiftShow>(500);
    private FrescoImageView giftIcon1;
    private FrescoImageView giftIcon2;
    private FrescoImageView avatar1;
    private FrescoImageView avatar2;
    private GiftViewHolder giftViewHolder2;
    private GiftViewHolder giftViewHolder1;
    private TextView lianNum1;
    private TextView lianNum2;
    private TextView lianText1;
    private TextView lianText2;

    public ShowGift(Context context) {
        super(context);
        this.context = context;
        initSendGift();
    }

    public ShowGift(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initSendGift();
    }

    public void initSendGift() {
        View view = View.inflate(context, R.layout.send_gift, null);
        llSendGift1 = (RelativeLayout) view.findViewById(R.id.ll_send_gift1);
        llSendGift2 = (RelativeLayout) view.findViewById(R.id.ll_send_gift2);
        avatar1 = (FrescoImageView) view.findViewById(R.id.avatar1);
        avatar2 = (FrescoImageView) view.findViewById(R.id.avatar2);
        people1 = (TextView) view.findViewById(R.id.people1);
        people2 = (TextView) view.findViewById(R.id.people2);
        gift1 = (TextView) view.findViewById(R.id.gift1);
        gift2 = (TextView) view.findViewById(R.id.gift2);
        giftIcon1 = (FrescoImageView) view.findViewById(R.id.gift_icon1);
        giftIcon2 = (FrescoImageView) view.findViewById(R.id.gift_icon2);
        number1 = (TextView) view.findViewById(R.id.number1);
        number2 = (TextView) view.findViewById(R.id.number2);
        lianNum1 = (TextView) view.findViewById(R.id.lian_num1);
        lianNum2 = (TextView) view.findViewById(R.id.lian_num2);
        lianText1 = (TextView) view.findViewById(R.id.lian_text1);
        lianText2 = (TextView) view.findViewById(R.id.lian_text2);
        this.addView(view);

        giftViewHolder1 = new GiftViewHolder();
        giftViewHolder1.relativeLayout = llSendGift1;
        giftViewHolder1.avatar = avatar1;
        giftViewHolder1.nick = people1;
        giftViewHolder1.giftName = gift1;
        giftViewHolder1.giftIcon = giftIcon1;
        giftViewHolder1.showNum = number1;
        giftViewHolder1.lianNum = lianNum1;
        giftViewHolder1.lianText = lianText1;
        giftViewHolder2 = new GiftViewHolder();
        giftViewHolder2.relativeLayout = llSendGift2;
        giftViewHolder2.avatar = avatar2;
        giftViewHolder2.nick = people2;
        giftViewHolder2.giftName = gift2;
        giftViewHolder2.giftIcon = giftIcon2;
        giftViewHolder2.lianNum = lianNum2;
        giftViewHolder2.lianText = lianText2;
        giftViewHolder2.showNum = number2;
        thread.start();
    }

    private static final int GIFT_QUEUE = 10;
    private static final int CANCEL_GIFT_SHOW = 11;
    private static final int REFRESH_GIFT_DELAY = 12;

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                    GiftShow gift = gifts.take();
                    Log.e("ShowGift", gift.sendGift.getGift().getName() + "-" + gift.sendGift.getCombo());
                    Message message = Message.obtain();
                    message.what = GIFT_QUEUE;
                    message.obj = gift;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    //判断是否是连送
    public boolean isContinueSendGift(int number) {
        if (number == 10 || number == 66 | number == 188 || number == 520 || number == 1314) {
            return true;
        } else {
            return false;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GIFT_QUEUE:
                    GiftShow gift = (GiftShow) msg.obj;
                    GiftViewHolder viewHolder = null;
                    if ((giftViewHolder1.senderName.equals(gift.sendGift.getFuser().getNick())
                            && giftViewHolder1.giftType.equals(gift.sendGift.getGift().getName()) && ((isContinueSendGift(gift.sendGift.getNum())
                            && giftViewHolder1.giftNumOnce == gift.sendGift.getNum()) || !isContinueSendGift(gift.sendGift.getNum())))) {
                        viewHolder = giftViewHolder1;
                        Log.e("TAG1", gift.sendGift.getCombo() + "");
                        viewHolder.avatarUrl = gift.sendGift.getFuser().getAvatar();
                        viewHolder.senderName = gift.sendGift.getFuser().getNick();
                        viewHolder.giftType = gift.sendGift.getGift().getName();
                        viewHolder.giftIconUrl = gift.sendGift.getGift().getIcon();
                        viewHolder.giftNumOnce = gift.sendGift.getNum();
                        viewHolder.refresh(gift.sendGift.getCombo());
                    } else if ((giftViewHolder2.senderName.equals(gift.sendGift.getFuser().getNick())
                            && giftViewHolder2.giftType.equals(gift.sendGift.getGift().getName()) && ((isContinueSendGift(gift.sendGift.getNum()) &&
                            giftViewHolder2.giftNumOnce == gift.sendGift.getNum()) || !isContinueSendGift(gift.sendGift.getNum())))) {
                        viewHolder = giftViewHolder2;
                        Log.e("TAG2", gift.sendGift.getCombo() + "");
                        viewHolder.avatarUrl = gift.sendGift.getFuser().getAvatar();
                        viewHolder.senderName = gift.sendGift.getFuser().getNick();
                        viewHolder.giftType = gift.sendGift.getGift().getName();
                        viewHolder.giftIconUrl = gift.sendGift.getGift().getIcon();
                        viewHolder.giftNumOnce = gift.sendGift.getNum();
                        viewHolder.refresh(gift.sendGift.getCombo());
                    } else {
                        Log.e("TAG3", "");
                        viewHolder = giftViewHolder1;
                        if (giftViewHolder2.lastRefTime < giftViewHolder1.lastRefTime) {
                            viewHolder = giftViewHolder2;
                        }
                        if (giftViewHolder1.relativeLayout.getVisibility() == VISIBLE && giftViewHolder2.relativeLayout.getVisibility() == VISIBLE) {
                            Message message = Message.obtain();
                            message.what = REFRESH_GIFT_DELAY;
                            List<Object> lists = new ArrayList<>();
                            lists.add(viewHolder);
                            lists.add(gift);
                            message.obj = lists;
                            handler.sendMessageDelayed(message, 3000);
                        } else {
                            viewHolder.avatarUrl = gift.sendGift.getFuser().getAvatar();
                            viewHolder.senderName = gift.sendGift.getFuser().getNick();
                            viewHolder.giftType = gift.sendGift.getGift().getName();
                            viewHolder.giftIconUrl = gift.sendGift.getGift().getIcon();
                            viewHolder.giftNumOnce = gift.sendGift.getNum();
                            viewHolder.refresh(gift.sendGift.getCombo());
                        }
                    }
                    break;
                case REFRESH_GIFT_DELAY:
                    List<Object> lists = (List<Object>) msg.obj;
                    GiftViewHolder giftViewHolder = (GiftViewHolder) lists.get(0);
                    GiftShow showGift = (GiftShow) lists.get(1);
                    giftViewHolder.avatarUrl = showGift.sendGift.getFuser().getAvatar();
                    giftViewHolder.senderName = showGift.sendGift.getFuser().getNick();
                    giftViewHolder.giftType = showGift.sendGift.getGift().getName();
                    giftViewHolder.giftIconUrl = showGift.sendGift.getGift().getIcon();
                    giftViewHolder.giftNumOnce = showGift.sendGift.getNum();
                    giftViewHolder.refresh(showGift.sendGift.getCombo());
                    break;
                case CANCEL_GIFT_SHOW:
                    GiftViewHolder viewHolder1 = (GiftViewHolder) msg.obj;
                    long currentTime = System.currentTimeMillis();
                    // 4990是为了做容错
                    if (currentTime - viewHolder1.lastRefTime >= delaymillis) {
                        viewHolder1.hideView();
                    }
                    break;
            }
        }
    };

    public ScaleAnimation showAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1.8f, 1.0f, 1.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.7f);
        animation.setDuration(1000);
        animation.setInterpolator(new OvershootInterpolator());
        return animation;
    }

    /**
     * 送一个礼物
     *
     * @param gift
     */
    public void sendGift(GiftShow gift) {
        Log.e("ShowGift1", gift.sendGift.getGift().getName() + "-" + gift.sendGift.getCombo());
        gifts.add(gift);
    }


    private long delaymillis = 5000; // 延迟隐藏时间

    private class GiftViewHolder {
        public long lastRefTime; // 最后一次刷新时间
        public String avatarUrl = "";
        public String senderName = "";
        public String giftType = "";
        public String giftIconUrl = "";
        public int giftNumOnce;
        public RelativeLayout relativeLayout;
        public ImageView avatar;
        public TextView nick;
        public TextView giftName;
        public ImageView giftIcon;
        public TextView showNum;
        public TextView lianNum;
        public TextView lianText;

        /**
         * 刷新
         */
        public void refresh(int giftNum) {
            lastRefTime = System.currentTimeMillis();
            relativeLayout.setVisibility(View.VISIBLE);
            avatar.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + avatarUrl));
            nick.setText(senderName);
            if (giftIconUrl != null) {
                giftIcon.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + giftIconUrl));
            }
            if (giftNumOnce == 10 || giftNumOnce == 66 || giftNumOnce == 188 || giftNumOnce == 520 || giftNumOnce == 1314) {
                relativeLayout.setBackgroundResource(R.drawable.send_gift_continue_bg);
                showNum.setVisibility(GONE);
                giftName.setText("送" + giftType);
                lianNum.setVisibility(VISIBLE);
                lianText.setVisibility(VISIBLE);
                giftName.setTextColor(context.getResources().getColor(R.color.send_gift_lian_color));
                lianNum.setText("x" + giftNumOnce);
                lianText.setText(TextShowUtil.showContinueGiftNumber(context, "连送 x" + giftNum));
                lianText.setAnimation(showAnimation());
            } else {
                relativeLayout.setBackgroundResource(R.drawable.send_gift_normal_bg);
                giftName.setText("送" + giftType);
                lianNum.setVisibility(GONE);
                lianText.setVisibility(GONE);
                showNum.setVisibility(VISIBLE);
                giftName.setTextColor(context.getResources().getColor(R.color.send_gift_color));
                showNum.setText(TextShowUtil.showNormalGiftNumber(context, "x" + giftNum));
                showNum.setAnimation(showAnimation());
            }
            Message message = Message.obtain();
            message.what = CANCEL_GIFT_SHOW;
            message.obj = this;
            handler.sendMessageDelayed(message, delaymillis);
        }

        /**
         * 隐藏
         */
        public void hideView() {
            relativeLayout.setVisibility(View.INVISIBLE);
            showNum.setVisibility(INVISIBLE);
        }
    }

}
