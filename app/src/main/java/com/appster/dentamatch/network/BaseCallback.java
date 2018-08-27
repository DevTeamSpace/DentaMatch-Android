/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.network;

import android.content.Context;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Callback wrapper written to handle some generic response sent by server like 401, 4.2, etc.
 */
public abstract class BaseCallback<T extends BaseResponse> implements Callback<T> {

    public static final String TAG = BaseCallback.class.getSimpleName();
    private static final int INVALID_SESSION = 204;
    private final WeakReference<BaseActivity> ref;
    private Context mContext;

    public BaseCallback(BaseActivity activity) {
        ref = new WeakReference<>(activity);
    }

    /**
     * Invoked when Successful response sent from server.
     *
     * @param response Response from server
     */
    public abstract void onSuccess(T response);

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call Failed Call instance
     */
    public abstract void onFail(Call<T> call, BaseResponse baseResponse);

    public void onResponse(Call<T> call, Response<T> response) {
        //Generic error response code are handled at base level
        BaseActivity act = ref.get();
        if (act != null)
            act.hideProgressBar();
        if (response.isSuccessful()) {

            BaseResponse responseBase = response.body();
            if (responseBase == null) {
                return;
            }

            if (responseBase.getStatusCode() == INVALID_SESSION) {
                if (act != null) {
                    act.showToast(act.getString(R.string.authentication_error));
                    act.localLogOut();
                }
                return;
            }

            onSuccess(response.body());

        } else {
            if (response.raw().code() != HttpURLConnection.HTTP_BAD_REQUEST) {
                if (act != null)
                    act.showSnackBar(act.getResources().getString(R.string.server_error));
            }
        }
    }

    public void onFailure(Call<T> call, Throwable t) {
        LogUtils.printStackTrace(t);
        BaseActivity act = ref.get();
        if (act != null)
            act.hideProgressBar();

        if (t instanceof ConnectException || t instanceof UnknownHostException) {
            if (act != null)
                act.showSnackBar(act.getResources().getString(R.string.no_internet));
        } else {
            if (act != null)
                act.showSnackBar(act.getResources().getString(R.string.error_something_wrong));
        }

        onFail(call, null);

    }


    private void fail(Call<T> call) {
        onFail(call, null);
    }
}
