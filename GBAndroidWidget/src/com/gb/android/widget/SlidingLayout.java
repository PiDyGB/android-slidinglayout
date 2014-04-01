/**
 * 
 */
package com.gb.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * @author pidy
 * 
 */
public class SlidingLayout extends FrameLayout {

    private static final String TAG = SlidingLayout.class.getName();
    private boolean mExpand = false;

    /**
     * @param context
     */
    public SlidingLayout(Context context) {
	super(context);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     */
    public SlidingLayout(Context context, AttributeSet attrs) {
	super(context, attrs);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	// TODO Auto-generated constructor stub
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
	child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST);
	Log.d(TAG, "child: " + getMeasuredHeight() + ":" + getHeight() + ":"
		+ child.getMeasuredHeight());
	super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	Log.d(TAG, "onMeasure");
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// if (mExpand) {
	// mExpand = false;
	// setMeasuredDimension(getLayoutParams().width, 0);
	// Log.d(TAG, "onMeasure: " + getMeasuredHeight() + ":" + getHeight()
	// + ":" + getChildAt(0).getMeasuredHeight());
	// internalExpand(getChildAt(0).getMeasuredHeight());
	// }
    }

    private void internalExpand(final int measuredHeight) {
	Animation a = new Animation() {
	    @Override
	    protected void applyTransformation(float interpolatedTime,
		    Transformation t) {
		Log.d(TAG, "apply: "
			+ ((interpolatedTime == 1) ? getLayoutParams().height
				: (int) (measuredHeight * interpolatedTime)));
		getLayoutParams().height = (interpolatedTime == 1) ? android.view.ViewGroup.LayoutParams.WRAP_CONTENT
			: (int) (measuredHeight * interpolatedTime);
		requestLayout();
	    }

	    @Override
	    public boolean willChangeBounds() {
		return true;
	    }
	};

	a.setDuration(3000);
	startAnimation(a);
    }

    public void slide() {
	if (getVisibility() == VISIBLE)
	    collapse();
	else
	    expand();
    }

    public void expand() {
	mExpand = true;
	setVisibility(VISIBLE);
	Log.d(TAG, "expanded");
    }

    public void collapse() {
	final int initialHeight = getMeasuredHeight();
	Log.d(TAG, "collapse: " + initialHeight);

	Animation a = new Animation() {
	    @Override
	    protected void applyTransformation(float interpolatedTime,
		    Transformation t) {
		if (interpolatedTime == 1) {
		    setVisibility(View.GONE);
		} else {
		    getLayoutParams().height = initialHeight
			    - (int) (initialHeight * interpolatedTime);
		    requestLayout();
		}
	    }

	    @Override
	    public boolean willChangeBounds() {
		return true;
	    }
	};

	a.setDuration(3000);
	startAnimation(a);
    }
}
