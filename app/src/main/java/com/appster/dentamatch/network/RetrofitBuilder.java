package com.appster.dentamatch.network;

import android.util.Log;

import com.appster.dentamatch.AppConfig;
import com.appster.dentamatch.AppController;
import com.appster.dentamatch.preferences.AppPreferences;
import com.appster.dentamatch.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Appster on 23/05/16.
 */
public class RetrofitBuilder {
    private static String TAG = "Retrofit";
    private static RetrofitBuilder sInstance;
    private static Retrofit retrofitObj;

    private OkHttpClient.Builder httpClient;
    private String mAccessToken;

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("url")
            .addConverterFactory(GsonConverterFactory.create());

    private RetrofitBuilder() {
        httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(AppConfig.SERVER_TIME_OUT, TimeUnit.MILLISECONDS);
        //Retrofit logger
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        httpClient.addNetworkInterceptor(new MyNetworkInterceptor());
    }

    public static synchronized RetrofitBuilder getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitBuilder();
        }
        return sInstance;
    }

    public ServerApiConfig getRetrofit() {

        mAccessToken = AppPreferences.getInstance().getStringPreference(Constants.ACCESS_TOKEN);

        Log.d(TAG, "accessToken "+mAccessToken);
        Log.d(TAG, "deviceId "+AppController.DEVICE_ID);
        Retrofit retrofit = retrofitBuilder.client(httpClient.build()).build();
        retrofitObj = retrofit;
        return retrofit.create(ServerApiConfig.class);
    }

    private class MyNetworkInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("accessToken", mAccessToken)
                    .addHeader("deviceId", AppController.DEVICE_ID)
                    .build();
            return chain.proceed(request);
        }
    }

    public static Retrofit getRetrofitObject() {
        return retrofitObj;
    }
}