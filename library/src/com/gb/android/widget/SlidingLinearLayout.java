/*
 * Copyright (C) 2014 Giuseppe Buzzanca
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gb.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;

/**
 * A {@link LinearLayout} version with sliding animation implementation
 * 
 */
public class SlidingLinearLayout extends LinearLayout {

    private int mExpandedValue;
    private int mSlideOrientation;
    private int mDuration;
    private int mVisibility;
    private OnSlideListener mOnSlideListener = null;
    private android.view.ViewGroup.LayoutParams mLayoutParams;
    private android.view.ViewGroup.LayoutParams mOrigParams;

    private AnimatorUpdateListener mAnimatorUpdateListener = new AnimatorUpdateListener() {

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
	    int mValue = (Integer) animation.getAnimatedValue();
	    if (mSlideOrientation == VERTICAL) {
		mLayoutParams.height = mValue;
	    } else if (mSlideOrientation == HORIZONTAL_LEFT) {
		mLayoutParams.width = mValue;

	    } else if (mSlideOrientation == HORIZONTAL_RIGHT) {
		mLayoutParams.width = mValue;
		ViewHelper.setX(SlidingLinearLayout.this,
			Float.valueOf(mExpandedValue - mValue));
	    }

	    setLayoutParams(mLayoutParams);
	}
    };

    /**
     * The slide is animated in horizontal left direction
     * 
     * Constant Value: 0 (0x00000000)
     */
    public static final int HORIZONTAL_LEFT = 0;
    /**
     * The slide is animated in horizontal right direction
     * 
     * Constant Value: 1 (0x00000001)
     */
    public static final int HORIZONTAL_RIGHT = 1;
    /**
     * The slide is animated in vertical direction
     * 
     * Constant Value: 2 (0x00000002)
     */
    public static final int VERTICAL = 2;

    public SlidingLinearLayout(Context context, AttributeSet attrs) {
	super(context, attrs);
	mVisibility = getVisibility();
	if (mVisibility == View.GONE) {
	    setVisibility(View.VISIBLE);
	}
	parseAttrs(context, attrs);
	observeViewAndSetHeight();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SlidingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	mVisibility = getVisibility();
	if (mVisibility == View.GONE) {
	    setVisibility(View.VISIBLE);
	}
	parseAttrs(context, attrs);
	observeViewAndSetHeight();
    }

    /**
     * Returns the slide orientation for this view
     * 
     * @return One of {@link #VERTICAL}, {@link #HORIZONTAL_LEFT},
     *         {@link #HORIZONTAL_RIGHT}.
     */
    public int getSlideOrientation() {
	return mSlideOrientation;
    }

    /**
     * Set the slide orientation for this view
     * 
     * @param orientation
     *            One of {@link #VERTICAL}, {@link #HORIZONTAL_LEFT},
     *            {@link #HORIZONTAL_RIGHT}.
     */
    public void setSlideOrientation(int orientation) {
	mSlideOrientation = orientation;
	invalidate();
	requestLayout();
    }

    /**
     * How long this animation should last
     * 
     * @return the duration in milliseconds of the animation
     */
    public int getDuration() {
	return mDuration;
    }

    /**
     * How long this animation should last. The duration cannot be negative.
     * 
     * @param duration
     *            Duration in milliseconds
     */
    public void setDuration(int duration) {
	mDuration = duration;
	invalidate();
	requestLayout();
    }

    /**
     * Binds an animation listener to this animation. The animation listener is
     * notified of animation events such as the start of the animation or the
     * end of the animation.
     * 
     * @param slideListener
     *            the animation listener to be notified
     */
    public void setOnSlideListener(OnSlideListener listener) {
	mOnSlideListener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
	super.onAttachedToWindow();
	mOrigParams = getLayoutParams();
    }

    /**
     * Display or hide this view with a sliding motion.
     */
    public void slide() {
	// Prepare ValueAnimator
	ValueAnimator mAnimator = null;
	int mTmpStartVisibility = 0;
	int mTmpEndVisibility = 0;
	switch (getVisibility()) {
	case VISIBLE:
	    mAnimator = ValueAnimator.ofInt(mExpandedValue, 0);
	    mTmpStartVisibility = VISIBLE;
	    mTmpEndVisibility = GONE;
	    break;
	case INVISIBLE:
	    mAnimator = ValueAnimator.ofInt(mExpandedValue, mExpandedValue);
	    mTmpStartVisibility = INVISIBLE;
	    mTmpEndVisibility = VISIBLE;
	    break;
	case GONE:
	    mAnimator = ValueAnimator.ofInt(0, mExpandedValue);
	    mTmpStartVisibility = VISIBLE;
	    mTmpEndVisibility = VISIBLE;
	    break;
	}

	// Finish to setup animation and start it
	mLayoutParams = getLayoutParams();
	final int mStartVisibility = mTmpStartVisibility;
	final int mEndVisibility = mTmpEndVisibility;
	mAnimator.addListener(new AnimatorListenerAdapter() {

	    @Override
	    public void onAnimationStart(Animator animation) {
		setVisibility(mStartVisibility);
		if (mOnSlideListener != null)
		    mOnSlideListener.onSlideStart(SlidingLinearLayout.this);
	    }

	    @Override
	    public void onAnimationEnd(Animator animation) {

		setLayoutParams(mOrigParams);

		setVisibility(mEndVisibility);
		requestLayout();
		if (mOnSlideListener != null)
		    mOnSlideListener.onSlideEnd(SlidingLinearLayout.this);
	    }
	});
	mAnimator.addUpdateListener(mAnimatorUpdateListener);
	mAnimator.setDuration(mDuration);
	mAnimator.start();
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
	mExpandedValue = 0;

	TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
		R.styleable.SlidingLinearLayout, 0, 0);
	try {
	    mSlideOrientation = a.getInteger(
		    R.styleable.SlidingLinearLayout_slideOrientation, VERTICAL);
	    mDuration = a.getInteger(R.styleable.SlidingLinearLayout_duration,
		    300);
	} finally {
	    a.recycle();
	}
    }

    private void observeViewAndSetHeight() {
	ViewTreeObserver observer = this.getViewTreeObserver();
	observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

	    @SuppressWarnings("deprecation")
	    @Override
	    public void onGlobalLayout() {
		if (mSlideOrientation == VERTICAL)
		    mExpandedValue = getMeasuredHeight();
		else
		    mExpandedValue = getMeasuredWidth();
		SlidingLinearLayout.this.getViewTreeObserver()
			.removeGlobalOnLayoutListener(this);
		setVisibility(mVisibility);
	    }
	});
    }

}
