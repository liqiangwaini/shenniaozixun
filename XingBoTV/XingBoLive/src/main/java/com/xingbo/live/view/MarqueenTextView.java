package com.xingbo.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xingbo_szd on 2016/8/19.
 */
public class MarqueenTextView extends TextView {

    public MarqueenTextView(Context context) {
        super(context);
    }

    public MarqueenTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueenTextView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
