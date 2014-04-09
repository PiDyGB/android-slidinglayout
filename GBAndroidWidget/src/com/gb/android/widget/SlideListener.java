package com.gb.android.widget;

import android.view.View;

public interface SlideListener {

    /**
     * Notifies the start of the slide animation.
     * 
     * @param The
     *            sliding view
     */
    public void onSlideStart(View slidingView);

    /**
     * Notifies the end of the slide animation.
     * 
     * @param The
     *            sliding view
     */
    public void onSlideEnd(View slidingView);

}
