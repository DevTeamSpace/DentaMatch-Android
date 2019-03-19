
/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.network.response.notification.NotificationData;
import com.appster.dentamatch.presentation.messages.ChatMessageModel;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
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

    private static final String TAG = LogUtils.makeLogTag(Utils.class);
    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_IN_MONTH = 30;
    private static final int MONTH_IN_YEAR = 12;

    /**
     * To get the device unique ID
     *
     * @return deviceId
     */
    public synchronized static String getDeviceID() {
        String uniqueID = PreferenceUtil.getDeviceId();

        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString();
            PreferenceUtil.setDeviceId(uniqueID);
        }

        return uniqueID;
    }

    /**
     * To get the formatted date
     *
     * @param date String date
     * @return formatted date
     */
    public static String parseDateForTemp(String date) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d1 = df.parse(date);


            return new SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(d1);
        } catch (ParseException e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

        return null;
    }

    /**
     * To get the formatted date
     *
     * @param date String date
     * @return formatted date
     */
    public static String parseDateForTempWitCom(String date) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date d1 = df.parse(date);


            return new SimpleDateFormat("EEE MMM d", Locale.getDefault()).format(d1);
        } catch (ParseException e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

        return null;
    }

    /**
     * To get the formatted date
     *
     * @param date String date
     * @return formatted date
     */
    public static String parseDateForNtfLst(NotificationData data, List<String> date) {
        try {
            if (date == null || date.isEmpty() || data.getNotificationType() != 9)
                return data.getNotificationData();
            StringBuilder sb = new StringBuilder();
            sb.append(data.getNotificationData());
            sb.append(" for ");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            for (String sngDate : date) {
                Date d1 = df.parse(sngDate);
                sb.append(new SimpleDateFormat("d MMM", Locale.getDefault()).format(d1));
                sb.append(", ");
            }
            sb.replace(sb.length() - 2, sb.length() - 1, ".");
            return sb.toString();
        } catch (ParseException e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

        return null;
    }

    /**
     * To get the User FCM token for push notification
     *
     * @return FCM token
     */
    public static String getDeviceToken() {
        if (PreferenceUtil.getFcmToken() != null) {
            return PreferenceUtil.getFcmToken();
        }
        return "";
    }

    /**
     * To get drawable based on API version
     *
     * @param context    context
     * @param drawableId drawable id
     * @return Drawable
     */
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(drawableId);
        }

        //noinspection deprecation
        return context.getResources().getDrawable(drawableId);
    }

    /**
     * To check if the message date are same or different
     *
     * @param lastMsgTime     last message time
     * @param receivedMsgTime received message time
     * @return boolean
     */
    public static boolean isMsgDateDifferent(long lastMsgTime, long receivedMsgTime) {
        final SimpleDateFormat DateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        DateOnlyFormat.setTimeZone(TimeZone.getDefault());

        Date lastMsgDate = new Date(lastMsgTime);
        Date receivedMsgDate = new Date(receivedMsgTime);

        String lastMsgDateLabel = DateOnlyFormat.format(lastMsgDate);
        String receivedMsgDateLabel = DateOnlyFormat.format(receivedMsgDate);

        return !lastMsgDateLabel.equalsIgnoreCase(receivedMsgDateLabel);
    }

    /**
     * TO get the address for the provided coordinate
     *
     * @param ct     context
     * @param latLng coordinate
     * @return address
     */
    public static Address getReverseGeoCode(Context ct, LatLng latLng) {
        Address address = null;
        if (ct != null) {

            if (Utils.isConnected(ct)) {
                Geocoder geocoder = new Geocoder(ct);
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0 && addressList.get(0) != null) {
                        address = addressList.get(0);
                    }
                } catch (IOException e) {
                    LogUtils.LOGE(TAG, e.getMessage());
                }
            }
        }


        return address;
    }

    /**
     * To convert dp to pixel
     *
     * @param ct context
     * @param dp value in dp
     * @return pixel value
     */
    public static int dpToPx(Context ct, int dp) {
        if (ct != null) {
            DisplayMetrics displayMetrics = ct.getResources().getDisplayMetrics();
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }

        return 0;
    }

    /**
     * To show the toast message
     *
     * @param context context
     * @param message message
     */
    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * To show long toast message
     *
     * @param context context
     * @param message message
     */
    public static void showToastLong(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * to set spannable string forgrounf color
     *
     * @param spanColor span color
     * @param start     start index
     * @param end       end index
     * @param colorCode color code
     */
    public static void setSpannColor(SpannableString spanColor, int start, int end, int colorCode) {
        spanColor.setSpan(new ForegroundColorSpan(colorCode), start, end, 0);
//        spanColor.setSpan(new BackgroundColorSpan(Color.WHITE), start, end, 0);

    }

    /**
     * to set spannable string background color
     *
     * @param spanColor span color
     * @param start     start index
     * @param end       end index
     * @param colorCode color code
     * @param bgColor   background color
     */
    public static void setSpannColorNBg(SpannableString spanColor, int start, int end, int colorCode, int bgColor) {
        spanColor.setSpan(new ForegroundColorSpan(colorCode), start, end, 0);
        spanColor.setSpan(new BackgroundColorSpan(bgColor), start, end, 0);

    }

    /**
     * set click event on spannable string
     *
     * @param spanClick     span click
     * @param start         start index
     * @param end           end index
     * @param clickableSpan clickable span area
     */
    public static void setSpannClickEvent(SpannableString spanClick, int start, int end, ClickableSpan clickableSpan) {
        spanClick.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    /**
     * set some comman proprty fo span
     *
     * @param txv        text view
     * @param spanString string to be span
     */
    public static void setSpannCommanProperty(TextView txv, SpannableString spanString) {
        txv.setMovementMethod(LinkMovementMethod.getInstance());
        txv.setText(spanString, TextView.BufferType.SPANNABLE);
        txv.setSelected(true);
    }

    /**
     * to capitalize a word in edit text
     *
     * @param inputText edit text
     */
    public static void capitalizeWord(EditText inputText) {
        inputText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    public static void setTransparentTheme(Activity activity) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * to show and hide password
     *
     * @param edtPassword edit text
     * @param isShow      boolean flag
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

    /**
     * To get string content from requested edit text
     *
     * @param editText edit text
     * @return string
     */
    public static String getStringFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * To set font value for the provided text view
     *
     * @param view text view
     */
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

    /**
     * Util function to convert UTC date time to its local locale date time
     *
     * @param UTCDateTime date time in UTC format
     * @return date time in local format
     */
    public static String convertUTCtoLocal(String UTCDateTime) {
        final SimpleDateFormat hourOnlyDateFormat = new SimpleDateFormat("ha", Locale.getDefault()); // DATE FORMAT : 9 am

        Date myDate = null;
        try {
//            timeOnlyDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat timeOnlyDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            myDate = timeOnlyDateFormat.parse(UTCDateTime);
        } catch (ParseException e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

        hourOnlyDateFormat.setTimeZone(TimeZone.getDefault());
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setAmPmStrings(new String[]{"am", "pm"});
        hourOnlyDateFormat.setDateFormatSymbols(symbols);
        return hourOnlyDateFormat.format(myDate);
    }

    /**
     * Util function to convert UTC time stamp to local time
     *
     * @param UTCDateTime time stamp in UTC
     * @return formatted time
     */
    public static String convertUTCtoLocalFromTimeStamp(String UTCDateTime) {
        final SimpleDateFormat chatTimeFormat = new SimpleDateFormat("hh:mma", Locale.getDefault()); // DATE FORMAT : 09:46 am

        Long time = Long.parseLong(UTCDateTime);
        Date date = new Date(time);
        chatTimeFormat.setTimeZone(TimeZone.getDefault());
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        symbols.setAmPmStrings(new String[]{"am", "pm"});
        chatTimeFormat.setDateFormatSymbols(symbols);
        return chatTimeFormat.format(date);
    }

    public static String convertUTCToTimeLabel(String UTCDateTime) {
        final SimpleDateFormat DateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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
        SimpleDateFormat FullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        final SimpleDateFormat DateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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
            LogUtils.LOGE(TAG, e.getMessage());
        }

        return "";
    }

    /**
     * To get formatted date string
     *
     * @param UTCDateTime date time in UTC
     * @return string in required format
     */
    public static String compareDateForDateLabel(String UTCDateTime) {
        final SimpleDateFormat chatDateLabelFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault()); // DATE FORMAT : 09:46 ams

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

    /**
     * To get expiry year based on provided month
     *
     * @param month month as int
     * @return year as a string
     */
    public static String getExpYears(int month) {
        String yearLabel, monthLabel;

        DentaApp app = DentaApp.Companion.getInstance();
        if (month / 12 == 1) {
            yearLabel = app.getString(R.string.txt_single_year);
        } else {
            yearLabel = app.getString(R.string.txt_multiple_years);
        }

        if (month % 12 == 1) {
            monthLabel = app.getString(R.string.txt_single_month);
        } else {
            monthLabel = app.getString(R.string.txt_multiple_months);
        }

        if (month / 12 == 0) {
            return month % 12 + " " + monthLabel;

        } else if (month % 12 == 0) {
            return month / 12 + " " + yearLabel;

        } else {
            return month / 12 + " " + yearLabel + " " + month % 12 + " " + monthLabel;
        }

    }

    /**
     * Util function to check if the device is connected with the network or not
     *
     * @param context context
     * @return boolean flag
     */
    public static boolean isConnected(Context context) {
        NetworkInfo netInfo = null;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null)
                netInfo = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    /**
     * To clear all the notifications
     *
     * @param ct context
     */
    public static void clearAllNotifications(Context ct) {
        if (ct != null) {
            NotificationManager notificationManager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.cancelAll();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH && am != null) {
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
        } else if (am != null) {
            List<ActivityManager.AppTask> taskInfo = am.getAppTasks();
            ComponentName componentInfo = taskInfo.get(0).getTaskInfo().topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static void showNotification(Context ct, String title, String message, Intent intent, String notificationId) {
        if (ct != null) {

            int uniqueID = (int) (System.currentTimeMillis() & 0xfffffff);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ct, notificationId);
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

            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(notificationId, title, NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(mChannel);
                }
                manager.notify(Integer.parseInt(notificationId), notification);
            }
        }

    }

    /**
     * To cler all the recruiter notifications
     *
     * @param ct          context
     * @param RecruiterID recruiter Id
     */
    public static void clearRecruiterNotification(Context ct, String RecruiterID) {
        if (ct != null) {
            NotificationManager manager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null)
                manager.cancel(Integer.parseInt(RecruiterID));
        }
    }

    /**
     * To parse date for message
     *
     * @param messageData message data as json object
     * @return ChatMessageModel
     */
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
            LogUtils.LOGE(TAG, e.getMessage());
        }
        return model;
    }

    /**
     * To parse date for History message
     *
     * @param messageData message data as json object
     * @return ChatMessageModel
     */
    public static ChatMessageModel parseDataForHistory(JSONObject messageData) {
        ChatMessageModel model = new ChatMessageModel();

        try {
            model.setFromID(messageData.getString(SocketManager.PARAM_FROM_ID));
            model.setToID(messageData.getString(SocketManager.PARAM_TO_ID));
            model.setMessageTime(messageData.getString(SocketManager.PARAM_SENT_TIME));
            model.setMessage(messageData.getString(SocketManager.PARAM_USER_MSG));
            model.setMessageId(messageData.getString(SocketManager.PARAM_MESSAGE_ID));
        } catch (JSONException e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
        return model;
    }

    /**
     * To get the formatted date for New Recruiters
     *
     * @param messageData date as json object
     * @return ChatMessageModel
     */
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
            LogUtils.LOGE(TAG, e.getMessage());
        }

        return model;
    }

    /**
     * To validate user entered email address
     *
     * @param email entered email address
     * @return boolean flag
     */
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * To get date in YYYY-MMMM-DD format
     *
     * @param dateStr input date
     * @return date in YYYY-MMMM-DD format
     */
    public static String dateFormatYYYYMMMMDD(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);

            SimpleDateFormat reqFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            return reqFormat.format(date);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
            return "";
        }
    }

    /**
     * To get date in YY-MM-DD format
     *
     * @param mydate input date
     * @return date in YY-MM-DD format
     */
    public static String dateFormetyyyyMMdd(Date mydate) {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sm.format(mydate);
    }

    /**
     * To get the required server date format
     *
     * @param dateStr date as string
     * @return formatted string
     */
    public static String getRequriedServerDateFormet(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);

            SimpleDateFormat reqFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return reqFormat.format(date);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
            return "";
        }
    }

    /**
     * Get formatted date of notification
     *
     * @param dateStr    date as string
     * @param dateFormat date format
     * @return Date
     */
    public static Date getDate(String dateStr, String dateFormat) {
        try {
            SimpleDateFormat inputFormat;
            if (dateFormat != null) {
                inputFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            } else {
                inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }

            return inputFormat.parse(dateStr);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Get formatted date of notification
     *
     * @param dateStr    date as string
     * @param dateFormat date format
     * @return Date
     */
    public static Date getDateNotification(String dateStr, String dateFormat) {
        try {
            SimpleDateFormat inputFormat;
            if (dateFormat != null) {
                inputFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            } else {
                inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            }
            return inputFormat.parse(dateStr);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * To get the day of the week
     *
     * @param dateStr date string
     * @return date
     */
    public static String getDayOfWeek(String dateStr) {
        try {

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);

            SimpleDateFormat reqFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return reqFormat.format(date);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
            return "";
        }
    }

    /**
     * Get formatted date
     *
     * @param date Date
     * @return Date
     */
    public static Date parseDate(Date date) {
        try {
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String strDate = sm.format(date);
            return sm.parse(strDate);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
            return null;

        }
    }

    /**
     * To get the duration time using created date
     *
     * @param createdDate created date
     * @param context     context
     * @return duration string
     */
    public static String getDuration(Date createdDate, Context context) {
        String time;
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        long currentMillis = currentDate.getTime();
        calendar.setTime(currentDate);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                1);
        long currentDay = calendar.getTimeInMillis();

        long createMillis = createdDate.getTime();
        calendar.setTime(createdDate);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                23,
                59,
                59);
        long createDay = calendar.getTimeInMillis();

        if (currentMillis >= createMillis) {
            long reqTime = (currentMillis - createMillis);
            long reqDays = (currentDay - createDay);
            long sec = reqTime / DateUtils.SECOND_IN_MILLIS;
            time = sec + " " + context.getString(R.string.sec);
            if (reqTime >= DateUtils.MINUTE_IN_MILLIS) {
                long minute = reqTime / DateUtils.MINUTE_IN_MILLIS;
                time = minute + " " + context.getString(R.string.min);
                if (reqTime >= DateUtils.HOUR_IN_MILLIS) {
                    long hrs = reqTime / DateUtils.HOUR_IN_MILLIS;
                    time = hrs + " " + context.getString(R.string.hrs);
                    if (reqTime > DateUtils.DAY_IN_MILLIS) {
                        int days = (int) (reqDays / DateUtils.DAY_IN_MILLIS);
                        days = days  == 0 ? 1 : days;
                        time = context.getResources()
                                .getQuantityString(R.plurals.days_ago_plurals, days, days);
                        if (days >= DAYS_IN_WEEK) {
                            int week = days / DAYS_IN_WEEK;
                            time = week + " " + context.getString(R.string.weeks);
                            if (days >= DAYS_IN_MONTH) {
                                int month = days / DAYS_IN_MONTH;
                                time = month + " " + context.getString(R.string.months);
                                if (month >= MONTH_IN_YEAR) {
                                    int year = month / MONTH_IN_YEAR;
                                    time = year + " " + context.getString(R.string.years);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            time = "20 sec ago";
        }
        return time;
    }
}