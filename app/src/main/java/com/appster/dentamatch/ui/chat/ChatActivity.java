package com.appster.dentamatch.ui.chat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.databinding.ActivityChatBinding;
import com.appster.dentamatch.databinding.ActivityChatListBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by ramkumar on 06/02/17.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Chat";
    private ActivityChatBinding mBinder;
    private Gson gson;

    private static final int REQUEST_LOGIN = 0;

    private static final int TYPING_TIMER_LENGTH = 600;

    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private String mUsername;

    private Boolean isConnected = true;
    private SocketManager socketManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.LOGD(TAG, "onCreate..");
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        gson = new Gson();
        mAdapter = new MessageAdapter(this, mMessages);

        mBinder.sendButton.setOnClickListener(this);
        mBinder.messages.setLayoutManager(new LinearLayoutManager(this));
        mBinder.messages.setAdapter(mAdapter);

        socketManager = SocketManager.getInstance(this);
        socketManager.connect();


    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.LOGD(TAG, "onResume..");

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("userId", "606");

        socketManager.init(userMap);
    }

    @Override
    public String getActivityName() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:

                String msg = mBinder.messageInput.getText().toString().trim();
                LogUtils.LOGD(TAG, "Send "+msg);

                if(TextUtils.isEmpty(msg)) return;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("fromId", "606");
                hashMap.put("toId", "420");
                hashMap.put("msg", msg);

                socketManager.sendMessage(hashMap);
                mBinder.messageInput.setText("");

                addMessage("Ram", msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socketManager.disconnect();
    }

    private JSONObject getUserObject() {
        LogUtils.LOGD(TAG, "getUserObject: ");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("User", "Ram");

        return new JSONObject(hashMap);
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    /*private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }*/

    private void addMessage(String username, String message) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

//    private void attemptSend() {
//        if (null == mUsername) return;
//        if (!mSocket.connected()) return;
//
//        mTyping = false;
//
//        String message = mInputMessageView.getText().toString().trim();
//        if (TextUtils.isEmpty(message)) {
//            mInputMessageView.requestFocus();
//            return;
//        }
//
//        mInputMessageView.setText("");
//        addMessage(mUsername, message);
//
//        // perform the sending message attempt.
//        mSocket.emit("new message", message);
//    }

/*    private void startSignIn() {
        mUsername = null;
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }*/

//    private void leave() {
//        mUsername = null;
//        mSocket.disconnect();
//        mSocket.connect();
////        startSignIn();
//    }

    private void scrollToBottom() {
        mBinder.messages.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    /*private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        if(null!=mUsername)
                            mSocket.emit("add user", mUsername);
                        Toast.makeText(ChatActivity.this.getApplicationContext(),
                                "Connected", Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Toast.makeText(ChatActivity.this.getApplicationContext(),
                            "Disconnected", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatActivity.this.getApplicationContext(),
                            "Failed to connect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    removeTyping(username);
                    addMessage(username, message);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");
        }
    };*/
}


/*public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Chat";
    private ActivityChatBinding mBinder;
    private Gson gson;

    private static final int REQUEST_LOGIN = 0;

    private static final int TYPING_TIMER_LENGTH = 600;

    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private String mUsername;
    private Socket mSocket;

    private Boolean isConnected = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.LOGD(TAG, "onCreate..");
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        gson = new Gson();
        mAdapter = new MessageAdapter(this, mMessages);

        mBinder.sendButton.setOnClickListener(this);
        mBinder.messages.setLayoutManager(new LinearLayoutManager(this));
        mBinder.messages.setAdapter(mAdapter);

        mSocket = ((DentaApp) getApplication()).getSocket();

        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
//        mSocket.on("typing", onTyping);
//        mSocket.on("stop typing", onStopTyping);
        mSocket.connect();

//        startSignIn();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.LOGD(TAG, "onResume..");

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("userId", "606");
        mSocket.emit("init", new JSONObject(userMap));
    }

    @Override
    public String getActivityName() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button:

                String msg = mBinder.messageInput.getText().toString().trim();
                LogUtils.LOGD(TAG, "Send "+msg);

                if(TextUtils.isEmpty(msg)) return;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("fromId", "606");
                hashMap.put("toId", "420");
                hashMap.put("msg", msg);

                JSONObject jsonObject = new JSONObject(hashMap);
                mSocket.emit("sendMsg", jsonObject);
                mBinder.messageInput.setText("");

                addMessage("Ram", msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();

//        mSocket.off(Socket.EVENT_CONNECT, onConnect);
//        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
//        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.off("new message", onNewMessage);
//        mSocket.off("typing", onTyping);
//        mSocket.off("stop typing", onStopTyping);
    }

    private JSONObject getUserObject() {
        LogUtils.LOGD(TAG, "getUserObject: ");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("User", "Ram");
//        String json = gson.toJson(hashMap);

        return new JSONObject(hashMap);
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    *//*private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }*//*

    private void addMessage(String username, String message) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

//    private void attemptSend() {
//        if (null == mUsername) return;
//        if (!mSocket.connected()) return;
//
//        mTyping = false;
//
//        String message = mInputMessageView.getText().toString().trim();
//        if (TextUtils.isEmpty(message)) {
//            mInputMessageView.requestFocus();
//            return;
//        }
//
//        mInputMessageView.setText("");
//        addMessage(mUsername, message);
//
//        // perform the sending message attempt.
//        mSocket.emit("new message", message);
//    }

*//*    private void startSignIn() {
        mUsername = null;
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }*//*

    private void leave() {
        mUsername = null;
        mSocket.disconnect();
        mSocket.connect();
//        startSignIn();
    }

    private void scrollToBottom() {
        mBinder.messages.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        if(null!=mUsername)
                            mSocket.emit("add user", mUsername);
                        Toast.makeText(ChatActivity.this.getApplicationContext(),
                                "Connected", Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Toast.makeText(ChatActivity.this.getApplicationContext(),
                            "Disconnected", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChatActivity.this.getApplicationContext(),
                            "Failed to connect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    removeTyping(username);
                    addMessage(username, message);
                }
            });
        }
    };

*//*    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_joined, username));
                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };*//*

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");
        }
    };
}*/
