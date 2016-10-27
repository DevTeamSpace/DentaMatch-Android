package com.appster.dentamatch.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.appster.dentamatch.R;
import com.appster.dentamatch.preferences.AppPreferences;
import com.appster.dentamatch.ui.activities.BaseActivity;
import com.appster.dentamatch.utils.Constants;
import com.appster.dentamatch.utils.Util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Appster on 14/07/16.
 */
public abstract class RestCallback<T> implements Callback<T> {
    private static final String TAG = "RestCallback";
    private ProgressDialog mProgressDialog;
    private Context mContext;

    public RestCallback(Context ct) {
        mContext = ct;
        mProgressDialog = new ProgressDialog(ct);
        mProgressDialog.setCancelable(false);
//        mProgressDialog.setMessage(ct.getString(R.string.loading_text));
        mProgressDialog.show();
    }

    public RestCallback(Context ct, boolean showProgress) {
        //Constructor to disable or enable progressDialog during apiCalls
        mContext = ct;
        mProgressDialog = new ProgressDialog(ct);
        mProgressDialog.setCancelable(false);
//        mProgressDialog.setMessage(ct.getString(R.string.loading_text));

        if (showProgress) {
            mProgressDialog.show();
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        mProgressDialog.dismiss();

        if (response.isSuccessful()) {
            ApiResponse apiResponse = (ApiResponse) response.body();

            if (response.code() == HttpURLConnection.HTTP_OK) {

                if (apiResponse.status) {
                    onApiSuccess(apiResponse);
                } else {
                    onApiFailure(apiResponse);
                }

            } else {
//                Log.w(TAG, mContext.getString(R.string.txt_retro_Response_label) + response.code());

                if (response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                    ApiResponse failureResponse = new ApiResponse();
                    failureResponse.noContentFound = true;
                    onApiFailure(failureResponse);
                }
            }

        } else {
            if (response.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                ApiResponse apiResponse = new ApiResponse();
//                apiResponse.message = mContext.getString(R.string.msg_500_error);
                onApiFailure(apiResponse);

            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                boolean isLogin = AppPreferences.getInstance().getBooleanPreference(Constants.IS_LOGIN);
                if (isLogin) {
                    // In case the user is logged in log him out and show session expired.
                    AppPreferences.getInstance().clearPrefs();
//                    Util.showAlert(mContext, mContext.getString("Session Expired", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ((BaseActivity) mContext).finish();
//                            mContext.startActivity(new Intent(mContext, SignInActivity.class)
//                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                        }
//                    });
                } else {
                    /**
                     * IN case the user is not logged in proceed the message in apiFailure.
                     */
                    ApiResponse apiResponse = null; //Util.parseDataOnError((Response<ApiResponse>) response);
                    onApiFailure(apiResponse);
                }

                } else {
                    ApiResponse apiResponse = null; //Util.parseDataOnError((Response<ApiResponse>) response);
                    onApiFailure(apiResponse);
                }
            }
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        mProgressDialog.dismiss();
        // Network check for network not found error.
        if (t instanceof IOException || t instanceof SocketTimeoutException) {
            Util.showNetworkError(mContext);
        }

        Log.e(TAG, "Retrofit onFailure: " + t);

        onRetroFitError(t);
    }

    public abstract void onApiSuccess(ApiResponse response);

    public abstract void onApiFailure(ApiResponse response);

    public abstract void onRetroFitError(Throwable t);
}
