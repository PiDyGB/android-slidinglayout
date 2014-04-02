package com.gb.android.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;

;

/**
 * @author 'Giuseppe Buzzanca <giuseppebuzzanca@gmail.com>'
 * 
 */
public class SlideHelper {

    public interface SlideListener {

        public void onSlideStart(View view);

        public void onSlideEnd(View view);

    }

    public static void slide(View v, SlideListener listener) {
        if (v.isShown())
            collapse(v, listener);
        else
            expand(v, listener);
    }

    public static void collapse(final View v, final SlideListener listener) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                    Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight
                            - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        
        a.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (listener != null)
                    listener.onSlideStart(v);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null)
                    listener.onSlideEnd(v);
            }
        });

        // 1dp per milliseconds
        a.setDuration((int) (initialHeight / v.getContext().getResources()
                .getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expand(final View v, final SlideListener listener) {
        Log.d("Slider", "expand");

        v.measure(v.getLayoutParams().width, v.getLayoutParams().height);
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);

        final int measuredHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime,
                    Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
                        : (int) (measuredHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (listener != null)
                    listener.onSlideStart(v);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null)
                    listener.onSlideEnd(v);
            }
        });

        // 1dp per milliseconds
        a.setDuration((int) (measuredHeight / v.getContext().getResources()
                .getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
