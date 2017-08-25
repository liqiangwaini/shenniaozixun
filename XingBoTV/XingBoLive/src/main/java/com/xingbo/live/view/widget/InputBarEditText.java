package com.xingbo.live.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by WuJinZhou on 2016/3/15.
 */
public class InputBarEditText extends EditText implements TextWatcher{
    private int maxLength=-1;
    private CharSequence currentText;
    public InputBarEditText(Context context) {
        super(context);
    }

    public InputBarEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputBarEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public InputBarEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        currentText=getText();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
