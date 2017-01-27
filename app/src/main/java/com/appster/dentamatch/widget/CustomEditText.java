package com.appster.dentamatch.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText {

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TypefacedText, 0, 0);
//            String fontName = a.getString(R.styleable.TypefacedText_fontface);
//            a.recycle();
//
//            if (fontName != null)
//                FontUtils.setFontFace(context, this, fontName);
//            else FontUtils.setFontFace(context, this, "REGULAR");
        }

		setTextColor(Color.parseColor("#4f4f4f"));
    }
}

