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
import android.widget.Toast;

import com.gb.android.widget.Button;
import com.gb.android.widget.SlidingLinearLayout;
import com.gb.android.widget.SlidingLinearLayout.SlideListener;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener,
	SlideListener, BannerComponent.OnPromotionalItemClickListener {
    
    private SlidingLinearLayout buttonSlidingLinearLayout;
    private Button openTextViews;
    private Button openButtons;
    private SlidingLinearLayout textViewsSlidingLinearLayout;

    @SuppressLint({ "NewApi", "ResourceAsColor" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	LinearLayout root = (LinearLayout) findViewById(R.id.root);

	ArrayList<BannerItem> list = new ArrayList<BannerItem>();
	for (int i = 0; i < 5; i++) {
	    BannerItem item = new BannerItem();
	    item.setAction("action");
	    item.setPromotionText("text: " + i);
	    list.add(item);
	}

	BannerComponent c = new BannerComponent(this, list, this);
	c.setBannerTitle("title");
	c.setBannerSubtitle("subtitle");
	c.setBannerAction("action");
	c.setBackgroundColor(android.R.color.holo_red_dark);
	root.addView(c);

	View v = getLayoutInflater().inflate(R.layout.textviews, root, false);
	root.addView(v);

	openTextViews = (Button) v.findViewById(R.id.button_open_textviews);

	textViewsSlidingLinearLayout = (SlidingLinearLayout) v
		.findViewById(R.id.slideTextviews);
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

    @Override
    public void OnItemClickListener(BannerItem item) {
	Toast.makeText(this, "Clicked: " + item.getPromotionText(),
		Toast.LENGTH_SHORT).show();
    }
}
