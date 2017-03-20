
package com.appster.dentamatch.util;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
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
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.ui.messages.ChatMessageModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static int NOTIFICATION_CODE = 00101;

    private static final SimpleDateFormat timeOnlyDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat DateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat FullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final SimpleDateFormat hourOnlyDateFormat = new SimpleDateFormat("h a", Locale.getDefault()); // DATE FORMAT : 9 am
    private static final SimpleDateFormat chatTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault()); // DATE FORMAT : 09:46 am
    private static final SimpleDateFormat chatDateLabelFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault()); // DATE FORMAT : 09:46 am

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
        if (PreferenceUtil.getFcmToken() != null) {
            return PreferenceUtil.getFcmToken();
        }
        return "";
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getDrawable(drawableId);

        //noinspection deprecation
        return context.getResources().getDrawable(drawableId);
    }

    public static boolean isMsgDateDifferent(long lastMsgTime, long receivedMsgTime) {
        DateOnlyFormat.setTimeZone(TimeZone.getDefault());

        Date lastMsgDate = new Date(lastMsgTime);
        Date receivedMsgDate = new Date(receivedMsgTime);

        String lastMsgDateLabel = DateOnlyFormat.format(lastMsgDate);
        String receivedMsgDateLabel = DateOnlyFormat.format(receivedMsgDate);

        if (lastMsgDateLabel.equalsIgnoreCase(receivedMsgDateLabel)) {
            return false;
        } else {
            return true;
        }
    }

    public static Address getReverseGeoCode(Context ct,LatLng latLng) {
        Address address = null;

        Geocoder geocoder = new Geocoder(ct);
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addressList.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        return address;
    }


    public static BaseResponse parseDataOnError(retrofit2.Response<BaseResponse> response) {
        Gson gson = new Gson();
        BaseResponse apiResponse = null;
        TypeAdapter<BaseResponse> adapter = gson.getAdapter(BaseResponse.class);

        try {
            if (response.errorBody() != null) {
                apiResponse = adapter.fromJson(response.errorBody().string());
            } else {
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

    public static void setFontFaceRobotoRegular(TextView view) {
        Typeface tf = Typeface.createFromAsset(view.getContext()
                .getAssets(), "Roboto-Regular.ttf");

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

    public static void setFontFaceRobotoMedium(TextView view) {
        Typeface tf = Typeface.createFromAsset(view.getContext()
                .getAssets(), "Roboto-Bold.ttf");

        view.setTypeface(tf);

    }

    public static String convertUTCtoLocal(String UTCDateTime) {
        Date myDate = null;
        try {
//            timeOnlyDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            myDate = timeOnlyDateFormat.parse(UTCDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        hourOnlyDateFormat.setTimeZone(TimeZone.getDefault());
        return hourOnlyDateFormat.format(myDate);
    }

    public static String convertUTCtoLocalFromTimeStamp(String UTCDateTime) {
        Long time = Long.parseLong(UTCDateTime);
        Date date = new Date(time);
        chatTimeFormat.setTimeZone(TimeZone.getDefault());
        return chatTimeFormat.format(date);
    }

    public static String convertUTCToTimeLabel(String UTCDateTime) {
        Long time = Long.parseLong(UTCDateTime);
        Date receivedDate = new Date(time);
        Date currentDate = new Date(System.currentTimeMillis());
        DateOnlyFormat.setTimeZone(TimeZone.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterdayDate = calendar.getTime();

        String receivedDateString = DateOnlyFormat.format(receivedDate);
        String currentDateString = DateOnlyFormat.format(currentDate);
        String yesterdaysDateString = DateOnlyFormat.format(yesterdayDate);

        if (receivedDateString.equalsIgnoreCase(currentDateString)) {
            return "Today";

        } else if (receivedDateString.equalsIgnoreCase(yesterdaysDateString)) {
            return "Yesterday";

        } else {
            return receivedDateString;
        }
    }

    public static String compareDateFromCurrentLocalTime(String date) {
        FullDateFormat.setTimeZone(TimeZone.getDefault());
        DateOnlyFormat.setTimeZone(TimeZone.getDefault());

        try {
            String currentDate = DateOnlyFormat.format(new Date(System.currentTimeMillis()));

            Date convertedDate = FullDateFormat.parse(date);
            String receivedDate = DateOnlyFormat.format(convertedDate);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String yesterdayDate = DateOnlyFormat.format(calendar.getTime());

            if (receivedDate.equalsIgnoreCase(currentDate)) {
                return "Today";

            } else if (receivedDate.equalsIgnoreCase(yesterdayDate)) {
                return "Yesterday";

            } else {
                return receivedDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String compareDateForDateLabel(String UTCDateTime) {
        Long time = Long.parseLong(UTCDateTime);
        Date date = new Date(time);
        chatDateLabelFormat.setTimeZone(TimeZone.getDefault());


        String currentDate = chatDateLabelFormat.format(new Date(System.currentTimeMillis()));

        String receivedDate = chatDateLabelFormat.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String yesterdayDate = chatDateLabelFormat.format(calendar.getTime());

        if (receivedDate.equalsIgnoreCase(currentDate)) {
            return "Today";

        } else if (receivedDate.equalsIgnoreCase(yesterdayDate)) {
            return "Yesterday";

        } else {
            return receivedDate;
        }


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

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void clearAllNotifications(Context ct) {
        NotificationManager notificationManager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static void showNotification(Context ct, String title, String message, Intent intent, String notificationId) {
        int uniqueID = (int) (System.currentTimeMillis() & 0xfffffff);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ct);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder.setContentTitle(title)
                .setSound(defaultSound)
                .setContentText(message)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setSmallIcon(R.drawable.bg_notification_icon)
                .setColor(ContextCompat.getColor(ct, R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(ct.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true);


        if (intent != null) {
            PendingIntent Pendingintent = PendingIntent.getActivity(ct, uniqueID, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(Pendingintent);
        }

        NotificationManager manager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        manager.notify(Integer.parseInt(notificationId), notification);
    }

    public static void clearRecruiterNotification(Context ct, String RecruiterID) {
        NotificationManager manager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(Integer.parseInt(RecruiterID));
    }

    public static ChatMessageModel parseData(JSONObject messageData) {
        ChatMessageModel model = new ChatMessageModel();

        try {
            model.setFromID(messageData.getString(SocketManager.PARAM_FROM_ID));
            model.setRecruiterName(messageData.getString(SocketManager.PARAM_RECRUITER_NAME));
            model.setToID(messageData.getString(SocketManager.PARAM_TO_ID));
            model.setMessageTime(messageData.getString(SocketManager.PARAM_SENT_TIME));
            model.setMessage(messageData.getString(SocketManager.PARAM_USER_MSG));
            model.setMessageId(messageData.getString(SocketManager.PARAM_MESSAGE_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static ChatMessageModel parseDataForHistory(JSONObject messageData) {
        ChatMessageModel model = new ChatMessageModel();

        try {
            model.setFromID(messageData.getString(SocketManager.PARAM_FROM_ID));
            model.setToID(messageData.getString(SocketManager.PARAM_TO_ID));
            model.setMessageTime(messageData.getString(SocketManager.PARAM_SENT_TIME));
            model.setMessage(messageData.getString(SocketManager.PARAM_USER_MSG));
            model.setMessageId(messageData.getString(SocketManager.PARAM_MESSAGE_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static ChatMessageModel parseDataForNewRecruiterMessage(JSONObject messageData) {
        ChatMessageModel model = new ChatMessageModel();

        try {
            model.setFromID(messageData.getString("recruiterId"));
            model.setToID(messageData.getString("seekerId"));
            model.setMessageTime(messageData.getString("timestamp"));
            model.setMessageId(messageData.getString("messageId"));
            model.setMessage(messageData.getString("message"));
            model.setMessageListId(messageData.getString("messageListId"));
            model.setRecruiterName(messageData.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static boolean isValidEmailAddress(String email) {
//        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

        String ePattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        String ePattern = "\"^[_A-Za-z0-9-]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String dateFormatYYYYMMMMDD(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateStr);

            SimpleDateFormat reqFormat = new SimpleDateFormat("dd MMMM yyyy");
            String formattedDate = reqFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String dateFormetyyyyMMdd(Date mydate) {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        // myDate is the java.util.Date in yyyy-mm-dd format
        // Converting it into String using formatter
        String strDate = sm.format(mydate);
        //Converting the String back to java.util.Date
        return strDate;
    }

    public static String getRequriedServerDateFormet(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = inputFormat.parse(dateStr);

            SimpleDateFormat reqFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = reqFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date getDate(String dateStr, String dateFormet) {
        try {
            SimpleDateFormat inputFormat;
            if(dateFormet != null){
                 inputFormat = new SimpleDateFormat(dateFormet, Locale.getDefault());
            }else {
                 inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = inputFormat.parse(dateStr);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDayOfWeek(String dateStr) {
        try {

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(dateStr);

            SimpleDateFormat reqFormat = new SimpleDateFormat("EEEE");
            String formattedDate = reqFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date parseDate(Date date) {
        try {


            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            // myDate is the java.util.Date in yyyy-mm-dd format
            // Converting it into String using formatter
            String strDate = sm.format(date);
            return sm.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public static String getDuration(Date createdDate, Context context) {
        String time = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdDate);
        long createMillis = cal.getTimeInMillis();
        long currentMillis = System.currentTimeMillis();
        long reqTime = currentMillis - createMillis;
        long sec = reqTime / 1000;
        time = sec + " " + context.getString(R.string.sec);
        if (sec >= 60) {
            long minute = sec / 60;
            time = minute + " " + context.getString(R.string.min);

            if (minute >= 60) {
                long hrs = minute / 60;
                time = hrs + " " + context.getString(R.string.hrs);

                if (hrs > 24) {
                    int days = (int) hrs / 24;
                    time = days + " " + context.getString(R.string.days);

                    if (days >= 7) {
                        int week = days / 7;
                        time = week + " " + context.getString(R.string.weeks);

                        if (days >= 30) {
                            int month = days / 30;
                            time = month + " " + context.getString(R.string.mon);

                            if (month >= 12) {
                                int year = month / 12;
                                time = year + " " + context.getString(R.string.years);

                            }
                        }
                    }
                }
            }
        }
        return time;
    }


}