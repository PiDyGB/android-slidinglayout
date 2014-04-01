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
package com.gb.android.widget.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.gb.android.widget.Button;
import com.gb.android.widget.SlidingHelper;
import com.gb.android.widget.SlidingLinearLayout;
import com.gb.android.widget.SlidingLinearLayout.SlideListener;

public class MainActivity extends Activity implements OnClickListener,
        SlideListener {

    private SlidingLinearLayout buttonSlidingLinearLayout;
    private Button openTextViews;
    private Button openButtons;
    private LinearLayout textViewsSlidingLinearLayout;

    @SuppressLint({ "NewApi", "ResourceAsColor" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout root = (LinearLayout) findViewById(R.id.root);

        View v = getLayoutInflater().inflate(R.layout.textviews, root, false);
        root.addView(v);

        openTextViews = (Button) v.findViewById(R.id.button_open_textviews);

        textViewsSlidingLinearLayout = (LinearLayout) v
                .findViewById(R.id.slideTextviews);

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
            if (!textViewsSlidingLinearLayout.isShown())
                SlidingHelper.expand(textViewsSlidingLinearLayout, 300);
            else
                SlidingHelper.collapse(textViewsSlidingLinearLayout, 300);
            // textViewsSlidingLinearLayout.slide();
            break;
        }
    }

    @Override
    public void onSlideStart(SlidingLinearLayout slidingLinearLayout) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSlideEnd(SlidingLinearLayout slidingLinearLayout) {
        Button b = null;
        switch (slidingLinearLayout.getId()) {
        case R.id.slideButtons:
            b = openButtons;
            break;
        case R.id.slideTextviews:
            b = openTextViews;
            break;
        }
        if (slidingLinearLayout.isExpanded())
            b.setText("CLOSE");
        else
            b.setText("OPEN");
    }
}