package com.xingbobase.extra.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import com.xingbobase.R;
import com.xingbobase.extra.pulltorefresh.internal.EmptyViewMethodAccessor;
import com.xingbobase.extra.sticky.StickyGridHeadersGridView;

/**
 * Created by WuJinZhou on 2015/12/17.
 */
public class PullToRefreshStickyHeaderGridView extends PullToRefreshAdapterViewBase<StickyGridHeadersGridView> {

    public PullToRefreshStickyHeaderGridView(Context context) {
        super(context);
    }

    public PullToRefreshStickyHeaderGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshStickyHeaderGridView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshStickyHeaderGridView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected final StickyGridHeadersGridView createRefreshableView(Context context, AttributeSet attrs) {
        final StickyGridHeadersGridView gv;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            gv = new InternalStickyGridHeadersGridViewSDK9(context, attrs);
        } else {
            gv = new InternalStickyGridHeadersGridView(context, attrs);
        }

        // Use Generated ID (from res/values/ids.xml)
        gv.setId(R.id.gridview);
        return gv;
    }

    @Override
    protected void onPtrRestoreInstanceState(Bundle savedInstanceState) {
        try {
            super.onPtrRestoreInstanceState(savedInstanceState);
        }catch (Exception e){
        }
        savedInstanceState=null;
    }

    class InternalStickyGridHeadersGridView extends StickyGridHeadersGridView implements EmptyViewMethodAccessor {

        public InternalStickyGridHeadersGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshStickyHeaderGridView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }
    }

    @TargetApi(9)
    final class InternalStickyGridHeadersGridViewSDK9 extends InternalStickyGridHeadersGridView {

        public InternalStickyGridHeadersGridViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshStickyHeaderGridView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

            return returnValue;
        }
    }
}

