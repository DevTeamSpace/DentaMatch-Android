
package com.appster.dentamatch.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appster.dentamatch.R;

import java.util.List;

/**
 * Utils for app
 */
public class Utils {
    public static final String TAG = "Utils";

    @Nullable
    /*
    * get device id
    * */
    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceToken() {
        return "agkj2899jhfj38ry3ry0yr0321y0yr";
    }

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

    public static Drawable getGalleryIcon(Context ct) {
        Drawable galleryIcon = null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        List<ResolveInfo> allHandlers = ct.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (int i = 0; i < allHandlers.size(); i++) {
            if (allHandlers.get(i).activityInfo.name.toLowerCase().contains("gallery")) {
                galleryIcon = allHandlers.get(i).loadIcon(ct.getPackageManager());
                break;
            }
        }

        return galleryIcon;
    }

    public static Drawable getCameraIcon(Context ct) {
        Drawable cameraIcon = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> allHandlers = ct.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (int i = 0; i < allHandlers.size(); i++) {
            if (allHandlers.get(i).activityInfo.name.toLowerCase().contains("camera")) {
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

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * to set underline on a specific string
     *
     * @param spanUnderline
     * @param start
     * @param end
     */
    public static void setSpannUnderline(SpannableString spanUnderline, int start, int end) {
//        spanUnderline.setSpan(new UnderlineSpan(), start, end, 0);
    }

    /**
     * to set spnanble string forgrounf color
     *
     * @param spanColor
     * @param start
     * @param end
     * @param colorCode
     */
    public static void setSpannColor(SpannableString spanColor, int start, int end, int colorCode) {
        spanColor.setSpan(new ForegroundColorSpan(colorCode), start, end, 0);
//        spanColor.setSpan(new BackgroundColorSpan(Color.WHITE), start, end, 0);

    }

    /**
     * to set spannable string background color
     *
     * @param spanColor
     * @param start
     * @param end
     * @param colorCode
     * @param bgColor
     */
    public static void setSpannColorNBg(SpannableString spanColor, int start, int end, int colorCode, int bgColor) {
        spanColor.setSpan(new ForegroundColorSpan(colorCode), start, end, 0);
        spanColor.setSpan(new BackgroundColorSpan(bgColor), start, end, 0);

    }

    /**
     * set typeface of spannable string
     *
     * @param spanTypeface
     * @param start
     * @param end
     * @param type
     */
    public static void setSpannTypeface(SpannableString spanTypeface, int start, int end, int type) {
        spanTypeface.setSpan(new StyleSpan(type), start, end, 0);

    }

    /**
     * set click event on spannable string
     *
     * @param spanClick
     * @param start
     * @param end
     * @param clickableSpan
     */
    public static void setSpannClickEvent(SpannableString spanClick, int start, int end, ClickableSpan clickableSpan) {
        spanClick.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    /**
     * set some comman proprty fo span
     *
     * @param txv
     * @param spanString
     */
    public static void setSpannCommanProperty(TextView txv, SpannableString spanString) {
        txv.setMovementMethod(LinkMovementMethod.getInstance());
        txv.setText(spanString, TextView.BufferType.SPANNABLE);
        txv.setSelected(true);
    }

    /**
     * to capitalize a word in edit text
     *
     * @param inputText
     */
    public static void capitalizeWord(EditText inputText) {
        inputText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    /**
     * to show and hide password
     *
     * @param edtPassword
     * @param isShow
     * @param eyeIcon
     */
    public static void showPassword(EditText edtPassword, boolean isShow, ImageView eyeIcon) {
//        if (isShow) {
//            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            eyeIcon.setImageResource(R.drawable.open_eye_blue);
//        } else {
//            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            eyeIcon.setImageResource(R.drawable.close_eye_white);
//
//        }
        edtPassword.setSelection(edtPassword.length());
    }

}
