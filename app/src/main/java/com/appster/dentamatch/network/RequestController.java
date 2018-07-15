package com.appster.dentamatch.network;

import android.text.TextUtils;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;

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
 * Controller class for handling all network calls.
 */
public final class RequestController {
    private static String TAG = "RequestController";
    private static final String HEADER_ACCESS_TOKEN = "accessToken";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_ACCEPT = "accept";
    private static final String APPLICATION_JSON = "application/json";

    private static Retrofit retrofit;
    private static final HttpLoggingInterceptor logger = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static Interceptor headerInterceptor;

    private static final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(logger)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES);

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, false);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass, final boolean isAuth) {

        if (headerInterceptor == null || !okHttpClient.interceptors().contains(headerInterceptor)) {

            if (isAuth) {
                addNetworkInterceptor();
            }

        }

        Retrofit retrofit = builder.client(okHttpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private static void addNetworkInterceptor() {

        headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Response response;

                String accessToken = PreferenceUtil.getKeyUserToken();
                if (!TextUtils.isEmpty(accessToken)) {
                    LogUtils.LOGD("accessToken>>","token::"+accessToken);

                    Request.Builder requestBuilder = original.newBuilder()
                            .header(HEADER_ACCESS_TOKEN, accessToken)
                            .header(HEADER_CONTENT_TYPE, APPLICATION_JSON)
                            .header(HEADER_ACCEPT, APPLICATION_JSON)
                            .method(original.method(), original.body());

                    response = chain.proceed(requestBuilder.build());
                } else {
                    response = chain.proceed(original);
                }

                return response;
            }
        };

        okHttpClient.addInterceptor(headerInterceptor);
    }
}
