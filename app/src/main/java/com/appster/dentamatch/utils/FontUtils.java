package com.appster.dentamatch.utils;

import android.content.Context;
import android.graphics.Typeface;

public final class FontUtils {
    public static Typeface getHelvetBold(Context mContext){
        Typeface mTypeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/HelvetBold.ttf");

        return mTypeface;
    }

    public static Typeface getHelveticaNeue(Context mContext){
        Typeface mTypeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/HelveticaNeue.ttf");

        return mTypeface;
    }

    public static Typeface getHelveticaNeueMedium(Context mContext){
        Typeface mTypeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/HelveticaNeue-Medium.ttf");

        return mTypeface;
    }

    public static Typeface getHelvnl(Context mContext){
        Typeface mTypeface = Typeface.createFromAsset(mContext.getAssets(),"fonts/HELVNL.ttf");

        return mTypeface;
    }

}
