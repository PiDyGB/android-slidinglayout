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

import java.util.Locale;

import com.gb.android.graphics.Typefaces;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;

public class TextView extends android.widget.TextView {

    private CharSequence originalText = "";
    private float mScaleLetterSpacing = 0;
    private String typeFaceName;
    private boolean toUpperCase;
    private Locale mLocale;

    public TextView(Context context) {
        super(context);
        mLocale = context.getResources().getConfiguration().locale;
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLocale = context.getResources().getConfiguration().locale;
        parseAttrs(context, attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mLocale = context.getResources().getConfiguration().locale;
        parseAttrs(context, attrs);
    }

    /**
     * Return the scale factor for the letter spacing
     * 
     * @return A {@link float} that represent the scaling factor
     */
    public float getScaleLetterSpacing() {
        return mScaleLetterSpacing;
    }

    /**
     * Set a scale factor for the letter spacing
     * 
     * @param letterSpacing
     *            A {@link float}
     */
    public void setScaleLetterSpacing(float letterSpacing) {
        this.mScaleLetterSpacing = letterSpacing;
        applyLetterSpacing();
    }

    /**
     * Sets a text in this TextView respecting the xml attributes
     * 
     * @param text
     *            A text
     */
    public void setCustomText(CharSequence text) {
        setText(text, null);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        if (toUpperCase)
            setUpperCase();
        else
            applyLetterSpacing();
    }

    @Override
    public CharSequence getText() {
        return originalText;
    }

    /**
     * Set the text in upper case
     * 
     */
    public void setUpperCase() {
        originalText = originalText != null ? originalText.toString()
                .toUpperCase(mLocale) : null;
        applyLetterSpacing();
    }

    private void applyLetterSpacing() {
        if (mScaleLetterSpacing == 0) {
            super.setText(originalText, BufferType.SPANNABLE);
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));
            if (i + 1 < originalText.length()) {
                builder.append("\u00A0");
            }
        }
        SpannableString finalText = new SpannableString(builder.toString());
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i += 2) {
                finalText.setSpan(new ScaleXSpan(mScaleLetterSpacing), i,
                        i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        super.setText(finalText, BufferType.SPANNABLE);
    }

    @SuppressLint("InlinedApi")
    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TextView, 0, 0);
        int set[] = { android.R.attr.text };
        TypedArray aa = context.obtainStyledAttributes(attrs, set);
        try {
            mScaleLetterSpacing = a.getFloat(
                    R.styleable.TextView_scale_tracking, 0f);
            originalText = aa.getText(0);
            toUpperCase = a.getBoolean(R.styleable.TextView_to_upper_case, false);
            typeFaceName = a.getString(R.styleable.TextView_typeface);
        } finally {
            a.recycle();
            aa.recycle();
        }
        setCustomFont(context);
        if (toUpperCase)
            originalText = originalText != null ? originalText.toString()
                    .toUpperCase(mLocale) : null;
        applyLetterSpacing();
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
