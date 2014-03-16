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
package com.gb.android.widget;

import com.gb.android.graphics.Typefaces;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class EditText extends android.widget.EditText {

    private String typeFaceName;

    public EditText(Context context) {
	super(context);
    }

    public EditText(Context context, AttributeSet attrs) {
	super(context, attrs);
	parseAttrs(context, attrs);
    }

    public EditText(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
	TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
		R.styleable.EditText, 0, 0);
	try {
	    typeFaceName = a.getString(R.styleable.TextView_typeface);
	} finally {
	    a.recycle();
	}
	setCustomFont(context);
    }

    private void setCustomFont(Context ctx) {
	if (typeFaceName != null) {
	    setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG
		    | Paint.LINEAR_TEXT_FLAG);
	    Typeface tf = Typefaces.get(ctx, typeFaceName);
	    if (tf != null)
		setTypeface(tf);
	}
    }

    /**
     * Set a custom font from the assets/fonts folder
     * 
     * @param ctx
     *            The {@link Context}
     * @param font
     *            A {@link String} that represent a filename in the assets/fonts
     *            folder
     */
    public void setCustomFont(Context ctx, String font) {
	typeFaceName = font;
	setCustomFont(ctx);
    }

    /**
     * Return the font filename
     * 
     * @return A {@link String} that represent the font filename
     */
    public String getCustomFont() {
	return typeFaceName;
    }
}
