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
package com.gb.android.view;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

;

/**
 * @author 'Giuseppe Buzzanca <giuseppebuzzanca@gmail.com>'
 */
public class SlideHelper {

    private static boolean isAnimated = false;

    public interface SlideListener {

	public void onSlideStart(View view);

	public void onSlideEnd(View view);

    }

    public static void slide(View view, int expandedHeight, int duration,
	    SlideListener listener) {
	if (view.isShown())
	    collapse(view, expandedHeight, duration, listener);
	else
	    expand(view, expandedHeight, duration, listener);
    }

    public static void collapse(final View view, final int expandedHeight,
	    int duration, final SlideListener listener) {
	if (expandedHeight == 0 || isAnimated)
	    return;
	isAnimated = true;
	final LayoutParams lp = view.getLayoutParams();

	ValueAnimator valueAnimator = ValueAnimator.ofInt(expandedHeight, 0);
	valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

	    @Override
	    public void onAnimationUpdate(ValueAnimator valueAnimator) {
		int val = (Integer) valueAnimator.getAnimatedValue();
		LayoutParams lp = view.getLayoutParams();
		lp.height = val;
		view.setLayoutParams(lp);
	    }
	});

	valueAnimator.addListener(new AnimatorListenerAdapter() {
	    @Override
	    public void onAnimationStart(Animator animation) {
		if (listener != null)
		    listener.onSlideStart(view);
		view.setLayoutParams(lp);
	    }

	    @Override
	    public void onAnimationEnd(Animator animation) {
		view.setVisibility(View.GONE);
		if (listener != null)
		    listener.onSlideEnd(view);
		isAnimated = false;
	    }
	});
	valueAnimator.setDuration(duration);
	valueAnimator.start();
    }

    public static void expand(final View view, final int expandedHeight, int duration,
	    final SlideListener listener) {
	if (expandedHeight == 0 || isAnimated)
	    return;
	isAnimated = true;
	final LayoutParams lp = view.getLayoutParams();

	ValueAnimator valueAnimator = ValueAnimator.ofInt(0, expandedHeight);
	valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

	    @Override
	    public void onAnimationUpdate(ValueAnimator valueAnimator) {
		int val = (Integer) valueAnimator.getAnimatedValue();
		LayoutParams lp = view.getLayoutParams();
		lp.height = val;
		view.setLayoutParams(lp);
	    }
	});

	valueAnimator.addListener(new AnimatorListenerAdapter() {
	    @Override
	    public void onAnimationStart(Animator animation) {
		if (listener != null)
		    listener.onSlideStart(view);
		view.setVisibility(View.VISIBLE);
	    }

	    @Override
	    public void onAnimationEnd(Animator animation) {
		view.setLayoutParams(lp);
		if (listener != null)
		    listener.onSlideEnd(view);
		isAnimated = false;
	    }
	});
	valueAnimator.setDuration(duration);
	valueAnimator.start();
    }

}
