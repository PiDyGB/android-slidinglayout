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
package com.gb.android.graphics;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * This Class handles font files in the assets folder
 */
public class Typefaces {
	private static final HashMap<String, Typeface> fontMap = new HashMap<String, Typeface>();

	/**
	 * Loads a font filename from the assets/fonts folder, implements a memory
	 * cache mechanism
	 * 
	 * @param context
	 *            The {@link Context}
	 * @param font
	 *            The filename of the font file to load
	 * @return Return a {@link Typeface}
	 */
	public static Typeface get(Context context, String font) {
		synchronized (fontMap) {
			if (!fontMap.containsKey(font)) {
				try {
					Typeface typeface = Typeface.createFromAsset(context
							.getResources().getAssets(), "fonts/" + font);
					fontMap.put(font, typeface);
				} catch (Exception e) {
					Log.e("FontFactory", "Could not get typeface '" + font
							+ "' because: " + e.getMessage());
					return null;
				}
			}
			return fontMap.get(font);
		}
	}
}
