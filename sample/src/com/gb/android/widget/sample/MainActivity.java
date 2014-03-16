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
import android.widget.Button;

import com.gb.android.widget.OnSlideListener;
import com.gb.android.widget.SlidingLinearLayout;

public class MainActivity extends Activity implements OnClickListener,
	OnSlideListener {

    private SlidingLinearLayout slidingLinearLayout;
    private Button button;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	button = (Button) findViewById(R.id.button_open);

	slidingLinearLayout = (SlidingLinearLayout) findViewById(R.id.slide);
	slidingLinearLayout.setOnSlideListener(this);

	button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
	slidingLinearLayout.slide();

    }

    @Override
    public void onSlideStart(SlidingLinearLayout slidingLinearLayout) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onSlideEnd(SlidingLinearLayout slidingLinearLayout) {
	if (slidingLinearLayout.isShown())
	    button.setText("CLOSE");
	else
	    button.setText("OPEN");
    }

}
