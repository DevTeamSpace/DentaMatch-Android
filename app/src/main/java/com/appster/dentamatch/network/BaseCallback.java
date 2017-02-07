package com.appster.dentamatch.network;

import android.content.Context;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Callback wrapper written to handle some generic response sent by server like 401, 4.2, etc.
 */
public abstract class BaseCallback<T extends BaseResponse> implements Callback<T> {

    public static final String TAG = BaseCallback.class.getSimpleName();
    private static final int HTTP_OK = 200;
    private static final int STATUS_FAIL = 0;
    private static final int INVALID_SESSION = 204;
    private static final int REFRESH_TOKEN = 440;
    private static final int NOT_AUTHORISED = 604;
    private static final int ERROR_400 = 400;
    private WeakReference<BaseActivity> ref;
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
        act.hideProgressBar();
        if (response.isSuccessful()) { //HTTP 200

            BaseResponse responseBase = response.body();
            if (responseBase == null) {
//                act.showSnackBar(act.getResources().getString(R.string.server_error), true);
                return;
            }

                if (responseBase.getStatusCode() == INVALID_SESSION) {
                    act.showToast(act.getString(R.string.authentication_error));
                    act.localLogOut();
                    return;
                }

//            if (responseBase.getStatus() == STATUS_FAIL) {//STATUS 0
//                if (responseBase.getStatusCode() == INVALID_SESSION) {
//                    act.showSnackBar(act.getResources().getString(R.string.authentication_error), true);
//                    act.logOut();
//                    return;
//                }
//                onFail(call, responseBase);
//                //  act.showSnakBarFromTop(act.getResources().getString(R.string.server_error), true);
//                return;
//            } else {
                onSuccess(response.body());
//            }

        } else {
            if (response.raw().code() == ERROR_400) {
//                BaseResponse error = ErrorUtils.parseError(response);
//                if (error.getStatusCode() == NOT_AUTHORISED) {
//                Utils.logOut(act);
                //   }
            } else {
                act.showSnackBar(act.getResources().getString(R.string.server_error));
            }
        }
    }

    public void onFailure(Call<T> call, Throwable t) {
        LogUtils.printStackTrace(t);
        BaseActivity act = ref.get();
        act.hideProgressBar();

        if (t instanceof ConnectException || t instanceof UnknownHostException) {
            act.showSnackBar(act.getResources().getString(R.string.no_internet));
        } else {
            act.showSnackBar(act.getResources().getString(R.string.error_something_wrong));
            onFail(call, null);
        }
    }


    private void fail(Call<T> call) {
        onFail(call, null);
    }
}
