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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * A {@link LinearLayout} version with sliding animation implementation
 */
public class SlidingLinearLayout extends LinearLayout {

    public interface SlideListener {

        /**
         * Notifies the start of the slide animation.
         * 
         * @param slidingLinearLayout
         *            An instance of {@link SlidingLinearLayout}
         */
        public void onSlideStart(SlidingLinearLayout slidingLinearLayout);

        /**
         * Notifies the end of the slide animation.
         * 
         * @param slidingLinearLayout
         *            An instance of {@link SlidingLinearLayout}
         */
        public void onSlideEnd(SlidingLinearLayout slidingLinearLayout);

    }

    private int mExpandedValue;
    private int mSlideOrientation;
    private int mDuration;
    private boolean mExpanded;
    private boolean isSliding;
    private SlideListener mOnSlideListener = null;
    private int layoutHeight;
    private int layoutWidth;

    /**
     * The slide is animated in horizontal left direction
     * <p/>
     * Constant Value: 0 (0x00000000)
     */
    public static final int HORIZONTAL = 0;
    /**
     * The slide is animated in vertical direction
     * <p/>
     * Constant Value: 2 (0x00000002)
     */
    public static final int VERTICAL = 2;

    public SlidingLinearLayout(Context context) {
        super(context);
        isSliding = false;
    }

    public SlidingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        isSliding = false;
        parseAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SlidingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        isSliding = false;
        parseAttrs(context, attrs);
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
     * @param listener
     *            the animation listener to be notified
     */
    public void setSlideListener(SlideListener listener) {
        mOnSlideListener = listener;
    }

    /**
     * Set the view in expanded mode or not.
     * 
     * @param expanded
     *            a {@link boolean}
     */
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
        if (mExpanded)
            setVisibility(VISIBLE);
        invalidate();
        requestLayout();
    }

    /**
     * Return if it's expanded
     * 
     * @return a {@link boolean}
     */
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        layoutHeight = getLayoutParams().height;
        layoutWidth = getLayoutParams().width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isSliding) {
            super.onLayout(changed, l, t, r, b);
            int tot = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v.getVisibility() != GONE) {
                    LayoutParams lp = (LayoutParams) v.getLayoutParams();
                    if (mSlideOrientation == VERTICAL) {
                        tot = tot + v.getHeight() + lp.bottomMargin
                                + lp.topMargin;
                        lp.height = v.getHeight();
                    } else {
                        tot = tot + v.getWidth() + lp.leftMargin
                                + lp.rightMargin;
                        lp.width = v.getWidth();
                    }
                }
            }

            if (mSlideOrientation == VERTICAL)
                mExpandedValue = tot + getPaddingBottom() + getPaddingTop();
            else
                mExpandedValue = tot + getPaddingLeft() + getPaddingRight();
            if (!mExpanded)
                setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isSliding)
            if (!mExpanded) {
                if (mSlideOrientation == VERTICAL)
                    setMeasuredDimension(getMeasuredWidth(), 0);
                else
                    setMeasuredDimension(0, getMeasuredHeight());
            }
    }

    /**
     * Display or hide this view with a sliding motion.
     */
    public void slide() {
        if (mExpanded)
            collapse();
        else
            expand();
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        mExpandedValue = 0;

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingLinearLayout, 0, 0);
        try {
            mSlideOrientation = a
                    .getInteger(
                            R.styleable.SlidingLinearLayout_slide_orientation,
                            VERTICAL);
            mDuration = a.getInteger(R.styleable.SlidingLinearLayout_duration,
                    300);
            mExpanded = a.getBoolean(R.styleable.SlidingLinearLayout_expanded,
                    false);
        } finally {
            a.recycle();
        }
        
        setSlideOrientation(mSlideOrientation);
        setDuration(mDuration);
        setExpanded(mExpanded);
    }

    public void collapse() {
        if (isSliding)
            return;

        isSliding = true;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                    Transformation t) {
                if (interpolatedTime == 1) {
                    SlidingLinearLayout.this.setVisibility(GONE);
                } else {
                    if (mSlideOrientation == VERTICAL)
                        SlidingLinearLayout.this.getLayoutParams().height = mExpandedValue
                                - (int) (mExpandedValue * interpolatedTime);
                    else
                        SlidingLinearLayout.this.getLayoutParams().width = mExpandedValue
                                - (int) (mExpandedValue * interpolatedTime);

                    SlidingLinearLayout.this.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp per milliseconds
        a.setDuration(mDuration);
        a.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideStart(SlidingLinearLayout.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isSliding = false;
                mExpanded = (mExpanded == true) ? false : true;
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideEnd(SlidingLinearLayout.this);
            }
        });
        this.startAnimation(a);
    }

    public void expand() {
        if (isSliding)
            return;

        isSliding = true;
        this.getLayoutParams().height = 0;
        this.setVisibility(View.VISIBLE);

        Log.d("Sliding", "h: " + mExpandedValue + ":" + layoutHeight);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                    Transformation t) {
                if (mSlideOrientation == VERTICAL) {
                    SlidingLinearLayout.this.getLayoutParams().height = interpolatedTime == 1 ? layoutHeight
                            : (int) (mExpandedValue * interpolatedTime);
                } else {
                    SlidingLinearLayout.this.getLayoutParams().width = interpolatedTime == 1 ? layoutWidth
                            : (int) (mExpandedValue * interpolatedTime);
                }
                SlidingLinearLayout.this.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp per milliseconds
        a.setDuration(mDuration);
        a.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideStart(SlidingLinearLayout.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isSliding = false;
                mExpanded = (mExpanded == true) ? false : true;
                if (mOnSlideListener != null)
                    mOnSlideListener.onSlideEnd(SlidingLinearLayout.this);
            }
        });
        this.startAnimation(a);
    }
}
