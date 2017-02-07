package com.appster.dentamatch.ui.chat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityChatListBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;


/**
 * Created by ramkumar on 06/02/17.
 */

public class ChatListActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ChatList";
    private ActivityChatListBinding mBinder;
    private Socket mSocket;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_chat_list);

        mBinder.chatLogin.setOnClickListener(this);

//        try {
//            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }

        mSocket = ((DentaApp) getApplication()).getSocket();

        gson = new Gson();
    }

    @Override
    public String getActivityName() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_login:
                Utils.showToast(this, "Login clicked");
                JSONObject jsonObject = getUserObject();
                mSocket.emit("init", jsonObject);

                startActivity(new Intent(this, ChatActivity.class));

                break;
        }
    }

    private JSONObject getUserObject() {
        LogUtils.LOGD(TAG, "getUserObject: ");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("User", "Ram");
//        String json = gson.toJson(hashMap);

        return new JSONObject(hashMap);
    }

}
