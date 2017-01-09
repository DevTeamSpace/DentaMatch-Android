package com.appster.dentamatch.network.retrofit;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.response.auth.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface AuthWebServices {

    String AUTHENTICATION_URL = BuildConfig.BASE_URL + "authenticateUser";
    String LOGOUT = BuildConfig.BASE_URL + "logout";
    String REFRESH_TOKEN = BuildConfig.BASE_URL + "refreshToken";

    String SIGN_IN = "users/sign-in";
    String SIGN_UP = "users/sign-up";
    String FORGOT_PASSWORD = "users/forgot-password";

    @POST(SIGN_IN)
    Call<LoginResponse> signIn(@Body LoginRequest loginRequest);

    @POST(SIGN_UP)
    Call<LoginResponse> signUp(@Body LoginRequest loginRequest);

    @PUT(FORGOT_PASSWORD)
    Call<LoginResponse> forgotPassword(@Body LoginRequest loginRequest);
}
