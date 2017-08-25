
package com.xingbo.live.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.SystemApp;
import com.xingbo.live.emotion.EmotionEditText;
import com.xingbo.live.emotion.EmotionManager;
import com.xingbo.live.emotion.EmotionPager;
import com.xingbo.live.emotion.Emotions;
import com.xingbo.live.eventbus.ChatListScrollEvent;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.DpOrSp2PxUtil;
import com.xingbo.live.util.SoftInputUtils;
import com.xingbobase.util.XingBoUtil;
import com.xingbobase.view.widget.CirclePageIndicator;

import de.greenrobot.event.EventBus;

/**
 * Created by WuJinZhou on 2016/1/25.
 */

public class InputBarWithBoard extends RelativeLayout implements View.OnClickListener,
        EmotionEditText.OnBackPressedListener,
        TextWatcher,
        CompoundButton.OnCheckedChangeListener {
    private final static String TAG = "InputLayout";
    private final static String INPUT_BAR_WITH_KEYBOARD_CONFIG = "input_bar_with_keyboard_config";
    private final static String FINAL_KEYBOARD_HEIGHT = "final_keyboard_height";//键盘最终高度缓存索引键
    private final static String FINAL_KEYBOARD_INPUT_BAR_Y_IS_CACHE = "FINAL_KEYBOARD_INPUT_BAR_Y_IS_CACHE";//弹出键盘时，输入栏的y坐标信息是否缓存
    private final static String FINAL_KEYBOARD_INPUT_BAR_Y = "final_keyboard_input_bar_y";//弹出键盘时，输入栏的y坐标
    private final static int DISTANCE_SLOP = 180;
    SharedPreferences sp;
    private CheckBox privateChatSwitchBtn;//弹幕与公聊切换按钮
    public EmotionEditText mInput;//主输入框
    private ImageView KESwitchBtn;//输入法面板切换按钮(文字/表情) key emotion switch button
    private Button sendBtn;//发送按钮
    // 表情容器
    public RelativeLayout emotionContainer;
    private EmotionPager emotionView;
    private CirclePageIndicator indicator;

    private int mScreenHeight;
    private Rect tmp = new Rect();
    public int inputBarY;
    private boolean isCacheRealInputBarY = false;
    private boolean mPendingShowEmotionBoard;//是否要显示表面板mPendingShowPlaceHolder

    public boolean isDanmu = false;//是否弹幕
    public String receiveMsgUid;
    public String receiveMsgNick;

    private int currentIcon = 0;//默认表情图标
    private InputBarWithBoardListener listener;

    //字符限定设置
    private boolean isSuperAdmin = false;//当前发言者为超级管理员
    private boolean isAdmin = false;//当前发言者为巡管
    private boolean isAnchor = false;//房主
    private int mRichLv = 0;
    private int mGuardLv = 0;
    private int mVipLv = 0;
    private InputFilter filter = new InputFilter.LengthFilter(150);//最长为30个输入元素，每一个字符或表情为一个输入元素，表情(实际为5个字符)
    private InputFilter[] inputFilters = {filter};
    private boolean isOpenSoft = false;
    private InputMethodManager imm;

    private Context context;

    public InputBarWithBoard(Context context) {
        super(context);
        init(context);
    }

    public InputBarWithBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputBarWithBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public InputBarWithBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mScreenHeight = (int) SystemApp.getInstance().screenHeight;
        sp = context.getSharedPreferences(INPUT_BAR_WITH_KEYBOARD_CONFIG, Context.MODE_PRIVATE);
        final int defaultInputBarY = XingBoUtil.dip2px(context, 53);
        isCacheRealInputBarY = sp.getBoolean(FINAL_KEYBOARD_INPUT_BAR_Y_IS_CACHE, false);
        inputBarY = sp.getInt(FINAL_KEYBOARD_INPUT_BAR_Y, defaultInputBarY);
        LayoutInflater.from(context).inflate(R.layout.input_bar_with_board, this, true);
        privateChatSwitchBtn = (CheckBox) findViewById(R.id.danmu_chat_switch_btn);
        privateChatSwitchBtn.setOnCheckedChangeListener(this);
        mInput = (EmotionEditText) findViewById(R.id.input_editText);
        mInput.setOnBackPressedListener(this);
        mInput.addTextChangedListener(this);
       /* mInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (emotionContainer.getVisibility() == VISIBLE) {
                    emotionContainer.setVisibility(GONE);
//                    showSoftInput(mInput);
                }
                return false;
            }
        });*/
        mInput.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //是否缓存Y的位置
                if (isKeyboardVisible()) {
                    if (!isCacheRealInputBarY) {
                        inputBarY = getDistanceFromInputToBottom() - defaultInputBarY;
                        isCacheRealInputBarY = true;
                        sp.edit().putInt(FINAL_KEYBOARD_INPUT_BAR_Y, inputBarY).putBoolean(FINAL_KEYBOARD_INPUT_BAR_Y_IS_CACHE, isCacheRealInputBarY).apply();
                    }
                }
                if (isEmotionBoardVisible()) {
                    if (currentIcon == 0) {
                        KESwitchBtn.setImageResource(R.mipmap.keyboard_icon);
                        currentIcon = 1;
                    }
                } else {
                    if (currentIcon == 1) {
                        KESwitchBtn.setImageResource(R.mipmap.emotion);
                        currentIcon = 0;
                    }
                }
//         Keyboard -> emotionContainer
                if (mPendingShowEmotionBoard) {/*
                    // 在设置mPendingShowEmotionContainer时已经调用了隐藏Keyboard的方法，直到Keyboard隐藏前都取消重给
                    if (isKeyboardVisible()) {
                        ViewGroup.LayoutParams params = emotionContainer.getLayoutParams();
                        int distance = getDistanceFromInputToBottom();
                        // 调整emotionContainer高度
                        if (distance > DISTANCE_SLOP && distance != params.height) {
                            params.height = distance;
                            emotionContainer.setLayoutParams(params);
                            sp.edit().putInt(FINAL_KEYBOARD_HEIGHT, distance).apply();
                        }
                        return false;
                    } else { // 键盘已隐藏，显示emotionContainer
                        showEmotionBoard();
                        mPendingShowEmotionBoard = false;
                        return false;
                    }
                } else {
                    if (isEmotionBoardVisible() && isKeyboardVisible()) {
                        hideEmotionBoard();
                        return false;
                    }
                    if (!isKeyboardVisible() && !isEmotionBoardVisible()) {//
                        //隐藏
//                        if ( privateChatSwitchBtn.getVisibility() == VISIBLE) {
//                            privateChatSwitchBtn.setVisibility(INVISIBLE);
//                            if (privateChatSwitchBtn.isChecked()) {
//                                privateChatSwitchBtn.setChecked(false);
//                                mInput.setHint("和大家聊聊吧");
//                            }
//                        }
                    }*/
                }
                return true;
            }
        });
        KESwitchBtn = (ImageView) findViewById(R.id.key_emotion_switch_btn);//mAdditionBtn
        KESwitchBtn.setOnClickListener(this);
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(this);
        emotionContainer = (RelativeLayout) findViewById(R.id.emotion_pager_container);//mPlaceHolder
       /* int defaultHeight = XingBoUtil.dip2px(context, 240);//表情区域默认高度240dp
        int keyboardHeight = sp.getInt(FINAL_KEYBOARD_HEIGHT, defaultHeight);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) emotionContainer.getLayoutParams();
        if (params != null) {
            params.height = mScreenHeight / 2;
            emotionContainer.setLayoutParams(params);
        }*/
        emotionView = (EmotionPager) findViewById(R.id.emotion_pager);
        emotionView.bindData(Emotions.DATA);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(emotionView);
        //获取焦点
