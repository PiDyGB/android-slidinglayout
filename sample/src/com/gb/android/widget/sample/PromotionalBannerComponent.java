/**
 * HM
 * PromotionalBannerComponent.java
 *
 * Copyright (c) 2014 Accenture. All rights reserved.
 *
 */
package com.gb.android.widget.sample;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gb.android.widget.SlidingLinearLayout;

public class PromotionalBannerComponent extends LinearLayout {

	private ArrayList<PromotionalBannerItem> mPromos;
	private Context mContext;
	private SlidingLinearLayout mPromotionalSlider;
	private LinearLayout mBanner;

	private TextView mBannertitle, mBannerSubtitle;
	private Button mBannerAction;
	private OnPromotionalItemClickListener mItemClickListener;
	
	private String mReceivedAction;
	private static final String CLOSE_ACTION = "CLOSE";

	public interface OnPromotionalItemClickListener {
		void OnItemClickListener(PromotionalBannerItem item);
	}

	public PromotionalBannerComponent(Context context) {
		super(context);
	}

	public PromotionalBannerComponent(Context context,
			ArrayList<PromotionalBannerItem> promos,
			OnPromotionalItemClickListener itemClickListener) {
		super(context);
		mContext = context;
		mPromos = promos;
		mItemClickListener = itemClickListener;
		prepareLayout();
	}

	private void prepareLayout() {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addView(inflater.inflate(R.layout.promotional_banner, this, false));

		mBanner = (LinearLayout) findViewById(R.id.banner);

		mPromotionalSlider = (SlidingLinearLayout) findViewById(R.id.promotionalSlider);

		mBannertitle = (TextView) findViewById(R.id.bannerTitle);
		mBannerSubtitle = (TextView) findViewById(R.id.bannerSubtitle);
		mBannerAction = (Button) findViewById(R.id.bannerAction);

		this.findViewById(R.id.banner).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mPromotionalSlider.getChildCount() > 0) {
							if (mPromotionalSlider.isShown()) {
								mBannerAction.setText(mReceivedAction);
							} else {
								mBannerAction.setText(CLOSE_ACTION);
							}
						}
						mPromotionalSlider.slide();

					}
				});

		for (int count = 0; count < mPromos.size(); count++) {

			LinearLayout v = (LinearLayout) inflater
					.inflate(R.layout.promotional_banner_item,
							mPromotionalSlider, false);

			FrameLayout itemContainer = (FrameLayout) v.getChildAt(0);

			((Button) itemContainer.getChildAt(0)).setText(mPromos.get(count)
					.getPromotionText());

			final PromotionalBannerItem bannerItem = mPromos.get(count);

			itemContainer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mItemClickListener.OnItemClickListener(bannerItem);
				}
			});

			mPromotionalSlider.addView(v);

		}

	}

	public void setBannerTitle(String title) {
		mBannertitle.setText(title);
	}

	public void setBannerSubtitle(String subtitle) {
		if (subtitle == null) {
			mBannerSubtitle.setVisibility(View.GONE);
		} else if (subtitle.equals("")) {
			mBannerSubtitle.setVisibility(View.GONE);
		} else {
			mBannerSubtitle.setVisibility(View.VISIBLE);
			mBannerSubtitle.setText(subtitle);
		}

	}

	public void setBannerAction(String action) {
		mReceivedAction = action;
		mBannerAction.setText(action);
	}

	public void setBackgroundColor(int colorResource) {
		mBanner.setBackgroundResource(colorResource);
		for (int count = 0; count < mPromotionalSlider.getChildCount(); count++) {
			mPromotionalSlider.getChildAt(count).setBackgroundResource(
					colorResource);
		}
	}

}