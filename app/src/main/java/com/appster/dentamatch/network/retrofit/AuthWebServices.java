package com.appster.dentamatch.network.retrofit;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.response.auth.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AuthWebServices {

    String API_URL = BuildConfig.BASE_URL;

//    String AUTHENTICATION_URL = BuildConfig.BASE_URL + "authenticateUser";
//    String LOGOUT = BuildConfig.BASE_URL + "logout";
//    String REFRESH_TOKEN = BuildConfig.BASE_URL + "refreshToken";

    String SIGN_IN = "users/sign-in";

    @POST(SIGN_IN)
    Call<LoginResponse> signIn(@Body LoginRequest loginRequest);

    /*@POST(AUTHENTICATION_URL)
    Call<LoginResponse> userAuthenticate(@Body LoginRequest params);

    @POST(LOGOUT)
    Call<BaseResponse> logout(@Body LoginRequest params);

    @POST(REFRESH_TOKEN)
    Call<RefreshTokenResponse> refreshToken(@Body RefreshTokenRequest params);*/
}
