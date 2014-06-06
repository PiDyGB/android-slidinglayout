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
package com.github.pidygb.android.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.github.pidygb.android.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * A {@link LinearLayout} version with sliding animation implementation
 */
public class SlidingLinearLayout extends LinearLayout {

    private int mDuration;
    private boolean mExpanded;
    private android.view.ViewGroup.LayoutParams mLayoutParams;
    private SlideListener mOnSlideListener;
    private boolean isObserved = true;
    private int mExpandedHeight;
    private boolean isAnimated;
    private boolean deliveryExpandOnGlobalLayoutFinished;
    private boolean deliveryCollapseOnGlobalLayoutFinished;

    public SlidingLinearLayout(Context context) {
        super(context);
    }

    public SlidingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SlidingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttrs(context, attrs);
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
     * @param duration Duration in milliseconds
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * Binds an animation listener to this animation. The animation listener is
     * notified of animation events such as the start of the animation or the
     * end of the animation.
     *
     * @param listener the animation listener to be notified
     */
    public void setSlideListener(SlideListener listener) {
        mOnSlideListener = listener;
    }

    /**
     * Return if it's expanded
     *
     * @return a {@link boolean}
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    /**
     * Set the view in expanded mode or not.
     *
     * @param expanded a {@link boolean}
     */
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
        isObserved = true;
        invalidate();
        requestLayout();
    }

    /**
     * Return the expanded measured height
     *
     * @return a {@link int}
     */
    public int getExpandedHeight() {
        return mExpandedHeight;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mLayoutParams = getLayoutParams();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isObserved && !mExpanded)
            setMeasuredDimension(widthMeasureSpec, 0);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        isObserved = true;
        setupObserver();
        super.addView(child, index, params);
    }

    /**
     * Display or hide this view with a sliding animation.
     */
    public void slide() {
        if (mExpandedHeight == 0 || isAnimated)
            return;
        if (mExpanded)
            collapse();
        else
            expand();
    }

    private void setupObserver() {

        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {

                    @SuppressLint("NewApi")
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        mExpandedHeight = getChildrenHeight()
                                + getPaddingBottom() + getPaddingTop();
                        if (!mExpanded)
                            setVisibility(GONE);

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                            getViewTreeObserver().removeGlobalOnLayoutListener(
                                    this);
                        else
                            getViewTreeObserver().removeOnGlobalLayoutListener(
                                    this);
                        isObserved = false;
                        if (deliveryExpandOnGlobalLayoutFinished) {
                            expand();
                            deliveryExpandOnGlobalLayoutFinished = false;
                        }
                        if (deliveryCollapseOnGlobalLayoutFinished) {
                            collapse();
                            deliveryCollapseOnGlobalLayoutFinished = false;
                        }

                    }
                }
        );
    }

    private int getChildrenHeight() {
        int t = 0;
        for (int i = 0; i < getChildCount(); i++) {
            t += getChildAt(i).getHeight()
                    + ((LayoutParams) getChildAt(i).getLayoutParams()).bottomMargin
                    + ((LayoutParams) getChildAt(i).getLayoutParams()).topMargin;
        }
        return t;
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingLinearLayout, 0, 0);
        try {
            mDuration = a.getInteger(R.styleable.SlidingLinearLayout_duration,
                    300);
            mExpanded = a.getBoolean(R.styleable.SlidingLinearLayout_expanded,
                    false);
        } finally {
            a.recycle();
        }
    }

    /**
     * Collapse the view with a sliding animation if it's not already collapsed
     */
    public void collapse() {
        if (isObserved && !deliveryExpandOnGlobalLayoutFinished) {
            deliveryCollapseOnGlobalLayoutFinished = true;
            return;
        }
        if (mExpandedHeight == 0 || isAnimated)
            return;
        isAnimated = true;
        mExpanded = !mExpanded;

        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(Math.max(mExpandedHeight, getMeasuredHeight()), 0);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                android.view.ViewGroup.LayoutParams lp = getLayoutParams();
                lp.height = val;
                setLayoutParams(lp);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideStart(SlidingLinearLayout.this);
                mLayoutParams.height = -2;
                setLayoutParams(mLayoutParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideEnd(SlidingLinearLayout.this);
                isAnimated = false;
            }
        });
        valueAnimator.setDuration(mDuration);
        valueAnimator.start();
    }

    /**
     * Expand the view with a sliding animation if it's not already expanded
     */
    public void expand() {
        if (isObserved && !deliveryCollapseOnGlobalLayoutFinished) {
            deliveryExpandOnGlobalLayoutFinished = true;
            return;
        }
        if (mExpandedHeight == 0 || isAnimated)
            return;
        isAnimated = true;
        mExpanded = !mExpanded;

        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, Math.max(mExpandedHeight, getMeasuredHeight()));
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                android.view.ViewGroup.LayoutParams lp = getLayoutParams();
                lp.height = val;
                requestLayout();
                // setLayoutParams(lp);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideStart(SlidingLinearLayout.this);
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLayoutParams.height = -2;
                setLayoutParams(mLayoutParams);
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideEnd(SlidingLinearLayout.this);
                isAnimated = false;
            }
        });
        valueAnimator.setDuration(mDuration);
        valueAnimator.start();
    }
}
