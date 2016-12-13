package com.appster.dentamatch.network;

import com.appster.dentamatch.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public final class RequestController {

    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES);

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, false);
    }

    public static <S> S createService(Class<S> serviceClass, boolean isAuth) {
        if (!isAuth) {
            okHttpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    /*String accessToken = PreferenceUtils.getAccessToken();
                    String dsmId = PreferenceUtils.getDsmId();
                    if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(dsmId)) {
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "bearer " + PreferenceUtils.getAccessToken())
                                .header("dsmId", PreferenceUtils.getDsmId())
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    } else*/
                    {
                        return chain.proceed(original);
                    }
                }
            });
        }
        Retrofit retrofit = builder.client(okHttpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
