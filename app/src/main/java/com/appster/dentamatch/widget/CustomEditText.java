/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class CustomEditText extends AppCompatEditText {

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(Color.parseColor("#4f4f4f"));
    }
}

