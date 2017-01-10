package com.appster.dentamatch.network.retrofit;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.response.auth.JobTitleResponse;
import com.appster.dentamatch.network.response.auth.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface AuthWebServices {

    String API_URL = BuildConfig.BASE_URL;

//    String AUTHENTICATION_URL = BuildConfig.BASE_URL + "authenticateUser";
//    String LOGOUT = BuildConfig.BASE_URL + "logout";
//    String REFRESH_TOKEN = BuildConfig.BASE_URL + "refreshToken";

    String SIGN_IN = "users/sign-in";
    String SIGN_UP = "users/sign-up";
    String FORGOT_APSSWORD = "users/forgot-password";
    String JOB_TITLE_LIST = "list-jobtitle";

    @POST(SIGN_IN)
    Call<LoginResponse> signIn(@Body LoginRequest loginRequest);

    @POST(SIGN_UP)
    Call<LoginResponse> signUp(@Body LoginRequest loginRequest);

    @PUT(FORGOT_APSSWORD)
    Call<LoginResponse> forgotPassword(@Body LoginRequest loginRequest);

    @GET(JOB_TITLE_LIST)
    Call<JobTitleResponse> jobTitle();

    /*@POST(AUTHENTICATION_URL)
    Call<LoginResponse> userAuthenticate(@Body LoginRequest params);

    @POST(LOGOUT)
    Call<BaseResponse> logout(@Body LoginRequest params);

    @POST(REFRESH_TOKEN)
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest params);*/
}
