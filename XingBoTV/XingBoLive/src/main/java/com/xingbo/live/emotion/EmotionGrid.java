package com.xingbo.live.emotion;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.xingbobase.util.XingBoUtil;

import java.util.List;

public class EmotionGrid extends GridView{
    private final static String TAG="EmotionGrid";
    public final static String DEL_EMOTION_ICON="del_emotion_icon";
    private SmileyAdapter<EmotionEntity> smileyAdapter;

    public EmotionGrid(Context context) {
        super(context);
        init();
    }

    public EmotionGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmotionGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmotionGrid(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setNumColumns(7);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (height != 0 && height == measuredHeight) {
            return;
        }
        setMeasuredDimension(getMeasuredWidth(), height);
    }



    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    public void bindData(List<EmotionEntity> emotionEntities) {
        if (smileyAdapter == null) {
            smileyAdapter = new SmileyAdapter<EmotionEntity>(getContext(), null);
        }
        smileyAdapter.setItems(emotionEntities);
        setAdapter(smileyAdapter);
    }

    public static class SmileyAdapter<Model extends EmotionEntity> extends BaseAdapter {

        private Context context;
        protected List<Model> itemList;

        public SmileyAdapter(Context context, List<Model> itemList) {
            this.context = context;
            this.itemList = itemList;

        }

        public List<Model> getItems() {
            return itemList;
        }

        public void setItems(List<Model> contacts) {
            this.itemList = contacts;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (itemList == null) {
                return 0;
            }
            return itemList.size()+1;
        }

        @Override
        public Model getItem(int position) {
            if (itemList == null || position > itemList.size() - 1) {
                return null;
            }
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EmotionView smileyView = null;
            if (convertView != null) {
                smileyView = (EmotionView) convertView;
            } else {
                smileyView = createView();
            }
            Model item = getItem(position);
            if(item!=null) {
                Log.e(TAG,"smileyView:"+(smileyView==null));
                smileyView.bindData(item);
            }else{
                XingBoUtil.log(TAG, "添加表情分页删除按钮");
                smileyView.bindData(EmotionEntity.fromAssert(DEL_EMOTION_ICON, "emotions/icon_del.png"));
            }
            return smileyView;
        }

        /**
         * 子类需要实现创建View的方法
         * @return
         */
        protected EmotionView createView() {
            return new EmotionView(context);
        }
    }

}
