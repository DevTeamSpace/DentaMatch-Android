package com.appster.dentamatch.util;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;

import com.appster.dentamatch.widget.CustomTextView;

public class TextViewUtils {

    public static void setTextColor(@NonNull TextView textView, @ColorRes int colorId) {
        textView.setTextColor(ResourcesCompat.getColor(textView.getResources(), colorId, null));
    }

    public static void setCustomFont(@NonNull TextView textView, @StringRes int fontStringId) {
        if (textView instanceof CustomTextView) {
            Context context = textView.getContext();
            ((CustomTextView) textView).setCustomFont(context, context.getString(fontStringId));
        }
    }
}
