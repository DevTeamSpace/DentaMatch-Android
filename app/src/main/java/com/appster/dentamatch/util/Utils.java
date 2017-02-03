
package com.appster.dentamatch.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.R;

import com.appster.dentamatch.network.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Utils for app
 */
public class Utils {
    private static final String TAG = "Utils";
    private static final SimpleDateFormat timeOnlyDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat hourOnlyDateFormat = new SimpleDateFormat("h a", Locale.getDefault()); // DATE FORMAT : 9 am

    @Nullable
    /*
    * get device id
    * */
//    public static String getDeviceID(Context context) {
//        return Settings.Secure.getString(context.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//    }

    public synchronized static String getDeviceID(Context context) {
        String uniqueID = PreferenceUtil.getDeviceId();

        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString();
            PreferenceUtil.setDeviceId(uniqueID);
        }

        return uniqueID;
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

    public static BaseResponse parseDataOnError(retrofit2.Response<BaseResponse> response){
        Gson gson = new Gson();
        BaseResponse apiResponse = null;
        TypeAdapter<BaseResponse> adapter = gson.getAdapter(BaseResponse.class);

        try {
            if (response.errorBody() != null) {
                apiResponse = adapter.fromJson(response.errorBody().string());
            }else {
                LogUtils.LOGE(TAG, "Retrofit response.errorBody found null!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return apiResponse;
    }

    public static boolean isSimulator() {
        boolean isSimulator = "google_sdk".equals(Build.PRODUCT)
                || "vbox86p".equals(Build.PRODUCT)
                || "sdk".equals(Build.PRODUCT);
        LogUtils.LOGD(TAG, "Build.PRODUCT= " + Build.PRODUCT + "  isSimulator= "
                + isSimulator);

        return isSimulator;
    }

    public static int dpToPx(Context ct, int dp) {
        DisplayMetrics displayMetrics = ct.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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

    public static void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
     */
    public static void showPassword(Context context, EditText edtPassword, boolean isShow, TextView tvShowPwd) {
        if (edtPassword.getText().toString().trim().length() > 0) {
            if (!isShow) {
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                tvShowPwd.setText(context.getString(R.string.hide_password));

            } else {
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                tvShowPwd.setText(context.getString(R.string.show_password));


            }
            edtPassword.setSelection(edtPassword.length());
        }
    }

    public static void showNetowrkAlert(Context context) {
        showToast(context, context.getString(R.string.error_no_network_connection));

    }

    public static String getStringFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }


    public static void setFontFaceRobotoLight(TextView view) {
        Typeface tf = Typeface.createFromAsset(view.getContext()
                .getAssets(), "Roboto-Light.ttf");

        view.setTypeface(tf);

    }

    public static int convertSpToPixels(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static void setFontFaceRobotoBold(TextView view) {
        Typeface tf = Typeface.createFromAsset(view.getContext()
                .getAssets(), "Roboto-Bold.ttf");

        view.setTypeface(tf);

    }

    public static String convertUTCtoLocal(String UTCDateTime) {
        Date myDate = null;
        try {
            timeOnlyDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            myDate = timeOnlyDateFormat.parse(UTCDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        hourOnlyDateFormat.setTimeZone(TimeZone.getDefault());
        return hourOnlyDateFormat.format(myDate);
    }

    public static String getExpYears(int month) {

        if (month != 0) {
            return month / 12 + " " + DentaApp.getAppContext().getString(R.string.year) + " " + month % 12 + " " + DentaApp.getAppContext().getString(R.string.month);
        }
        return "";
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    private void setPhoneNumberFormat(final EditText editText, final int count) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (count <= editText.getText().toString().length()
                        && (editText.getText().toString().length() == 4
                        || editText.getText().toString().length() == 10
                        || editText.getText().toString().length() == 15)) {
                    editText.setText("(" + editText.getText().toString() + " )");
                    int pos = editText.getText().length();
                    editText.setSelection(pos);
                } else if (count >= editText.getText().toString().length()
                        && (editText.getText().toString().length() == 4
                        || editText.getText().toString().length() == 10
                        || editText.getText().toString().length() == 15)) {
                    editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                    int pos = editText.getText().length();
                    editText.setSelection(pos);
                }
//                count = editText.getText().toString().length();
            }

        });
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