//        showSoftInput(mInput);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.key_emotion_switch_btn:
                if (emotionContainer.getVisibility() == View.GONE) {
                    hideSoftInput(mInput);
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.popup_in);
                                        animation.setDuration(500);
                                        emotionContainer.startAnimation(animation);
                                        emotionContainer.setVisibility(View.VISIBLE);
                                        listener.onEmotionShow(DpOrSp2PxUtil.dp2pxConvertInt(context, 170));
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else {
                    emotionContainer.setVisibility(View.GONE);
                    showSoftInput(mInput);
                    listener.onEmotionShow(DpOrSp2PxUtil.dp2pxConvertInt(context, 10));
                }
                // 除非软键盘和emotionBoard全隐藏时直接显示emotionBoard，其他情况此处处理软键盘，onPreDrawListener处理emotionBoard
               /* if (isEmotionBoardVisible()) { // emotionBoard -> Keyboard
                    showSoftInput(mInput);
                    KESwitchBtn.setImageResource(R.mipmap.emotion_icon);

                } else if (isKeyboardVisible()) { // Keyboard -> emotionBoard
                    mPendingShowEmotionBoard = true;
                    hideSoftInput(mInput);
                    KESwitchBtn.setImageResource(R.mipmap.keyboard_icon);
                } else { // Just show emotionBoard
                    showEmotionBoard();
                    KESwitchBtn.setImageResource(R.mipmap.keyboard_icon);
                }*/
                break;
            case R.id.send_btn:
                if (listener != null && !TextUtils.isEmpty(mInput.getText())) {
                    listener.onSend(mInput.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    public void hideEmotionContainer() {
        if (emotionContainer.getVisibility() != GONE) {
            emotionContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isDanmu = isChecked;
        if (listener != null) {
            listener.onDanmuStateChange(isChecked);
        }
        if (isDanmu) {
            mInput.setHint("开启弹幕100星币/条");
            KESwitchBtn.setVisibility(GONE);
        } else {
            mInput.setHint("和主播说点什么");
            KESwitchBtn.setVisibility(VISIBLE);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isEmotionBoardVisible()) {
            hideEmotionBoard();
            return true;
        } else if (isKeyboardVisible()) {
            hideSoftInput(mInput);
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 0) {
            sendBtn.setSelected(false);
            return;
        }

        if (!sendBtn.isSelected()) {
            sendBtn.setSelected(true);
        }

        if (s.toString().startsWith(" ")) {
            Toast.makeText(context, "首字符不可为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isAdmin && !isSuperAdmin && !isAnchor) {//发言者不是超管，不是巡管,也不是主播
            int CURRENT_MAX_LENGTH = 30;//发言上限
            if (isDanmu) {//弹幕
                CURRENT_MAX_LENGTH = 30;
            } else {//公聊
                if (mRichLv < 10) {//非会员财富等级小于10
                    CURRENT_MAX_LENGTH = 30;
                } else if (mRichLv > 9 && mRichLv < 19) {//非会员财富等级星爵1--星皇3
                    CURRENT_MAX_LENGTH = 50;
                } else if (mRichLv > 19) {//非会员财富等级星灵级以上
                    CURRENT_MAX_LENGTH = 70;
                }
            }
            mInput.setMaxLength(CURRENT_MAX_LENGTH);
            mInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(CURRENT_MAX_LENGTH)});
            int num = CURRENT_MAX_LENGTH - EmotionManager.checkEmotionCount(s.toString());
            if(num==0){
                Toast.makeText(context, "您最多可输入" + CURRENT_MAX_LENGTH + "个文字", Toast.LENGTH_SHORT).show();
            }
//            } else {
//                if (mInput.getMaxLength() != -1) {
//                    mInput.setFilters(inputFilters);
//                  //  CommonUtil.tip(getContext(),"您目前只能发送"+CURRENT_MAX_LENGTH+"个字",Gravity.CENTER);
//                    mInput.setMaxLength(-1);
//                }
//            }
        }
    }

    private void showEmotionBoard() {
        if (emotionContainer.getVisibility() != View.VISIBLE) {
            emotionContainer.setVisibility(View.VISIBLE);
            KESwitchBtn.setImageResource(R.mipmap.keyboard_icon);
        }
    }

    private void hideEmotionBoard() {
        if (emotionContainer.getVisibility() != View.GONE) {
            emotionContainer.setVisibility(View.GONE);
        }
    }

    public void showSoftInput(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
        KESwitchBtn.setImageResource(R.mipmap.emotion);
    }

    public void hideSoftInput(View view) {
        view.clearFocus();
        view.setFocusable(false);
        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isKeyboardVisible() {

        return (getDistanceFromInputToBottom() > DISTANCE_SLOP && !isEmotionBoardVisible())
                || (getDistanceFromInputToBottom() > (emotionContainer.getHeight() + DISTANCE_SLOP) && isEmotionBoardVisible());
    }

    private boolean isEmotionBoardVisible() {
        // 忽略INVISIBLE状态
        return emotionContainer.getVisibility() == View.VISIBLE;
    }


    /**
     * 输入框的下边距离屏幕的距离
     */

    private int getDistanceFromInputToBottom() {
        return mScreenHeight - getInputBottom();
    }


    /**
     * 输入框下边的位置
     */

    private int getInputBottom() {
        mInput.getGlobalVisibleRect(tmp);
        return tmp.bottom;
    }

    public void reset() {
        mInput.setText("");
        if (isEmotionBoardVisible()) {
            hideEmotionBoard();
        }
        if (isKeyboardVisible()) {
            hideSoftInput(mInput);
            //直接隐藏输入键盘  显示按钮
        }
        if (privateChatSwitchBtn.isChecked()) {
            privateChatSwitchBtn.setChecked(true);
        }
//        EventBus.getDefault().post(new ChatListScrollEvent());
    }

    public void updatePrivate(boolean isPrivateChat, String uid, String nick) {
        this.isDanmu = isPrivateChat;
        this.receiveMsgUid = uid;
        this.receiveMsgNick = nick;
        privateChatSwitchBtn.setChecked(isPrivateChat);
    }

    public void setInputBarWithBoardListener(InputBarWithBoardListener listener) {
        this.listener = listener;
    }


    /**
     * 更新当前用户财富等级
     */
    public void updateRichLv(int richLv) {
        mRichLv = richLv;
    }


    /**
     * 更新当前用户守护等级
     */

    public void updateGuardLv(int guardLv) {
        mGuardLv = guardLv;
    }


    /**
     * 更新当前用户会员等级
     */

    public void updateVipLv(int guardLv) {
        mGuardLv = guardLv;
    }

    /**
     * 输入面板监听
     */

    public interface InputBarWithBoardListener {
        void onSend(String msg);

        void onDanmuStateChange(boolean isChecked);

        void onEmotionShow(int marginBottom);
    }
}
