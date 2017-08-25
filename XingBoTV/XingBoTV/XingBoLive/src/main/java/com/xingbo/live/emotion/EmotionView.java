package com.xingbo.live.emotion;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xingbo.live.emotion.EmotionManager;


/**
 * 显示单个表情view
 */
public class EmotionView extends ImageView implements View.OnClickListener {

    private EmotionEntity mItem;

    public EmotionView(Context context) {
        super(context);
        init();
    }

    public EmotionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmotionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmotionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_INSIDE);
        setOnClickListener(this);
    }

    /**
     * 计算每行的高度，绝定了每页可以显示多少行表情
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        View parent = (View) getParent();
        if (parent != null) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), parent.getHeight() == 0 ? 0 : parent.getHeight() / 4);
        } else {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), getMeasuredHeight());
        }
    }

    public void bindData(EmotionEntity item) {
        mItem = item;
        Log.e("EmotionManager1",(item)+"");
        Log.e("EmotionManager2",(item.getSource())+"");
        Log.e("EmotionManager3",(EmotionManager.getBitmap(item.getSource())==null)+"");
        setImageBitmap(EmotionManager.getBitmap(item.getSource()));
    }

    @Override
    public void onClick(View v) {
        EmotionInputEventBus.instance.postEmotion(mItem);
    }
}
