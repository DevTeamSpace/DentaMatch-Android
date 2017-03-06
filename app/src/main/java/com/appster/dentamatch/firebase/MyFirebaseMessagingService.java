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
    private String KEY_JOB_DETAIL = "jobDetails";
    private String KEY_NOTIFICATION_DETAIL = "notification_details";
    private boolean isChatMessage = false;
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. ReadNotificationRequest messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        LogUtils.LOGD(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        DentaApp.NOTIFICATION_COUNTER=DentaApp.NOTIFICATION_COUNTER++;
        if (remoteMessage.getData().size() > 0) {
            LogUtils.LOGD(TAG, "Message data payload: " + remoteMessage.getData());
        }
        Map<String, String> dataPayload =  remoteMessage.getData();
//        int type,id,jobId=0;
        int type = 0, id, jobId = 0;
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

//            id =Integer.parseInt(dataPayload.get(Constants.APIS.NOTIFICATION_ID));
//            type = Integer.parseInt(dataPayload.get(Constants.APIS.NOTIFICATION_TYPE));
            Log.d("message payload:", "DATA PAYLOAD" + dataPayload);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null && !isChatMessage) {
            LogUtils.LOGD(TAG, "Message ReadNotificationRequest Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody(), type, jobId);

        }else{
            try {
                if(remoteMessage.getData() != null) {
                     ChatMessageModel model;
                    String data = dataPayload.get("data");
                    JSONObject object = new JSONObject(data);

                    if (object.has("messageListId")) {
                        model = Utils.parseDataForNewRecruiterMessage(object);
                    }else {
                        model = Utils.parseData(object);
                    }

                    final Message notificationMessage = new Message(model.getMessage(), model.getRecruiterName(), model.getMessageTime(), model.getMessageId(), Message.TYPE_MESSAGE_RECEIVED);

                    Handler handler = new Handler(Looper.getMainLooper());
                    final ChatMessageModel finalModel = model;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            DBHelper.getInstance().insertIntoDB(finalModel.getFromID(), notificationMessage, finalModel.getRecruiterName(), 1, finalModel.getMessageListId());
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

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

    private void sendNotification(String messageBody, int notificationType, int jobId) {
        Intent intent = redirectNotification(notificationType, jobId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(DentaApp.NOTIFICATION_COUNTER /* ID of notification */, notificationBuilder.build());
    }

    private Intent redirectNotification(int notiifcationType, int jobId) {
        Intent intent = null;
        if (notiifcationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_ACCEPT_JOB || notiifcationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_CANCEL || notiifcationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_HIRED|| notiifcationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_INVITE) {
            intent = new Intent(this, JobDetailActivity.class);
            intent.putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobId);
        } else if (notiifcationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_COMPLETE_PROFILE || notiifcationType == Constants.NOTIFICATIONTYPES.NOTIFICATION_VERIFY_DOC) {
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