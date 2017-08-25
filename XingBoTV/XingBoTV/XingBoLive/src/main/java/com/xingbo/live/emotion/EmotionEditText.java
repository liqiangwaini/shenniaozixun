package com.xingbo.live.emotion;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;


import com.xingbo.live.util.CommonUtil;


public class EmotionEditText extends EditText implements TextWatcher,EmotionInputEventBus.EmotionInputEventListener {
    private OnBackPressedListener mListener;

    private String mDealingText;
    private int maxLength=-1;
    public EmotionEditText(Context context) {
        super(context);
    }

    public EmotionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmotionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(21)
    public EmotionEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addTextChangedListener(this);
        EmotionInputEventBus.instance.setEmotionInputEventListener(this);
    }

    /**
     * 有文字输入
     */
    @Override
    public void onEmotionInput(EmotionEntity emotionEntity) {
        //表情删除按钮事件
        if(emotionEntity.getCode()!=null&&emotionEntity.getCode().equals(EmotionGrid.DEL_EMOTION_ICON)){
           int action = KeyEvent.ACTION_DOWN;
           int code = KeyEvent.KEYCODE_DEL;
           KeyEvent event = new KeyEvent(action, code);
           onKeyDown(KeyEvent.KEYCODE_DEL, event);
           return;
        }
        CommonUtil.log("", "输入前个数：" + getText().toString().length());
        if(maxLength!=-1) {
            if (getText().toString().length() + 5 > maxLength) {
                return;
            }
        }
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        mDealingText = emotionEntity.getCode();
        EmotionManager.getImageSpan(emotionEntity);
        Editable text = getText();
        ImageSpan imageSpan = EmotionManager.getImageSpan(emotionEntity);
        if (selectionStart != selectionEnd) {
            text.replace(selectionStart, selectionEnd, emotionEntity.getCode());
            text.setSpan(imageSpan, selectionStart, selectionStart + emotionEntity.getCode().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            text.insert(selectionStart, emotionEntity.getCode());
            text.setSpan(imageSpan, selectionStart, selectionStart + emotionEntity.getCode().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mDealingText = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count >1 && !TextUtils.equals(s.subSequence(start, start + count), mDealingText)) {
            EmotionManager.parseCharSequence((SpannableStringBuilder) s, start, start + count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mListener != null) {
                if (mListener.onBackPressed()) {
                    return true;
                }
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
