package com.xingbo.live.view;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.msg.ScrollGiftMsg;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.AnimatorUtils;
import com.xingbo.live.util.DrawableUtils;
import com.xingbo.live.util.TextShowUtil;
import com.xingbo.live.util.TimeUtils;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by xingbo_szd on 2016/9/5.
 */
public class ScrollGift extends LinearLayout {
    private ArrayBlockingQueue<ScrollGiftMsg> scrollGiftMsgs = new ArrayBlockingQueue<ScrollGiftMsg>(500);
    private Context context;
    private Animator animatorX;
    private LinearLayout.LayoutParams params;
    private View view;
    private LinearLayout background;
    private TextView text;

    public ScrollGift(Context context) {
        super(context);
        init(context);
    }

    public ScrollGift(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        view = View.inflate(context, R.layout.scroll_gift, null);
        background = (LinearLayout) view.findViewById(R.id.scroll_gift_bg);
        text = (TextView) view.findViewById(R.id.scroll_gift_text);
        this.addView(view);
        thread.start();
    }

    public void add(ScrollGiftMsg scrollGiftMsg) {
        scrollGiftMsgs.add(scrollGiftMsg);
    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    ScrollGiftMsg scrollGiftMsg = scrollGiftMsgs.take();
                    Message message = Message.obtain();
                    message.what = XingBoConfig.SHOW_SCROLL_GIFT;
                    message.obj = scrollGiftMsg;
                    handler.sendMessage(message);
                    Thread.sleep(15000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == XingBoConfig.SHOW_SCROLL_GIFT) {
                ScrollGiftMsg scrollGiftMsg = (ScrollGiftMsg) msg.obj;
                showScrollGift(scrollGiftMsg);
            } else if (msg.what == XingBoConfig.CANCEL_SCROLL_GIFT) {
                if ((System.currentTimeMillis() - (long) background.getTag()) > 15000) {
                    background.setVisibility(INVISIBLE);
                }
            }
        }
    };

    public void showScrollGift(final ScrollGiftMsg scrollGiftMsg) {
        if (background.getVisibility() != VISIBLE) {
            background.setVisibility(VISIBLE);
        }
        background.setTag(System.currentTimeMillis());
        String string = TimeUtils.getHourAndMins(scrollGiftMsg.getData().getSendtime()) + "  " + scrollGiftMsg.getData().getFuser().getNick() + "  送给  " +
                scrollGiftMsg.getData().getTuser().getNick() + "  " + scrollGiftMsg.getData().getNum() + "个" + scrollGiftMsg.getData().getGift().getName()+"image";
        final int textViewWidth = (int) text.getPaint().measureText(string);
        params = (LinearLayout.LayoutParams) text.getLayoutParams();
        params.width = textViewWidth;
        text.setLayoutParams(params);
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                return DrawableUtils.getBitMBitmap(params[0]);
            }

            protected void onPostExecute(Bitmap result) {
                text.setText(TextShowUtil.showScrollGift(context, TimeUtils.getHourAndMins(scrollGiftMsg.getData().getSendtime()) + "  ", scrollGiftMsg.getData().getFuser().getNick(),
                        scrollGiftMsg.getData().getTuser().getNick() + "  ", scrollGiftMsg.getData().getNum() + "个" + scrollGiftMsg.getData().getGift().getName(), new BitmapDrawable(context.getResources(),result)));
                AnimatorUtils.showScrollGift(text, textViewWidth);
                handler.sendEmptyMessageDelayed(XingBoConfig.CANCEL_SCROLL_GIFT, 15000);
            }
        }.execute(HttpConfig.FILE_SERVER + scrollGiftMsg.getData().getGift().getIcon());

    }
}
