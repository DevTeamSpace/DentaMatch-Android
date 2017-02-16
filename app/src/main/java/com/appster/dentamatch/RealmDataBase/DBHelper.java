package com.appster.dentamatch.RealmDataBase;


import android.content.Context;

import com.appster.dentamatch.ui.messages.Message;
import com.appster.dentamatch.util.LogUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Appster on 14/02/17.
 */

public class DBHelper {
    private  final String TAG = "RealmDBHelper";
    public static final String UNREAD_MSG_COUNT = "UNREAD_MSG_COUNT";
    public static final String LAST_MSG = "LAST_MSG";
    public static final String USER_CHATS = "USER_CHATS";
    public static final String IS_RECRUITED_BLOCKED = "IS_RECRUITED_BLOCKED";
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

        return mRealmInstance.where(DBModel.class).findAll();
    }

    public void insertIntoDB(String recruiterId, Message userMessage, String recruiterName, String unreadMsgCount) {

        if (mRealmInstance == null) {
            LogUtils.LOGD(TAG, REALM_INSTANCE_ERROR);
        }else {
            mRealmInstance.beginTransaction();
            DBModel retrievedModel = getDBData(recruiterId);

            /**
             * Check if the entry exists in the DB , if not then insert a new entry into the DB.
             */
            if (retrievedModel != null ) {
                retrievedModel.getUserChats().add(userMessage);
                retrievedModel.setLastMessage(userMessage.getMessage());
            } else {

                DBModel newModel = mRealmInstance.createObject(DBModel.class, recruiterId);
                newModel.setLastMessage(userMessage.getMessage());
                newModel.setUnReadChatCount(unreadMsgCount);
                newModel.setSeekerHasBlocked(0); // Set unblocked as default.
                newModel.setName(recruiterName);
                newModel.getUserChats().add(userMessage);
            }
            mRealmInstance.commitTransaction();
        }
    }

    /**
     * Gets the DB model based on the recruitedId and updates the value corresponding to the key.
     * @param key: the key value to be sorted with.
     * @param value :the value of the parameter to be sorted with.
     */
    public void upDateDB(String recruiterId, String key, String value, Message message){
        DBModel retrievedModel = mRealmInstance.where(DBModel.class).equalTo(DB_PRIMARY_KEY, recruiterId).findFirst();

        if(retrievedModel != null){
            mRealmInstance.beginTransaction();
            switch (key){

                case UNREAD_MSG_COUNT:
                    retrievedModel.setUnReadChatCount(value);
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

                default: break;

            }
            mRealmInstance.commitTransaction();

        }
    }

    public RealmList<Message> getRecruiterPastChats(String recruiterId){
        DBModel retrievedModel = getDBData(recruiterId);

        if(retrievedModel != null){

           if( retrievedModel.getUserChats().size() > 0){
               return retrievedModel.getUserChats();
           }
        }

        return null;
    }

    /**
     * Get the DBModel for the specified recruiterId.
     * @param recruiterId : the recruiter ID.
     */
    public DBModel getDBData(String recruiterId){
        return mRealmInstance.where(DBModel.class).equalTo(DB_PRIMARY_KEY, recruiterId).findFirst();
    }


}
