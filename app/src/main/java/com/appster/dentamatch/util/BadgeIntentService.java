/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.util;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;

import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;

/**
 * Created by atul on 23/08/18.
 * To inject activity reference.
 */

public class BadgeIntentService extends IntentService {

    public BadgeIntentService() {
        super("BadgeIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getBatchCount();
    }

    private void getBatchCount() {

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getUnreadNotificationCount().enqueue(new BaseCallback<UnReadNotificationCountResponse>(null) {
            @Override
            public void onSuccess(UnReadNotificationCountResponse response) {
                if (response.getStatus() == 1) {
                    LogUtils.LOGI("BadgeIntentService", String.valueOf(response.getUnReadNotificationResponse().getNotificationCount()));
                    if (response.getUnReadNotificationResponse().getNotificationCount() == 0) {
                        ShortcutBadger.removeCount(getApplicationContext());
                        return;
                    }
                    ShortcutBadger.applyCount(getApplicationContext(), response.getUnReadNotificationResponse().getNotificationCount());
                } else {
                    LogUtils.LOGI("BadgeIntentService", response.getMessage());
                }
            }

            @Override
            public void onFail(Call<UnReadNotificationCountResponse> call, BaseResponse baseResponse) {
            }
        });

    }

}
