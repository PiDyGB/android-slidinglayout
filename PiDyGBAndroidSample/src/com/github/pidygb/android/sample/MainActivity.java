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
package com.github.pidygb.android.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.github.pidygb.android.widget.Button;
import com.github.pidygb.android.widget.SlideListener;
import com.github.pidygb.android.widget.SlidingLayout;
import com.github.pidygb.android.widget.SlidingLinearLayout;

public class MainActivity extends Activity implements OnClickListener,
        SlideListener {

    private SlidingLinearLayout buttonSlidingLinearLayout;
    private Button openTextViews;
    private Button openButtons;
    private SlidingLayout textViewsSlidingLinearLayout;
    private View child;

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout root = (LinearLayout) findViewById(R.id.root);

        openTextViews = (Button) findViewById(R.id.button_open_textviews);


        child = getLayoutInflater().inflate(R.layout.textviews,
                textViewsSlidingLinearLayout, false);


        textViewsSlidingLinearLayout = (SlidingLayout) findViewById(R.id.slideTextviews);
        textViewsSlidingLinearLayout.addView(child);
        textViewsSlidingLinearLayout.setExpanded(false);
        textViewsSlidingLinearLayout.setSlideListener(this);

        openTextViews.setOnClickListener(this);

        View buttons = getLayoutInflater().inflate(R.layout.buttons, root,
                false);
        root.addView(buttons);
        openButtons = (Button) buttons.findViewById(R.id.button_open_buttons);

        buttonSlidingLinearLayout = (SlidingLinearLayout) buttons
                .findViewById(R.id.slideButtons);
        buttonSlidingLinearLayout.setSlideListener(this);

        openButtons.setOnClickListener(this);

        View et = getLayoutInflater().inflate(R.layout.editexts, root, false);
        root.addView(et);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_open_buttons:

                buttonSlidingLinearLayout.slide();
                break;

            case R.id.button_open_textviews:

                textViewsSlidingLinearLayout.slide();
                break;
        }
    }

    @Override
    public void onSlideStart(View slidingView) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSlideEnd(View slidingView) {
        Button b = null;
        switch (slidingView.getId()) {
            case R.id.slideButtons:
                b = openButtons;
                if (((SlidingLinearLayout) slidingView).isExpanded())
                    b.setText("CLOSE");
                else
                    b.setText("OPEN");
                break;
            case R.id.slideTextviews:
                b = openTextViews;
                if (((SlidingLayout) slidingView).isExpanded())
                    b.setText("CLOSE");
                else
                    b.setText("OPEN");
                break;
        }
    }
}
