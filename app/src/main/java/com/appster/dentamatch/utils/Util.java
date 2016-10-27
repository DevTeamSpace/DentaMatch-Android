package com.appster.dentamatch.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Appster on 03/03/16.
 */
public class Util {

    private static final String TAG = "Util";
    private static final SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final SimpleDateFormat completeDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * To validate  the password pattern
     */
    public static boolean isPasswordValid(String pass) {
        return !(pass.trim().length() < 8 || pass.trim().length() > 25);
    }

    /*
    * get valid email
    * */
    public static boolean isEmailValid(String email) {
        Pattern pattern1 = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher matcher1 = pattern1.matcher(email);

        return matcher1.matches();

//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /*
    * get device id
    * */
    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /*
    * Check valid URL
    * */
    public static boolean isURLValid(String sentText) {
        final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(sentText.toLowerCase());
        if (m.find()) {
            return true;
        }
        return false;
    }

    /*
    * get country code
    * */
    public static String getCountryCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String iso = tm.getSimCountryIso();
        if (iso.isEmpty())
            iso = Locale.getDefault().getCountry();
        return iso;
    }

    public static boolean isMobileValid(String mobile) {
        return mobile.trim().length() >= 7 && mobile.trim().length() <= 14;
    }

//    public static void showSnackbar(View view, String msg) {
//        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
//    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void setUnderLineText(TextView textView, String text) {
        SpannableString content = new SpannableString(text);

        if (text.equalsIgnoreCase("Not registered yet? Sign Up")) {
            int len = "Not registered yet?".length();
            content.setSpan(new UnderlineSpan(), len, content.length(), 0);
        } else
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    public static Uri createURI(Context context, int resourceId) {
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(),
                BitmapFactory.decodeResource(context.getResources(), resourceId), null, null));

        return uri;
    }

    public static String convertToJson(Object object) {
        return new Gson().toJson(object);
    }

    public static int[] getDeviceDimension(Context context) {
        int dimenArray[] = new int[2];
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        dimenArray[0] = height;
        dimenArray[1] = width;
        return dimenArray;
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

    /**
     * Set spanned string on text view
     * @param view
     * @param spanString
     * @param startIndex
     * @param color
     */
    public static void setSpanString(TextView view,SpannableString spanString, int startIndex, int color) {
        setSpanUnderline(spanString, startIndex, spanString.length());
        setSpanColor(spanString, startIndex, spanString.length(), color);
        setSpanCommonProperty(view, spanString);
    }

    public static void showPassword(EditText edtPassword, boolean isShow) {
        if (isShow) {
            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        }
        edtPassword.setSelection(edtPassword.length());
    }

    public static void showAlert(Context ct, String message, DialogInterface.OnClickListener listener){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ct)
                    .setTitle(ct.getString(R.string.app_name))
                    .setMessage(message)
                    .setCancelable(false)
//                    .setPositiveButton(ct.getString(R.string.ok), listener);
                    .setPositiveButton("OK", listener);
            builder.create().show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showAlert(Context ct, String msg, String positiveText, String negativeText, DialogInterface.OnClickListener l ) {
        try {
            new AlertDialog.Builder(ct).setTitle(ct.getString(R.string.app_name))
                    .setMessage(msg)
                    .setPositiveButton(positiveText, l)
                    .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static void showNetworkError(Context ct) {
//        showAlert(ct, ct.getString(R.string.error_network_connection), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
        showAlert(ct, "Network Error", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static void openGooglePlacePicker(Activity ct,LatLngBounds boundsMountainView,int iPlacePickerRequest){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//            intentBuilder.setLatLngBounds(boundsMountainView);
            ct.startActivityForResult(builder.build(ct), iPlacePickerRequest);
        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public static String getBookLaterDate(Date bookDate){
        return dateOnlyFormat.format(bookDate);
    }

    public static String convertLocalToUTC(String dateTime){
        Date UTCTime = null;
        try {
            completeDateFormat.setTimeZone(TimeZone.getDefault());
            UTCTime = completeDateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        completeDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return completeDateFormat.format(UTCTime);
    }

    public static String convertUTCtoLocal(String UTCDateTime){
        Date myDate = null;
        try {
            completeDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
             myDate = completeDateFormat.parse(UTCDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        completeDateFormat.setTimeZone(TimeZone.getDefault());
       return completeDateFormat.format(myDate);
    }

    public static boolean checkIfTimeElapsed(String date){
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        try {
            completeDateFormat.setTimeZone(TimeZone.getDefault());
            Date previousDate = completeDateFormat.parse(date);
            calendar.setTime(previousDate);
            long selectedTime = calendar.getTimeInMillis();

            if(selectedTime < currentTime){
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
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


}

