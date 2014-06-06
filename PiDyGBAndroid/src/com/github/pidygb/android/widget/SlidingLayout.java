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
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

import com.github.pidygb.android.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * @author pidy
 */
public class SlidingLayout extends FrameLayout {

    protected int mExpandedHeight;
    protected boolean isObserved = true;
    private int mDuration;
    private boolean mExpanded;
    private android.view.ViewGroup.LayoutParams mLayoutParams;
    private SlideListener mOnSlideListener;
    private boolean isAnimated;
    private boolean deliveryExpandOnGlobalLayoutFinished;
    private boolean deliveryCollapseOnGlobalLayoutFinished;

    public SlidingLayout(Context context) {
        super(context);
        parseAttrs(context, null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
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
        this.mDuration = duration;
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
        this.mExpanded = expanded;
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
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "SlideLayout can host only one direct child");
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "SlideLayout can host only one direct child");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "SlideLayout can host only one direct child");
        }
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "SlideLayout can host only one direct child");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException(
                    "SlideLayout can host only one direct child");
        }
        isObserved = true;
        setupObserver();
        super.addView(child, index, params);
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
     * Display or hide this view with a sliding animation.
     */
    public void slide() {
        if (mExpandedHeight == 0 || isAnimated)
            return;
        if (mExpanded)
            collapse();
        else {
            expand();
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
        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(
                Math.max(mExpandedHeight, getMeasuredHeight()), 0);
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
                    mOnSlideListener.onSlideStart(SlidingLayout.this);
                mLayoutParams.height = -2;
                setLayoutParams(mLayoutParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideEnd(SlidingLayout.this);
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
        measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,
                Math.max(mExpandedHeight, getMeasuredHeight()));
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
                    mOnSlideListener.onSlideStart(SlidingLayout.this);
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLayoutParams.height = -2;
                setLayoutParams(mLayoutParams);
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideEnd(SlidingLayout.this);
                isAnimated = false;
            }
        });
        valueAnimator.setDuration(mDuration);
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isObserved && !mExpanded)
            setMeasuredDimension(widthMeasureSpec, 0);
    }

    private void setupObserver() {

        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {

                    @SuppressLint("NewApi")
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        mExpandedHeight = getChildAt(0).getHeight()
                                + getPaddingBottom()
                                + getPaddingTop()
                                + ((LayoutParams) getChildAt(0)
                                .getLayoutParams()).bottomMargin
                                + ((LayoutParams) getChildAt(0)
                                .getLayoutParams()).topMargin;
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

    private void parseAttrs(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingLayout, 0, 0);
        try {
            mDuration = a.getInteger(R.styleable.SlidingLayout_duration, 300);
            mExpanded = a.getBoolean(R.styleable.SlidingLayout_expanded, false);
        } finally {
            a.recycle();
        }
    }
}
