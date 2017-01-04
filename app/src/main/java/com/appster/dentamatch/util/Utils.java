
package com.appster.dentamatch.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.util.List;

/**
 * Utils for app
 */
public class Utils {
    public static final String TAG = "Utils";

    @Nullable
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getDrawable(drawableId);

        //noinspection deprecation
        return context.getResources().getDrawable(drawableId);
    }


    public static boolean isSimulator() {
        boolean isSimulator = "google_sdk".equals(Build.PRODUCT)
                || "vbox86p".equals(Build.PRODUCT)
                || "sdk".equals(Build.PRODUCT);
        LogUtils.LOGD(TAG, "Build.PRODUCT= " + Build.PRODUCT + "  isSimulator= "
                + isSimulator);

        return isSimulator;
    }

    public static Drawable getGalleryIcon(Context ct){
        Drawable galleryIcon = null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        List<ResolveInfo> allHandlers = ct.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for(int i = 0; i < allHandlers.size(); i++){
            if(allHandlers.get(i).activityInfo.name.toLowerCase().contains("gallery")){
                galleryIcon = allHandlers.get(i).loadIcon(ct.getPackageManager());
                break;
            }
        }

        return galleryIcon;
    }

    public static Drawable getCameraIcon(Context ct){
        Drawable cameraIcon = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> allHandlers = ct.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for(int i = 0; i < allHandlers.size(); i++){
            if(allHandlers.get(i).activityInfo.name.toLowerCase().contains("camera")){
                cameraIcon = allHandlers.get(i).loadIcon(ct.getPackageManager());
                break;
            }
        }
        return cameraIcon;
    }

    public static void setSpanUnderline(SpannableString spannableString, int start, int end) {
        spannableString.setSpan(new UnderlineSpan(), start, end, 0);
    }

    public static void setSpanColor(SpannableString spanColor, int start, int end, int colorCode) {
        spanColor.setSpan(new ForegroundColorSpan(colorCode), start, end, 0);
    }

    public static void setSpanClickEvent(SpannableString spannableString, int start, int end, ClickableSpan clickableSpan) {
        spannableString.setSpan(clickableSpan, start, end, 0);
    }

    public static void setSpanCommonProperty(TextView txv, SpannableString spanString) {
        txv.setMovementMethod(LinkMovementMethod.getInstance());
        txv.setText(spanString, TextView.BufferType.SPANNABLE);
        txv.setSelected(true);
    }
}
