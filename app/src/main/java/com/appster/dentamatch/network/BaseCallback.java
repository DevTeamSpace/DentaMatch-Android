package com.appster.dentamatch.network;



import com.appster.dentamatch.ui.BaseActivity;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Callback wrapper written to handle some generic response sent by server like 401, 4.2, etc.
 */
public abstract class BaseCallback<T extends BaseResponse> implements Callback<T> {

    public static final String TAG = BaseCallback.class.getSimpleName();
    private static final int HTTP_OK = 200;
    private static final int INVALID_SESSION = 401;
    private static final int REFRESH_TOKEN = 440;
    private WeakReference<BaseActivity> ref;

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
    public abstract void onFail(Call<T> call);

    public void onResponse(Call<T> call, Response<T> response) {
        //Generic error response code are handled at base level
        if (response.body() != null && response.body().getStatusCode() == REFRESH_TOKEN) {
            refreshToken(call);
        } else {
            if (response.body() != null) {
                onSuccess(response.body());
            } else {
                onFail(call);
            }
        }
    }

    public void onFailure(Call<T> call, Throwable t) {
        //LogUtils.printStackTrace(t);
        onFail(call);
    }

    //FIXME:Need to better handle this situation
    private void refreshToken(final Call<T> prevCall) {
        /*RefreshTokenRequest request = new RefreshTokenRequest();
        request.setUserId(PreferenceUtils.getUserId());
        request.setRefreshToken(PreferenceUtils.getRefreshToken());
        request.setDeviceType(Config.DEVICE_TYPE);
        Call<RefreshTokenResponse> refreshTokenCall = RequestController.createService(AuthWebServices
                .class).refreshToken(request);
        refreshTokenCall.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if (response.body().isSuccess()) {
                    Call<T> reCall = prevCall.clone();
                    reCall.enqueue(new Callback<T>() {
                        @Override
                        public void onResponse(Call<T> call, Response<T> response) {
                            if (response.code() == HTTP_OK) {
                                onSuccess(response.body());
                            } else {
                                fail(prevCall);
                            }
                        }

                        @Override
                        public void onFailure(Call<T> call, Throwable t) {
                            fail(prevCall);
                        }
                    });
                } else {
                    fail(prevCall);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                fail(prevCall);
            }
        });*/
    }

    private void fail(Call<T> call) {
        onFail(call);
    }
}
