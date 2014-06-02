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
package com.github.pidygb.android.text.method;

import android.content.Context;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.view.View;

import java.util.Locale;

/**
 * Transforms source text into an ALL CAPS string, locale-aware.
 *
 * @hide
 */
public class TextViewTransformationMethod implements TransformationMethod2 {

    private Locale mLocale;
    private float mScaleLetterSpacing;
    private boolean mAllCaps;

    public TextViewTransformationMethod(Context context, boolean allCaps,
                                        float scaleLetterSpacing) {
        mLocale = context.getResources().getConfiguration().locale;
        mAllCaps = allCaps;
        mScaleLetterSpacing = scaleLetterSpacing;
    }

    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        if (source == null)
            return null;
        CharSequence transformedText = source;
        if (mAllCaps)
            transformedText = source.toString().toUpperCase(mLocale);

        if (mScaleLetterSpacing == 0) {
            return transformedText;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < transformedText.length(); i++) {
            builder.append(transformedText.charAt(i));
            if (i + 1 < transformedText.length()) {
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
        return finalText;

    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText,
                               boolean focused, int direction, Rect previouslyFocusedRect) {
    }

    @Override
    public void setLengthChangesAllowed(boolean allowLengthChanges) {
    }

}