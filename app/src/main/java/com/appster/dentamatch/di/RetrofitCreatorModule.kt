package com.appster.dentamatch.di

import android.content.Context
import com.appster.dentamatch.BuildConfig
import com.appster.dentamatch.base.ApiErrorHandler
import com.appster.dentamatch.util.PreferenceUtil
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitCreatorModule {

    companion object {
        private const val API_URL = BuildConfig.BASE_URL
        private const val TIMEOUT_SECONDS = 5L
        private const val LOGGING_INTERCEPTOR = "loggingInterceptor"
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val HEADER_ACCEPT = "accept"
        private const val APPLICATION_JSON = "application/json"
    }

    @Provides
    @Singleton
    fun provideApiErrorHandler(context: Context): ApiErrorHandler =
        ApiErrorHandler(context)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(@Named(LOGGING_INTERCEPTOR) loggingInterceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                if (PreferenceUtil.getIsLogin())
                    request = builder.addHeader("Authorization", "Basic ${PreferenceUtil.getKeyUserToken()}")
                            .addHeader(HEADER_CONTENT_TYPE, APPLICATION_JSON)
                            .addHeader(HEADER_ACCEPT, APPLICATION_JSON)
                            .method(request.method(), request.body())
                            .build()
                chain.proceed(request)
            }
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .hostnameVerifier { hostname, session -> hostname.equals(session.peerHost, ignoreCase = true) }
            .build()

    @Provides
    @Singleton
    @Named(LOGGING_INTERCEPTOR)
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            )
}