/**
 *
 */
package com.gb.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * @author pidy
 */
public class SlidingLayout extends FrameLayout {

    private int mDuration;
    private boolean mExpanded;
    protected int mExpandedHeight;
    private android.view.ViewGroup.LayoutParams mLayoutParams;
    private SlideListener mOnSlideListener;
    protected boolean isObserved = true;
    private boolean isAnimated;

    /**
     * @param context
     */
    public SlidingLayout(Context context) {
	super(context);
	parseAttrs(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public SlidingLayout(Context context, AttributeSet attrs) {
	super(context, attrs);
	parseAttrs(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	parseAttrs(context, attrs);
    }

    /**
     * @return the duration
     */
    public int getDuration() {
	return mDuration;
    }

    /**
     * @param duration
     *            the duration to set
     */
    public void setDuration(int duration) {
	this.mDuration = duration;
    }

    /**
     * @return the expanded
     */
    public boolean isExpanded() {
	return mExpanded;
    }

    /**
     * @param expanded
     *            the expanded to set
     */
    public void setExpanded(boolean expanded) {
	this.mExpanded = expanded;
	isObserved = true;
	invalidate();
	requestLayout();
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
	setupObserver();
	super.addView(child, index, params);
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

    public void slide() {
	if (mExpandedHeight == 0 || isAnimated)
	    return;
	if (mExpanded)
	    collapse();
	else {
	    expand();
	}
    }

    public void collapse() {
	if (mExpandedHeight == 0 || isAnimated)
	    return;
	isAnimated = true;
	mExpanded = !mExpanded;

	ValueAnimator valueAnimator = ValueAnimator.ofInt(mExpandedHeight, 0);
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

    public void expand() {
	if (mExpandedHeight == 0 || isAnimated)
	    return;
	isAnimated = true;
	mExpanded = !mExpanded;

	ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mExpandedHeight);
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
		    }
		});
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
