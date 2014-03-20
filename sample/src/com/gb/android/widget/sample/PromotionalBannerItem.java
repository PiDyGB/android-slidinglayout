/**
 * HM
 * PromotionalItem.java
 *
 * Copyright (c) 2014 Accenture. All rights reserved.
 *
 */
package com.gb.android.widget.sample;


/**
 * This class represents an item in the PromotionalBanner component.
 * 
 */
public class PromotionalBannerItem {

	private String mPromotionText;
	private String mAction;

	public void setPromotionText(String promotionText) {
		mPromotionText = promotionText;
	}

	public void setAction(String action) {
		mAction = action;
	}

	public String getPromotionText() {
		return mPromotionText;
	}

	public String getAction() {
		return mAction;
	}

}
