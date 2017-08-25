/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xingbobase.extra.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.xingbobase.R;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase.Mode;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase.Orientation;

public class RotateLoadingLayout extends LoadingLayout {

	static final int ROTATION_ANIMATION_DURATION = 1200;

	private final Animation mRotateAnimation;

	private final boolean mRotateDrawableWhilePulling;

	public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);
        mode.showFooterLoadingLayout();
		mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);
		mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}


    /**
     * @see com.xingbobase.extra.pulltorefresh.internal.LoadingLayout;
     * onPull(float scaleOfLayout)
     */
	protected void onPullImpl(float scaleOfLayout) {
        //滑动过程中的动画控制
		float angle;
		if (mRotateDrawableWhilePulling) {
			angle = scaleOfLayout * 90f;
		} else {
			angle = Math.max(800f, Math.min(180f, scaleOfLayout * 360f - 180f));
		}
        mHeaderImage.setRotation(angle);
	}

	@Override
	protected void refreshingImpl() {
		mHeaderImage.startAnimation(mRotateAnimation);
	}

	@Override
	protected void resetImpl() {
		mHeaderImage.clearAnimation();
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_rotate;
	}

}
