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
package com.github.pidygb.slidinglayout.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.pidygb.slidinglayout.widget.SlideListener;
import com.github.pidygb.slidinglayout.widget.SlidingLayout;
import com.github.pidygb.slidinglayout.widget.SlidingLinearLayout;

public class MainActivity extends Activity implements OnClickListener,
        SlideListener {

    private SlidingLinearLayout buttonSlidingLinearLayout;
    private SlidingLayout textViewsSlidingLinearLayout;
    private Button openTextViews;

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout root = (LinearLayout) findViewById(R.id.root);

        openTextViews = (Button) findViewById(R.id.button_open_textviews);



        textViewsSlidingLinearLayout = (SlidingLayout) findViewById(R.id.slideTextviews);
        textViewsSlidingLinearLayout.setSlideListener(this);

        openTextViews.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
