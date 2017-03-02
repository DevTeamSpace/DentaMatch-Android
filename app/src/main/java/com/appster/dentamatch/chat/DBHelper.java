package com.appster.dentamatch.chat;


import android.content.Context;
import android.text.TextUtils;

import com.appster.dentamatch.ui.messages.Message;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Appster on 14/02/17.
 */

public class DBHelper {
    private final String TAG = "RealmDBHelper";
    public static final String UNREAD_MSG_COUNT = "UNREAD_MSG_COUNT";
    public static final String LAST_MSG = "LAST_MSG";
    public static final String USER_CHATS = "USER_CHATS";
    public static final String IS_RECRUITED_BLOCKED = "IS_RECRUITED_BLOCKED";
    public static final String IS_SYNCED = "IS_SYNCED";
    private final String DB_PRIMARY_KEY = "recruiterId";
    private final String REALM_INSTANCE_ERROR = "realm instance is null";

    private static DBHelper realmDBHelper;
    private Realm mRealmInstance;


    public static DBHelper getInstance() {
        if (realmDBHelper == null) {
            synchronized (DBHelper.class) {
                if (realmDBHelper == null) {
                    realmDBHelper = new DBHelper();
                }
            }
        }

        return realmDBHelper;
    }

    /**
     * Call this method from Application class.
     * Initialize the DB with the default configuration.
     *
     * @param appContext : Context
     */
    public void initializeRealmConfig(Context appContext) {
        Realm.init(appContext);
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        mRealmInstance = Realm.getDefaultInstance();
    }


    public RealmResults<DBModel> getAllUserChats() {
        if (mRealmInstance == null) {
            LogUtils.LOGD(TAG, REALM_INSTANCE_ERROR);
            return null;
        }

        return mRealmInstance.where(DBModel.class).findAllSorted("lastMsgTime", Sort.DESCENDING);
    }

    public void insertIntoDB(String recruiterId, Message userMessage, String recruiterName, int unreadMsgCount, String messageListID) {

        if (mRealmInstance == null) {
            LogUtils.LOGD(TAG, REALM_INSTANCE_ERROR);
        } else {
            mRealmInstance.beginTransaction();
            DBModel retrievedModel = getDBData(recruiterId);

            /**
             * Check if the entry exists in the DB , if not then insert a new entry into the DB.
             */
            if (retrievedModel != null) {
                if (!checkIfMessageAlreadyExists(recruiterId, userMessage)) {
                    if (!TextUtils.isEmpty(recruiterName)) {
                        retrievedModel.setName(recruiterName);
                    }
                    retrievedModel.setLastMsgTime(userMessage.getmMessageTime());
                    retrievedModel.setLastMessage(userMessage.getMessage());
                    retrievedModel.setUnReadChatCount(retrievedModel.getUnReadChatCount() + unreadMsgCount);

                    /**
                     * Checking for date changes which needs to be shown on the ChatActivity as date header above messages. Eg. Today, yesterday etc.
                     */
                    if (retrievedModel.getUserChats().size() > 0) {
                        if (Utils.isMsgDateDifferent(Long.parseLong(retrievedModel.getUserChats().get(retrievedModel.getUserChats().size() - 1).getmMessageTime()),
                                Long.parseLong(userMessage.getmMessageTime()))) {

                            Message dateHeaderMessage = new Message("", "", userMessage.getmMessageTime(), "", Message.TYPE_DATE_HEADER);
                            retrievedModel.getUserChats().add(dateHeaderMessage);
                        }
                    }

                    retrievedModel.getUserChats().add(userMessage);
                }
            } else {

                DBModel newModel = mRealmInstance.createObject(DBModel.class, recruiterId);
                newModel.setLastMessage(userMessage.getMessage());
                newModel.setMessageListId(messageListID);
                newModel.setLastMsgTime(userMessage.getmMessageTime());
                newModel.setUnReadChatCount(unreadMsgCount);
                newModel.setSeekerHasBlocked(0); // Set unblocked as default.
                newModel.setName(recruiterName);

                /**
                 * Adding date label on the new entry.
                 */
                Message dateHeaderMessage = new Message("", "", userMessage.getmMessageTime(), "", Message.TYPE_DATE_HEADER);
                newModel.getUserChats().add(dateHeaderMessage);
                newModel.getUserChats().add(userMessage);
            }

            mRealmInstance.commitTransaction();
        }
    }

    /**
     * Check if the message adding into the DB is already contained in the DB or not. If
     * not then we add or else we don't.
     */
    public boolean checkIfMessageAlreadyExists(String recruiterID, Message messageObj) {
        boolean isAlreadyAdded = false;
        DBModel retrievedData = getDBData(recruiterID);

        if (retrievedData != null && retrievedData.getUserChats()!= null && retrievedData.getUserChats().size() > 0) {
            for (Message message : retrievedData.getUserChats()) {
                if (message.getmMessageId().equalsIgnoreCase(messageObj.getmMessageId())) {
                    isAlreadyAdded = true;
                    break;
                }
            }

        } else {
            /**
             * Since ChatArray provided has no entries of chat in it , thus we conclude that
             * the chatArray needs to be updated.
             */
            isAlreadyAdded = false;
        }

        return isAlreadyAdded;
    }

    /**
     * Gets the DB model based on the recruitedId and updates the value corresponding to the key.
     *
     * @param key:  the key value to be sorted with.
     * @param value :the value of the parameter to be sorted with.
     */
    public void upDateDB(String recruiterId, String key, String value, Message message) {
        DBModel retrievedModel = mRealmInstance.where(DBModel.class).equalTo(DB_PRIMARY_KEY, recruiterId).findFirst();

        if (retrievedModel != null) {
            mRealmInstance.beginTransaction();
            switch (key) {

                case UNREAD_MSG_COUNT:
                    retrievedModel.setUnReadChatCount(Integer.parseInt(value));
                    break;

                case LAST_MSG:
                    retrievedModel.setLastMessage(value);
                    break;

                case USER_CHATS:
                    retrievedModel.getUserChats().add(message);
                    break;

                case IS_RECRUITED_BLOCKED:
                    retrievedModel.setSeekerHasBlocked(Integer.parseInt(value));
                    break;

                case IS_SYNCED:
                    retrievedModel.setHasDBUpdated(Boolean.parseBoolean(value));
                    break;

                default:
                    break;

            }

            mRealmInstance.commitTransaction();

        }
    }

    public void clearRecruiterChats(String recruiterID) {
        DBModel retrievedModel = mRealmInstance.where(DBModel.class).equalTo(DB_PRIMARY_KEY, recruiterID).findFirst();

        if (retrievedModel != null) {
            mRealmInstance.beginTransaction();
            retrievedModel.getUserChats().clear();
            mRealmInstance.commitTransaction();
        }
    }

    /**
     * Get the DBModel for the specified recruiterId.
     *
     * @param recruiterId : the recruiter ID.
     */
    public DBModel getDBData(String recruiterId) {
        return mRealmInstance.where(DBModel.class).equalTo(DB_PRIMARY_KEY, recruiterId).findFirst();
    }

    public void setSyncNeeded() {
        mRealmInstance.beginTransaction();
        RealmResults<DBModel> DBData = getAllUserChats();
        for (DBModel model : DBData) {
            model.setHasDBUpdated(false);
        }
        mRealmInstance.commitTransaction();
    }

    public void clearDBData() {
        try {
            mRealmInstance.beginTransaction();
            mRealmInstance.deleteAll();
            mRealmInstance.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}