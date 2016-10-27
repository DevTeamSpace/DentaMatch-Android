/*
 * Appster Information Technology Pvt. Ltd., Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Written by Ram Bhambhu December 2015
 */
package com.appster.dentamatch.network;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface to define network communication interface APIs using Retrofit
 */
public interface ServerApiConfig {
    String LOGIN = "user/login";
    String SIGN_UP = "user/register";



}
