/**
 * 
 */
package com.gb.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.gb.android.graphics.Typefaces;
import com.gb.android.text.method.TextViewTransformationMethod;

/**
 * @author pidy
 * 
 */
public class TextView extends android.widget.TextView {

    private float mScaleLetterSpacing;
    private Context mContext;
    private boolean mAllCaps;
    private String mTypeFaceName;

    public TextView(Context context) {
	super(context);
	mContext = context;
	parseAttrs(context, null);
    }

    public TextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	mContext = context;
	parseAttrs(context, attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	mContext = context;
	parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
	TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
		R.styleable.TextView, 0, 0);
	try {
	    mScaleLetterSpacing = a.getFloat(
		    R.styleable.TextView_scaleLetterSpacing, 0f);
	    mAllCaps = a.getBoolean(R.styleable.TextView_textUpperCase, false);
	    mTypeFaceName = a.getString(R.styleable.TextView_typeface);
	} finally {
	    a.recycle();
	}
	setTypefaceName(context, mTypeFaceName);
	setTransformationMethod(new TextViewTransformationMethod(context,
		mAllCaps, mScaleLetterSpacing));
    }

    /**
     * Sets the properties of this field to transform input to ALL CAPS display.
     * This may use a "small caps" formatting if available. This setting will be
     * ignored if this field is editable or selectable.
     * 
     * This call replaces the current transformation method. Disabling this will
     * not necessarily restore the previous behavior from before this was
     * enabled.
     * 
     * @see #setTransformationMethod(TransformationMethod)
     * @attr ref android.R.styleable#TextView_textAllCaps
     */
    public void setAllCaps(boolean allCaps) {
	mAllCaps = allCaps;
	setTransformationMethod(new TextViewTransformationMethod(mContext,
		allCaps, mScaleLetterSpacing));
    }

    /**
     * Sets the text color, size, style, hint color, and highlight color from
     * the specified TextAppearance resource.
     */
    @Override
    public void setTextAppearance(Context context, int resid) {
	super.setTextAppearance(context, resid);

	TypedArray appearance = context.obtainStyledAttributes(resid,
		R.styleable.TextView);

	boolean allCaps = appearance.getBoolean(
		R.styleable.TextView_textUpperCase, false);

	float scaleLetterSpacing = appearance.getFloat(
		R.styleable.TextView_scaleLetterSpacing, 0f);

	setTransformationMethod(new TextViewTransformationMethod(context,
		allCaps, scaleLetterSpacing));

	appearance.recycle();
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
    public void setTypefaceName(Context ctx, String font) {
	mTypeFaceName = font;
	if (mTypeFaceName != null) {
	    setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG
		    | Paint.LINEAR_TEXT_FLAG);
	    Typeface tf = Typefaces.get(ctx, mTypeFaceName);
	    if (tf != null)
		setTypeface(tf);

	}
    }

    /**
     * Return the font filename
     * 
     * @return A {@link String} that represent the font filename
     */
    public String getTypefaceName() {
	return mTypeFaceName;
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
    public void setScaleLetterSpacing(float scaleLetterSpacing) {
	setTransformationMethod(new TextViewTransformationMethod(mContext,
		mAllCaps, scaleLetterSpacing));
    }
}
