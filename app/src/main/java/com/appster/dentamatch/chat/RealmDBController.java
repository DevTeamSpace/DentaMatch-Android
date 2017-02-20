package com.appster.dentamatch.chat;

import com.appster.dentamatch.util.LogUtils;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmDBController {
    private final String TAG = "RealmController";

    public Realm realm;

    //This constructor must be called only for UI Thread
    //Don't call this method from worker thread, it will throw exception otherwise
    public RealmDBController() {
//        realm = RealmManager.getRealm();
    }

    //This constructor must be called for each worker Thread
    public RealmDBController(Realm realm) {
        this.realm = realm;
    }

    public RealmResults<ChatThread> getAllUserMessageList(Realm realm) {
        if (realm == null) {
            realm = RealmManager.getRealm();
        }
        RealmResults<ChatThread> chatMessages = null;
        try {
//            if (checkIfChatsExists()) {

            //Add value to the thread
            chatMessages = realm.where(ChatThread.class).findAllSorted("msg", Sort.DESCENDING);
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return chatMessages;
    }


    public boolean checkIfChatThreadExists(Realm realm, String id) {

        RealmQuery<ChatThread> query = realm.where(ChatThread.class)
                .equalTo("chatThreadId", id);

        return query.count() != 0;
    }


    public boolean checkIfChatsExists(Realm realm) {

        ChatThread chatThread = realm.where(ChatThread.class).findFirst();

        return (chatThread != null);
    }

    public void deleteDb(Realm realm) {
        if (realm == null) {
            realm = RealmManager.getRealm();
        }
        try {
            if (!realm.isInTransaction())
                realm.beginTransaction();
            realm.delete(ChatThread.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            realm.commitTransaction();
        }

    }

    public void saveChatMessage(Realm realm, ChatMessage chatMessage) {
        LogUtils.LOGD(TAG, "saveChatMessage");

        if (chatMessage == null) return;

        if (realm == null) {
            realm = RealmManager.getRealm();
        }

        if (!realm.isInTransaction())
            realm.beginTransaction();

        try {
            ChatMessage chatMessageNew = realm.copyToRealm(chatMessage);

            if (checkIfChatThreadExists(realm, chatMessage.getChatThreadId())) {
                //Add value to the thread
                ChatThread chatThread = realm.where(ChatThread.class).equalTo("chatThreadId", chatMessage.getChatThreadId()).findFirst();

                //CHECK DATE WITH LAST
                long lastdate = chatThread.getMessages().last().getMsgTimeStamp();
                if (chatMessageNew.getMsgTimeStamp() != lastdate) {
                    //ADD DATE HEADER
//                    chatThread.getMessages().add(Utils.getDateHeaderMessage(realm));
                }

                //ADD NEW MESSAGE
                chatThread.getMessages().add(chatMessageNew);
                chatThread.setLatestChatMsg(chatMessageNew);
                chatThread.setLatestMsgTimeStamp(chatMessageNew.getMsgTimeStamp());

            } else {
                //Create a thread
                ChatThread chatThread = realm.createObject(ChatThread.class, chatMessage.getChatThreadId());
                //Add date header
//                chatThread.getMessages().add(Utils.getDateHeaderMessage(realm));
                //Add First Message
                chatThread.getMessages().add(chatMessageNew);
                chatThread.setLatestChatMsg(chatMessageNew);
                chatThread.setLatestMsgTimeStamp(chatMessageNew.getMsgTimeStamp());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            realm.commitTransaction();
        }
    }

    public RealmList<ChatMessage> getAllMsgByThreadId(Realm realm, String threadId) {
        LogUtils.LOGI(TAG, "getAllMsgByThreadId");

        if (realm == null) {
            realm = RealmManager.getRealm();
        }

        RealmList<ChatMessage> chatMessages = new RealmList<ChatMessage>();

        try {
//            if (checkIfChatThreadExists(realm, jabberId)) {
                //Add value to the thread
                ChatThread thread = realm.where(ChatThread.class).equalTo("chatThreadId", threadId).findFirst();

                if (thread != null) {
                    LogUtils.LOGI(TAG, "Yes, Chat Thread Exists");
                    chatMessages = thread.getMessages();
                }
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return chatMessages;
    }

    public RealmResults<ChatThread> getAllChatThreads(Realm realm) {

        if (realm == null) {
            realm = RealmManager.getRealm();
        }

        RealmResults<ChatThread> realmResults = realm.where(ChatThread.class).findAll();

//        realmResults.size()

        return realmResults;
    }



//
//    public ChatThread getChatThreadByJabberId(Realm realm, String jabberId) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        ChatThread chatThread = null;
//        try {
//            if (checkIfChatThreadExists(realm, jabberId)) {
//                //Add value to the thread
//                chatThread = realm.where(ChatThread.class).equalTo("jabberId", jabberId).findFirst();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return chatThread;
//    }

//    public ChatMessage getLastReceivedMessage(Realm realm, String jabberId) {
//        if (StringUtils.isNullOrEmpty(jabberId)) {
//            LogUtils.LOGW(TAG, "null jid cannot process");
//            return null;
//        }
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        ChatMessage chatMessage = null;
//        try {
//            if (checkIfChatThreadExists(realm, jabberId)) {
//                ChatThread chatThread = realm.where(ChatThread.class).equalTo("jabberId", jabberId).findFirst();
//                chatMessage = chatThread.getMessages().where().equalTo("userType", AppConstants.UserType.receiver.toString()).findAllSorted("msgTimestamp", Sort.DESCENDING).first();
//
//                if (chatMessage == null) {
//                    chatMessage = chatThread.getMessages().where().equalTo("userType", AppConstants.UserType.sender.toString()).findAllSorted("msgTimestamp", Sort.DESCENDING).first();
//                }
//
//                if (chatMessage == null) {
//                    chatMessage = chatThread.getMessages().where().equalTo("userType", AppConstants.UserType.headerMsg.toString()).findAllSorted("msgTimestamp", Sort.DESCENDING).first();
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return chatMessage;
//    }
//
//    public void updateMessageStatusByMsgIdAndJabberId(Realm realm, String friendJabberId, String msgId, int msgStatus, String fromJid) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        if (!realm.isInTransaction())
//            realm.beginTransaction();
//        try {
//
//
//            if (checkIfChatThreadExists(realm, friendJabberId)) {
//                //Add value to the thread
//                ChatMessage chatMessageToUpdate = realm.where(ChatThread.class).equalTo("jabberId", friendJabberId).findFirst().getMessages().where().equalTo("msgId", msgId).findFirst();
//                if (chatMessageToUpdate != null) {
//                    if (Utils.isGroupJid(friendJabberId)) {
//
//                        updateGroupMsg(realm, msgStatus, fromJid, chatMessageToUpdate, friendJabberId);
//                    } else
//                        chatMessageToUpdate.setMsgStatus(msgStatus);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//    }

//    public void updateMsgToReadStatusSent(Realm realm, String friendJabberId, String msgId, String fromJid) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        if (!realm.isInTransaction())
//            realm.beginTransaction();
//        try {
//
//
//            if (checkIfChatThreadExists(realm, friendJabberId)) {
//                //Add value to the thread
//                ChatMessage chatMessageToUpdate = realm.where(ChatThread.class).equalTo("jabberId", friendJabberId).findFirst().getMessages().where().equalTo("msgId", msgId).findFirst();
//                if (chatMessageToUpdate != null) {
//                    chatMessageToUpdate.setReadStatusSent(true);
////                    if (Utils.isGroupJid(friendJabberId)) {
////
////                        updateGroupMsg(realm, msgStatus, fromJid, chatMessageToUpdate, friendJabberId);
////                    } else
////                        chatMessageToUpdate.setMsgStatus(msgStatus);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//    }
//
//
//    public void updateAllSentMessageStatusByJabberId(Realm realm, String friendJabberId, int msgStatus) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        if (!realm.isInTransaction())
//            realm.beginTransaction();
//        try {
//            if (checkIfChatThreadExists(realm, friendJabberId)) {
//                //Add value to the thread
//                RealmResults<ChatMessage> chatMessageRealmList = realm.where(ChatThread.class).equalTo("jabberId", friendJabberId).findFirst().getMessages().where().equalTo("userType", AppConstants.UserType.sender.toString()).findAll();
//                for (ChatMessage chatMessage : chatMessageRealmList) {
//                    chatMessage.setMsgStatus(msgStatus);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//
//        }
//    }
//
//    public void markMessagesAsRead(Realm realm, String friendJabberId) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        if (!realm.isInTransaction())
//            realm.beginTransaction();
//        try {
//            if (checkIfChatThreadExists(realm, friendJabberId)) {
//                ChatThread chatThread = realm.where(ChatThread.class).equalTo("jabberId", friendJabberId).findFirst();
//                //Add value to the thread
//                if (chatThread != null) {
//                    RealmResults<ChatMessage> chatMessagesToUpdate = chatThread.getMessages().where().equalTo("msgStatus", AppConstants.MsgStatus.unread.getValue()).findAll();
//                    if (chatMessagesToUpdate != null) {
//                        for (ChatMessage chatMessage : chatMessagesToUpdate) {
//                            chatMessage.setMsgStatus(AppConstants.MsgStatus.read.getValue());
//                        }
//                    }
//                    chatThread.setUnreadCount(0);
//                    chatThread.setUnread(false);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//
//        }
//    }



    //***********************************************//


//    public RealmList<ChatMessage> getAllUnsentMessages(Realm realm) {
//        if (realm == null ) {
//            realm = RealmManager.getRealm();
//        }
//        RealmList<ChatMessage> unsentChat = new RealmList<>();
//
//        try {
//            RealmResults<ChatThread> unsentThreads = realm.where(ChatThread.class).equalTo("messages.msgStatus", AppConstants.MsgStatus.unsent.getValue())
//                    .findAll();
//            for (ChatThread chat : unsentThreads) {
//
//                RealmResults<ChatMessage> unsentMessages = chat.getMessages().where().equalTo("msgStatus", AppConstants.MsgStatus.unsent.getValue()).findAll();
//                unsentChat.addAll(unsentMessages);
//
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return unsentChat;
//    }

//    public RealmResults<ChatThread> getAllUnsentChatThread(Realm realm) {
//        if (realm == null ) {
//            realm = RealmManager.getRealm();
//        }
//        RealmResults<ChatThread> unsentThreads = null;
//        try {
//
//            unsentThreads = realm.where(ChatThread.class).equalTo("messages.msgStatus", AppConstants.MsgStatus.unsent.getValue())
//                    .findAll();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return unsentThreads;
//    }


//    public void selectThread(Realm realm, ChatThread chatThreads) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            chatThreads.setSelected(true);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }
//
//    public void unselectThread(Realm realm, ChatThread chatThreads) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            chatThreads.setSelected(false);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }

//    public void unselectAllSelectedThreads(Realm realm) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            RealmResults<ChatThread> selectedContacts = realm.where(ChatThread.class).equalTo("isSelected", true).findAll();
//            for (ChatThread chatThreads : selectedContacts) {
//                chatThreads.setSelected(false);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }

//    public void deleteAllSelectedThreads(Realm realm) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            realm.where(ChatThread.class).equalTo("isSelected", true).findAll().deleteAllFromRealm();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }

//    public void readAllSelectedThreads(Realm realm) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            RealmResults<ChatThread> selectedContacts = realm.where(ChatThread.class).equalTo("isSelected", true).findAll();
//            for (ChatThread chatThread : selectedContacts) {
//                chatThread.setUnread(false);
//                RealmResults<ChatMessage> chatMessagesToUpdate = chatThread.getMessages().where().equalTo("msgStatus", AppConstants.MsgStatus.unread.getValue()).findAll();
//                if (chatMessagesToUpdate != null) {
//                    for (ChatMessage chatMessage : chatMessagesToUpdate) {
//                        chatMessage.setMsgStatus(AppConstants.MsgStatus.read.getValue());
//                    }
//                }
//                chatThread.setUnreadCount(0);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }


//    public void unreadAllSelectedThreads(Realm realm) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            RealmResults<ChatThread> selectedContacts = realm.where(ChatThread.class).equalTo("isSelected", true).findAll();
//            for (ChatThread chatThreads : selectedContacts) {
//                chatThreads.setUnread(true);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }


//    public boolean deleteChatThread(Realm realm, String groupJid) {
//
//        boolean isSuccess = true;
//        if (StringUtils.isNullOrEmpty(groupJid)) {
//            return false;
//        }
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            if (checkIfChatThreadExists(realm, groupJid)) {
//                realm.where(ChatThread.class).equalTo("jabberId", groupJid).findFirst().deleteFromRealm();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            isSuccess = false;
//        } finally {
//            realm.commitTransaction();
//        }
//        return isSuccess;
//
//    }

//    public long getSelectedThreadCount(Realm realm) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        long selectedCount = 0;
//        try {
//
//            selectedCount = realm.where(ChatThread.class).equalTo("isSelected", true).count();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return selectedCount;
//    }


//    public long getSelectedAndReadThreadCount(Realm realm) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        long selectedCount = 0;
//        try {
//
//            selectedCount = realm.where(ChatThread.class).equalTo("isSelected", true).equalTo("unreadCount", 0).equalTo("isUnread", false).count();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return selectedCount;
//    }

//    public long getSelectedAndUnReadThreadCount(Realm realm) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        long selectedCount = 0;
//        try {
//
//            selectedCount = realm.where(ChatThread.class).equalTo("isSelected", true).greaterThan("unreadCount", 0).or().equalTo("isUnread", true).count();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return selectedCount;
//    }


    //---------- Message Deletion
//    public void selectMessage(Realm realm, ChatMessage chatMessage) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            chatMessage.setSelected(true);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }

//    public void unselectMessage(Realm realm, ChatMessage chatMessage) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            chatMessage.setSelected(false);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }

//    public void unselectAllSelectedMessages(Realm realm, String friendJId) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            ChatThread chatThreads = realm.where(ChatThread.class).equalTo("jabberId", friendJId).findFirst();
//            if (chatThreads != null) {
//                RealmResults<ChatMessage> messages = chatThreads.getMessages().where().equalTo("isSelected", true).findAll();
//                for (ChatMessage chatMessage : messages) {
//                    chatMessage.setSelected(false);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }

//    public void deleteAllSelectedMessages(Realm realm, String friendJId) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        try {
//            if (!realm.isInTransaction())
//                realm.beginTransaction();
//            ChatThread chatThreads = realm.where(ChatThread.class).equalTo("jabberId", friendJId).findFirst();
//            if (chatThreads != null) {
//                chatThreads.getMessages().where().equalTo("isSelected", true).findAll().deleteAllFromRealm();
//                ChatMessage lastMessage = chatThreads.getMessages().where().equalTo("userType", AppConstants.UserType.sender.toString()).or().equalTo("userType", AppConstants.UserType.receiver.toString()).or().equalTo("userType", AppConstants.UserType.headerMsg.toString()).findAll().last();
//                if (lastMessage != null) {
//                    chatThreads.setLatestMessage(lastMessage);
//                } else {
//                    chatThreads.setLatestMessage(null);
//                }
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//    }
//
//    public long getSelectedMessageCount(Realm realm, String friendJId) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        long selectedCount = 0;
//        try {
//
//            ChatThread chatThreads = realm.where(ChatThread.class).equalTo("jabberId", friendJId).findFirst();
//            if (chatThreads != null) {
//                selectedCount = chatThreads.getMessages().where().equalTo("isSelected", true).count();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return selectedCount;
//    }
//
//
//    public void addTypingMessage(Realm realm, String friendJId, boolean startTransaction) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        if (startTransaction && !realm.isInTransaction())
//            realm.beginTransaction();
//        try {
//            ChatThread chatThreads = realm.where(ChatThread.class).equalTo("jabberId", friendJId).findFirst();
//            if (chatThreads != null) {
//                if (chatThreads.getMessages() != null && chatThreads.getMessages().where().equalTo("isEmptyMsg", true).count() == 0) {
//                    chatThreads.getMessages().add(Utils.getEmptySenderMessage(realm, friendJId));
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (startTransaction && !realm.isInTransaction())
//                realm.commitTransaction();
//        }
//
//    }
//
//    public void removeTypingMessage(Realm realm, String friendJId, boolean startTransaction) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        if (startTransaction && !realm.isInTransaction())
//            realm.beginTransaction();
//        try {
//            ChatThread chatThreads = realm.where(ChatThread.class).equalTo("jabberId", friendJId).findFirst();
//            if (chatThreads != null) {
//                RealmResults<ChatMessage> messages = chatThreads.getMessages().where().equalTo("isEmptyMsg", true).findAll();
//                chatThreads.getMessages().removeAll(messages);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (startTransaction && !realm.isInTransaction())
//                realm.commitTransaction();
//        }
//
//    }


    // no need to start transaction in this method because realm object is passed as parameter is already in write transaction
//    private void updateGroupMsg(Realm realm, int msgStatus, String fromJid, ChatMessage chatMessageToUpdate, String friendJabberId) {
//        RealmList<GroupUser> members = null;
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        members = new RealmControllerGroup(realm).getGroupInfoByGroupId(friendJabberId).getGroupUsers();
//
//        int size = 0;
//        if (members != null && members.size() > 0) {
//            size = members.size();
//        }
//        if (msgStatus == AppConstants.MsgStatus.delivered.getValue()) {
//            if (fromJid != null)
//                chatMessageToUpdate.getRealmListMsgDelivered().add(new ChatUser(fromJid));
//            if (chatMessageToUpdate.getRealmListMsgDelivered().size() >= size - 1)
//                chatMessageToUpdate.setMsgStatus(msgStatus);
//
//        } else if (msgStatus == AppConstants.MsgStatus.read.getValue()) {
//            if (fromJid != null) {
//                chatMessageToUpdate.getRealmListMsgSeen().add(new ChatUser(fromJid));
//                chatMessageToUpdate.getRealmListMsgDelivered().add(new ChatUser(fromJid));
//            }
//
//            if (chatMessageToUpdate.getRealmListMsgSeen().size() >= size - 1)
//                chatMessageToUpdate.setMsgStatus(msgStatus);
//            else if (chatMessageToUpdate.getRealmListMsgDelivered().size() >= size - 1)
//                chatMessageToUpdate.setMsgStatus(AppConstants.MsgStatus.delivered.getValue());
//        } else {
//            chatMessageToUpdate.setMsgStatus(msgStatus);
//        }
//    }

//    public void updateMessageByMsgIdAndJabberId(Realm realm, String friendJabberId, String messageId, ChatMessage chatMessage) {
//        if (chatMessage == null) return;
//
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//
//        if (!realm.isInTransaction())
//            realm.beginTransaction();
//        try {
//            if (checkIfChatThreadExists(realm, friendJabberId)) {
//
//                ChatMessage chatMessageToUpdate = realm.where(ChatThread.class).equalTo("jabberId", friendJabberId).findFirst().getMessages().where().equalTo("msgId", messageId).findFirst();
//                if (chatMessageToUpdate != null) {
//                    copyMessage(realm, chatMessageToUpdate, chatMessage, false);
//                }
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//
//
//    }

//    public void updateUnreadStatus(Realm realm, String friendJabberId) {
//        if (realm == null) {
//            realm = RealmManager.getRealm();
//        }
//        if (!realm.isInTransaction())
//            realm.beginTransaction();
//        try {
//            if (checkIfChatThreadExists(realm, friendJabberId)) {
//
//                ChatThread chatThread = realm.where(ChatThread.class).equalTo("jabberId", friendJabberId).findFirst();
//                if (chatThread != null) {
//                    long unreadCount = chatThread.getMessages().where().equalTo("msgStatus", AppConstants.MsgStatus.unread.getValue()).count();
//                    chatThread.setUnreadCount(unreadCount);
//                    if (unreadCount > 0) {
//                        chatThread.setUnread(false);
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            realm.commitTransaction();
//        }
//    }

}
