package com.appster.dentamatch.network;

import android.text.TextUtils;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Header;


/**
 * Controller class for handling all network calls.
 */
public final class RequestController {

    private static String TAG = "RequestController";
    private static String HEADER_ACCESS_TOKEN = "accessToken";
    private static String HEADER_CONTENT_TYPE = "Content-Type";
    private static String HEADER_ACCEPT = "accept";
    private static String APPLICATION_JSON = "application/json";

    private static Retrofit retrofit;
    private static HttpLoggingInterceptor logger = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(logger)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES);

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, false);
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass, final boolean isAuth) {
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                if (isAuth) {
                    String accessToken = PreferenceUtil.getKeyUserToken();
                    LogUtils.LOGD(TAG, HEADER_ACCESS_TOKEN + " : " + accessToken);

                    if (!TextUtils.isEmpty(accessToken)) {
                        Request.Builder requestBuilder = original.newBuilder()
                                .header(HEADER_ACCESS_TOKEN, accessToken)
                                .header(HEADER_CONTENT_TYPE, APPLICATION_JSON)
                                .header(HEADER_ACCEPT, APPLICATION_JSON)
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                } else {
                    return chain.proceed(original);
                }

                return chain.proceed(original);
            }
        });

//        okHttpClient.addInterceptor(logger);
        Retrofit retrofit = builder.client(okHttpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
