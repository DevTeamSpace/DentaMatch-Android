package com.appster.dentamatch.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.DBHelper;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.messages.ChatMessageModel;
import com.appster.dentamatch.ui.messages.Message;
import com.appster.dentamatch.ui.notification.NotificationActivity;
import com.appster.dentamatch.ui.searchjob.JobDetailActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private String KEY_JOB_DETAIL = "jobDetails";
    private String KEY_NOTIFICATION_DETAIL = "notification_details";
    private String KEY_MESSAGE_LIST_ID ="messageListId";
    private String KEY_DATA = "data";
    private String KEY_NOTIFICATION_DATA = "notification_data";
    private boolean isChatMessage = false;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /**
         * There are two types of messages data messages and notification messages. Data messages are handled
         * here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
         * traditionally used with GCM. ReadNotificationRequest messages are only received here in onMessageReceived when the app
         * is in the foreground. When the app is in the background an automatically generated notification is displayed.
         * When the user taps on the notification they are returned to the app. Messages containing both notification
         * and data payloads are treated as notification messages. The Firebase console always sends notification
         * messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
         */

        /**
         * Not getting messages here? See why this may be: https://goo.gl/39bRNJ
         */
        LogUtils.LOGD(TAG, "From: " + remoteMessage.getFrom());
        DentaApp.NOTIFICATION_COUNTER = DentaApp.NOTIFICATION_COUNTER++;

        if (remoteMessage.getData().size() > 0) {
            LogUtils.LOGD(TAG, "Message data payload:" + remoteMessage.getData());
        }

        Map<String, String> dataPayload = remoteMessage.getData();
        int type = 0, id, jobId = 0;
        String messageBody = "";

        if (dataPayload != null && dataPayload.size() > 0) {
            Set<String> keys = dataPayload.keySet();
            String notificationData = parsePayloadData(dataPayload, KEY_NOTIFICATION_DETAIL);
            String jobData = parsePayloadData(dataPayload, KEY_JOB_DETAIL);

            if (!TextUtils.isEmpty(notificationData)) {
                isChatMessage = false;

                try {
                    JSONObject notificationObject = new JSONObject(notificationData);
                    id = notificationObject.optInt(Constants.APIS.NOTIFICATION_ID);
                    type = notificationObject.optInt((Constants.APIS.NOTIFICATION_TYPE));
                    messageBody = notificationObject.getString(KEY_NOTIFICATION_DATA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!TextUtils.isEmpty(jobData)) {
                isChatMessage = false;

                try {
                    JSONObject notificationObject = new JSONObject(jobData);
                    jobId = notificationObject.optInt(Constants.APIS.ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (TextUtils.isEmpty(jobData) && TextUtils.isEmpty(notificationData)) {
                isChatMessage = true;
            }

        }

        /**
         * Check if message contains a notification payload.
         */
        if (!isChatMessage) {
            sendNotification(messageBody, type, jobId);

        } else {
            try {
                if (remoteMessage.getData() != null) {
                    ChatMessageModel model;
                    String data = dataPayload.get(KEY_DATA);
                    JSONObject object = new JSONObject(data);

                    if (object.has(KEY_MESSAGE_LIST_ID)) {
                        model = Utils.parseDataForNewRecruiterMessage(object);
                    } else {
                        model = Utils.parseData(object);
                    }

                    Handler handler = new Handler(Looper.getMainLooper());
                    final ChatMessageModel finalModel = model;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            DBHelper.getInstance().updateRecruiterDetails(finalModel.getFromID(),
                                    finalModel.getRecruiterName(),
                                    1,
                                    finalModel.getMessageListId(),
                                    finalModel.getMessage(),
                                    finalModel.getMessageTime(),
                                    false);
                        }
                    });

                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(Constants.EXTRA_FROM_CHAT, model.getFromID());
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    Utils.showNotification(this, model.getRecruiterName(), model.getMessage(), intent, finalModel.getFromID());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void sendNotification(String messageBody, int notificationType, int jobId) {
        Intent intent = redirectNotification(notificationType, jobId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Utils.showNotification(this, getString(R.string.app_name),
                messageBody,
                intent,
                String.valueOf(DentaApp.NOTIFICATION_COUNTER));
    }

    private Intent redirectNotification(int notificationType, int jobId) {
        Intent intent = null;

        if (notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_ACCEPT_JOB ||
                notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_CANCEL ||
                notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_HIRED ||
                notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE) {
            intent = new Intent(this, JobDetailActivity.class);
            intent.putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobId);

        } else if (notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_COMPLETE_PROFILE ||
                notificationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_VERIFY_DOC) {
            intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        } else {
            intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        }

        return intent;
    }

    private String parsePayloadData(Map<String, String> dataPayload, String key) {
        String data = null;

        if (dataPayload.containsKey(key)) {
            data = dataPayload.get(key);
        }

        return data;
    }

}